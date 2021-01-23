package BrianW.AKA.BigChan.Tools;


public class utils {
	/**
	 * @param str     原字符串
	 * @param sToFind 需要查找的字符串
	 * @return 返回在原字符串中sToFind出现的次数
	 */
	public static int countStr(String str, String sToFind) {
		int num = 0;
		while (str.contains(sToFind)) {
			str = str.substring(str.indexOf(sToFind) + sToFind.length());
			num++;
		}
		return num;
	}
	
	/**
	 * convert byte[] to HexString
	 *
	 * @param bArray
	 * @param length
	 * @return
	 */
	public static String bytesToHexString(byte[] bArray, int length) {
		StringBuffer sb = new StringBuffer(length);
		String sTemp;
		for (int i = 0; i < length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}
	
}

