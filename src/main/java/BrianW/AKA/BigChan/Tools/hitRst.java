package BrianW.AKA.BigChan.Tools;

public class hitRst {
	private int cdoe;
	private String compareWithNegative_Same;
	private String compareWithNegative_Diff;
	private String compareWithPositive_Same;
	private String compareWithPositive_Diff;
	
	
	public hitRst(
			int hitCode,
			String compareWithNegative_Diff,
			String compareWithNegative_Same,
			String compareWithPositive_Diff,
			String compareWithPositive_Same
	) {
		this.cdoe = hitCode;
		this.compareWithNegative_Same = compareWithNegative_Same;
		this.compareWithNegative_Diff = compareWithNegative_Diff;
		this.compareWithPositive_Diff = compareWithPositive_Diff;
		this.compareWithPositive_Same = compareWithPositive_Same;
	}
	
	public int getCdoe() {
		return cdoe;
	}
	
	public String getCompareWithPositive_Same() {
		return compareWithPositive_Same;
	}
	
	public String getCompareWithPositive_Diff() {
		return compareWithPositive_Diff;
	}
	
	public hitRst setCompareWithPositive_Same(String compareWithPositive_Same) {
		this.compareWithPositive_Same = compareWithPositive_Same;
		return this;
	}
	
	public hitRst setCompareWithPositive_Diff(String compareWithPositive_Diff) {
		this.compareWithPositive_Diff = compareWithPositive_Diff;
		return this;
	}
	
	public void setCdoe(int cdoe) {
		this.cdoe = cdoe;
	}
	
	public String getCompareWithNegative_Same() {
		return compareWithNegative_Same;
	}
	
	public hitRst setCompareWithNegative_Same(String compareWithNegative_Same) {
		this.compareWithNegative_Same = compareWithNegative_Same;
		return this;
	}
	
	public String getCompareWithNegative_Diff() {
		return compareWithNegative_Diff;
	}
	
	public hitRst setCompareWithNegative_Diff(String compareWithNegative_Diff) {
		this.compareWithNegative_Diff = compareWithNegative_Diff;
		return this;
	}
}
