package BideBakarrekoZubia;

public class ZubiaApp {

	public static void main(String[] args) {
		int maxg = 4;
		int maxu = 3;
		int zabalera = 9;
		int zubezk = zabalera / 2;
		int zubesk = (zabalera / 2) + 1;
		KotxeGorria[] gorria = new KotxeGorria[maxg];
		KotxeUrdina[] urdina = new KotxeUrdina[maxu];
		Pantaila p = new Pantaila(maxg, maxu, zabalera, zubezk, zubesk);
		Zubia z;
		// z = new Zubia();
		z = new ZubiSegurua();
		// z = new BidezkoZubia();

		for (int i = 0; i < maxg; i++) {
			gorria[i] = new KotxeGorria(z, p, i);
			gorria[i].start();
		}
		for (int i = 0; i < maxu; i++) {
			urdina[i] = new KotxeUrdina(z, p, i);
			urdina[i].start();
		}
	}
}

class KotxeGorria extends Thread {
	Zubia zubia;
	Pantaila pantaila;
	int zenb;

	KotxeGorria(Zubia z, Pantaila p, int zenb) {
		this.zenb = zenb;
		zubia = z;
		pantaila = p;
	}

	public void run() {
		try {
			// KOTXEA = (sartu->irten->KOTXEA).
			while (true) {
				while (!pantaila.mugituGorria(zenb)) // mugitu zubitik kanpoan
					sleep(500 + (int) (2000 * Math.random()));
				zubia.sartuGorria(); // eskatzen du zubiaren atzipena
				while (pantaila.mugituGorria(zenb)) // mugitu zubiaren barruan
					sleep(500 + (int) (2000 * Math.random()));
				zubia.irtenGorria(); // askatzen du zubiaren atzipena
			}
		} catch (InterruptedException e) {
		}
	}
}

class KotxeUrdina extends Thread {
	Zubia zubia;
	Pantaila pantaila;
	int zenb;

	KotxeUrdina(Zubia z, Pantaila p, int zenb) {
		this.zenb = zenb;
		zubia = z;
		pantaila = p;
	}

	public void run() {
		try {
			while (true) {
				while (!pantaila.mugituUrdina(zenb)) // mugitu zubitik kanpoan
					sleep(500 + (int) (2000 * Math.random()));
				zubia.sartuUrdina(); // eskatzen du zubiaren atzipena
				while (pantaila.mugituUrdina(zenb)) // mugitu zubiaren barruan
					sleep(500 + (int) (2000 * Math.random()));
				zubia.irtenUrdina(); // askatzen du zubiaren atzipena
			}
		} catch (InterruptedException e) {
		}
	}
}

class Zubia {
	synchronized void sartuGorria() throws InterruptedException {
	}

	synchronized void irtenGorria() {
	}

	synchronized void sartuUrdina() throws InterruptedException {
	}

	synchronized void irtenUrdina() {
	}
}

class ZubiSegurua extends Zubia {
	private int kGorria = 0; // kotxe gorrien kopurua zubian;
	private int kUrdina = 0; // kotxe urdinen kopurua zubian;
	// Monitoreraren Inbariantea: kGorria>=0 and kUrdina>=0 and
	// not (kGorria>0 and kUrdina>0)

	// when (ku==0) gorria[ID].sartu -> ZUBIA[kg+1][ku]
	synchronized void sartuGorria() throws InterruptedException {
		while (!(kUrdina == 0))
			wait();
		++kGorria;
	}

	// gorria[ID].irten -> ZUBIA[kg-1][ku]
	synchronized void irtenGorria() {
		--kGorria;
		if (kGorria == 0)
			notifyAll();
	}

	// when (kg==0) urdina[ID].sartu -> ZUBIA[kg] [ku+1]
	synchronized void sartuUrdina() throws InterruptedException {
		while (!(kGorria == 0))
			wait();
		++kUrdina;
	}

	// urdina[ID].irten -> ZUBIA[kg] [ku-1]
	synchronized void irtenUrdina() {
		--kUrdina;
		if (kUrdina == 0)
			notifyAll();
	}
}

class BidezkoZubia extends Zubia {
	private int kGorria = 0; // kotxe gorrien kopurua zubian;
	private int kUrdina = 0; // kotxe urdinen kopurua zubian;
	private int zaiGorria = 0; // sartzeko zai dauden kotxe gorrien kopurua
	private int zaiUrdina = 0; // sartzeko zai dauden kotxe urdinen kopurua
	private boolean urdinTx = true;

	// gorria[ID].eskatu-> ZUBIA[kg] [ku] [zg+1][zu] [ut]
	// when (ku==0&&(zu==0||!ut)) gorria[ID].sartu -> ZUBIA[kg+1][ku] [zg-1][zu]
	// [ut]
	synchronized void sartuGorria() throws InterruptedException {
		++zaiGorria;
		while (!(kUrdina == 0 && (zaiUrdina == 0 || !urdinTx)))
			wait();
		--zaiGorria;
		++kGorria;
	}

	// gorria[ID].irten -> ZUBIA[kg-1][ku] [zg] [zu] [True]
	synchronized void irtenGorria() {
		--kGorria;
		urdinTx = true;
		if (kGorria == 0)
			notifyAll();
	}

	// urdina[ID].eskatu-> ZUBIA[kg] [ku] [zg] [zu+1][ut]
	// when (kg==0&&(zg==0||ut)) urdina[ID].sartu -> ZUBIA[kg] [ku+1][zg] [zu-1][ut]
	synchronized void sartuUrdina() throws InterruptedException {
		++zaiUrdina;
		while (!(kGorria == 0 && (zaiGorria == 0 || urdinTx)))
			wait();
		--zaiUrdina;
		++kUrdina;
	}

	// urdina[ID].irten -> ZUBIA[kg] [ku-1][zg] [zu] [False]
	synchronized void irtenUrdina() {
		--kUrdina;
		urdinTx = false;
		if (kUrdina == 0)
			notifyAll();
	}
}

class Pantaila {
	int zab, maxg, maxu, zubezk, zubesk;
	int[] gorriaX, urdinaX;
	String[] tabul;

	public Pantaila(int mg, int mu, int zabalera, int ezk, int esk) {
		maxg = mg;
		maxu = mu;
		zab = zabalera;
		zubezk = ezk;
		zubesk = esk;
		gorriaX = new int[maxg];
		urdinaX = new int[maxu];
		// Kotxe bakoitzaren hasierako posizioa
		for (int i = 0; i < maxg; i++)
			gorriaX[i] = i - maxg;

		for (int i = 0; i < maxu; i++)
			urdinaX[i] = zab - i + maxu - 1;

		// Tabulazioak
		tabul = new String[zab + 1];
		for (int i = 0; i < zab + 1; ++i) {
			tabul[i] = "";
			for (int j = 0; j < i; ++j)
				tabul[i] = tabul[i] + "\t";
		}
		pantailaratu();
	}

	synchronized public void pantailaratu() {
		// for (int x = 0; x<24 ; x++){ System.out.println();}
		for (int i = 0; i < maxg; i++) // Gorriak
			if (gorriaX[i] > -1) {
				System.out.println(gorriaX[i] + "|\t" + tabul[gorriaX[i]] + "gorri" + i + ">>");
			} else {
				System.out.println(gorriaX[i] + "|");
			}
		for (int i = 0; i < maxu; i++) // Urdinak
			if (urdinaX[i] < zab) {
				System.out.println(urdinaX[i] + "|\t" + tabul[urdinaX[i]] + "<<urdin" + i);
			} else {
				System.out.println(urdinaX[i] + "|");
			}
		System.out.print("\t" + tabul[zubezk]); // zubia
		for (int i = zubezk; i < zubesk + 1; i++)
			System.out.print("********");
		System.out.println();
	}

	synchronized public boolean mugituGorria(int i) throws InterruptedException {
		int X = gorriaX[i];
		if (X == zab && gorriaX[(i + 1) % maxg] != 0)
			X = 0; // Bukaerara iristean
		else if (X != zab && gorriaX[(i + 1) % maxg] != X + 1)
			X = X + 1; // Beste posizioetan
		if (gorriaX[i] != X) {
			gorriaX[i] = X;
			pantailaratu();
		} // Mugitu ahal bada, mugitu eta pantailaratu
		return (X >= zubezk - 1 && X <= zubesk); // Zubira sartzeko edo zubian badago
	}

	synchronized public boolean mugituUrdina(int i) throws InterruptedException {
		int X = urdinaX[i];
		if (X == 0 && urdinaX[(i + 1) % maxu] != zab)
			X = zab;
		else if (X != 0 && urdinaX[(i + 1) % maxu] != X - 1)
			X = X - 1;
		if (urdinaX[i] != X) {
			urdinaX[i] = X;
			pantailaratu();
		}
		return (X >= zubezk && X <= zubesk + 1);
	}

}
