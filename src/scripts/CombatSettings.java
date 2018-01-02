package scripts;

import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;

import java.io.Serializable;

/**
 * @author Wastedbro
 */
public class CombatSettings implements Serializable
{
    private RSTile centralTile = new RSTile(0,0,0);
    private int radius = 10;


    private int[] enemyIds = new int[]{};

    private int lootMinPrice = -1;

    private boolean attemptToBank = false;

    private boolean useAbcReactions = false;

    public CombatSettings()
    {

    }

    //region Getters and Setters

    public RSTile getCentralTile()
    {
        return centralTile;
    }

    public void setCentralTile(RSTile centralTile)
    {
        this.centralTile = centralTile;
    }

    public int getRadius()
    {
        return radius;
    }

    public void setRadius(int radius)
    {
        this.radius = radius;
    }

    public int[] getEnemyIds()
    {
        return enemyIds;
    }

    public void setEnemyIds(int[] enemyIds)
    {
        this.enemyIds = enemyIds;
    }

    public int getLootMinPrice()
    {
        return lootMinPrice;
    }

    public void setLootMinPrice(int lootMinPrice)
    {
        this.lootMinPrice = lootMinPrice;
    }

    public boolean shouldAttemptToBank()
    {
        return attemptToBank;
    }

    public void setAttemptToBank(boolean attemptToBank)
    {
        this.attemptToBank = attemptToBank;
    }

    public boolean shouldUseAbcReactions()
    {
        return useAbcReactions;
    }

    public void setUseAbcReactions(boolean useAbcReactions)
    {
        this.useAbcReactions = useAbcReactions;
    }

    //endregion
}
