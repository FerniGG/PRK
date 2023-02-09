public class LorategiaApp {
	public final static int MAX = 6;

	public static void main(String args[]) {
		System.out.println("LORATEGIA: return sakatu hasteko");
		try {
			int c = System.in.read();
		} catch (Exception ex) {
		}
		System.out.println("Aurre \t\tAtze \t\tGuztira");
		Kontagailua k = new Kontagailua();
		Simulatu s = new Simulatu();
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
				sleep((long) (Math.random() * 1000));
				// ausazko denbora itxaron (0 eta 1 segunduren tartean)
				System.out.print(atea);
				System.out.print("[");
				for (int j = 1; j <= i; j++) {
					System.out.print("*");
					s.HWinterrupt();
				}
				for (int k = i; k < LorategiaApp.MAX; k++) {
					System.out.print(" ");
				}
				System.out.println("]");
				kont.gehitu();
			}
		} catch (InterruptedException e) {
		}
	}
}

class Kontagailua {
	int balioa = 0;

	Kontagailua() {
		String kont_izarra = "[";
		for (int k = balioa; k < 2 * LorategiaApp.MAX; k++) {
			kont_izarra += " ";
		}
		kont_izarra += "]";
		System.out.println("\t\t\t\t" + kont_izarra);
	}

	void gehitu() {
		int lag;
		lag = balioa; // balioa irakurri

		balioa = lag + 1; // balioa idatzi
		System.out.print("\t\t\t\t");
		System.out.print("[");
		for (int j = 1; j <= balioa; j++) {
			System.out.print("*");
			Simulatu.HWinterrupt();
		}
		for (int k = balioa; k < 2 * LorategiaApp.MAX; k++) {
			System.out.print(" ");
		}
		System.out.println("]");

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

