
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

 class G3_A7_KotxeaIdazlea extends Thread{
	private String kotxea;
	private int kopurua,pos=0;
	private double denbora;
	private G3_A7_Pantaila p;
	
	public G3_A7_KotxeaIdazlea(String s, double i, int j,G3_A7_Pantaila pp){
		kotxea=s; denbora=i; kopurua=j;
		p=pp;
		}


	public void run() {
			try {
			 int i;
			 for (i=0;i<kopurua;i++) {
			p.margotu();
				pos=pos+1;
			sleep((int)denbora);
			 }
			 p.margotu(kotxea+" bukatu da");
			
			}catch (InterruptedException e) {}
			}
	
	
	

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public String getKotxea() {
		return kotxea;
	}

	public void setKotxea(String kotxea) {
		this.kotxea = kotxea;
	}
}

class G3_A7_Pantaila {
	private G3_A7_KotxeaIdazlea k1=null;
	private G3_A7_KotxeaIdazlea k2=null;
	public G3_A7_Pantaila() {
		
	}
	public void sartuKotxeak(G3_A7_KotxeaIdazlea pk1,G3_A7_KotxeaIdazlea pk2) {
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


