/*
 * Created by JFormDesigner on Mon Dec 14 02:03:06 CST 2020
 */

package BrianW.AKA.BigChan.GUI;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import BrianW.AKA.BigChan.Tools.Global;
import org.ini4j.Ini;
/**
 * @author usual
 */
public class PowerPannel extends JPanel {
	public PowerPannel() {
		initComponents();
		
	}
	private boolean initConfigFile() throws IOException {
		File file = new File(Global.configFile);
		if(file.exists()){
			return false;
		}
		file.createNewFile();
		Ini ini = new Ini();
		ini.load(file);
		ini.add(Global.configIniSectionGlobal,
				"entity.getKey()",
				""
				);
		//将文件内容保存到文件中
		ini.store(file);
		return true;
	}
	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		tabbedPane1 = new JTabbedPane();
		panel1 = new JPanel();
		checkBox2 = new JCheckBox();
		checkBox3 = new JCheckBox();
		checkBox6 = new JCheckBox();
		scrollPane2 = new JScrollPane();
		textArea1 = new JTextArea();
		checkBox7 = new JCheckBox();
		checkBox8 = new JCheckBox();
		scrollPane3 = new JScrollPane();
		textArea2 = new JTextArea();
		panel2 = new JPanel();
		scrollPane1 = new JScrollPane();
		table1 = new JTable();
		panel3 = new JPanel();
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

				//---- checkBox2 ----
				checkBox2.setText("\u6ce8\u5165\u6d4b\u8bd5");
				panel1.add(checkBox2);
				checkBox2.setBounds(new Rectangle(new Point(15, 175), checkBox2.getPreferredSize()));

				//---- checkBox3 ----
				checkBox3.setText("\u547d\u4ee4\u6ce8\u5165(\u4e0b\u65b9\u586b\u5199\u5355\u884c\u547d\u4ee4)");
				panel1.add(checkBox3);
				checkBox3.setBounds(new Rectangle(new Point(15, 205), checkBox3.getPreferredSize()));

				//---- checkBox6 ----
				checkBox6.setText("\u4f4e\u8c03\u6a21\u5f0f(\u6700\u5927\u7a0b\u5ea6\u51cf\u5c11\u53d1\u5305\u91cf\uff0c\u540c\u65f6\u63d0\u9ad8\u8bef\u62a5\u7387)");
				panel1.add(checkBox6);
				checkBox6.setBounds(new Rectangle(new Point(10, 10), checkBox6.getPreferredSize()));

				//======== scrollPane2 ========
				{

					//---- textArea1 ----
					textArea1.setText("ping -c 3");
					scrollPane2.setViewportView(textArea1);
				}
				panel1.add(scrollPane2);
				scrollPane2.setBounds(15, 230, 270, 35);

				//---- checkBox7 ----
				checkBox7.setText("\u8def\u5f84\u7a7f\u8d8a");
				panel1.add(checkBox7);
				checkBox7.setBounds(new Rectangle(new Point(15, 275), checkBox7.getPreferredSize()));

				//---- checkBox8 ----
				checkBox8.setText("\u654f\u611f\u6587\u4ef6\u63a2\u6d4b(\u4e0b\u65b9\u586b\u5199\u591a\u884c\u63a2\u6d4b\u6587\u4ef6\u5217\u8868)");
				panel1.add(checkBox8);
				checkBox8.setBounds(15, 305, 220, checkBox8.getPreferredSize().height);

				//======== scrollPane3 ========
				{

					//---- textArea2 ----
					textArea2.setText("robots.txt");
					scrollPane3.setViewportView(textArea2);
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
	private JCheckBox checkBox2;
	private JCheckBox checkBox3;
	private JCheckBox checkBox6;
	private JScrollPane scrollPane2;
	private JTextArea textArea1;
	private JCheckBox checkBox7;
	private JCheckBox checkBox8;
	private JScrollPane scrollPane3;
	private JTextArea textArea2;
	private JPanel panel2;
	private JScrollPane scrollPane1;
	private JTable table1;
	private JPanel panel3;
	private JLabel label2;
	private JCheckBox checkBox1;
	private JCheckBox checkBox4;
	private JCheckBox checkBox5;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
