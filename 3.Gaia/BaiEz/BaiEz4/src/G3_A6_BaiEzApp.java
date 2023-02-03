public class G3_A6_BaiEzApp{
	public static void main (String args[]) {
		Pantaila p = new Pantaila();
		G3_A6_Idazlea bai = new G3_A6_Idazlea("bai",500,25,p);
		G3_A6_Idazlea ez = new G3_A6_Idazlea("\tez",500,10,p);
		bai.start();
		ez.start();
		}
		}