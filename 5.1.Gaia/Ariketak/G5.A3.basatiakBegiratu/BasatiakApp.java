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
/*BASATIA = ( begiratu[p:PR]-> 	
							if(p>0) then (hartu[r:1..p]->jan->lo->BASATIA)
							else(askatu->BASATIA)).*/
	public void run() {
		int rand = 0;
		try {
			while (true) {
				int zenbatDaude = kontrol.LapikoaBegiratu(this.basatiaid);
				if (zenbatDaude != 0) {
					kontrol.LapikotikHartu(this.basatiaid, getRandomNumberInRange(1, zenbatDaude));
					rand = getRandomNumberInRange(1, 30);
					sleep(rand * 100);
					p.jan(this.basatiaid);
					rand = getRandomNumberInRange(1, 30);
					sleep(rand * 100);
					p.lo(this.basatiaid);
					rand = getRandomNumberInRange(1, 30);
					sleep(rand * 100);
				} else {
					kontrol.LapikoaAskatu();
					sleep(rand * 100);
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
/*SUKALDARIA = ( begiratu[p:PR]-> 	
							if(0<=PK-p) then (bota[r:0..PK-p]->SUKALDARIA)
							else(askatu->SUKALDARIA)).*/
	public void run() {
		int rand = 1;
		try {
			int zenbatDaude = kontrol.LapikoaBegiratu(-1);
				zenbatbota = getRandomNumberInRange(1, BasatiakApp.Misiolari_puska_Max - zenbatDaude);
				kontrol.LapikoaBete(zenbatbota);
				sleep(rand * 500);
			while (true) {

				zenbatDaude = kontrol.LapikoaBegiratu(-1);
				if (zenbatDaude < BasatiakApp.Misiolari_puska_Max) {
					zenbatbota = getRandomNumberInRange(1, BasatiakApp.Misiolari_puska_Max - zenbatDaude);
					kontrol.LapikoaBete(zenbatbota);
					rand = getRandomNumberInRange(0, 20);
					sleep(rand * 500);
				} else {
					kontrol.LapikoaAskatu();
					sleep(rand * 500);
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
	boolean block = true;

	Kontrolatzailea(int pusk, Pantaila pant) {
		misiolariPuskakMax = pusk;
		pantaila = pant;
		unekop = 0;
	}
/* when (blok==0)	s.begiratu[i] 		-> LAPIKOA[i][1] 
when (blok==0) b[BR].begiratu[i] 	-> LAPIKOA[i][1]*/
	synchronized int LapikoaBegiratu(int id) throws InterruptedException {
		while (!(block))
			wait();
		pantaila.begiratu(unekop, id);
		block = false;
		notify();
		return unekop;
	}
/*when  (blok==1) s.askatu		-> LAPIKOA[i][0]
when  (blok==1) b[BR].askatu -> LAPIKOA[i][0]*/
	synchronized void LapikoaAskatu() throws InterruptedException {
		block = true;
		notify();
	}
//when (i<PK && blok==1)	s.bota[b:1..PK-i] 	-> LAPIKOA[i+b][0]
	synchronized void LapikoaBete(int zenbat) throws InterruptedException {
		int lekulibre = this.misiolariPuskakMax - unekop;
		// HUTSIK BADAGO//while (!(unekop == 0))
		/* HUTSIK EZ BADAGO ETA LEKUA BADAGO */
		while (!(unekop + zenbat <= this.misiolariPuskakMax))
			wait();
		unekop += zenbat;
		pantaila.bota(this.unekop, zenbat);
		block = true;
		notify();

	}
//when (i>0 && blok==1) 	b[BR].hartu[p:1..i] -> LAPIKOA[i-p][0]
	synchronized void LapikotikHartu(int id, int zenbat) throws InterruptedException {
		while (!(unekop > 0))
			wait();
		unekop -= zenbat;
		pantaila.hartu(unekop, id, zenbat);
		block = true;
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
