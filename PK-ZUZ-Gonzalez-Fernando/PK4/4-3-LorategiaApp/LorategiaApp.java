package G4_3_LorategiakInterferentziaKonponduta;

public class LorategiaApp {
	public final static int MAX = 6;

	public static void main(String args[]) {
		Pantaila p= new Pantaila();
		try {
			int c = System.in.read();
		} catch (Exception ex) {
		}
		p.idatziGoiburua();
		
		Simulatu s = new Simulatu();
		Kontagailua k = new Kontagailua(s,p);

		Atea aurrekoa = new Atea("", k, s);
		Atea atzekoa = new Atea("\t\t", k, s);
		aurrekoa.start();
		atzekoa.start();
	}
}

class Atea extends Thread {
	Kontagailua kont;
	String atea;
	Simulatu s;

	public Atea(String zeinAte, Kontagailua k, Simulatu ps) {
		kont = k;
		atea = zeinAte;
		s = ps;
	}

	public void run() {
		try {

			for (int i = 1; i <= LorategiaApp.MAX; i++) {
				// ausazko denbora itxaron (0 eta 1 segunduren tartean)
				kont.gehitu(atea, i);
				sleep((long) (Math.random() * 1000));
			}
		} catch (InterruptedException e) {
		}
	}
}

class Kontagailua {
	int balioa = 0;
	Simulatu s;
	Pantaila p;

	Kontagailua(Simulatu ps,Pantaila pp) {
		p=pp;
		s = ps;
		String kont_izarra = "[";
		for (int k = balioa; k < 2 * LorategiaApp.MAX; k++) {
			kont_izarra += " ";
		}
		kont_izarra += "]";
		p.idatzi("\t\t\t\t" + kont_izarra+"\n");
	}

	synchronized void gehitu(String atea, int i) {
		p.idatzi(atea);
		p.idatzi("[");
		for (int j = 1; j <= i; j++) {
			p.idatzi("*");
			s.HWinterrupt();
		}
		for (int k = i; k < LorategiaApp.MAX; k++) {
			p.idatzi(" ");
		}
		p.idatzi("]\n");
		int lag;
		lag = balioa; // balioa irakurri

		balioa = lag + 1; // balioa idatzi
		p.idatzi("\t\t\t\t");
		p.idatzi("[");
		for (int j = 1; j <= balioa; j++) {
			p.idatzi("*");
			Simulatu.HWinterrupt();
		}
		for (int k = balioa; k < 2 * LorategiaApp.MAX; k++) {
			p.idatzi(" ");
		}
		p.idatzi("]\n");

	}
}

class Simulatu {
	public static void HWinterrupt() {
		if (Math.random() < 0.2)
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
	}
}
class Pantaila {
	public Pantaila() {
		idatzi("LORATEGIA: return sakatu hasteko\n");
		
	}
	public synchronized void idatzi(String mezua) {
		System.out.print(mezua);
	}
	public synchronized void idatziGoiburua() {
		idatzi("Aurre \t\tAtze \t\tGuztira\n");
	}
}

