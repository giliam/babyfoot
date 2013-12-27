package core;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class Utils {
	public static String hash(String s){
		byte[] bytes = s.getBytes();
		byte[] hashTable = null;
		try {
			hashTable = MessageDigest.getInstance("MD5").digest(bytes);
		}
		catch(NoSuchAlgorithmException e){
			
		}
		StringBuffer hashString = new StringBuffer();
		int n = hashTable.length;
		for (int i = 0; i < n; i++)
		{
	        String hex = Integer.toHexString(hashTable[i]);
            if (hex.length() == 1){
            	hashString.append('0');
            	hashString.append(hex.charAt(hex.length() - 1));
            }
            else
            	hashString.append(hex.substring(hex.length() - 2));
		}
		return new String(hashString);
	}
	
	public static void main( String[] args ){
		System.out.println(Utils.hash("bobabcdefghijklmnopqrstuvwxyz1234567890.!:;,"));
	}
}
