public class G3_A5_BaiEzAgianBetiApp{
	public static void main (String args[]) {
		Pantaila p = new Pantaila();
		Idazlea bai = new Idazlea("bai",1000,4,p);
		Idazlea ez = new Idazlea("\tez",400,15,p);
		Idazlea agian = new Idazlea("\t\tagian",2000,15,p);
		Idazlea beti = new Idazlea("\t\t\tbeti",1,60,p);
		bai.start();
		ez.start();
		agian.start();
		beti.start();
		}
		}