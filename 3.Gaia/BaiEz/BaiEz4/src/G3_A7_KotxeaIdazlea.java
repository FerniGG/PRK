
public class G3_A7_KotxeaIdazlea extends Thread{
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


