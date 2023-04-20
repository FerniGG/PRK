
public class SemaforoApp {
	public final static int SemKop = 3;

	public static void main(String[] args) {
		Pantaila p = new Pantaila();
		for (int i = 1; i <= SemKop; i++) {
			p.idatzi("sem" + i + "\t");
		}
		p.idatzi("\n=====================\n");
		String space = "";
		Semaforo semList[] = new Semaforo[SemKop];
		MutexLoop mList[] = new MutexLoop[SemKop];
		for (int i = 0; i < SemKop; i++) {
			semList[i] = new Semaforo(1);
			mList[i] = new MutexLoop(semList[i], 1, 3, p, space);
			space += "\t";
		}
		for (int i = 0; i < SemKop; i++) {
			mList[i].start();
		}

	}

}

class Semaforo {
	private int balioa;

	public Semaforo(int hasierakoa) {
		balioa = hasierakoa;
	}

	synchronized public void gora() {
		++balioa;
		notify();
	}

	synchronized public void behera() throws InterruptedException {
		while (!(balioa > 0))
			wait();
		--balioa;
	}
}

class MutexLoop extends Thread {
	Semaforo sema;
	int arrunt;
	int kritik;
	String tartea;
	Pantaila p;

	MutexLoop(Semaforo s, int ar, int kr, Pantaila pp, String tabul) {
		sema = s;
		arrunt = ar;
		kritik = kr;
		tartea = tabul;
		p = pp;
	}

	public void run() {
		try {
			while (true) {
				for (int i = 1; i <= arrunt; i++)
					ekintza("|"); // Ekintza ez kritikoa
				sema.behera(); // Eskuratu elkar-bazterk
				for (int i = 1; i <= kritik; i++)
					ekintza("*"); // Ekintza kritikoa
				sema.gora(); // Askatu elkar-bazterk
			}
		} catch (InterruptedException e) {
		}
	}

	void ekintza(String ikurra) throws InterruptedException {
		System.out.println(tartea + ikurra + "\n");
		sleep(1000);
	}
}

class Pantaila {
	Pantaila() {

	}

	public synchronized void idatzi(String mezua) {
		System.out.print(mezua);
	}
}

