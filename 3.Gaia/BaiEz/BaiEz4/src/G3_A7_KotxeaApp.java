
public class G3_A7_KotxeaApp {
	public static void main (String args[]) {
		G3_A7_Pantaila p = new G3_A7_Pantaila();
		G3_A7_KotxeaIdazlea G3_A7_k1 = new G3_A7_KotxeaIdazlea("*",450,10,p);
		G3_A7_KotxeaIdazlea G3_A7_k2 = new G3_A7_KotxeaIdazlea("+",200,40,p);
		p.sartuKotxeak(G3_A7_k1, G3_A7_k2);
		G3_A7_k1.start();
		G3_A7_k2.start();
		}
}