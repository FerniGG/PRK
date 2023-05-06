package A_7_1_b;

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
	public final static int muga=16;

	public static void main(String[] args) {
		Pantaila p = new Pantaila();
		for (int i = 0; i < ProzKop; i++) {
			p.idatzi("Id" + i + "\t");
		}
		p.idatzi("Ilara\n");
		p.idatzi("=====================================================================================\n");
		Buffer buf = new Buffer(tam,muga, p);
		p.idatzi(buf.toString(0));
		Idazlea idList[] = new Idazlea[ProzKop];

		String space = "";
		for (int i = 0; i < ProzKop; i++) {
			idList[i] = new Idazlea(i, buf, p, space);
			space += "\t";
		}

		for (int i = 0; i < ProzKop; i++) {
			idList[i].start();
		}

		buf.setSpace(space);

	}
}

//kont: LIFOan une honetan dauden prozesu kopurua
//prozesu bat sartzen bada, zein posizioan sartu behar den eta ateratzen bada kont-1 posiziotik atera behar da.
class Buffer {
	int kont,it = 0;
	int itMuga;
	char[] buf;
	int tam;
	Pantaila p;
	String space;

	public Buffer(int ptam,int pit, Pantaila pp) {
		p = pp;
		tam = ptam;
		itMuga=pit;
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
		while (!(kont < tam && it<itMuga))
			wait();
		buf[kont] = '*';
		kont++;
		it++;
		p.idatzi(spaceP + "sartu:" + (kont-1));
		p.idatzi(this.toString(id));
		notifyAll();
		return kont-1;
	}

	/*| when (k>0) atera[k-1] -> if(k-1==0) then LIFO[0][0]
											else LIFO[k-1][i]*/
	public synchronized void irten(int id, String spaceP, int prz) throws InterruptedException {
		while (!(kont-1 == prz))
			wait();
		
		kont--;
		if(kont==0) {
			this.it=0;
		}
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
		pbuf += this.buf[this.tam - 1] + "]  Iterazio Kop="+this.it+" \n";
		return pbuf;
	}
}

class Idazlea extends Thread {
	int id;
	int acctiveProccess = -1;
	String Space;
	Buffer buf;
	Pantaila p;

	Idazlea(int pid, Buffer b, Pantaila pp, String pspace) {
		buf = b;
		p = pp;
		id = pid;
		Space = pspace;
	}

	public int zenbakiaAuzas() {
		Random rand = new Random();
		return rand.nextInt((10 - 1) + 1) + 1;
	}

	public void run() {
		try {
			while (true) {
				this.acctiveProccess = buf.sartu(this.id, this.Space);
				this.itxaron_sartu();
				buf.irten(this.id, this.Space, this.acctiveProccess);
				this.itxaron_irten();
			}
		} catch (InterruptedException e) {
		}

	}

	public void itxaron_sartu() throws InterruptedException {
		sleep((long) (Math.random()*1000));
	}
	
	public void itxaron_irten() throws InterruptedException {
		sleep((long) (Math.random()*1000));
	}
}

class Pantaila {

	public Pantaila() {

	}

	synchronized public void idatzi(String s) {
		System.out.print(s);
	}
}

