package BrianW.AKA.BigChan.Tools;

import burp.IBurpExtenderCallbacks;
import org.ini4j.Ini;

import java.io.File;
import java.io.IOException;

public class Config {
	public int fetchCollaboratorMaxThreads = 20;
	public long fetchCollaboratorWaitSecond = 5;
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
	
	private String configSensitiveParamEnable_key = "SensiveParamEnable";
	private Boolean configSensitiveParamEnable_value = true;
	
	private String configRandomIPEnable_key = "RandomIPEnable";
	private Boolean configRandomIPEnable_value = true;
	
	private String configClearCookieEnable_key = "ClearCookieEnable";
	private Boolean configClearCookieEnable_value = true;
	
	private String configRandomUAEnable_key = "RandomUAEnable";
	private Boolean configRandomUAEnable_value = true;
	
	private String configRandomHostEnable_key = "RandomHostEnable";
	private Boolean configRandomHostEnable_value = true;

	private String configJson2UnicodeEnable_key = "Json2UnicodeEnable";
	private Boolean configJson2UnicodeEnable_value = true;

	private String configFastjsonEnable_key = "FastjsonEnableEnable";
	private Boolean configFastjsonEnable_value = true;

	private String configLog4jEnable_key = "Log4jEnable";
	private Boolean configLog4jEnable_value = true;

	private String configRequestRouteEnable_key = "RequestRouteEnable";
	private Boolean configRequestRouteEnable_value = true;
	private String configRequestRoute_key = "RequestRoute";
	private String configRequestRoute_value = "127.0.0.1:9999\n127.0.0.1:8888";
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
	private String configSectionGetFofaInfo = "GetFofaInfo";
	private String configFofa_Ico_key = "Fofa_Ico";
	private Boolean configFofa_Ico_value = true;
	private String configFofa_Title_key = "Fofa_Title";
	private Boolean configFofa_Title_value = true;
	private String configFofa_SSL_key = "Fofa_SSL";
	private Boolean configFofa_SSL_value = true;
	private String configFofa_Domain_key = "Fofa_Domain";
	private Boolean configFofa_Domain_value = true;
	private String configFofa_Email_key = "Fofa_Email";
	private String configFofa_Email_value = "test@hotmail.com";
	private String configFofa_ApiKey_key = "Fofa_ApiKey";
	private String configFofa_ApiKey_value = "xxxxxxxxxxxxxxxxxxxxxxxxxx";
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
				this.configSensitiveParamEnable_value = Boolean.valueOf(ini.get(configSectionGlobal, configSensitiveParamEnable_key));
				this.configRandomIPEnable_value = Boolean.valueOf(ini.get(configSectionGlobal, configRandomIPEnable_key));
				this.configClearCookieEnable_value = Boolean.valueOf(ini.get(configSectionGlobal, configClearCookieEnable_key));
				this.configRandomUAEnable_value = Boolean.valueOf(ini.get(configSectionGlobal, configRandomUAEnable_key));
				this.configRandomHostEnable_value = Boolean.valueOf(ini.get(configSectionGlobal, configRandomHostEnable_key));
				this.configRCEcmd_value = ini.get(configSectionRCE, configRCEcmd_key);
				this.configSensitiveFiles_value = ini.get(configSectionSensiveFilesScan, configSensitiveFiles_key);
				this.configJson2UnicodeEnable_value = Boolean.valueOf(ini.get(configSectionGlobal, configJson2UnicodeEnable_key));
				this.configLog4jEnable_value = Boolean.valueOf(ini.get(configSectionGlobal, configLog4jEnable_key));
				this.configFastjsonEnable_value = Boolean.valueOf(ini.get(configSectionGlobal, configFastjsonEnable_key));
				this.configFofa_Ico_value = Boolean.valueOf(ini.get(configSectionGetFofaInfo, configFofa_Ico_key));
				this.configFofa_Title_value = Boolean.valueOf(ini.get(configSectionGetFofaInfo, configFofa_Title_key));
				this.configFofa_SSL_value = Boolean.valueOf(ini.get(configSectionGetFofaInfo, configFofa_SSL_key));
				this.configFofa_Domain_value = Boolean.valueOf(ini.get(configSectionGetFofaInfo, configFofa_Domain_key));
				this.configFofa_Email_value = String.valueOf(ini.get(configSectionGetFofaInfo, configFofa_Email_key));
				this.configFofa_ApiKey_value = String.valueOf(ini.get(configSectionGetFofaInfo, configFofa_ApiKey_key));
				this.configRequestRouteEnable_value = Boolean.valueOf(ini.get(configSectionGlobal, configRequestRouteEnable_key));
				this.configRequestRoute_value = ini.get(configSectionGlobal, configRequestRoute_key);
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
				ini.add(configSectionGlobal,
						configSensitiveParamEnable_key,
						configSensitiveParamEnable_value
				);
				ini.add(configSectionGlobal,
						configRandomIPEnable_key,
						configRandomIPEnable_value
				);
				ini.add(configSectionGlobal,
						configClearCookieEnable_key,
						configClearCookieEnable_value
				);
				ini.add(configSectionGlobal,
						configRandomUAEnable_key,
						configRandomUAEnable_value
				);
				ini.add(configSectionGlobal,
						configRandomHostEnable_key,
						configRandomHostEnable_value
				);
				ini.add(configSectionGlobal,
						configJson2UnicodeEnable_key,
						configJson2UnicodeEnable_value
				);
				ini.add(configSectionGlobal,
						configFastjsonEnable_key,
						configFastjsonEnable_value
				);
				ini.add(configSectionGlobal,
						configLog4jEnable_key,
						configLog4jEnable_value
				);
				ini.add(configSectionGlobal,
						configRequestRouteEnable_key,
						configRequestRouteEnable_value
				);
				ini.add(configSectionGlobal,
						configRequestRoute_key,
						configRequestRoute_value
				);
				ini.add(configSectionGetFofaInfo,
						configFofa_Ico_key,
						configFofa_Ico_value
				);
				ini.add(configSectionGetFofaInfo,
						configFofa_Title_key,
						configFofa_Title_value
				);
				ini.add(configSectionGetFofaInfo,
						configFofa_SSL_key,
						configFofa_SSL_value
				);
				ini.add(configSectionGetFofaInfo,
						configFofa_Domain_key,
						configFofa_Domain_value
				);
				ini.add(configSectionGetFofaInfo,
						configFofa_Email_key,
						configFofa_Email_value
				);
				ini.add(configSectionGetFofaInfo,
						configFofa_ApiKey_key,
						configFofa_ApiKey_value
				);
				//将文件内容保存到文件中
				ini.store(this.file);
				
			}
		} catch (IOException e) {
			callbacks.printError(e.toString());
			callbacks.printError(Utils.getStackMsg(e));
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
	
	public Config setConfigSensitiveParamEnable_value(Boolean configSensitiveParamEnable_value) {
		setValue(this.configSectionGlobal,
				this.configSensitiveParamEnable_key,
				configSensitiveParamEnable_value.toString()
		);
		this.configSensitiveParamEnable_value = configSensitiveParamEnable_value;
		return this;
	}
	
	public Config setConfigRandomIPEnable_value(Boolean configRandomIPEnable_value) {
		setValue(this.configSectionGlobal,
				this.configRandomIPEnable_key,
				configRandomIPEnable_value.toString()
		);
		this.configRandomIPEnable_value = configRandomIPEnable_value;
		return this;
	}
	
	public Config setConfigClearCookieEnable_value(Boolean configClearCookieEnable_value) {
		setValue(this.configSectionGlobal,
				this.configClearCookieEnable_key,
				configClearCookieEnable_value.toString()
		);
		this.configClearCookieEnable_value = configClearCookieEnable_value;
		return this;
	}
	
	public Config setConfigRandomUAEnable_value(Boolean configRandomUAEnable_value) {
		setValue(this.configSectionGlobal,
				this.configRandomUAEnable_key,
				configRandomUAEnable_value.toString()
		);
		this.configRandomUAEnable_value = configRandomUAEnable_value;
		return this;
	}
	
	public Config setConfigRandomHostEnable_value(Boolean configRandomHostEnable_value) {
		setValue(this.configSectionGlobal,
				this.configRandomHostEnable_key,
				configRandomHostEnable_value.toString()
		);
		this.configRandomHostEnable_value = configRandomHostEnable_value;
		return this;
	}

	public Config setConfigJson2UnicodeEnable_value(Boolean configJson2UnicodeEnable_value) {
		setValue(this.configSectionGlobal,
				this.configJson2UnicodeEnable_key,
				configJson2UnicodeEnable_value.toString()
		);
		this.configJson2UnicodeEnable_value = configJson2UnicodeEnable_value;
		return this;
	}

	public Config setConfigFastjsonEnable_value(Boolean configFastjsonEnable_value) {
		setValue(this.configSectionGlobal,
				this.configFastjsonEnable_key,
				configFastjsonEnable_value.toString()
		);
		this.configFastjsonEnable_value = configFastjsonEnable_value;
		return this;
	}

	public Config setConfigLog4jEnable_value(Boolean configLog4jEnable_value) {
		setValue(this.configSectionGlobal,
				this.configLog4jEnable_key,
				configLog4jEnable_value.toString()
		);
		this.configLog4jEnable_value = configLog4jEnable_value;
		return this;
	}

	public Config setConfigRequestRouteEnable_value(Boolean configRequestRouteEnable_value) {
		setValue(this.configSectionGlobal,
				this.configRequestRouteEnable_key,
				configRequestRouteEnable_value.toString()
		);
		this.configRequestRouteEnable_value = configRequestRouteEnable_value;
		return this;
	}

	public Config setConfigRequestRoute_value(String configRequestRoute_value) {
		setValue(this.configSectionGlobal,
				this.configRequestRoute_key,
				configRequestRoute_value
		);
		this.configRequestRoute_value = configRequestRoute_value;
		return this;
	}

	public Config setConfigFofa_Ico_value(Boolean configFofa_Ico_value) {
		setValue(this.configSectionGetFofaInfo,
				this.configFofa_Ico_key,
				configFofa_Ico_value.toString()
		);
		this.configFofa_Ico_value = configFofa_Ico_value;
		return this;
	}

	public void setConfigFofa_Title_value(Boolean configFofa_Title_value) {
		setValue(this.configSectionGetFofaInfo,
				this.configFofa_Title_key,
				configFofa_Title_value.toString()
		);
		this.configFofa_Title_value = configFofa_Title_value;
	}

	public void setConfigFofa_SSL_value(Boolean configFofa_SSL_value) {
		setValue(this.configSectionGetFofaInfo,
				this.configFofa_SSL_key,
				configFofa_SSL_value.toString()
		);
		this.configFofa_SSL_value = configFofa_SSL_value;
	}

	public void setConfigFofa_Domain_value(Boolean configFofa_Domain_value) {
		setValue(this.configSectionGetFofaInfo,
				this.configFofa_Domain_key,
				configFofa_Domain_value.toString()
		);
		this.configFofa_Domain_value = configFofa_Domain_value;
	}

	public void setConfigFofa_Email_value(String configFofa_Email_value) {
		setValue(this.configSectionGetFofaInfo,
				this.configFofa_Email_key,
				configFofa_Email_value.toString()
		);
		this.configFofa_Email_value = configFofa_Email_value;
	}

	public void setConfigFofa_ApiKey_value(String configFofa_ApiKey_value) {
		setValue(this.configSectionGetFofaInfo,
				this.configFofa_ApiKey_key,
				configFofa_ApiKey_value.toString()
		);
		this.configFofa_ApiKey_value = configFofa_ApiKey_value;
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
	
	public Boolean getConfigSensitiveParamEnable_value() {
		return configSensitiveParamEnable_value;
	}
	
	public Boolean getConfigRandomIPEnable_value() {
		return configRandomIPEnable_value;
	}
	
	public Boolean getConfigClearCookieEnable_value() {
		return configClearCookieEnable_value;
	}
	
	public Boolean getConfigRandomUAEnable_value() {
		return configRandomUAEnable_value;
	}
	
	public Boolean getConfigRandomHostEnable_value() {
		return configRandomHostEnable_value;
	}

	public Boolean getConfigJson2UnicodeEnable_value() {
		return configJson2UnicodeEnable_value;
	}

	public Boolean getConfigFastjsonEnable_value() {
		return configFastjsonEnable_value;
	}

	public Boolean getConfigLog4jEnable_value() {
		return configLog4jEnable_value;
	}

	public Boolean getConfigRequestRouteEnable_value() {
		return configRequestRouteEnable_value;
	}

	public String getConfigRequestRoute_value() {
		return configRequestRoute_value;
	}

	public Boolean getConfigFofa_Ico_value() {
		return configFofa_Ico_value;
	}

	public Boolean getConfigFofa_Title_value() {
		return configFofa_Title_value;
	}

	public Boolean getConfigFofa_SSL_value() {
		return configFofa_SSL_value;
	}

	public Boolean getConfigFofa_Domain_value() {
		return configFofa_Domain_value;
	}

	public String getConfigFofa_Email_value() {
		return configFofa_Email_value;
	}

	public String getConfigFofa_ApiKey_value() {
		return configFofa_ApiKey_value;
	}

	private void setValue(String section, String key, String value){
		try {
			Ini ini = new Ini(this.file);
			ini.put(section, key, value);
			ini.store();
		} catch (IOException e) {
			callbacks.printError(e.toString());
			callbacks.printError(Utils.getStackMsg(e));
		}
	}
}
