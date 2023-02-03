
public class G3_A8_Pantaila {
	private G3_A8_KotxeaIdazlea k1=null;
	private G3_A8_KotxeaIdazlea k2=null;
	public G3_A8_Pantaila() {
		
	}
	public void sartuKotxeak(G3_A8_KotxeaIdazlea pk1,G3_A8_KotxeaIdazlea pk2) {
		k1=pk1;
		k2=pk2;
		
	}
	public void margotu(String s){
		System.out.println(s);
		}
	public void garbitu() {
		for(int i=0; i<24; i++){System.out.println();}
	}
	public void margotu() {
		String k_1="";
		String k_2="";
		for (int i=0;i<k1.getPos();i++) {
			k_1+="|";
		}
		for (int j=0;j<k2.getPos();j++) {
			k_2+="|";
		}
		k_1+=k1.getKotxea();
		k_2+=k2.getKotxea();
		
		System.out.println(k_1);
		System.out.println(k_2);
		
		System.out.println("--------------------------------------------------------");
	}
		}
