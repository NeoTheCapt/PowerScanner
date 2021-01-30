package BrianW.AKA.BigChan.GUI;

import BrianW.AKA.BigChan.Tools.Global;
import BrianW.AKA.BigChan.Tools.InteractionServer;
import burp.IBurpExtenderCallbacks;
import burp.ITab;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.util.Objects;

public class PowerTab implements ITab {
	public PowerPannel PowerPannel = new PowerPannel();
	Integer tabIndex;
	JTabbedPane tabPane;
	IBurpExtenderCallbacks callbacks;
	public PowerTab(IBurpExtenderCallbacks callbacks) {
		this.callbacks = callbacks;
		Global.interactionServer = new InteractionServer(callbacks);
		Global.interactionServer.start();
	}

	public void findTab() {
		if(tabIndex != null) {
			return;
		}
		tabPane = (JTabbedPane) PowerPannel.getParent();
		if(tabPane == null) {
			return;
		}
		for(int i = 0; i < tabPane.getTabCount(); i++) {
			if(Objects.equals(tabPane.getTitleAt(i), getTabCaption())) {
				tabIndex = i;
			}
		}
		tabPane.addChangeListener((ChangeEvent e1) -> {
			if(tabPane.getSelectedIndex() == tabIndex) {
				tabPane.setBackgroundAt(tabIndex, Color.BLACK);
			}
		});
	}
	
	void alertTab() {
		tabPane.setBackgroundAt(tabIndex, new Color((float) 0.894, (float) 0.535, (float) 0.0));
	}
	
	@Override
	public String getTabCaption() {
		return "PowerScanner";
	}
	
	@Override
	public PowerPannel getUiComponent() {
		return PowerPannel;
	}
	
}
