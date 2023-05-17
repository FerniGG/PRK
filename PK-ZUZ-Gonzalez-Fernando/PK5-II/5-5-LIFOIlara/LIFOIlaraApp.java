package G5_5_LIFO;

import java.util.Random;

//////////////////////////////////////////////////
///   Egilea: Fernando Gonzalez                ///
///   Data : 24/04/2023                        ///
//////////////////////////////////////////////////

//Gai Zenb : 5.2
//Ariketa Zenb : 5                         
//Ariketa Izena : FIFOIlara  
//Enuntziatua : LIFO (Last Input First Output) pila batean sartutako azken prozesua da
//ateratzen lehena. FSPz modelatu eta Java-z inplementatu

public class LIFOIlaraApp {
	public final static int ProzKop = 10;

	public static void main(String[] args) {
		Pantaila p = new Pantaila();
		for (int i = 0; i < ProzKop; i++) {
			p.idatzi("Id" + i + "\t");
		}
		p.idatzi("Ilara\n");
		p.idatzi("=====================================================================================\n");
		Ilara il = new Ilara(ProzKop, p);
		p.idatzi(il.toString(0));
		Prozesua idList[] = new Prozesua[ProzKop];

		String space = "";
		for (int i = 0; i < ProzKop; i++) {
			idList[i] = new Prozesua(i, il, p, space);
			space += "\t";
		}

		for (int i = 0; i < ProzKop; i++) {
			idList[i].start();
		}

		il.setSpace(space);

	}
}

//kont: LIFOan une honetan dauden prozesu kopurua
//prozesu bat sartzen bada, zein posizioan sartu behar den eta ateratzen bada kont-1 posiziotik atera behar da.
class Ilara {
	int kont = 0;
	char[] buf;
	int tam;
	Pantaila p;
	String space;

	public Ilara(int ptam, Pantaila pp) {
		p = pp;
		tam = ptam;
		this.buf = new char[tam];
		for (int i = 0; i < this.tam; i++) {
			this.buf[i] = ' ';
		}
	}

	public void setSpace(String spce) {
		this.space = spce;
	}

//when (k<FK) sartu[s]
	public synchronized int sartu(int id, String spaceP) throws InterruptedException {
		while (!(kont < tam))
			wait();
		buf[kont] = '*';
		kont++;
		p.idatzi(spaceP + "sartu:" + (kont-1));
		p.idatzi(this.toString(id));
		notifyAll();
		return kont-1;
	}

//when(x==a) atera[a]
	public synchronized void irten(int id, String spaceP, int prz) throws InterruptedException {
		while (!(kont-1 == prz))
			wait();
		kont--;
		buf[kont] = ' ';
		p.idatzi(spaceP + "atera:" + prz);
		p.idatzi(this.toString(id));
		notifyAll();
	}

	public synchronized String toString(int id) {
		for (int i = id; i < LIFOIlaraApp.ProzKop; i++) {
			p.idatzi("\t");
		}
		String pbuf = "[";
		for (int i = 0; i < this.tam - 1; i++) {
			pbuf += this.buf[i];
		}
		pbuf += this.buf[this.tam - 1] + "]\n";
		return pbuf;
	}
}

class Prozesua extends Thread {
	int id;
	int acctiveProccess = -1;
	String Space;
	Ilara buf;
	Pantaila p;

	Prozesua(int pid, Ilara b, Pantaila pp, String pspace) {
		buf = b;
		p = pp;
		id = pid;
		Space = pspace;
	}

	public int zenbakiaAuzas() {
		Random rand = new Random();
		return rand.nextInt((10 - 1) + 1) + 1;
	}
//P = ( sartu[x:FR] -> atera[x] -> P).
	public void run() {
		try {
			while (true) {
				this.itxaron();
				this.acctiveProccess = buf.sartu(this.id, this.Space);
				this.itxaron();
				buf.irten(this.id, this.Space, this.acctiveProccess);
				this.itxaron();
			}
		} catch (InterruptedException e) {
		}

	}

	public void itxaron() throws InterruptedException {
		sleep((long) (Math.random() * 1000));
	}
}

class Pantaila {

	public Pantaila() {

	}

	synchronized public void idatzi(String s) {
		System.out.print(s);
	}
}


