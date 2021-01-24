/*
 * Created by JFormDesigner on Mon Dec 14 02:03:06 CST 2020
 */

package BrianW.AKA.BigChan.GUI;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;

import BrianW.AKA.BigChan.Tools.Config;
import BrianW.AKA.BigChan.Tools.Global;

/**
 * @author usual
 */
public class PowerPannel extends JPanel {
	public PowerPannel() {
		initComponents();
		this.text_Log.append("inited\n");
		this.checkBox_PathTraversal.setSelected(Global.config.getConfigPathTraversalEnable_value());
		this.checkBox_Sqli.setSelected(Global.config.getConfigSqliEnable_value());
		this.checkBox_RCE.setSelected(Global.config.getConfigRCEEnable_value());
		this.checkBox_SensitiveFilesScan.setSelected(Global.config.getConfigSensitiveFilesScanEnable_value());
		
	}

	private void checkBox_SqliActionPerformed(ActionEvent e) {
//		this.text_Log.append("checkBox_SqliSelectedChange: \n");
		Global.config.setConfigSqliEnable_value(this.checkBox_Sqli.isSelected());
	}

	private void checkBox_RCEActionPerformed(ActionEvent e) {
		Global.config.setConfigRCEEnable_value(this.checkBox_RCE.isSelected());
		this.textArea_RCE_cmd.setEnabled(this.checkBox_RCE.isSelected());

	}

	private void checkBox_PathTraversalActionPerformed(ActionEvent e) {
		Global.config.setConfigPathTraversalEnable_value(this.checkBox_PathTraversal.isSelected());
	}

	private void checkBox_SensitiveFilesScanActionPerformed(ActionEvent e) {
		Global.config.setConfigSensitiveFilesScanEnable_value(this.checkBox_SensitiveFilesScan.isSelected());
		this.textArea_SensitiveFiles.setEnabled(this.checkBox_SensitiveFilesScan.isSelected());
	}

	private void textArea_RCE_cmdFocusLost(FocusEvent e) {
		Global.config.setConfigRCEcmd_value(this.textArea_RCE_cmd.getText());
//		this.text_Log.append("textArea_RCE_cmdFocusLost: " + this.textArea_RCE_cmd.getText() + "\n");
	}

	private void textArea_SensitiveFilesFocusLost(FocusEvent e) {
		Global.config.setConfigSensitiveFiles_value(this.textArea_SensitiveFiles.getText());
	}
	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		tabbedPane1 = new JTabbedPane();
		panel1 = new JPanel();
		checkBox_Sqli = new JCheckBox();
		checkBox_RCE = new JCheckBox();
		checkBox6 = new JCheckBox();
		scrollPane2 = new JScrollPane();
		textArea_RCE_cmd = new JTextArea();
		checkBox_PathTraversal = new JCheckBox();
		checkBox_SensitiveFilesScan = new JCheckBox();
		scrollPane3 = new JScrollPane();
		textArea_SensitiveFiles = new JTextArea();
		panel2 = new JPanel();
		scrollPane1 = new JScrollPane();
		table1 = new JTable();
		panel3 = new JPanel();
		scrollPane4 = new JScrollPane();
		text_Log = new JTextArea();
		label2 = new JLabel();
		checkBox1 = new JCheckBox();
		checkBox4 = new JCheckBox();
		checkBox5 = new JCheckBox();

		//======== this ========
		setLayout(null);

		//======== tabbedPane1 ========
		{

			//======== panel1 ========
			{
				panel1.setLayout(null);

				//---- checkBox_Sqli ----
				checkBox_Sqli.setText("\u6ce8\u5165\u6d4b\u8bd5");
				checkBox_Sqli.addActionListener(e -> checkBox_SqliActionPerformed(e));
				panel1.add(checkBox_Sqli);
				checkBox_Sqli.setBounds(new Rectangle(new Point(15, 175), checkBox_Sqli.getPreferredSize()));

				//---- checkBox_RCE ----
				checkBox_RCE.setText("\u547d\u4ee4\u6ce8\u5165(\u4e0b\u65b9\u586b\u5199\u5355\u884c\u547d\u4ee4)");
				checkBox_RCE.addActionListener(e -> checkBox_RCEActionPerformed(e));
				panel1.add(checkBox_RCE);
				checkBox_RCE.setBounds(new Rectangle(new Point(15, 205), checkBox_RCE.getPreferredSize()));

				//---- checkBox6 ----
				checkBox6.setText("\u4f4e\u8c03\u6a21\u5f0f(\u6700\u5927\u7a0b\u5ea6\u51cf\u5c11\u53d1\u5305\u91cf\uff0c\u540c\u65f6\u63d0\u9ad8\u8bef\u62a5\u7387)");
				panel1.add(checkBox6);
				checkBox6.setBounds(new Rectangle(new Point(10, 10), checkBox6.getPreferredSize()));

				//======== scrollPane2 ========
				{

					//---- textArea_RCE_cmd ----
					textArea_RCE_cmd.setText("ping -c 3");
					textArea_RCE_cmd.addFocusListener(new FocusAdapter() {
						@Override
						public void focusLost(FocusEvent e) {
							textArea_RCE_cmdFocusLost(e);
						}
					});
					scrollPane2.setViewportView(textArea_RCE_cmd);
				}
				panel1.add(scrollPane2);
				scrollPane2.setBounds(15, 230, 270, 35);

				//---- checkBox_PathTraversal ----
				checkBox_PathTraversal.setText("\u8def\u5f84\u7a7f\u8d8a");
				checkBox_PathTraversal.addActionListener(e -> checkBox_PathTraversalActionPerformed(e));
				panel1.add(checkBox_PathTraversal);
				checkBox_PathTraversal.setBounds(new Rectangle(new Point(15, 275), checkBox_PathTraversal.getPreferredSize()));

				//---- checkBox_SensitiveFilesScan ----
				checkBox_SensitiveFilesScan.setText("\u654f\u611f\u6587\u4ef6\u63a2\u6d4b(\u4e0b\u65b9\u586b\u5199\u591a\u884c\u63a2\u6d4b\u6587\u4ef6\u5217\u8868)");
				checkBox_SensitiveFilesScan.addActionListener(e -> checkBox_SensitiveFilesScanActionPerformed(e));
				panel1.add(checkBox_SensitiveFilesScan);
				checkBox_SensitiveFilesScan.setBounds(15, 305, 220, checkBox_SensitiveFilesScan.getPreferredSize().height);

				//======== scrollPane3 ========
				{

					//---- textArea_SensitiveFiles ----
					textArea_SensitiveFiles.setText("robots.txt");
					textArea_SensitiveFiles.addFocusListener(new FocusAdapter() {
						@Override
						public void focusLost(FocusEvent e) {
							textArea_SensitiveFilesFocusLost(e);
						}
					});
					scrollPane3.setViewportView(textArea_SensitiveFiles);
				}
				panel1.add(scrollPane3);
				scrollPane3.setBounds(20, 335, 265, 55);

				{
					// compute preferred size
					Dimension preferredSize = new Dimension();
					for(int i = 0; i < panel1.getComponentCount(); i++) {
						Rectangle bounds = panel1.getComponent(i).getBounds();
						preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
						preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
					}
					Insets insets = panel1.getInsets();
					preferredSize.width += insets.right;
					preferredSize.height += insets.bottom;
					panel1.setMinimumSize(preferredSize);
					panel1.setPreferredSize(preferredSize);
				}
			}
			tabbedPane1.addTab("\u9009\u9879", panel1);

			//======== panel2 ========
			{
				panel2.setLayout(null);

				//======== scrollPane1 ========
				{
					scrollPane1.setViewportView(table1);
				}
				panel2.add(scrollPane1);
				scrollPane1.setBounds(0, 0, 895, 335);

				{
					// compute preferred size
					Dimension preferredSize = new Dimension();
					for(int i = 0; i < panel2.getComponentCount(); i++) {
						Rectangle bounds = panel2.getComponent(i).getBounds();
						preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
						preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
					}
					Insets insets = panel2.getInsets();
					preferredSize.width += insets.right;
					preferredSize.height += insets.bottom;
					panel2.setMinimumSize(preferredSize);
					panel2.setPreferredSize(preferredSize);
				}
			}
			tabbedPane1.addTab("\u6f0f\u6d1e", panel2);

			//======== panel3 ========
			{
				panel3.setLayout(null);

				//======== scrollPane4 ========
				{
					scrollPane4.setViewportView(text_Log);
				}
				panel3.add(scrollPane4);
				scrollPane4.setBounds(5, 0, 890, 585);

				{
					// compute preferred size
					Dimension preferredSize = new Dimension();
					for(int i = 0; i < panel3.getComponentCount(); i++) {
						Rectangle bounds = panel3.getComponent(i).getBounds();
						preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
						preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
					}
					Insets insets = panel3.getInsets();
					preferredSize.width += insets.right;
					preferredSize.height += insets.bottom;
					panel3.setMinimumSize(preferredSize);
					panel3.setPreferredSize(preferredSize);
				}
			}
			tabbedPane1.addTab("\u65e5\u5fd7", panel3);
		}
		add(tabbedPane1);
		tabbedPane1.setBounds(-5, 25, 895, 615);

		//---- label2 ----
		label2.setText("PowerScanner By Brian.W AKA BigChan");
		add(label2);
		label2.setBounds(0, 0, 320, 25);

		{
			// compute preferred size
			Dimension preferredSize = new Dimension();
			for(int i = 0; i < getComponentCount(); i++) {
				Rectangle bounds = getComponent(i).getBounds();
				preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
				preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
			}
			Insets insets = getInsets();
			preferredSize.width += insets.right;
			preferredSize.height += insets.bottom;
			setMinimumSize(preferredSize);
			setPreferredSize(preferredSize);
		}

		//---- checkBox1 ----
		checkBox1.setText("text");

		//---- checkBox4 ----
		checkBox4.setText("text");

		//---- checkBox5 ----
		checkBox5.setText("text");
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JTabbedPane tabbedPane1;
	private JPanel panel1;
	private JCheckBox checkBox_Sqli;
	private JCheckBox checkBox_RCE;
	private JCheckBox checkBox6;
	private JScrollPane scrollPane2;
	private JTextArea textArea_RCE_cmd;
	private JCheckBox checkBox_PathTraversal;
	private JCheckBox checkBox_SensitiveFilesScan;
	private JScrollPane scrollPane3;
	private JTextArea textArea_SensitiveFiles;
	private JPanel panel2;
	private JScrollPane scrollPane1;
	private JTable table1;
	private JPanel panel3;
	private JScrollPane scrollPane4;
	private JTextArea text_Log;
	private JLabel label2;
	private JCheckBox checkBox1;
	private JCheckBox checkBox4;
	private JCheckBox checkBox5;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
