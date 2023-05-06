package G5_7_PITSTOP;



import java.util.Random;


//////////////////////////////////////////////////
///   Egilea: Fernando Gonzalez                ///
///   Data : 30/04/2023                        ///
//////////////////////////////////////////////////

//Gai Zenb : 5.2
//Ariketa Zenb : 7                        
//Ariketa Izena : PitStop  
/*   Enuntziatua : Lehenik eta behin gure scuderia box deialdia emango die bi girariei
ondoren gidari bakoitzak dei horri erantzungo dio edo ez, pitlanean
sartuko dira eta geldituko dira, beste gidari bat sartzen bada, honen atzean geldituko da
Gelditu ostean kotxea altxatuko dute gurpilak aldatu eta finished deia emango diote gelditutako gidariari
honek dei horri erantzungo dio eta pit lanetik aterako da, ondoren atzeko gidaria geldituko da eta prozesu bera errepikatuko da.
PD: suposatzen dugu dei bakoitzean 8 gurpil aterako direla (2 gurpil set) horregatik deia egiterakoan gurpil set bat esleituko zaio gidariaren kotxe bati,
baina beste gidaria ez badu box deia erantzuten bere taldekidea erantzun dezake eta beste pit stop bat egin dezake buelta baten ostean.

*/


public class PitStopApp {
	public static int N = 2;
	public static int gidariMax = 4;

	public static void main(String[] args) {
		Pantaila p = new Pantaila();
		p.idatzi("SCU\t\t");
		for (int i = 1; i <= gidariMax; i++) {
			p.idatzi("DR[" + (i) + "]\t\t");
		}
		p.idatzi("PITLANE\n");
		p.idatzi(
				"=====================================================================================================\n");
		PitLane g = new PitLane(N, p);
		p.idatzi(g.toString(0));
		Scuderia ir = new Scuderia(g, p);
		Driver ikList[] = new Driver[gidariMax];

		String space = "\t\t";
		for (int i = 0; i < gidariMax; i++) {
			ikList[i] = new Driver(i + 1, g, p, space);
			space += "\t\t";
		}
		ir.start();
		for (int i = 0; i < gidariMax; i++) {
			ikList[i].start();
		}

	}

}


//PITLANE[k][g][f]
//k: Pit-ean sartu diren gidari kop
//g: kotxea gelditu den edo ez
//f: finished deia eman den edo ez
class PitLane extends Thread {
	int PitTam;
	Pantaila p;
	int[] PitLaneList;
	int k;
	boolean g, f;
	int whoPit=0;
	int tyresLeft=0;

	public PitLane(int pn, Pantaila pp) {
		this.PitTam = pn;
		p = pp;
		this.PitLaneList = new int[pn];
		for (int i = 0; i < this.PitTam; i++) {
			this.PitLaneList[i] = 0;
		}
	}
	
	public void itxaron() throws InterruptedException {
		sleep((long) (Math.random() * 1000));
	}
	
//	(when(k==0 && g==0 && f==0)		scu.boxboxDeitu->PITLANE[N][g][f]	
	public synchronized void boxboxDeitu(int id, String space) throws InterruptedException {
		while(!(this.k==0 && this.g==false && this.f==false))
			wait();
		this.k=this.PitTam;
		p.idatzi("BOXBOX\n");
		this.tyresLeft=4*this.PitTam;
		notifyAll();

	}
//|when(k>0) 						dr[DR].boxboxErantzun->PITLANE[k-1][g][f]
	public synchronized void boxboxErantzun(int id, String space) throws InterruptedException {
		while(!(this.k>0))
			wait();
		this.k--;
		p.idatzi(space+"BOX_OK\n");
		itxaron();
		p.idatzi(space+"sartu");
		boolean found = false;
		for (int i = this.PitTam-1; i>=0; i--) {
			if (!found) {
				if (this.PitLaneList[i] == 0) {
					this.PitLaneList[i] = id;
					found = true;
				}
			}
		}
		p.idatzi(this.toString(id));
		notifyAll();

	}
	
	//|when(g==0) 					dr[DR].gelditu->PITLANE[k][1][f]
	public synchronized void gelditu(int id, String space) throws InterruptedException {
		while(!(this.g==false))
			wait();
		this.g=true;
		this.whoPit=id;
		p.idatzi(space+"gelditu");
		p.idatzi(this.toString(id));
		notifyAll();

	}
	
	//|when(g==1 && f==0)				scu.kotxeaAltxatu->scu.gurpilakAldatu->scu.kotxeaJeitsi->scu.finishedDeitu->PITLANE[k][g][1]
	public synchronized void gurpilakAldatu(int id, String space) throws InterruptedException {
		while(!(this.g==true && this.f==false))
			wait();
		this.f=true;
		p.idatzi(space+"Tyre change["+this.whoPit+"]\n");
		itxaron();
		p.idatzi(space+"finished["+this.whoPit+"]\n");
		this.tyresLeft-=4;
		p.idatzi(this.toString(id));
		notifyAll();

	}
	
	//									|when(f==1 && g==1) 			dr[DR].finishedErantzun->dr[DR].atera->PITLANE[k][0][0]
		public synchronized void atera(int id, String space) throws InterruptedException {
			while(!(this.g==true && this.f==true))
				wait();
			this.whoPit=0;
			this.g=false;
			this.f=false;
			p.idatzi(space+"finishedOK\n");
			itxaron();
			p.idatzi(space+"atera");
			boolean found = false;
			for (int i = this.PitTam-1; i>=0; i--) {
				if (!found) {
					if (this.PitLaneList[i] == id) {
						this.PitLaneList[i] = 0;
						found = true;
					}
				}
			}
			p.idatzi(this.toString(id));
			notifyAll();

		}
	

	public synchronized String toString(int id) {
		for (int i = id; i < PitStopApp.gidariMax + 1; i++) {
			p.idatzi("\t\t");
		}
		String mezua = "[";
		for (int i = 0; i < this.PitTam; i++) {
			if (this.PitLaneList[i] == 0) {
				mezua += "__";
			} else {
				mezua += "" + this.PitLaneList[i];
			}
			if (i != this.PitTam - 1)
				mezua += "|";

		}
		mezua += "] Tyres Left:"+tyresLeft+"\n";
		return mezua;
	}
	
	
	


}

class Scuderia extends Thread {
	int id = 0;
	PitLane pit;
	Pantaila p;
	String space="";

	public Scuderia(PitLane ppit, Pantaila pp) {
		pit = ppit;
		p = pp;
	}


	public void itxaron() throws InterruptedException {
		sleep((long) (Math.random() * 1000));
	}

	public void run() {
		try {
			while (true) {
				itxaron();
				pit.boxboxDeitu(id,space);
				itxaron();
				pit.gurpilakAldatu(id, space);
				itxaron();
				pit.gurpilakAldatu(id, space);
				itxaron();
			}

		} catch (InterruptedException e) {
		}
	}
}

class Driver extends Thread {
	int id;
	PitLane pit;
	Pantaila p;
	String space;

	public Driver(int pn, PitLane ppit, Pantaila pp, String pspace) {
		this.id = pn;
		pit = ppit;
		p = pp;
		space = pspace;
	}
//DRIVER = (boxboxErantzun->sartu->gelditu->finishedErantzun->atera->DRIVER).
	public void run() {
		try {
			while (true) {
				itxaron();			
				pit.boxboxErantzun(id, space);
				itxaron();
				pit.gelditu(id, space);
				itxaron();
				pit.atera(id, space);
				itxaron();
			}

		} catch (InterruptedException e) {
		}
	}


	public void itxaron() throws InterruptedException {
		sleep((long) (Math.random() * 1000));
	}
}

class Pantaila {
	public Pantaila() {

	}

	public synchronized void idatzi(String mezua) {
		System.out.print(mezua);
	}
}

