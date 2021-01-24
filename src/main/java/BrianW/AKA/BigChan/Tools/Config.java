package BrianW.AKA.BigChan.Tools;

import burp.IBurpExtenderCallbacks;
import com.sun.jdi.Value;
import org.ini4j.Ini;

import java.io.File;
import java.io.IOException;

public class Config {
	private IBurpExtenderCallbacks callbacks;
	private File file = new File(Global.configFile);
	private String configSectionGlobal = "Global";
	
	private String configSqliEnable_key = "SqliEnable";
	private Boolean configSqliEnable_value = true;
	
	private String configRCEEnable_key = "RCEEnable";
	private Boolean configRCEEnable_value = true;
	
	
	private String configPathTraversalEnable_key = "PathTraversalEnable";
	private Boolean configPathTraversalEnable_value = true;
	
	private String configSensitiveFilesScanEnable_key = "SensiveFilesScanEnable";
	private Boolean configSensitiveFilesScanEnable_value = true;
	
	//=====================================================
	private String configSectionSqli = "Sqli";
	//=====================================================
	private String configSectionRCE = "RCE";
	private String configRCEcmd_key = "RCEcmd";
	private String configRCEcmd_value = "ping -n 3";
	//=====================================================
	private String configSectionPathTraversal = "PathTraversal";
	//=====================================================
	private String configSectionSensiveFilesScan = "SensiveFilesScan";
	private String configSensitiveFiles_key = "SensitiveFiles";
	private String configSensitiveFiles_value = "robots.txt\nWEB-INF/web.xml\n.git/config\nadmin\nmanager";
	//=====================================================
	
	public Config(IBurpExtenderCallbacks callbacks) {
		this.callbacks = callbacks;
		try {
			callbacks.printOutput(file.getAbsolutePath());
			Ini ini = new Ini();
			if (this.file.exists()) {
				ini.load(this.file);
				callbacks.printOutput("config file exist");
				this.configSqliEnable_value = Boolean.valueOf(ini.get(configSectionGlobal, configSqliEnable_key));
				this.configRCEEnable_value = Boolean.valueOf(ini.get(configSectionGlobal, configRCEEnable_key));
				this.configPathTraversalEnable_value = Boolean.valueOf(ini.get(configSectionGlobal, configPathTraversalEnable_key));
				this.configSensitiveFilesScanEnable_value = Boolean.valueOf(ini.get(configSectionGlobal, configSensitiveFilesScanEnable_key));
				this.configRCEcmd_value = ini.get(configSectionRCE, configRCEcmd_key);
				this.configSensitiveFiles_value = ini.get(configSectionSensiveFilesScan, configSensitiveFiles_key);
			} else {
				callbacks.printOutput("config file not exist");
				Boolean r = this.file.createNewFile();
				ini.load(this.file);
				ini.add(configSectionGlobal,
						configSqliEnable_key,
						configSqliEnable_value
				);
				ini.add(configSectionGlobal,
						configRCEEnable_key,
						configRCEEnable_value
				);
				ini.add(configSectionGlobal,
						configPathTraversalEnable_key,
						configPathTraversalEnable_value
				);
				ini.add(configSectionGlobal,
						configSensitiveFilesScanEnable_key,
						configSensitiveFilesScanEnable_value
				);
				ini.add(configSectionGlobal,
						configSensitiveFilesScanEnable_key,
						configSensitiveFilesScanEnable_value
				);
				ini.add(configSectionRCE,
						configRCEcmd_key,
						configRCEcmd_value
				);
				ini.add(configSectionSensiveFilesScan,
						configSensitiveFiles_key,
						configSensitiveFiles_value
				);
				//将文件内容保存到文件中
				ini.store(this.file);
				
			}
		} catch (IOException e) {
			callbacks.printError(e.toString());
			callbacks.printError(utils.getStackMsg(e));
		}
	}
	
	public Config setConfigSqliEnable_value(Boolean configSqliEnable_value) {
		setValue(this.configSectionGlobal,
				this.configSqliEnable_key,
				String.valueOf(configSqliEnable_value)
		);
		this.configSqliEnable_value = configSqliEnable_value;
		return this;
	}
	
	public Config setConfigRCEEnable_value(Boolean configRCEEnable_value) {
		setValue(this.configSectionGlobal,
				this.configRCEEnable_key,
				String.valueOf(configRCEEnable_value)
		);
		this.configRCEEnable_value = configRCEEnable_value;
		return this;
	}
	
	public Config setConfigPathTraversalEnable_value(Boolean configPathTraversalEnable_value) {
		setValue(this.configSectionGlobal,
				this.configPathTraversalEnable_key,
				String.valueOf(configPathTraversalEnable_value)
		);
		this.configPathTraversalEnable_value = configPathTraversalEnable_value;
		return this;
	}
	
	public Config setConfigSensitiveFilesScanEnable_value(Boolean configSensitiveFilesScanEnable_value) {
		setValue(this.configSectionGlobal,
				this.configSensitiveFilesScanEnable_key,
				String.valueOf(configSensitiveFilesScanEnable_value)
		);
		this.configSensitiveFilesScanEnable_value = configSensitiveFilesScanEnable_value;
		return this;
	}
	
	public Config setConfigRCEcmd_value(String configRCEcmd_value) {
		setValue(this.configSectionRCE,
				this.configRCEcmd_key,
				configRCEcmd_value
		);
		this.configRCEcmd_value = configRCEcmd_value;
		return this;
	}
	
	public Config setConfigSensitiveFiles_value(String configSensitiveFiles_value) {
		setValue(this.configSectionSensiveFilesScan,
				this.configSensitiveFiles_key,
				configSensitiveFiles_value
		);
		this.configSensitiveFiles_value = configSensitiveFiles_value;
		return this;
	}
	
	public Boolean getConfigSqliEnable_value() {
		return configSqliEnable_value;
	}
	
	public Boolean getConfigRCEEnable_value() {
		return configRCEEnable_value;
	}
	
	public Boolean getConfigPathTraversalEnable_value() {
		return configPathTraversalEnable_value;
	}
	
	public Boolean getConfigSensitiveFilesScanEnable_value() {
		return configSensitiveFilesScanEnable_value;
	}
	
	public String getConfigRCEcmd_value() {
		return configRCEcmd_value;
	}
	
	public String getConfigSensitiveFiles_value() {
		return configSensitiveFiles_value;
	}
	
	private void setValue(String section, String key, String value){
		try {
			Ini ini = new Ini(this.file);
			ini.put(section, key, value);
			ini.store();
		} catch (IOException e) {
			callbacks.printError(e.toString());
			callbacks.printError(utils.getStackMsg(e));
		}
	}
}
