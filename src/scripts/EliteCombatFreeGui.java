/*
 * Created by JFormDesigner on Mon Jan 01 18:09:58 EST 2018
 */

package scripts;

import obf.E;
import org.tribot.api2007.Login;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSTile;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.border.*;

/**
 * @author Clay Gillman
 */
public class EliteCombatFreeGui extends JFrame
{
    private boolean isDone = false;

    private CombatSettings settings = new CombatSettings();


    public EliteCombatFreeGui() {
        initComponents();
    }

    private void scanAreaButtonActionPerformed(ActionEvent e)
    {
        if(Login.getLoginState() == Login.STATE.INGAME)
        {
            RSTile position = Player.getPosition();
            xTextField.setText(position.getX()+"");
            yTextField.setText(position.getY()+"");
            zTextField.setText(position.getPlane()+"");
        }
    }

    private void startButtonActionPerformed(ActionEvent e)
    {
        ArrayList<Integer> ids = new ArrayList<>();
        for(String id : enemyIdsTextField.getText().trim().split(","))
        {
            try
            {
                ids.add(Integer.parseInt(id.trim()));
            }catch (Exception g)
            {
                JOptionPane.showMessageDialog(this, "Incorrect Enemy ID format. Please use numeric IDs separate by commas.");
                return;
            }
        }
        settings.setEnemyIds(ids.stream().mapToInt(i -> i).toArray());

        try
        {
            settings.setCentralTile(new RSTile(Integer.parseInt(xTextField.getText()),Integer.parseInt(yTextField.getText()),Integer.parseInt(zTextField.getText())));
        }catch (Exception g)
        {
            JOptionPane.showMessageDialog(this, "Incorrect X,Y,Z tile coordinates. Please enter numeric coordinates or use the scan button");
            return;
        }

        settings.setRadius((int)radiusSpinner.getValue());
        settings.setLootMinPrice((int)minLootPriceSpinner.getValue());

        settings.setAttemptToBank(attemptToBankCheckbox.isSelected());
        settings.setUseAbcReactions(abcReactionCheckbox.isSelected());

        isDone = true;
    }



    public boolean isDone()
    {
        return isDone;
    }

    public CombatSettings getSettings()
    {
        return settings;
    }




    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        panel1 = new JPanel();
        label1 = new JLabel();
        enemyIdsTextField = new JTextField();
        panel2 = new JPanel();
        label2 = new JLabel();
        xTextField = new JTextField();
        yTextField = new JTextField();
        label3 = new JLabel();
        zTextField = new JTextField();
        label4 = new JLabel();
        scanAreaButton = new JButton();
        label5 = new JLabel();
        radiusSpinner = new JSpinner();
        panel3 = new JPanel();
        label6 = new JLabel();
        minLootPriceSpinner = new JSpinner();
        label7 = new JLabel();
        label8 = new JLabel();
        attemptToBankCheckbox = new JCheckBox();
        abcReactionCheckbox = new JCheckBox();
        startButton = new JButton();

        //======== this ========
        setResizable(false);
        setTitle("Elite Combat Free");
        Container contentPane = getContentPane();

        //======== panel1 ========
        {
            panel1.setBorder(new TitledBorder(null, "NPC Selection", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, null, Color.black));

            //---- label1 ----
            label1.setText("NPC Ids (Separate by comma)");

            GroupLayout panel1Layout = new GroupLayout(panel1);
            panel1.setLayout(panel1Layout);
            panel1Layout.setHorizontalGroup(
                panel1Layout.createParallelGroup()
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panel1Layout.createParallelGroup()
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addComponent(label1)
                                .addGap(0, 272, Short.MAX_VALUE))
                            .addComponent(enemyIdsTextField))
                        .addContainerGap())
            );
            panel1Layout.setVerticalGroup(
                panel1Layout.createParallelGroup()
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(label1)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(enemyIdsTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
        }

        //======== panel2 ========
        {
            panel2.setBorder(new TitledBorder(null, "Location", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, null, Color.black));

            //---- label2 ----
            label2.setText("X:");

            //---- label3 ----
            label3.setText("Y:");

            //---- label4 ----
            label4.setText("Z:");

            //---- scanAreaButton ----
            scanAreaButton.setText("Scan");
            scanAreaButton.addActionListener(e -> scanAreaButtonActionPerformed(e));

            //---- label5 ----
            label5.setText("Radius:");

            //---- radiusSpinner ----
            radiusSpinner.setModel(new SpinnerNumberModel(10, 3, 80, 1));

            GroupLayout panel2Layout = new GroupLayout(panel2);
            panel2.setLayout(panel2Layout);
            panel2Layout.setHorizontalGroup(
                panel2Layout.createParallelGroup()
                    .addGroup(panel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addGroup(panel2Layout.createSequentialGroup()
                                .addComponent(label2)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(xTextField, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(label3))
                            .addGroup(panel2Layout.createSequentialGroup()
                                .addComponent(label5)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(radiusSpinner)))
                        .addGap(6, 6, 6)
                        .addComponent(yTextField, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(label4, GroupLayout.PREFERRED_SIZE, 12, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(zTextField, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(scanAreaButton, GroupLayout.PREFERRED_SIZE, 122, GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(28, Short.MAX_VALUE))
            );
            panel2Layout.setVerticalGroup(
                panel2Layout.createParallelGroup()
                    .addGroup(panel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panel2Layout.createParallelGroup()
                            .addGroup(panel2Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(label3))
                            .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(yTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(label4)
                                .addComponent(zTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(scanAreaButton))
                            .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(label2)
                                .addComponent(xTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                        .addGap(22, 22, 22)
                        .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(label5)
                            .addComponent(radiusSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
        }

        //======== panel3 ========
        {
            panel3.setBorder(new TitledBorder(null, "Other CombatSettings", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, null, Color.black));

            //---- label6 ----
            label6.setText("Loot Items Worth Over:");

            //---- minLootPriceSpinner ----
            minLootPriceSpinner.setModel(new SpinnerNumberModel(0, 0, 0, 100));

            //---- label7 ----
            label7.setText("GP");

            //---- label8 ----
            label8.setText("(Leave at 0 for no looting)");

            //---- attemptToBankCheckbox ----
            attemptToBankCheckbox.setText("Attempt To Bank");

            //---- abcReactionCheckbox ----
            abcReactionCheckbox.setText("Use ABCv2 Reaction Times (Lower Kills per Hour)");

            GroupLayout panel3Layout = new GroupLayout(panel3);
            panel3.setLayout(panel3Layout);
            panel3Layout.setHorizontalGroup(
                panel3Layout.createParallelGroup()
                    .addGroup(panel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panel3Layout.createParallelGroup()
                            .addGroup(panel3Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(label8))
                            .addGroup(panel3Layout.createSequentialGroup()
                                .addComponent(label6)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(minLootPriceSpinner, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(label7))
                            .addComponent(attemptToBankCheckbox)
                            .addComponent(abcReactionCheckbox))
                        .addContainerGap(141, Short.MAX_VALUE))
            );
            panel3Layout.setVerticalGroup(
                panel3Layout.createParallelGroup()
                    .addGroup(panel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(label6)
                            .addComponent(minLootPriceSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(label7))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label8)
                        .addGap(18, 18, 18)
                        .addComponent(attemptToBankCheckbox)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(abcReactionCheckbox)
                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
        }

        //---- startButton ----
        startButton.setText("Start");
        startButton.addActionListener(e -> startButtonActionPerformed(e));

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                            .addComponent(panel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(panel2, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(panel3, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(startButton, GroupLayout.PREFERRED_SIZE, 131, GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 20, Short.MAX_VALUE))
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(panel1, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(panel2, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(panel3, GroupLayout.PREFERRED_SIZE, 155, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(startButton, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(12, Short.MAX_VALUE))
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel panel1;
    private JLabel label1;
    private JTextField enemyIdsTextField;
    private JPanel panel2;
    private JLabel label2;
    private JTextField xTextField;
    private JTextField yTextField;
    private JLabel label3;
    private JTextField zTextField;
    private JLabel label4;
    private JButton scanAreaButton;
    private JLabel label5;
    private JSpinner radiusSpinner;
    private JPanel panel3;
    private JLabel label6;
    private JSpinner minLootPriceSpinner;
    private JLabel label7;
    private JLabel label8;
    private JCheckBox attemptToBankCheckbox;
    private JCheckBox abcReactionCheckbox;
    private JButton startButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
