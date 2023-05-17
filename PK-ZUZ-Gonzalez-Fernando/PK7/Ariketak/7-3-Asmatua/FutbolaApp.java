package A_7_3;

import java.util.Random;

public class FutbolaApp {
	public static int N=2;
	public static int pasMax=5;
	
	public static void main(String[] args) {
		Pantaila p=new Pantaila();
		Zelaia z = new Zelaia(p);
		String space="";
		FutbolTaldea fList[]= new FutbolTaldea[N];
		for (int i = 0; i < N; i++) {
			fList[i] = new FutbolTaldea(i,space,z,p);
			space += "\t\t\t";
		}
		z.start();
		for (int i = 0; i < N; i++) {
			fList[i].start();
		}
		
	}
}
class FutbolTaldea extends Thread{
	int id;
	int erabPasa;
	int erabBota;
	int arbitroariBeg;
	String space;
	Pantaila p;
	Zelaia zelaia;
	
	public FutbolTaldea(int pid,String pspace,Zelaia pz,Pantaila pp) {
		id=pid;
		space=pspace;
		zelaia=pz;
		p=pp;
	}
	private void itxaron() throws InterruptedException {
		sleep((long) (Math.random() * 500));
	}
	private static int getRandomNumberInRange01() {

		Random r = new Random();
		return r.ints(0, (1 + 1)).findFirst().getAsInt();

	}
	/*FUT=(pilotaHartu->erabPasa[p:BOOL]->arbitroariBeg[a]->if(p==1 && a<PK) then (pasatu->FUT)
								 	else(erabBota[b:BOOL]->	if(b==1) then (gola->FUT)
								 							else(kanpora->FUT))*/
	public void run() {
		while(true) {
			try {
				itxaron();
				zelaia.pilotaHartu(id,space);
				itxaron();
				/*||PARTIDUA_BIZITASUNA = (PARTIDUA)<<{{ft[TR]}.erabPasa[1]}.*/
				this.erabPasa=1;
				this.arbitroariBeg=zelaia.arbitroariBegiratu(id,space);
				if(erabPasa==1 && arbitroariBeg<FutbolaApp.pasMax) {
					zelaia.pasatu(id,space);
					itxaron();
				}
				else {
					this.erabBota=this.getRandomNumberInRange01();
					if(this.erabBota==1) {
						zelaia.gola(id,space);
						
						itxaron();
					}else {
						zelaia.kanpora(id, space);
						itxaron();
					}
				}
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	
}
/*//ZELAIA
// t:Zein taldea duen baloia.
// k: zenbat pase egin duen talde batek segituan.
 ZELAIA=ZELAIA[0][0],*/
class Zelaia extends Thread{
	int t;
	int k;
	Pantaila p;
	int markagailua[]=new int[FutbolaApp.N];
	
	public Zelaia(Pantaila pp) {
		this.t=0;
		this.k=0;
		p=pp;
		for (int i = 0; i < FutbolaApp.N; i++) {
			markagailua[i]=0;
		}
		p.idatzi(this.markagailuaimprimatu(0));
	}
	
	/*|ft[t].arbitroariBeg[k]->ZELAIA[t][k]*/
	public synchronized int arbitroariBegiratu(int id, String space) throws InterruptedException {
		while(!(id==t))
			wait();
		int paskop=this.k;
		p.idatzi(space+"arbitroa Begiratu "+paskop+"\n");
		notifyAll();
		return paskop;
	}
	/*(ft[t].pilotaHartu->ZELAIA[t][k]*/
	public synchronized void pilotaHartu(int id,String space) throws InterruptedException {
		while(!(id==t))
			wait();
		p.idatzi(space+"Pilota Hartu\n");
		notifyAll();
	}
	
	/*|ft[t].pasatu->ZELAIA[t][k+1]*/
	public synchronized void pasatu(int id,String space) throws InterruptedException {
		while(!(id==t))
			wait();
		this.k++;
		p.idatzi(space+"Pasatu\n");
		notifyAll();
	}
	
	/*ft[t].gola->ZELAIA[(t+1)%N][0]*/
	public synchronized void gola(int id,String space) throws InterruptedException {
		while(!(id==t))
			wait();
		this.k=0;
		this.t=(this.t+1)%FutbolaApp.N;
		this.markagailua[id]=this.markagailua[id]+1;
		p.idatzi(space+"Gola");
		p.idatzi(this.markagailuaimprimatu(id));
		notifyAll();
	}
	
	/*|ft[t].kanpora->ZELAIA[(t+1)%N][0]*/
	public synchronized void kanpora(int id,String space) throws InterruptedException {
		while(!(id==t))
			wait();
		this.k=0;
		this.t=(this.t+1)%FutbolaApp.N;
		p.idatzi(space+"kanpora");
		p.idatzi(this.markagailuaimprimatu(id));
		notifyAll();
	}
	private synchronized String markagailuaimprimatu(int id) {
		String mezua="";
		for (int i=id;i<FutbolaApp.N;i++) {
			mezua+="\t\t\t";
		}
		mezua+="[";
		for(int i=0;i<FutbolaApp.N-1;i++) {
			mezua+=this.markagailua[i]+"-";
		}
			mezua+=this.markagailua[FutbolaApp.N-1]+"]\n";
		return mezua;
	}
	
}

class Pantaila {
	public Pantaila() {
		for (int i = 0; i < FutbolaApp.N; i++) {
			idatzi("FT["+i+"]\t\t\t");
		}
		idatzi("PARTIDUA\n");
		idatzi("===============================================\n");
		}

	public synchronized void idatzi(String m) {
		System.out.print(m);
	}
}
