class BufferSemApp {
	final static int bufferTamaina = 5;

	public static void main(String[] args) {
		Pantaila p = new Pantaila();
		p.idatzi("Idazlea\t\tBufferra\t\tIrakurlea\n");
		p.idatzi("===================================================\n");
		SemaBuffer sem = new SemaBuffer(bufferTamaina,p);
		Idazlea id = new Idazlea(sem,p);
		Irakurlea irak = new Irakurlea(sem,p);

		id.start();
		irak.start();
	}
}

class Semaforo {
	private int balioa;
	boolean unblock = true;

	public Semaforo(int hasierakoa) {
		balioa = hasierakoa;
	}
	synchronized public int begiratu() throws InterruptedException {
		while (!(unblock))
			wait();
		unblock = false;
		notify();
		return balioa;
	}
	
	synchronized void askatu() throws InterruptedException {
		unblock = true;
		notify();
	}
	synchronized public void gora() {
		++balioa;
		notify();
		
	}

	synchronized public void behera() throws InterruptedException {
		while (balioa == 0)
			wait();
		--balioa;
		notify();
	}

	public int balioa() {
		return balioa;
	}
}

class SemaBuffer {
	private int tam;
	private int in = 0, out = 0;
	private char[] buf;
	Semaforo okupatuak;
	/** item kopurua zenbatzen du */
	Semaforo libreak;
	/** toki kopurua zenbatzen du */
	
	Pantaila p;
	SemaBuffer(int size,Pantaila pp) {
		this.tam = size;
		p=pp;
		buf = new char[size];
		for (int i = 0; i < size; i++)
			buf[i] = ' ';
		okupatuak = new Semaforo(0);
		libreak = new Semaforo(tam);
	}

	public void erakutsi() {
		p.idatzi("\t\t|");
		for (int i = 0; i < this.tam; i++)
			p.idatzi(buf[i] + "|");
		p.idatzi("\n");
	}

	public synchronized void put(char c) throws InterruptedException {
		libreak.behera();
		if(libreak.begiratu()!=0) {
		buf[in] = c;
		in = (in + 1) % tam;
		erakutsi();
		okupatuak.gora();
		this.okupatuak.unblock = true;
		}else {
			libreak.askatu();
		}
	}

	public synchronized char get() throws InterruptedException {
		okupatuak.behera();
		char c = buf[out];
		if(okupatuak.begiratu()!=0) {
		
		buf[out] = ' ';
		out = (out + 1) % tam;
		erakutsi();
		libreak.gora();
		this.libreak.unblock = true;
		}else {
			okupatuak.askatu();
		}
		return (c);
	}
}

class Idazlea extends Thread {
	SemaBuffer buf;
	String alphabet = "abcdefghijklmnopqrstuvwxyz";
	Pantaila p;
	Idazlea(SemaBuffer b,Pantaila pp) {
		buf = b;
		p=pp;
	}

	public void run() {
		try {
			int ai = 0;
			while (true) {
				if (Math.random() < 0.3)
					sleep(1000);
				p.idatzi(alphabet.charAt(ai) + ">\n");
				buf.put(alphabet.charAt(ai));
				ai = (ai + 1) % alphabet.length();
			}
		} catch (InterruptedException e) {
		}
	}
}

class Irakurlea extends Thread {
	SemaBuffer buf;
	Pantaila p;

	Irakurlea(SemaBuffer b,Pantaila pp) {
		buf = b;
		p=pp;
	}

	public void run() {
		try {
			while (true) {
				if (Math.random() < 0.3)
					sleep(1000);
				char c = buf.get();
				p.idatzi("\t\t\t\t\t\t" + c + ">\n");
			}
		} catch (InterruptedException e) {
		}
	}
}
class Pantaila {

	public Pantaila() {

	}

	synchronized public void idatzi(String s) {
		System.out.print(s);
	}
}

