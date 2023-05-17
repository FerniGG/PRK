package G4_3_LorategiakInterferentziaKonponduta;

public class LorategiaApp {
	public final static int MAX = 6;

	public static void main(String args[]) {

		Simulatu s = new Simulatu();
		Pantaila p = new Pantaila(s);
		try {
			int c = System.in.read();
		} catch (Exception ex) {
		}
		p.idatziGoiburua();

		Kontagailua k = new Kontagailua(s, p);

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

	Kontagailua(Simulatu ps, Pantaila pp) {
		p = pp;
		s = ps;
	}

	synchronized void gehitu(String atea, int i) {
		balioa++;
		p.idatziEgoera(atea, i, balioa);

	}
}

class Simulatu {
	public void HWinterrupt() {
		if (Math.random() < 0.2)
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
	}
}

class Pantaila {
	Simulatu s;

	public Pantaila(Simulatu ps) {
		s = ps;
		idatzi("LORATEGIA: return sakatu hasteko\n");

	}

	public synchronized void idatziEgoera(String atea, int i, int balioa) {
		this.idatzi(atea);
		this.idatzi("[");
		for (int j = 1; j <= i; j++) {
			this.idatzi("*");
			s.HWinterrupt();
		}
		for (int k = i; k < LorategiaApp.MAX; k++) {
			this.idatzi(" ");
			s.HWinterrupt();
		}
		this.idatzi("]\n");
		
		this.idatzi("\t\t\t\t");
		this.idatzi("[");
		for (int j = 1; j <= balioa; j++) {
			this.idatzi("*");
			s.HWinterrupt();
		}
		for (int k = balioa; k < 2 * LorategiaApp.MAX; k++) {
			this.idatzi(" ");
			s.HWinterrupt();
		}
		this.idatzi("]\n");
	}

	public synchronized void idatzi(String mezua) {
		System.out.print(mezua);
	}

	public synchronized void idatziGoiburua() {
		idatzi("Aurre \t\tAtze \t\tGuztira\n");
		String kont_izarra = "[";
		for (int k = 0; k < 2 * LorategiaApp.MAX; k++) {
			kont_izarra += " ";
		}
		kont_izarra += "]";
		this.idatzi("\t\t\t\t" + kont_izarra + "\n");
	}
}

