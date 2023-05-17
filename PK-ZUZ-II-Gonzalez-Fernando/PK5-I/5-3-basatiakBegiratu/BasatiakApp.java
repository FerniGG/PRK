package G5_3_basatiakBegiratu;
//////////////////////////////////////////////////

///   Egilea: Fernando Gonzalez                ///
///   Data : 24/04/2023                        ///
//////////////////////////////////////////////////

//Gai Zenb : 5.1
//Ariketa Zenb : 3                          
//Ariketa Izena : BasatienFestaBotaAusaz 
//Enuntziatua : Basatien festa, baina orain:
//- sukaldariak hainbat puska bota, eta
//- basatiek hainbat puska hartu, eta
//- bota edo hartu aurretik,lapikoan zenbat dagoen begiratzen dute
import java.util.Random;

public class BasatiakApp {
	public final static int Misiolari_puska_Max = 6;
	public final static int Basati_kop = 3;
	public final static int zenbatBota = 3;

	public static void main(String args[]) {
		Pantaila pant = new Pantaila(Misiolari_puska_Max, 0, Basati_kop);
		Kontrolatzailea k = new Kontrolatzailea(Misiolari_puska_Max, pant);
		Sukaldaria s = new Sukaldaria(zenbatBota, k, pant);
		Basatiak b[] = new Basatiak[Basati_kop];
		s.start();
		for (int i = 0; i < Basati_kop; i++) {
			b[i] = new Basatiak(i, k, pant);
		}
		for (int i = 0; i < Basati_kop; i++) {
			b[i].start();
		}

	}

}


class Basatiak extends Thread {
	int basatiaid;
	Kontrolatzailea kontrol;
	Pantaila p;

	Basatiak(int id, Kontrolatzailea k, Pantaila pp) {
		basatiaid = id;
		kontrol = k;
		p = pp;
	}

	private static int getRandomNumberInRange(int min, int max) {

		Random r = new Random();
		return r.ints(min, (max + 1)).findFirst().getAsInt();

	}

	private void itxaron() throws InterruptedException {
		int rand = getRandomNumberInRange(1, 30);
		sleep(rand * 100);
	}

	/*
	 * BASATIA = ( begiratu[p:PR]-> if(p>0) then (hartu[r:1..p]->jan->lo->BASATIA)
	 * else(askatu->BASATIA)).
	 */
	public void run() {
		try {
			while (true) {
				int zenbatDaude = kontrol.LapikoaBegiratu(this.basatiaid);
				itxaron();
				if (zenbatDaude != 0) {
					kontrol.LapikotikHartu(this.basatiaid, getRandomNumberInRange(1, zenbatDaude));
					itxaron();
					p.jan(this.basatiaid);
					itxaron();
					p.lo(this.basatiaid);
					itxaron();
				} else {
					kontrol.LapikoaAskatu();
					itxaron();
				}

			}
		} catch (InterruptedException e) {
		}
	}
}


class Sukaldaria extends Thread {
	Kontrolatzailea kontrol;
	Pantaila p;
	int zenbatbota;

	Sukaldaria(int zenbat, Kontrolatzailea k, Pantaila pp) {
		zenbatbota = zenbat;
		kontrol = k;
		p = pp;
	}

	private static int getRandomNumberInRange(int min, int max) {

		Random r = new Random();
		return r.ints(min, (max + 1)).findFirst().getAsInt();

	}

	private void itxaron() throws InterruptedException {
		int rand = getRandomNumberInRange(1, 30);
		sleep(rand * 100);
	}

	/*
	SUKALDARIA = ( begiratu[p:PR]-> 	
							if(p<PK) then (bota[r:..PK-p]->SUKALDARIA)
							else(askatu->SUKALDARIA)).
	 */
	public void run() {
		try {
			while (true) {
				int zenbatDaude = kontrol.LapikoaBegiratu(-1);
				if (zenbatDaude < BasatiakApp.Misiolari_puska_Max) {
					zenbatbota = getRandomNumberInRange(1, BasatiakApp.Misiolari_puska_Max - zenbatDaude);
					kontrol.LapikoaBete(zenbatbota);
					itxaron();
				} else {
					kontrol.LapikoaAskatu();
					itxaron();
				}
			}
		} catch (InterruptedException e) {
		}
	}
}

class Kontrolatzailea {
	private int unekop;
	private int misiolariPuskakMax;
	private Pantaila pantaila;
	boolean block = false;

	Kontrolatzailea(int pusk, Pantaila pant) {
		misiolariPuskakMax = pusk;
		pantaila = pant;
		unekop = 0;
	}

	/*
	 when (blok==0)	s.begiratu[i] 		-> LAPIKOA[i][1]
	| when (blok==0)	b[BR].begiratu[i] 	-> LAPIKOA[i][1]
	 */
	synchronized int LapikoaBegiratu(int id) throws InterruptedException {
		while (!(block==false))
			wait();
		pantaila.begiratu(unekop, id);
		block = true;
		notify();
		return unekop;
	}

	/*
	 * 	|s.askatu -> LAPIKOA[i][0] 
		| b[BR].askatu -> LAPIKOA[i][0]
	 */
	synchronized void LapikoaAskatu() throws InterruptedException {
		block = false;
		notify();
	}

//when (i<PK && blok==1)	s.bota[b:1..PK-i] 	-> LAPIKOA[i+b][0]
	synchronized void LapikoaBete(int zenbat) throws InterruptedException {
		// HUTSIK BADAGO//while (!(unekop == 0))
		/* HUTSIK EZ BADAGO ETA LEKUA BADAGO */
		while (!(unekop < this.misiolariPuskakMax && block==true))
			wait();
		unekop += zenbat;
		pantaila.bota(this.unekop, zenbat);
		block = false;
		notify();

	}

//when (i>0 && blok==1) 	b[BR].hartu[p:1..i] -> LAPIKOA[i-p][0]
	synchronized void LapikotikHartu(int id, int zenbat) throws InterruptedException {
		while (!(unekop > 0 && block == true))
			wait();
		unekop -= zenbat;
		pantaila.hartu(unekop, id, zenbat);
		block = false;
		notify();
	}

}

class Pantaila {
	private int puskaKop;
	private int misolariPuskaMax;
	private int basatiKop;

	public Pantaila(int max, int zenbat, int pbasatikop) {
		misolariPuskaMax = max;
		puskaKop = zenbat;
		basatiKop = pbasatikop;
		System.out.print("suk");
		for (int i = 0; i < basatiKop; i++) {
			System.out.print("\tb[" + i + "]");
		}
		System.out.print("\tLapikoa\n");
		System.out.println("=======================================");
	}

	synchronized public void bota(int unekop, int zenbat) {
		System.out.print("S[" + zenbat + "]");
		margotuLapikoa(unekop, -1);
	}

	synchronized public void begiratu(int unekop, int basatiaid) {

		for (int i = basatiKop - basatiaid - 1; i < this.basatiKop; i++) {
			System.out.print("\t");
		}
		System.out.print("B[" + unekop + "]");
		margotuLapikoa(unekop, basatiaid);

	}

	synchronized public void hartu(int k, int basatiaid, int zenbat) {
		int kop = k;

		for (int i = basatiKop - basatiaid - 1; i < this.basatiKop; i++) {
			System.out.print("\t");
		}
		System.out.print("H[" + zenbat + "]");
		margotuLapikoa(kop, basatiaid);
	}

	synchronized public void jan(int basatiaid) {
		for (int i = basatiKop - basatiaid - 1; i < this.basatiKop; i++) {
			System.out.print("\t");
		}
		System.out.print("J\n");

	}

	synchronized public void lo(int basatiaid) {
		for (int i = basatiKop - basatiaid - 1; i < this.basatiKop; i++) {
			System.out.print("\t");
		}
		System.out.print("L\n");

	}

	synchronized public void margotuLapikoa(int k, int basatiaid) {
		for (int i = basatiKop - basatiaid; 0 < i; i--) {
			System.out.print("\t");
		}
		System.out.print("[");
		for (int i = 0; i < k; ++i) {
			System.out.print("*");
		}
		for (int i = k; i < misolariPuskaMax; ++i) {
			System.out.print(" ");
		}
		System.out.println("]");
	}
}

