package BrianW.AKA.BigChan.Tools;

public class hitRst {
	private int cdoe;
	private String compareWithRespEvil_Same;
	private String compareWithRespEvil_Diff;
	
	public hitRst(
			int hitCode,
			String compareWithRespEvil_Diff,
			String compareWithRespEvil_Same
	
	) {
		this.cdoe = hitCode;
		this.compareWithRespEvil_Same = compareWithRespEvil_Same;
		this.compareWithRespEvil_Diff = compareWithRespEvil_Diff;
	}
	
	public int getCdoe() {
		return cdoe;
	}
	
	public void setCdoe(int cdoe) {
		this.cdoe = cdoe;
	}
	
	public String getCompareWithRespEvil_Same() {
		return compareWithRespEvil_Same;
	}
	
	public hitRst setCompareWithRespEvil_Same(String compareWithRespEvil_Same) {
		this.compareWithRespEvil_Same = compareWithRespEvil_Same;
		return this;
	}
	
	public String getCompareWithRespEvil_Diff() {
		return compareWithRespEvil_Diff;
	}
	
	public hitRst setCompareWithRespEvil_Diff(String compareWithRespEvil_Diff) {
		this.compareWithRespEvil_Diff = compareWithRespEvil_Diff;
		return this;
	}
}
