package net.BukkitAuth.manage;

import java.security.MessageDigest;

public class Encrypt {
	public static String encryptData(String str) {
		StringBuilder sb = new StringBuilder();
		try {
			MessageDigest sha = MessageDigest.getInstance("SHA-256");
			sha.update(str.getBytes());
			byte[] shaencrypt = sha.digest();

			for (int i = 0; i < shaencrypt.length; i++) {
				sb.append(Integer.toHexString((int) shaencrypt[i] & 0xFF));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
