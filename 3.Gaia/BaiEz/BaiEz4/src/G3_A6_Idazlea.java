
public class G3_A6_Idazlea extends Thread {
	private String baiEz;
	private int kopurua;
	private double denbora;
	private Pantaila p;
	public G3_A6_Idazlea(String s, float i, int j, Pantaila pant){
	baiEz=s; denbora=i; kopurua=j;
	p=pant;
	}
	public void run() {
		try {
		 int i;
		 for (i=0;i<kopurua;i++) {
		p.margotu(baiEz);

		sleep((int)denbora);
		 if(this.baiEz.compareTo("bai")==0) {
			 this.denbora=this.denbora/1.2;
		 }
		 else {
			 this.denbora=this.denbora*1.2;
		 }
		 }
		 p.margotu(baiEz+" bukatu da");
		
		}catch (InterruptedException e) {}
		}


	}