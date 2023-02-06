
public class G3_A8_KotxeaApp {
	public static void main (String args[]) {
		G3_A8_Pantaila p = new G3_A8_Pantaila();
		G3_A8_KotxeaIdazlea G3_A8_k1 = new G3_A8_KotxeaIdazlea("*",2000,30,p);
		G3_A8_KotxeaIdazlea G3_A8_k2 = new G3_A8_KotxeaIdazlea("+",500,10,p);
		p.sartuKotxeak(G3_A8_k1, G3_A8_k2);
		G3_A8_k1.start();
		G3_A8_k2.start();
		}
}
