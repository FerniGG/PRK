
public class LorategiaApp {
	public final static int MAX = 6;

	public static void main(String args[]) {
		System.out.println("LORATEGIA: return sakatu hasteko");
		try {
			int c = System.in.read();
		} catch (Exception ex) {
		}
		System.out.println("Aurre \tAtze \tGuztira");
		Kontagailua k = new Kontagailua();
		Atea aurrekoa = new Atea("", k);
		Atea atzekoa = new Atea("\t", k);
		aurrekoa.start();
		atzekoa.start();
	}
}

class Atea extends Thread {
	Kontagailua kont;
	String atea;

	public Atea(String zeinAte, Kontagailua k) {
		kont = k;
		atea = zeinAte;
	}

	public void run() {
		try {
			for (int i = 1; i <= LorategiaApp.MAX; i++) {
				sleep((long) (Math.random() * 1000));
				// ausazko denbora itxaron (0 eta 1 segunduren tartean)
				System.out.println(atea + i);
				kont.gehitu();
			}
		} catch (InterruptedException e) {
		}
	}
}

class Kontagailua {
	int balioa = 0;

	Kontagailua() {
		System.out.println("\t\t" + balioa);
	}

	void gehitu() {
		int lag;
		lag = balioa; // balioa irakurri
		Simulatu.HWinterrupt();
		balioa = lag + 1; // balioa idatzi
		System.out.println("\t\t" + balioa);
	}
}

class Simulatu {
	public static void HWinterrupt() {
		if (Math.random() < 0.2)
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
		;
	}
}
