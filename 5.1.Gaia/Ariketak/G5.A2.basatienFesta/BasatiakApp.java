package G5_2_basatienFesta;

import java.util.Random;

public class BasatiakApp {
	public final static int Misiolari_puska_Max = 5;
	public final static int Basati_kop = 3;
	public final static int zenbatBota = 3;

	public static void main(String args[]) {
		Pantaila pant = new Pantaila(Misiolari_puska_Max, zenbatBota,Basati_kop);
		Kontrolatzailea k = new Kontrolatzailea(Misiolari_puska_Max, pant);
		Basatiak b[] = new Basatiak[Basati_kop];
		for (int i = 0; i < Basati_kop; i++) {
			b[i] = new Basatiak(i, k, pant);
		}
		for (int i = 0; i < Basati_kop; i++) {
			b[i].start();
		}
		Sukaldaria s = new Sukaldaria(zenbatBota, k, pant);
		s.start();

	}

}

class Basatiak extends Thread {
	int basatiaid;
	Kontrolatzailea kontrol;
	Pantaila p;
	boolean stop=false;

	Basatiak(int id, Kontrolatzailea k, Pantaila pp) {
		basatiaid = id;
		kontrol = k;
		p = pp;
	}

	private static int getRandomNumberInRange(int min, int max) {

		Random r = new Random();
		return r.ints(min, (max + 1)).findFirst().getAsInt();

	}
	public void itxaron() throws InterruptedException {
		int rand = getRandomNumberInRange(1, 30);
		sleep(rand * 100);
	}
//BASATIA = (hartu->jan->lo->BASATIA).
	public void run() {
		int rand = 0;
		try {
			while (true) {
				kontrol.LapikotikHartu(this.basatiaid);
				itxaron();
				p.jan(this.basatiaid);
				itxaron();
				p.lo(this.basatiaid);
				itxaron();

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
	public void itxaron() throws InterruptedException {
		int rand = getRandomNumberInRange(1, 30);
		sleep(rand * 100);
	}
	
//SUKALDARIA = (bota[BT]->SUKALDARIA).
	public void run() {
		int rand = 1;
		try {
			while (true) {
				kontrol.LapikoaBete(zenbatbota);
				itxaron();

			}
		} catch (InterruptedException e) {
		}
	}
}

class Kontrolatzailea {
	private int unekop;
	private int misiolariPuskakMax;
	private Pantaila pantaila;

	Kontrolatzailea(int pusk, Pantaila pant) {
		misiolariPuskakMax = pusk;
		pantaila = pant;
		unekop = 0;
	}
//  when(BT<=LM-kop) 	s.bota[BT]->LAPIKO[kop+BT]
	synchronized void LapikoaBete(int zenbat) throws InterruptedException {
		int lekulibre=this.misiolariPuskakMax-unekop;
		while (!(unekop+zenbat<=this.misiolariPuskakMax))
			wait();
		unekop += zenbat;
		pantaila.bota(this.unekop,zenbat);
		notify();
	}
//when(kop>0) 	b[BR].hartu->LAPIKO[kop-1]
	synchronized void LapikotikHartu(int id) throws InterruptedException {
		while (!(unekop > 0))
			wait();
		--unekop;
		pantaila.hartu(unekop, id);

		notify();
	}

}

class Pantaila {
	private int puskaKop;
	private int misolariPuskaMax;
	private int basatiKop;

	public Pantaila(int max, int zenbat,int pbasatikop) {
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

	synchronized public void bota(int unekop,int zenbat) {
		System.out.print("bota["+zenbat+"]");
		margotuLapikoa(unekop, -1);
	}

	synchronized public void hartu(int k, int basatiaid) {
		int kop = k;

		for (int i = basatiKop - basatiaid - 1; i < this.basatiKop; i++) {
			System.out.print("\t");
		}
		System.out.print("hartu");
		margotuLapikoa(kop, basatiaid);
	}

	synchronized public void jan(int basatiaid) {
		for (int i = basatiKop - basatiaid - 1; i < this.basatiKop; i++) {
			System.out.print("\t");
		}
		System.out.print("jan\n");

	}

	synchronized public void lo(int basatiaid) {
		for (int i = basatiKop - basatiaid - 1; i < this.basatiKop; i++) {
			System.out.print("\t");
		}
		System.out.print("lo\n");

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

