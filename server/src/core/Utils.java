package core;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.text.SimpleDateFormat;

public abstract class Utils {
	public static final int MATCH_END = 100;
	
	public static enum Sides { BOTTOM, UP };
	
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
	
	public static String[] formatStringArray(String[] list){
		for(int i = 0; i<list.length; i++){
			String[] m = list[i].split(" - ",2);
			list[i] = m[1];
		}
		return list;
	}
	
	public static String formatDate(String date){
		Date d = new Date((long)Integer.valueOf(date));
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM hh:mm:ss");
		return formatter.format(d);
	}
	
	/** Affiche un array de à deux dimensions */
	public static void printArray(int[][] s){
		for( int i = 0; i<s.length; i++){
			for( int j = 0; j<s[0].length; j++){
				System.out.println(i + ". " + s[i][j] );
			}
		}
	}
}
