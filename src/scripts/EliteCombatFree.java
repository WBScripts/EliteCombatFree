package scripts;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.generic.Condition;
import org.tribot.api.types.generic.Filter;
import org.tribot.api2007.*;
import org.tribot.api2007.types.*;
import org.tribot.api2007.util.ThreadSettings;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;
import scripts.wastedbro.api.abc3.Abc3Util;
import scripts.wastedbro.api.abc3.reactions.REACTIONS;
import scripts.wastedbro.api.banking.Banking;
import scripts.wastedbro.api.banking.Intent;
import scripts.wastedbro.api.price_lookup.RSItemData;
import scripts.wastedbro.api.price_lookup.RSItemGELookup;
import scripts.wastedbro.api.utils.CameraUtil;
import scripts.wastedbro.api.utils.InteractionUtil;
import scripts.wastedbro.api.waiting.Waiting;
import scripts.wastedbro.api.walking.Walker;
import scripts.wastedbro.api.webwalker_logic.WebWalker;
import scripts.wastedbro.api.webwalker_logic.local.walker_engine.local_pathfinding.Reachable;
import scripts.wastedbro.components.concrete.AntibanSettings;

import javax.swing.*;
import java.awt.*;

/**
 * @author Wastedbro
 */
@ScriptManifest(name = "Elite Combat Free", authors = "Wastedbro", category = "Combat")
public class EliteCombatFree extends Script implements Painting
{
    private boolean isRunning = true;

    boolean isHovering = false;

    Abc3Util abc;
    CombatSettings combatSettings;

    RSNPC currentNpc;
    RSNPC hoverNpc;

    int runActivation = 0;

    @Override
    public void run()
    {
        ThreadSettings.get().setClickingAPIUseDynamic(true);
        abc = Abc3Util.instance();
        AntibanSettings antibanSettings = new AntibanSettings();
        antibanSettings.setMouseSpeed(120);
        antibanSettings.setReactionModifier(50);
        antibanSettings.setUseAbc3Reactions(false);
        abc.configureSettings(antibanSettings);


        EliteCombatFreeGui gui = new EliteCombatFreeGui();

        SwingUtilities.invokeLater(() ->
        {
            try
            {
                gui.setVisible(true);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        });

        while(!gui.isDone())
            sleep(400,1000);
        gui.setVisible(false);


        combatSettings = gui.getSettings();

        runActivation = abc.generateRunActivation();

        RSArea combatArea = new RSArea(combatSettings.getCentralTile(), combatSettings.getRadius());

        while(isRunning)
        {
            if(combatSettings.shouldAttemptToBank() && Inventory.isFull()) // Needs to bank
            {
                if(Banking.isInBank())
                {
                    if(Banking.isBankScreenOpen())
                    {
                        Banking.depositAll();
                        if(Banking.close())
                            abc.sleep(abc.generateSleepTime(REACTIONS.PREDICTABLE_MEDIUM));
                    }
                    else
                        Banking.openBank(Intent.DEPOSIT_ALL);
                }
                else
                {
                    Walker.walkToBank(true);
                }
            }
            else if(combatArea.contains(Player.getRSPlayer()) && Player.getPosition().getPlane() == combatSettings.getCentralTile().getPlane()) // In Combat Area
            {
                if (!Game.isRunOn() && Game.getRunEnergy() >= this.runActivation) {
                    Options.setRunOn(true);
                    this.runActivation = abc.generateRunActivation();
                }
                if(inCombat() && currentNpc != null)
                {
                    boolean exitedEarly = false;
                    long combatStartTime = Timing.currentTimeMillis();
                    while(Login.getLoginState() == Login.STATE.INGAME && inCombat())
                    {
                        abc.performTimedActions();
                        General.sleep(10);
                        if(loot())
                            exitedEarly = true;
                    }

                    if(!exitedEarly)
                    {
                        try
                        {
                            abc.addIdleTime("Attacking " + currentNpc.getName(), (int) (Timing.currentTimeMillis() - combatStartTime));
                            General.println("Average " + currentNpc.getName() + " Attack Time: " + abc.getAverageIdleTime("Attacking " + currentNpc.getName()));
                        }catch (Exception e)
                        {
                            println("Failed to record Idle Time");
                        }

                        if (combatSettings.shouldUseAbcReactions())
                            abc.sleep(abc.generateSleepTime(abc.getAverageIdleTime("Attacking " + currentNpc.getName()), isHovering));
                        else
                            General.sleep(General.randomSD(750, 200));
                    }

                    currentNpc = null;
                }
                else if(loot())
                {
                    abc.sleep(abc.generateSleepTime(REACTIONS.PREDICTABLE_MEDIUM));
                }
                else
                {
                    if(ChooseOption.isOpen() && hoverNpc != null && hoverNpc.isValid() && !hoverNpc.isInCombat() && ChooseOption.isOptionValid("Attack " + hoverNpc.getName()))
                    {
                        if(ChooseOption.select("Attack " + hoverNpc.getName()))
                            abc.sleep(abc.generateSleepTime(REACTIONS.PREDICTABLE_SHORT));
                    }
                    else
                    {
                        attackNpc();
                    }
                }
            }
            else
            {
                if(Player.getPosition().getPlane() != combatSettings.getCentralTile().getPlane())
                {
                    RSObject[] possibleOuts = Objects.findNearest(30,"Ladder");
                    if(possibleOuts.length > 0)
                    {
                        if(InteractionUtil.interactWithEntity(possibleOuts[0],""))
                        {
                            Timing.waitCondition(new Condition()
                            {
                                @Override
                                public boolean active()
                                {
                                    return Player.getPosition().getPlane() == combatSettings.getCentralTile().getPlane();
                                }
                            },General.random(8000,13000));
                            abc.sleep(abc.generateSleepTime(REACTIONS.PREDICTABLE_MEDIUM));
                        }
                    }
                }
                else
                    Walker.walkTo(combatSettings.getCentralTile(), true);
            }
        }
    }







    @Override
    public void onPaint(Graphics graphics)
    {

    }









    private boolean inCombat()
    {
        return Player.getRSPlayer().getInteractingCharacter() != null;
    }

    private void attackNpc()
    {
        Reachable reachable = new Reachable();

        RSNPC npc;
        RSCharacter interactingChar = Player.getRSPlayer().getInteractingCharacter();
        if(interactingChar != null)
            npc = (RSNPC)interactingChar;
        else
            npc = abc.getPreferredTarget(NPCs.find(enemyFilter));

        if (npc != null)
        {
            if(!reachable.canReach(npc.getPosition()))
            {
                if(!WebWalker.walkTo(npc))
                    return;
                abc.sleep(abc.generateSleepTime(REACTIONS.PREDICTABLE_SHORT));
            }

            if (!npc.isClickable()) CameraUtil.attemptAimCamera(npc, 19);

            if (npc != null && npc.isClickable() && npc.click("Attack " + npc.getDefinition().getName()))
            {
                if (Waiting.waitForAnyAnimation(Waiting.calculateTimeOutFromDistance(reachable.getDistance(npc))))
                {
                    currentNpc = npc;
                    abc.sleep(abc.generateSleepTime(REACTIONS.PREDICTABLE_SHORT));

                    if (abc.shouldHover() && Mouse.isInBounds())
                    {
                        isHovering = true;

                        General.println("ABCv2: Hovering next target");
                        RSNPC hoverNpc = abc.getPreferredTarget(NPCs.find(enemyFilter));

                        if (hoverNpc != null && hoverNpc.hover())
                        {
                            this.hoverNpc = hoverNpc;
                            if (abc.shouldOpenMenu())
                                Mouse.click(3);
                        }
                    }
                    else
                        isHovering = false;
                }
            }
        }
    }

    private boolean loot()
    {
        if(combatSettings.getLootMinPrice() == 0) return false;

        RSGroundItem[] items = GroundItems.find(lootFilter);

        if(items.length > 0)
        {
            for(RSGroundItem item : items)
            {
                if(InteractionUtil.interactWithEntity(item, "Take " + item.getDefinition().getName()))
                {
                    int invCount = Inventory.getAll().length;
                    if(Timing.waitCondition(new Condition()
                    {
                        @Override
                        public boolean active()
                        {
                            return Inventory.getAll().length > invCount;
                        }
                    }, General.random(3000,5000)))
                    {
                        abc.sleep(abc.generateSleepTime(REACTIONS.PREDICTABLE_MEDIUM));
                    }
                }
            }
            return true;
        }
        return false;
    }

    private Filter<RSGroundItem> lootFilter = new Filter<RSGroundItem>() {
        @Override
        public boolean accept(RSGroundItem rsGroundItem)
        {
            if(rsGroundItem == null || Player.getPosition().distanceTo(rsGroundItem) > 10) return false;

            int id = rsGroundItem.getID();

            if(!PathFinding.canReach(rsGroundItem, false))
                return false;


            // Check if GE Price is met
            if(combatSettings.getLootMinPrice() > 0) {
                RSItemData itemData = RSItemGELookup.getInstance().getItemData(id);
                if(itemData != null)
                    return itemData.getOverallPrice()*rsGroundItem.getStack() >= combatSettings.getLootMinPrice();
            }


            return false;
        }
    };

    private Filter<RSNPC> enemyFilter = new Filter<RSNPC>()
    {
        @Override
        public boolean accept(RSNPC rsnpc)
        {
            if(rsnpc == null || !rsnpc.isValid() || rsnpc.isInCombat() || rsnpc.getInteractingCharacter() != null)
                return false;

            boolean idMatch = false;
            for(int id : combatSettings.getEnemyIds())
            {
                if(id == rsnpc.getID())
                {
                    idMatch = true;
                    break;
                }
            }

            return idMatch && rsnpc.getPosition().distanceTo(combatSettings.getCentralTile()) <= combatSettings.getRadius();
        }
    };

}
