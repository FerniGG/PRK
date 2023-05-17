package IrakIdaz;

public class IrakIdazApp {

	public static void main(String args[]) {

		// IrakurriIdatziSegurua blokeoa = new IrakurriIdatziSegurua();
		IrakurriIdatziLehentasuna blokeoa = new IrakurriIdatziLehentasuna();

		int IrKop = 5;
		int loeginIr = 1;
		int laneginIr = 3;
		int IdKop = 3;
		int loeginId = 2;
		int laneginId = 2;
		String tabul = "";

		for (int i = 1; i <= IrKop; i++) {
			System.out.print(i + ".irak\t");
		}
		for (int i = 1; i <= IdKop; i++) {
			System.out.print("\t" + i + ".idaz");
		}
		System.out.println();
		for (int i = 1; i <= IdKop + IrKop + 1; i++) {
			System.out.print("========");
		}
		System.out.println();

		Irakurlea ir[] = new Irakurlea[IrKop];
		Idazlea id[] = new Idazlea[IdKop];

		for (int i = 0; i < IrKop; i++) {
			ir[i] = new Irakurlea(blokeoa, tabul, loeginIr, laneginIr);
			ir[i].start();
			tabul = tabul + "\t";
		}

		for (int i = 0; i < IdKop; i++) {
			tabul = tabul + "\t";
			id[i] = new Idazlea(blokeoa, tabul, loeginId, laneginId);
			id[i].start();
		}
	}
}

class Idazlea extends Thread {
	IrakurriIdatzi blok;
	String tab;
	int lo, lan, x;

	Idazlea(IrakurriIdatzi blokeoa, String tabul, int loegiten, int lanegiten) {
		blok = blokeoa;
		tab = tabul;
		lan = lanegiten;
		lo = loegiten;
		x = 1;
	}

	public void run() {
		try {
			while (true) {
				while (!jarduera("lo"))
					;
				// sekzio kritikoan sartu
				blok.eskuratuIdatzi();
				while (jarduera("aldatu"))
					;
				blok.askatuIdatzi();
			}
		} catch (InterruptedException e) {
		}
	}

	boolean jarduera(String s) throws InterruptedException {
		System.out.println(tab + s);
		Thread.sleep((int) (1000 * Math.random()));
		if (x < lo) {
			x = x + 1;
			return false;
		} else if (x < lo + lan) {
			x = x + 1;
			return true;
		} else {
			x = 1;
			return false;
		}
	}
}

class Irakurlea extends Thread {
	IrakurriIdatzi blok;
	String tab;
	int lo, lan, x;

	Irakurlea(IrakurriIdatzi blokeoa, String tabul, int loegiten, int lanegiten) {
		blok = blokeoa;
		tab = tabul;
		lan = lanegiten;
		lo = loegiten;
		x = 1;
	}

	public void run() {
		try {
			while (true) {
				while (!jarduera("lo"))
					;
				// sekzio kritikoan sartu
				blok.eskuratuIrakurri();
				while (jarduera("aztertu"))
					;
				blok.askatuIrakurri();
			}
		} catch (InterruptedException e) {
		}
	}

	boolean jarduera(String s) throws InterruptedException {
		System.out.println(tab + s);
		Thread.sleep((int) (1000 * Math.random()));
		if (x < lo) {
			x = x + 1;
			return false;
		} else if (x < lo + lan) {
			x = x + 1;
			return true;
		} else {
			x = 1;
			return false;
		}
	}
}

interface IrakurriIdatzi {
	public void eskuratuIrakurri() throws InterruptedException;

	public void askatuIrakurri();

	public void eskuratuIdatzi() throws InterruptedException;

	public void askatuIdatzi();
}

class IrakurriIdatziSegurua implements IrakurriIdatzi {
	private int irakurleak = 0;
	private boolean idazten = false;

	public synchronized void eskuratuIrakurri() throws InterruptedException {
		while (idazten)
			wait();
		++irakurleak;
	}

	public synchronized void askatuIrakurri() {
		--irakurleak;
		if (irakurleak == 0)
			notify();
	}

	public synchronized void eskuratuIdatzi() throws InterruptedException {
		while (irakurleak > 0 || idazten)
			wait();
		idazten = true;
	}

	public synchronized void askatuIdatzi() {
		idazten = false;
		notifyAll();
	}
}

class IrakurriIdatziLehentasuna implements IrakurriIdatzi {
	private int irakurleak = 0;
	private boolean idazten = false;
	private int zaiW = 0; // zai dauden Idazleen kopurua.

	public synchronized void eskuratuIrakurri() throws InterruptedException {
		while (idazten || zaiW > 0)
			wait();
		++irakurleak;
	}

	public synchronized void askatuIrakurri() {
		--irakurleak;
		if (irakurleak == 0)
			notify();
	}

	synchronized public void eskuratuIdatzi() throws InterruptedException {
		++zaiW;
		while (irakurleak > 0 || idazten)
			wait();
		--zaiW;
		idazten = true;
	}

	synchronized public void askatuIdatzi() {
		idazten = false;
		notifyAll();
	}

	public class Pantaila {

		public void Margotu(String s) {
			System.out.println(s);
		}

		public void MargotuBotea(String s, int kopurua, int kopuruMaximoa) {
			System.out.print(s + "[");
			for (int i = 0; i < kopuruMaximoa; i++) {
				if (i < kopurua) {
					System.out.print("*");
				} else {
					System.out.print(" ");
				}
			}
			System.out.println("]");
		}
	}

}
