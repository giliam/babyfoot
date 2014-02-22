package clientCore;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Hashtable;

/** Cette classe dipose de fonctions utiles qui peuvent être nécessaires par toutes les parties du programme. Elle contient aussi
les constantes nécessaires (par exemple de type enum) concernant le programme. */
public abstract class Utils {
	public static enum Types { ONEVSONE, TWOVSTWO, ONEVSTWO };
	public static enum States { WAITING, FULL, PLAYING, FINISHED };  
	public static enum Sides { DOWN, UP };
	
	public static final int MATCH_END = 100;
	
	public static enum RodPositions { GARDIEN , DEFENSE, MILIEU, ATTAQUE };

	public static enum CollisionType { SIDES, UPANDDOWN };
	
	public static final String SEPARATOR = ";";
	
	public static final int GOAL_SIZE = 2*100;
	public static final int LINE_STRENGTH = 4;
	public static final int GAP_EDGE = 2*20;
	public static final int IMAGE_PLAYER_Y = 38;
	public static final int IMAGE_PLAYER_X = 30;
	public static final int MOVE_STEP = 20;
	public static final int BALL_RADIUS = 15;
	
	public static final int HEIGHT = 700;
	public static final int WIDTH = 900;

	public static final int MAX_INITIAL_SPEED = 15;
	
	public static final int GARDIEN_POSITION = GAP_EDGE+30;
	public static final int DEFENSE_POSITION = GAP_EDGE+30+100;
	public static final int MILIEU_POSITION = (WIDTH-LINE_STRENGTH)/2-70;
	public static final int ATTAQUE_POSITION = WIDTH-Utils.LINE_STRENGTH-Utils.GAP_EDGE-230;	
	
	
	@SuppressWarnings("serial")
	public static final Hashtable<RodPositions, Integer> Y_STAGGERING_DEFAULT = new Hashtable<RodPositions, Integer>(){{ put(RodPositions.GARDIEN, 100); put(RodPositions.DEFENSE, 150 ); put(RodPositions.MILIEU, 100); put(RodPositions.ATTAQUE, 100); } };
	
	

	
	/** Hache un mot de passe en se basant sur le principe de l'algorithme MD5. Retourne la chaine.
	@param s contient le mot de passe à chiffrer. */
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
	
	/** Formate un array en renvoyant un array. C'est nécessaire lorsque le paramètre contient des chaines
	avec des informations concaténées par des tirets. L'array de sortie ne contient que ces informations.
	@param list est un ensemble de chaines de caractères de la forme "foo - bar". Le programme récupère bar et renvoie le tableau des "bar" */
	public static String[] formatStringArray(String[] list){
		for(int i = 0; i<list.length; i++){
			String[] m = list[i].split(" - ",2);
			list[i] = m[1];
		}
		return list;
	}
	
	
	/** Retourne une date formatée à partir d'une chaine de caractère de type timestamp. 
	@param date est de type Timestamp */
	public static String formatDate(String date){
		Date d = new Date((long)Integer.valueOf(date));
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM hh:mm:ss");
		return formatter.format(d);
	}
	
	/** Affiche un array */
	public static void printArray(String[] s){
		for( int i = 0; i<s.length; i++){
			System.out.println(i + ". " + s[i] );
		}
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
