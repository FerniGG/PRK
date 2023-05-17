package A_7_1_a;

import java.util.Random;

//////////////////////////////////////////////////
///   Egilea: Fernando Gonzalez                ///
///   Data : 27/04/2023                        ///
//////////////////////////////////////////////////

//Gai Zenb : 7
//Ariketa Zenb : 1-a                         
//Ariketa Izena : LIFOIlara  
//Enuntziatua :. LIFO pilaren arazoa soluzionatu, prozesu guztiak noizbait pilatik aterako direla ziurtatuz.
//Bizitasun propietatea bortxatzen dela ikusten dena.

public class LIFOIlaraApp {
	public final static int ProzKop = 10;
	public final static int tam=7;

	public static void main(String[] args) {
		Pantaila p = new Pantaila();

		LifoIlara lifo = new LifoIlara(tam, p);
		
		Prozesua idList[] = new Prozesua[ProzKop];

		String space = "";
		for (int i = 0; i < ProzKop; i++) {
			idList[i] = new Prozesua(i, lifo, p, space);
			space += "\t";
		}

		for (int i = 0; i < ProzKop; i++) {
			idList[i].start();
		}

		lifo.setSpace(space);

	}
}

//kont: LIFOan une honetan dauden prozesu kopurua
//prozesu bat sartzen bada, zein posizioan sartu behar den eta ateratzen bada kont-1 posiziotik atera behar da.
class LifoIlara {
	int kont = 0;
	char[] buf;
	int tam;
	Pantaila p;
	String space;

	public LifoIlara(int ptam, Pantaila pp) {
		p = pp;
		tam = ptam;
		this.buf = new char[tam];
		for (int i = 0; i < this.tam; i++) {
			this.buf[i] = ' ';
		p.idatzi(this.toString(0));
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

//when (k>0) atera[k-1]
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
	int acctiveProccess = -1; //ilaran zein posizioan dagoen gordeko duen atributua -1 izango da baldin eta ilaran ez badago
	String Space;
	LifoIlara ilara;
	Pantaila p;

	Prozesua(int pid, LifoIlara pil, Pantaila pp, String pspace) {
		ilara = pil;
		p = pp;
		id = pid;
		Space = pspace;
	}


	public void run() {
		try {
			while (true) {
				this.acctiveProccess = ilara.sartu(this.id, this.Space);
				this.itxaron_sartu();
				ilara.irten(this.id, this.Space, this.acctiveProccess);
				this.itxaron_irten();
			}
		} catch (InterruptedException e) {
		}

	}

	public void itxaron_sartu() throws InterruptedException {
		sleep((long) (Math.random()*1000));
	}
	
	public void itxaron_irten() throws InterruptedException {
		sleep((long) (Math.random()*100));
	}
}

class Pantaila {

	public Pantaila() {
		for (int i = 0; i < LIFOIlaraApp.ProzKop; i++) {
			idatzi("Id" + i + "\t");
		}
		idatzi("Ilara\n");
		idatzi("=====================================================================================\n");
	}

	synchronized public void idatzi(String s) {
		System.out.print(s);
	}
}


