package BrianW.AKA.BigChan.Tools;

public class Config {
	private String configSectionGlobal = "";
	private String configSectionSqli;
	private String configSectionRCE;
	private String configSectionPathTraversal;
	private String configSectionSensiveFilesScan;
	public Config(String configSectionGlobal, String configSectionSqli, String configSectionRCE, String configSectionPathTraversal, String configSectionSensiveFilesScan) {
		this.configSectionGlobal = configSectionGlobal;
		this.configSectionSqli = configSectionSqli;
		this.configSectionRCE = configSectionRCE;
		this.configSectionPathTraversal = configSectionPathTraversal;
		this.configSectionSensiveFilesScan = configSectionSensiveFilesScan;
	}
	
	public Config setConfigSectionGlobal(String configSectionGlobal) {
		this.configSectionGlobal = configSectionGlobal;
		return this;
	}
	
	public Config setConfigSectionSqli(String configSectionSqli) {
		this.configSectionSqli = configSectionSqli;
		return this;
	}
	
	public Config setConfigSectionRCE(String configSectionRCE) {
		this.configSectionRCE = configSectionRCE;
		return this;
	}
	
	public Config setConfigSectionPathTraversal(String configSectionPathTraversal) {
		this.configSectionPathTraversal = configSectionPathTraversal;
		return this;
	}
	
	public Config setConfigSectionSensiveFilesScan(String configSectionSensiveFilesScan) {
		this.configSectionSensiveFilesScan = configSectionSensiveFilesScan;
		return this;
	}
	
	public Config() {
	
	}
}
