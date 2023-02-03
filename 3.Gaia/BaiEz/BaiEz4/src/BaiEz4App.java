
public class BaiEz4App{
	public static void main (String args[]) {
		Pantaila p = new Pantaila();
		Idazlea bai = new Idazlea("bai",1000,4,p);
		Idazlea ez = new Idazlea("\tez",400,15,p);
		bai.start();
		ez.start();
		}
		}