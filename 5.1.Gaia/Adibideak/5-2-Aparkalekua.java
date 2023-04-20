
public class AparkalekuaApp {
	final static int Plazak = 4;

	public static void main(String args[]) {
		Pantaila pant = new Pantaila(Plazak);
		Kontrolatzailea k = new Kontrolatzailea(Plazak, pant);
		Sarrerak sar = new Sarrerak(k);
		Irteerak irt = new Irteerak(k);
		sar.start();
		irt.start();
	}
}

class Sarrerak extends Thread {
	Kontrolatzailea aparkalekua;

	Sarrerak(Kontrolatzailea k) {
		aparkalekua = k;
	}

	public void run() {
		try {
			while (true) {
				sleep(1000);
				aparkalekua.sartu();
			}
		} catch (InterruptedException e) {
		}
	}
}

class Irteerak extends Thread {
	Kontrolatzailea aparkalekua;

	Irteerak(Kontrolatzailea k) {
		aparkalekua = k;
	}

	public void run() {
		try {
			while (true) {
				sleep(1000);
				aparkalekua.irten();
			}
		} catch (InterruptedException e) {
		}
	}
}

class Kontrolatzailea {
	private int kop;
	private int plazak;
	private Pantaila pantaila;

	Kontrolatzailea(int pl, Pantaila pant) {
		plazak = pl;
		pantaila = pant;
		kop = 0;
	}

	synchronized void sartu() throws InterruptedException {
		while (!(kop < plazak))
			wait();
		++kop;
		pantaila.sartu(kop);
		notify();
	}

	synchronized void irten() throws InterruptedException {
		while (!(kop > 0))
			wait();
		--kop;
		pantaila.irten(kop);
		notify();
	}
}

class Pantaila {
	private int plazak;

	public Pantaila(int p) {
		plazak = p;
	}

	synchronized public void sartu(int k) {
		int kop = k;
		System.out.print("sartu\t\t");
		margotuKotxeak(kop);
	}

	synchronized public void irten(int k) {
		int kop = k;
		System.out.print("\tirten\t");
		margotuKotxeak(kop);
	}

	synchronized public void margotuKotxeak(int k) {
		System.out.print("[");
		for (int i = 0; i < k; ++i) {
			System.out.print("*");
		}
		for (int i = k; i < plazak; ++i) {
			System.out.print(" ");
		}
		System.out.println("]");
	}
}
