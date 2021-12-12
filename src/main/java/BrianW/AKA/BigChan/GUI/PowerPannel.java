/*
 * Created by JFormDesigner on Mon Dec 14 02:03:06 CST 2020
 */

package BrianW.AKA.BigChan.GUI;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executors;
import javax.swing.*;

import BrianW.AKA.BigChan.Tools.Config;
import BrianW.AKA.BigChan.Tools.Global;

/**
 * @author usual
 */
public class PowerPannel extends JPanel {
    public PowerPannel() {
        initComponents();
        Global.fixedThreadPool= Executors.newFixedThreadPool(Global.config.fetchCollaboratorMaxThreads);
        this.text_Log.append("inited\n");
        this.checkBox_PathTraversal.setSelected(Global.config.getConfigPathTraversalEnable_value());
        this.checkBox_Sqli.setSelected(Global.config.getConfigSqliEnable_value());
        this.checkBox_RCE.setSelected(Global.config.getConfigRCEEnable_value());
        this.checkBox_SensitiveFilesScan.setSelected(Global.config.getConfigSensitiveFilesScanEnable_value());
        this.textArea_RCE_cmd.setText(Global.config.getConfigRCEcmd_value());
        this.textArea_SensitiveFiles.setText(Global.config.getConfigSensitiveFiles_value());
        this.checkBox_SensitiveParam.setSelected(Global.config.getConfigSensitiveParamEnable_value());
        this.checkBox_RandomIP.setSelected(Global.config.getConfigRandomIPEnable_value());
        this.checkBox_RandomHost.setSelected(Global.config.getConfigRandomHostEnable_value());
        this.checkBox_RandomUA.setSelected(Global.config.getConfigRandomUAEnable_value());
        this.checkBox_ClearCookies.setSelected(Global.config.getConfigClearCookieEnable_value());
        this.checkBox_Json2Unicode.setSelected(Global.config.getConfigJson2UnicodeEnable_value());
        this.checkBox_fastjson.setSelected(Global.config.getConfigFastjsonEnable_value());
        this.checkBox_log4j.setSelected(Global.config.getConfigLog4jEnable_value());
        this.checkBox_fofa_ico.setSelected(Global.config.getConfigFofa_Ico_value());
        this.checkBox_fofa_title.setSelected(Global.config.getConfigFofa_Title_value());
        this.checkBox_fofa_ssl.setSelected(Global.config.getConfigFofa_SSL_value());
        this.checkBox_fofa_domain.setSelected(Global.config.getConfigFofa_Domain_value());
        this.textField_fofa_email.setText(Global.config.getConfigFofa_Email_value());
        this.textField_fofa_apikey.setText(Global.config.getConfigFofa_ApiKey_value());
        this.checkBox_requestRouter.setSelected(Global.config.getConfigRequestRouteEnable_value());
        this.textArea_requestRouter.setEnabled(Global.config.getConfigRequestRouteEnable_value());
        this.textArea_requestRouter.setText(Global.config.getConfigRequestRoute_value());
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

    private void checkBox_SensitiveParamActionPerformed(ActionEvent e) {
        Global.config.setConfigSensitiveParamEnable_value(this.checkBox_SensitiveParam.isSelected());
    }

    private void checkBox_RandomIPActionPerformed(ActionEvent e) {
        Global.config.setConfigRandomIPEnable_value(this.checkBox_RandomIP.isSelected());
    }

    private void checkBox_ClearCookiesActionPerformed(ActionEvent e) {
        Global.config.setConfigClearCookieEnable_value(this.checkBox_ClearCookies.isSelected());
    }

    private void checkBox_RandomUAActionPerformed(ActionEvent e) {
        Global.config.setConfigRandomUAEnable_value(this.checkBox_RandomUA.isSelected());
    }

    private void checkBox_RandomHostActionPerformed(ActionEvent e) {
        Global.config.setConfigRandomHostEnable_value(this.checkBox_RandomHost.isSelected());
    }

    private void checkBox_Json2UnicodeActionPerformed(ActionEvent e) {
        Global.config.setConfigJson2UnicodeEnable_value(this.checkBox_Json2Unicode.isSelected());
    }


    private void checkBox_log4jActionPerformed(ActionEvent e) {
        Global.config.setConfigLog4jEnable_value(this.checkBox_log4j.isSelected());
    }

    private void checkBox_fofa_icoActionPerformed(ActionEvent e) {
        Global.config.setConfigFofa_Ico_value(this.checkBox_fofa_ico.isSelected());
    }

    private void checkBox_fofa_titleActionPerformed(ActionEvent e) {
        Global.config.setConfigFofa_Title_value(this.checkBox_fofa_title.isSelected());
    }

    private void checkBox_fofa_sslActionPerformed(ActionEvent e) {
        Global.config.setConfigFofa_SSL_value(this.checkBox_fofa_ssl.isSelected());
    }

    private void checkBox_fofa_domainActionPerformed(ActionEvent e) {
        Global.config.setConfigFofa_Domain_value(this.checkBox_fofa_domain.isSelected());
    }

    private void textField_fofa_emailFocusLost(FocusEvent e) {
        Global.config.setConfigFofa_Email_value(this.textField_fofa_email.getText());
    }

    private void textField_fofa_apikeyFocusLost(FocusEvent e) {
        Global.config.setConfigFofa_ApiKey_value(this.textField_fofa_apikey.getText());
    }

    private void checkBox_fastjsonActionPerformed(ActionEvent e) {
        Global.config.setConfigFastjsonEnable_value(this.checkBox_fastjson.isSelected());
    }

    private void _requestRouter(ActionEvent e) {
        this.textArea_requestRouter.setEnabled(this.checkBox_requestRouter.isSelected());
        Global.config.setConfigRequestRouteEnable_value(this.checkBox_requestRouter.isSelected());
    }

    private void textPane_requestRouterFocusLost(FocusEvent e) {
        // TODO add your code here
    }

    private void textArea_requestRouterFocusLost(FocusEvent e) {
        String[] proxyList = this.textArea_requestRouter.getText().split("\\r?\\n");
        textField_requestRouterCheck.setText("");
        for (String proxyStr : proxyList) {
            Proxy proxy;
            try {
                proxy = new Proxy(
                        Proxy.Type.HTTP,
                        new InetSocketAddress(proxyStr.split(":")[0],
                                Integer.parseInt(proxyStr.split(":")[1]))
                );
                Global.config.setConfigRequestRoute_value(this.textArea_requestRouter.getText());
            } catch (Exception error) {
                textField_requestRouterCheck.setText(String.format("\"%s\" not a valid proxy", proxyStr));
            }
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - test
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
        checkBox_SensitiveParam = new JCheckBox();
        checkBox_fastjson = new JCheckBox();
        checkBox_log4j = new JCheckBox();
        panel4 = new JPanel();
        checkBox_RandomIP = new JCheckBox();
        checkBox_ClearCookies = new JCheckBox();
        checkBox_RandomUA = new JCheckBox();
        checkBox_RandomHost = new JCheckBox();
        checkBox_Json2Unicode = new JCheckBox();
        panel6 = new JPanel();
        checkBox_requestRouter = new JCheckBox();
        scrollPane5 = new JScrollPane();
        textArea_requestRouter = new JTextArea();
        textField_requestRouterCheck = new JTextField();
        panel5 = new JPanel();
        checkBox_fofa_ico = new JCheckBox();
        checkBox_fofa_title = new JCheckBox();
        checkBox_fofa_ssl = new JCheckBox();
        checkBox_fofa_domain = new JCheckBox();
        textField_fofa_email = new JTextField();
        label1 = new JLabel();
        label3 = new JLabel();
        textField_fofa_apikey = new JTextField();
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
        setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new
                javax.swing.border.EmptyBorder(0, 0, 0, 0), "JF\u006frmD\u0065sig\u006eer \u0045val\u0075ati\u006fn", javax
                .swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BOTTOM, new java
                .awt.Font("Dia\u006cog", java.awt.Font.BOLD, 12), java.awt
                .Color.red), getBorder()));
        addPropertyChangeListener(new java.beans.
                PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent e) {
                if ("\u0062ord\u0065r".
                        equals(e.getPropertyName())) throw new RuntimeException();
            }
        });
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
                checkBox_Sqli.setBounds(new Rectangle(new Point(30, 45), checkBox_Sqli.getPreferredSize()));

                //---- checkBox_RCE ----
                checkBox_RCE.setText("\u547d\u4ee4\u6ce8\u5165(\u4e0b\u65b9\u586b\u5199\u5355\u884c\u547d\u4ee4)");
                checkBox_RCE.addActionListener(e -> checkBox_RCEActionPerformed(e));
                panel1.add(checkBox_RCE);
                checkBox_RCE.setBounds(new Rectangle(new Point(30, 75), checkBox_RCE.getPreferredSize()));

                //---- checkBox6 ----
                checkBox6.setText("\u4f4e\u8c03\u6a21\u5f0f(\u6700\u5927\u7a0b\u5ea6\u51cf\u5c11\u53d1\u5305\u91cf\uff0c\u540c\u65f6\u63d0\u9ad8\u8bef\u62a5\u7387)");
                checkBox6.setSelected(true);
                checkBox6.setEnabled(false);
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
                scrollPane2.setBounds(30, 100, 270, 35);

                //---- checkBox_PathTraversal ----
                checkBox_PathTraversal.setText("\u8def\u5f84\u7a7f\u8d8a");
                checkBox_PathTraversal.addActionListener(e -> checkBox_PathTraversalActionPerformed(e));
                panel1.add(checkBox_PathTraversal);
                checkBox_PathTraversal.setBounds(new Rectangle(new Point(30, 145), checkBox_PathTraversal.getPreferredSize()));

                //---- checkBox_SensitiveFilesScan ----
                checkBox_SensitiveFilesScan.setText("\u654f\u611f\u6587\u4ef6\u63a2\u6d4b(\u4e0b\u65b9\u586b\u5199\u591a\u884c\u63a2\u6d4b\u6587\u4ef6\u5217\u8868)");
                checkBox_SensitiveFilesScan.addActionListener(e -> checkBox_SensitiveFilesScanActionPerformed(e));
                panel1.add(checkBox_SensitiveFilesScan);
                checkBox_SensitiveFilesScan.setBounds(30, 175, 220, checkBox_SensitiveFilesScan.getPreferredSize().height);

                //======== scrollPane3 ========
                {

                    //---- textArea_SensitiveFiles ----
                    textArea_SensitiveFiles.setText("robots.txt\nWEB-INF/web.xml\n.git/config\nadmin\nmanager");
                    textArea_SensitiveFiles.addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusLost(FocusEvent e) {
                            textArea_SensitiveFilesFocusLost(e);
                        }
                    });
                    scrollPane3.setViewportView(textArea_SensitiveFiles);
                }
                panel1.add(scrollPane3);
                scrollPane3.setBounds(35, 205, 265, 55);

                //---- checkBox_SensitiveParam ----
                checkBox_SensitiveParam.setText("\u62a5\u544a\u53ef\u80fd\u5b58\u5728\u6f0f\u6d1e\u7684\u654f\u611f\u53c2\u6570\u540d");
                checkBox_SensitiveParam.addActionListener(e -> checkBox_SensitiveParamActionPerformed(e));
                panel1.add(checkBox_SensitiveParam);
                checkBox_SensitiveParam.setBounds(30, 275, 280, 25);

                //---- checkBox_fastjson ----
                checkBox_fastjson.setText("Fastjson\u53cd\u5e8f\u5217\u5316");
                checkBox_fastjson.addActionListener(e -> checkBox_fastjsonActionPerformed(e));
                panel1.add(checkBox_fastjson);
                checkBox_fastjson.setBounds(30, 310, 190, checkBox_fastjson.getPreferredSize().height);

                //---- checkBox_log4j ----
                checkBox_log4j.setText("log4j RCE");
                checkBox_log4j.addActionListener(e -> checkBox_log4jActionPerformed(e));
                panel1.add(checkBox_log4j);
                checkBox_log4j.setBounds(30, 345, 190, checkBox_log4j.getPreferredSize().height);

                {
                    // compute preferred size
                    Dimension preferredSize = new Dimension();
                    for (int i = 0; i < panel1.getComponentCount(); i++) {
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
            tabbedPane1.addTab("\u6316\u6d1e", panel1);

            //======== panel4 ========
            {
                panel4.setLayout(null);

                //---- checkBox_RandomIP ----
                checkBox_RandomIP.setText("IP\u968f\u673a\u5316");
                checkBox_RandomIP.addActionListener(e -> checkBox_RandomIPActionPerformed(e));
                panel4.add(checkBox_RandomIP);
                checkBox_RandomIP.setBounds(30, 20, 215, checkBox_RandomIP.getPreferredSize().height);

                //---- checkBox_ClearCookies ----
                checkBox_ClearCookies.setText("\u6e05\u7a7aCookies");
                checkBox_ClearCookies.addActionListener(e -> checkBox_ClearCookiesActionPerformed(e));
                panel4.add(checkBox_ClearCookies);
                checkBox_ClearCookies.setBounds(new Rectangle(new Point(30, 50), checkBox_ClearCookies.getPreferredSize()));

                //---- checkBox_RandomUA ----
                checkBox_RandomUA.setText("User-agent\u968f\u673a\u5316");
                checkBox_RandomUA.addActionListener(e -> checkBox_RandomUAActionPerformed(e));
                panel4.add(checkBox_RandomUA);
                checkBox_RandomUA.setBounds(new Rectangle(new Point(30, 80), checkBox_RandomUA.getPreferredSize()));

                //---- checkBox_RandomHost ----
                checkBox_RandomHost.setText("Host\u968f\u673a\u5316");
                checkBox_RandomHost.addActionListener(e -> checkBox_RandomHostActionPerformed(e));
                panel4.add(checkBox_RandomHost);
                checkBox_RandomHost.setBounds(new Rectangle(new Point(30, 115), checkBox_RandomHost.getPreferredSize()));

                //---- checkBox_Json2Unicode ----
                checkBox_Json2Unicode.setText("json\u7f16\u7801");
                checkBox_Json2Unicode.addActionListener(e -> checkBox_Json2UnicodeActionPerformed(e));
                panel4.add(checkBox_Json2Unicode);
                checkBox_Json2Unicode.setBounds(30, 150, 125, 25);

                {
                    // compute preferred size
                    Dimension preferredSize = new Dimension();
                    for (int i = 0; i < panel4.getComponentCount(); i++) {
                        Rectangle bounds = panel4.getComponent(i).getBounds();
                        preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                        preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                    }
                    Insets insets = panel4.getInsets();
                    preferredSize.width += insets.right;
                    preferredSize.height += insets.bottom;
                    panel4.setMinimumSize(preferredSize);
                    panel4.setPreferredSize(preferredSize);
                }
            }
            tabbedPane1.addTab("BypassWAF", panel4);

            //======== panel6 ========
            {
                panel6.setLayout(null);

                //---- checkBox_requestRouter ----
                checkBox_requestRouter.setText("\u5f00\u542f\u8bf7\u6c42\u8def\u7531");
                checkBox_requestRouter.addActionListener(e -> _requestRouter(e));
                panel6.add(checkBox_requestRouter);
                checkBox_requestRouter.setBounds(25, 15, 150, checkBox_requestRouter.getPreferredSize().height);

                //======== scrollPane5 ========
                {

                    //---- textArea_requestRouter ----
                    textArea_requestRouter.setEnabled(false);
                    textArea_requestRouter.setText("127.0.0.1:8888\n127.0.0.1:9999");
                    textArea_requestRouter.addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusLost(FocusEvent e) {
                            textArea_requestRouterFocusLost(e);
                        }
                    });
                    scrollPane5.setViewportView(textArea_requestRouter);
                }
                panel6.add(scrollPane5);
                scrollPane5.setBounds(30, 50, 860, 455);

                //---- textField_requestRouterCheck ----
                textField_requestRouterCheck.setEditable(false);
                textField_requestRouterCheck.setForeground(new Color(204, 0, 51));
                panel6.add(textField_requestRouterCheck);
                textField_requestRouterCheck.setBounds(25, 520, 865, 45);

                {
                    // compute preferred size
                    Dimension preferredSize = new Dimension();
                    for (int i = 0; i < panel6.getComponentCount(); i++) {
                        Rectangle bounds = panel6.getComponent(i).getBounds();
                        preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                        preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                    }
                    Insets insets = panel6.getInsets();
                    preferredSize.width += insets.right;
                    preferredSize.height += insets.bottom;
                    panel6.setMinimumSize(preferredSize);
                    panel6.setPreferredSize(preferredSize);
                }
            }
            tabbedPane1.addTab("\u8bf7\u6c42\u8def\u7531", panel6);

            //======== panel5 ========
            {
                panel5.setLayout(null);

                //---- checkBox_fofa_ico ----
                checkBox_fofa_ico.setText("\u67e5\u627eico");
                checkBox_fofa_ico.addActionListener(e -> checkBox_fofa_icoActionPerformed(e));
                panel5.add(checkBox_fofa_ico);
                checkBox_fofa_ico.setBounds(10, 10, 140, 25);

                //---- checkBox_fofa_title ----
                checkBox_fofa_title.setText("title\u627e\u6e90");
                checkBox_fofa_title.addActionListener(e -> checkBox_fofa_titleActionPerformed(e));
                panel5.add(checkBox_fofa_title);
                checkBox_fofa_title.setBounds(10, 40, 125, checkBox_fofa_title.getPreferredSize().height);

                //---- checkBox_fofa_ssl ----
                checkBox_fofa_ssl.setText("ssl\u8bc1\u4e66\u627e\u6e90");
                checkBox_fofa_ssl.addActionListener(e -> checkBox_fofa_sslActionPerformed(e));
                panel5.add(checkBox_fofa_ssl);
                checkBox_fofa_ssl.setBounds(10, 70, 120, 25);

                //---- checkBox_fofa_domain ----
                checkBox_fofa_domain.setText("\u67e5\u627e\u5176\u4ed6\u76f8\u5173\u7f51\u7ad9");
                checkBox_fofa_domain.addActionListener(e -> checkBox_fofa_domainActionPerformed(e));
                panel5.add(checkBox_fofa_domain);
                checkBox_fofa_domain.setBounds(10, 100, 150, 25);

                //---- textField_fofa_email ----
                textField_fofa_email.setText("test@test.com");
                textField_fofa_email.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusLost(FocusEvent e) {
                        textField_fofa_emailFocusLost(e);
                    }
                });
                panel5.add(textField_fofa_email);
                textField_fofa_email.setBounds(100, 210, 235, 30);

                //---- label1 ----
                label1.setText("Email");
                panel5.add(label1);
                label1.setBounds(new Rectangle(new Point(15, 220), label1.getPreferredSize()));

                //---- label3 ----
                label3.setText("ApiKey");
                panel5.add(label3);
                label3.setBounds(new Rectangle(new Point(15, 265), label3.getPreferredSize()));

                //---- textField_fofa_apikey ----
                textField_fofa_apikey.setText("xxxxxxxxxxxxxxxxxxxxxxxx");
                textField_fofa_apikey.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusLost(FocusEvent e) {
                        textField_fofa_apikeyFocusLost(e);
                    }
                });
                panel5.add(textField_fofa_apikey);
                textField_fofa_apikey.setBounds(100, 260, 235, 25);

                {
                    // compute preferred size
                    Dimension preferredSize = new Dimension();
                    for (int i = 0; i < panel5.getComponentCount(); i++) {
                        Rectangle bounds = panel5.getComponent(i).getBounds();
                        preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                        preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                    }
                    Insets insets = panel5.getInsets();
                    preferredSize.width += insets.right;
                    preferredSize.height += insets.bottom;
                    panel5.setMinimumSize(preferredSize);
                    panel5.setPreferredSize(preferredSize);
                }
            }
            tabbedPane1.addTab("fofa", panel5);

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
                    for (int i = 0; i < panel2.getComponentCount(); i++) {
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
                    for (int i = 0; i < panel3.getComponentCount(); i++) {
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
            for (int i = 0; i < getComponentCount(); i++) {
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
    // Generated using JFormDesigner Evaluation license - test
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
    private JCheckBox checkBox_SensitiveParam;
    private JCheckBox checkBox_fastjson;
    private JCheckBox checkBox_log4j;
    private JPanel panel4;
    private JCheckBox checkBox_RandomIP;
    private JCheckBox checkBox_ClearCookies;
    private JCheckBox checkBox_RandomUA;
    private JCheckBox checkBox_RandomHost;
    private JCheckBox checkBox_Json2Unicode;
    private JPanel panel6;
    private JCheckBox checkBox_requestRouter;
    private JScrollPane scrollPane5;
    private JTextArea textArea_requestRouter;
    private JTextField textField_requestRouterCheck;
    private JPanel panel5;
    private JCheckBox checkBox_fofa_ico;
    private JCheckBox checkBox_fofa_title;
    private JCheckBox checkBox_fofa_ssl;
    private JCheckBox checkBox_fofa_domain;
    private JTextField textField_fofa_email;
    private JLabel label1;
    private JLabel label3;
    private JTextField textField_fofa_apikey;
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
