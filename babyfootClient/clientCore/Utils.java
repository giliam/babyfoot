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
	
	public static enum Rod { GARDIEN , DEFENSE, MILIEU, ATTAQUE };
	public static enum RodStatus { NORMAL, SHOOTING, HOLDING };  
	
	public static enum CollisionType { SIDES, UPANDDOWN };
	
	public static final String SEPARATOR = ";";
	
	public static final int GOAL_SIZE = 2*100;
	public static final int LINE_STRENGTH = 4;
	public static final int GAP_EDGE = 2*20;
	public static final int IMAGE_PLAYER_Y = 38;
	public static final int IMAGE_PLAYER_X = 30;
	public static final int MOVE_STEP = 10;
	public static final int BALL_RADIUS = 15;
	
	public static final int HEIGHT = 700;
	public static final int WIDTH = 900;

	public static final int MAX_INITIAL_SPEED = 4;
	
	public static final int GARDIEN_POSITION = GAP_EDGE+30;
	public static final int DEFENSE_POSITION = GAP_EDGE+30+100;
	public static final int MILIEU_POSITION = (WIDTH-LINE_STRENGTH)/2-70;
	public static final int ATTAQUE_POSITION = WIDTH-Utils.LINE_STRENGTH-Utils.GAP_EDGE-230;
	
	
	private static int sensibility = 30;
	
	
	@SuppressWarnings("serial")
	public static final Hashtable<Rod, Integer> Y_STAGGERING_DEFAULT = new Hashtable<Rod, Integer>(){{ put(Rod.GARDIEN, 100); put(Rod.DEFENSE, 150 ); put(Rod.MILIEU, 100); put(Rod.ATTAQUE, 100); } };

	public static final int IMAGE_PLAYER_SHOOTING_X = 30;
	
	
	public static int getYPositionPlayer( Hashtable<Rod, Integer>[] yDecal, Rod rod, int i, int nb, Sides side ){
		return yDecal[side == Sides.UP ? 1 : 0].get(rod)-Utils.Y_STAGGERING_DEFAULT.get(rod)+i*Utils.HEIGHT/(1+nb)-Utils.IMAGE_PLAYER_Y/2;
	}
	
	public static int getYPositionPlayer( int position, Rod rod, int i, int nb ){
		return position-Utils.Y_STAGGERING_DEFAULT.get(rod)+i*Utils.HEIGHT/(1+nb)-Utils.IMAGE_PLAYER_Y/2;
	}

	
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
			if( !list[i].equals("Pas de messages") ){
				String[] m = list[i].split(" - ",2);
				list[i] = m[1];
			}
		}
		return list;
	}
	
	public static String getChatServerNameFromHost(String loginHost){
		return "Partie de " + loginHost;
	}
	
	
	/** Retourne une date formatée à partir d'une chaine de caractère de type timestamp. 
	@param date est de type Timestamp */
	public static String formatDate(String date){
		return formatDate(Integer.valueOf(date));
	}
	
	/** Retourne une date formatée à partir d'une chaine de caractère de type timestamp. 
	@param date est de type Timestamp */
	public static String formatDate(int date){
		Date d = new Date((long)date);
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

	public static int getSensibility() {
		return sensibility;
	}

	public static void setSensibility(int sensibility) {
		Utils.sensibility = sensibility;
	}
}
