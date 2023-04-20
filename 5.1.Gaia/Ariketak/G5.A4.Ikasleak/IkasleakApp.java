import java.util.Random;

public class IkasleakApp {
	public final static int Edukiera_Max = 10;
	public final static int Ikasle_Kop = 3;
	public final static int Alokairua = 5;

	public static void main(String args[]) throws InterruptedException {
		Pantaila pant = new Pantaila();
		Kontrolatzailea k = new Kontrolatzailea(Edukiera_Max, pant);
		Jabea s = new Jabea(k, pant);
		Ikasleak b[] = new Ikasleak[Ikasle_Kop];
		s.start();
		for (int i = 0; i < Ikasle_Kop; i++) {
			b[i] = new Ikasleak(i, k, pant);
		}
		for (int i = 0; i < Ikasle_Kop; i++) {
			b[i].start();
		}

	}

}

class Ikasleak extends Thread {
	int id;
	Kontrolatzailea kontrol;
	Pantaila p;

	Ikasleak(int pid, Kontrolatzailea k, Pantaila pp) {
		id = pid;
		kontrol = k;
		p = pp;
	}

	private static int getRandomNumberInRange(int min, int max) {

		Random r = new Random();
		return r.ints(min, (max + 1)).findFirst().getAsInt();

	}
/*IKASLEA = ( auzas[e:ERABR][d:BR] -> 
			if (e==0) then (begiratu[d] ->
								if (d>0)	then ( auzas[h:1..d]	-> hartu[h] -> IKASLEA)
								else ( askatu -> IKASLEA ))
		  	else (begiratu[d] ->
								if (d<BT)	then(auzas[s:1..BT-d] -> sartu[s] -> IKASLEA)
								else ( askatu -> IKASLEA ))
			
		  ).*/
	public void run() {
		int rand = 0;
		int zenbatDaude = 0;
		int aukera = 0;
		int zenbatBota;
		try {
			while (true) {
				aukera = getRandomNumberInRange(0, 1);
				kontrol.SartuBota(this.id, aukera);
				zenbatDaude = kontrol.BoteaBegiratu(this.id);
				if (aukera == 0) {
					if (zenbatDaude != 0) {
						kontrol.BotetikHartu(this.id, getRandomNumberInRange(1, zenbatDaude));
						rand = getRandomNumberInRange(1, 30);
						sleep(rand * 100);
						sleep(rand * 100);
					} else {
						kontrol.BoteaAskatu(this.id);
						sleep(rand * 2000);
					}
				} else {
					if (zenbatDaude < IkasleakApp.Edukiera_Max) {
						zenbatBota = getRandomNumberInRange(1, IkasleakApp.Edukiera_Max - zenbatDaude);
						kontrol.BoteaBete(zenbatBota, this.id);
						rand = getRandomNumberInRange(0, 20);
						sleep(rand * 500);
					} else {
						kontrol.BoteaAskatu(this.id);
						sleep(rand * 500);
					}
				}
			}
		} catch (InterruptedException e) {
		}
	}
}

class Jabea extends Thread {
	Kontrolatzailea kontrol;
	Pantaila p;

	Jabea(Kontrolatzailea k, Pantaila pp) throws InterruptedException {
		kontrol = k;
		p = pp;
	}

	private static int getRandomNumberInRange(int min, int max) {

		Random r = new Random();
		return r.ints(min, (max + 1)).findFirst().getAsInt();

	}
/*JABEA = ( begiratu[dk:BR] ->
				if (dk>=JA) then ( hartu[JA] -> JABEA )
				else (askatu -> JABEA)
		  ).*/
	public void run() {
		int rand = 1;
		int zenbatDaude = 0;
		try {
			while (true) {
				zenbatDaude = kontrol.BoteaBegiratu(-1);
				if (zenbatDaude >= IkasleakApp.Alokairua) {
					kontrol.AlokairuaHartu(-1);
					rand = getRandomNumberInRange(1, 30);
					sleep(rand * 100);
				} else {
					kontrol.BoteaAskatu(-1);
					sleep(rand * 2000);
				}
			}
		} catch (InterruptedException e) {
		}
	}
}

class Kontrolatzailea {
	private int unekop;
	private Pantaila pantaila;
	boolean block = true;

	Kontrolatzailea(int pusk, Pantaila pant) {
		pantaila = pant;
		unekop = 0;
	}
// when (blok==0)			 j.begiratu[i] 		 -> BOTEA[i][0][1]
	synchronized int BoteaBegiratu(int id) throws InterruptedException {
		while (!(block))
			wait();
		pantaila.begiratu(unekop, id);
		block = false;
		notify();
		return unekop;
	}
//| j.askatu	 -> BOTEA[i][0][0]
//| ik[IKR].askatu 								 -> BOTEA[i][0][0]
	synchronized void BoteaAskatu(int id) throws InterruptedException {
		block = true;
		pantaila.askatu(id);
		notify();
	}
//when (i<BT) ik[IKR].sartu[s:1..BT-i] -> BOTEA[i+s][0][0]
	synchronized void BoteaBete(int zenbat, int id) throws InterruptedException {
		int lekulibre = IkasleakApp.Edukiera_Max - unekop;
		while (!(unekop + zenbat <= IkasleakApp.Edukiera_Max))
			wait();
		unekop += zenbat;
		pantaila.sartu(this.unekop, zenbat, id);
		block = true;
		notify();

	}
//when (i>0)  ik[IKR].hartu[h:1..i]	 -> BOTEA[i-h][1][0]

	synchronized void BotetikHartu(int id, int zenbat) throws InterruptedException {
		while (!(unekop > 0))
			wait();
		unekop -= zenbat;
		pantaila.hartu(unekop, id, zenbat);
		block = true;
		notify();
	}

//when (blok==0) ik[IKR].auzas[e:ERABR][i]		 -> BOTEA[i][e][1]
	synchronized void SartuBota(int id, int auk) throws InterruptedException {

		pantaila.sartubota(id, auk);
		notify();
	}
//when (i>=JA)			 j.hartu[JA] 			 -> BOTEA[i-JA][0][0]
	synchronized void AlokairuaHartu(int id) throws InterruptedException {
		while (!(unekop >= IkasleakApp.Alokairua))
			wait();
		unekop -= IkasleakApp.Alokairua;
		pantaila.alokairuaHartu(unekop, id);
		block = true;
		notify();
	}

}

class Pantaila {

	public Pantaila() {
		System.out.print("J");
		for (int i = 0; i < IkasleakApp.Ikasle_Kop; i++) {
			System.out.print("\tIK[" + i + "]");
		}
		System.out.print("\tBotea\n");
		System.out.println("=======================================");
	}

	synchronized public void sartu(int unekop, int zenbat, int id) {
		for (int i = IkasleakApp.Ikasle_Kop - id - 1; i < IkasleakApp.Ikasle_Kop; i++) {
			System.out.print("\t");
		}
		System.out.print("sar[" + zenbat + "]");
		margotuLapikoa(unekop, id);
	}

	synchronized public void sartubota(int id, int auk) {
		for (int i = IkasleakApp.Ikasle_Kop - id - 1; i < IkasleakApp.Ikasle_Kop; i++) {
			System.out.print("\t");
		}
		/*
		 * if (auk == 0) { System.out.print("Hartzera\n"); } else {
		 * System.out.print("Sartzera\n"); }
		 */
		System.out.println();
	}

	synchronized public void begiratu(int unekop, int id) {

		for (int i = IkasleakApp.Ikasle_Kop - id - 1; i < IkasleakApp.Ikasle_Kop; i++) {
			System.out.print("\t");
		}
		System.out.print("beg[" + unekop + "]");
		margotuLapikoa(unekop, id);

	}

	synchronized public void askatu(int id) {
		for (int i = IkasleakApp.Ikasle_Kop - id - 1; i < IkasleakApp.Ikasle_Kop; i++) {
			System.out.print("\t");
		}
		System.out.print("ask\n");

	}

	synchronized public void hartu(int k, int id, int zenbat) {
		int kop = k;

		for (int i = IkasleakApp.Ikasle_Kop - id - 1; i < IkasleakApp.Ikasle_Kop; i++) {
			System.out.print("\t");
		}
		System.out.print("hart[" + zenbat + "]");
		margotuLapikoa(kop, id);
	}

	synchronized public void alokairuaHartu(int k, int id) {
		int kop = k;

		for (int i = IkasleakApp.Ikasle_Kop - id - 1; i < IkasleakApp.Ikasle_Kop; i++) {
			System.out.print("\t");
		}
		System.out.print("alok[" + IkasleakApp.Alokairua + "]");
		margotuLapikoa(kop, id);
	}

	synchronized public void margotuLapikoa(int k, int id) {
		for (int i = IkasleakApp.Ikasle_Kop - id; 0 < i; i--) {
			System.out.print("\t");
		}
		System.out.print("[");
		for (int i = 0; i < k; ++i) {
			System.out.print("*");
		}
		for (int i = k; i < IkasleakApp.Edukiera_Max; ++i) {
			System.out.print(" ");
		}
		System.out.println("]");
	}
}
