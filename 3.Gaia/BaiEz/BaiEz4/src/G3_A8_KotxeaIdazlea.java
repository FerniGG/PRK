
public class G3_A8_KotxeaIdazlea extends Thread{
	private String kotxea;
	private int kopurua,pos=0;
	private double denbora;
	private G3_A8_Pantaila p;
	
	public G3_A8_KotxeaIdazlea(String s, double i, int j,G3_A8_Pantaila pp){
		kotxea=s; denbora=i; kopurua=j;
		p=pp;
		}


	public void run() {
			try {
			 int i;
			 for (i=0;i<kopurua;i++) {
			sleep((int)denbora);
			p.margotu();
				pos=pos+1;
			
			 if(this.kotxea.compareTo("*")==0) {
				 this.denbora=this.denbora/1.3;
			 }
			 else {
				 this.denbora=this.denbora*1.1;
			 }
			 }
			p.margotu(this.kotxea+"finished");
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


