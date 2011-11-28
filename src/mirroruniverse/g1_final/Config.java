package mirroruniverse.g1_final;

public class Config {
	public static boolean DEBUG =  false;
	
	public static final int MAX_SIZE = 50;
	public static final int MAX_MAP_SIZE = 2*MAX_SIZE;
	public static final int MAX_GLOBAL_VIEW_SIZE = 2*MAX_MAP_SIZE;
	
	public static int getMaxGlobalViewSize() {
		return MAX_GLOBAL_VIEW_SIZE;
	}
	public static boolean isDEBUG() {
		return DEBUG;
	}
	public static void setDEBUG(boolean dEBUG) {
		DEBUG = dEBUG;
	}
	public static int getMaxSize() {
		return MAX_SIZE;
	}
	public static int getMaxMapSize() {
		return MAX_MAP_SIZE;
	}
}
