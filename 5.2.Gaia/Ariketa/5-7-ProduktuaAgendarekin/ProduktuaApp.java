//////////////////////////////////////////////////
///   Egilea: Fernando Gonzalez                ///
///   Data : 26/03/2023                        ///
//////////////////////////////////////////////////

//   Gai Zenb : 5.2
//   Ariketa Zenb : 7                          
//   Ariketa Izena : Produktua agendatekin  
//   Enuntziatua : Array bateko zenbakien produktua lortu agendaren eredua erabiliz.
//	 				FSP eredua eman eta Javaz inplementatu, 

import java.util.Random;

public class ProduktuaApp {
	public final static int ProzKop = 3;
	public final static int N = 10;

	public static void main(String[] args) {
		Pantaila p = new Pantaila();
		for (int i = 0; i < ProzKop; i++) {
			p.idatzi("P[" + (i + 1) + "]\t\t\t");
		}
		p.idatzi("Agenda\n");
		p.idatzi("=======================================================\n");
		Agenda ag = new Agenda(N, p);
		Prozesua idList[] = new Prozesua[ProzKop];

		String space = "";
		for (int i = 0; i < ProzKop; i++) {
			idList[i] = new Prozesua(i + 1, ag, p, space);
			space += "\t\t\t";
		}
		ag.setSpace(space);
		for (int i = 0; i < ProzKop; i++) {
			idList[i].start();
		}

	}
}

class Agenda {
	int i;
	int j = 0;
	int tam;
	long[] agenda;
	Pantaila p;
	String space;

	// AGENDA[uneko i-ren posizioa][Zenbat prozesuk hartu duten zenbakiak
	// biderkatzeko]
	public Agenda(int ptam, Pantaila pp) {
		p = pp;
		tam = ptam;
		i = tam - 1;
		this.agenda = new long[tam];
		for (int i = 0; i < this.tam - 1; i++) {
			int zenb = this.zenbakiaAuzas();
			this.agenda[i] = zenb;

		}
		int zenb = this.zenbakiaAuzas();
		this.agenda[this.tam - 1] = zenb;
		// p.idatzi(this.toString(0));

	}

	public int zenbakiaAuzas() {
		Random rand = new Random();
		return rand.nextInt((100 - 1) + 1) + 1;
	}

	public void setSpace(String spce) {
		this.space = spce;
		p.idatzi(this.toString(spce));
	}

	// pr[PR].sartu -> [i+1] -> if(i==1 && j==1)then (emaitza->STOP )
	// else AGENDA[i+1][j-1]
	public synchronized long sartu(int pid, String spaceP, long zenb) throws InterruptedException {
		while (!(i < tam))
			wait();
		long signal = 0;
		agenda[i + 1] = zenb;
		p.idatzi(pid, "sartu[" + zenb + "]\n");
		p.idatzi(this.toString(this.space));
		if (i == -1 && j == 1) {
			signal = zenb;
		} else {
			i = i + 1;
			j = j - 1;
		}
		notify();
		return signal;// STOP
	}

//	when(i>0) pr[PR].hartu
	public synchronized long[] hartu(int pid, String spaceP) throws InterruptedException {
		long[] zenb = new long[2];
		while (!(i > 0))
			wait();
		zenb[0] = agenda[i];
		zenb[1] = agenda[i - 1];
		this.agenda[i] = 0;
		this.agenda[i - 1] = 0;
		p.idatzi(pid, "hartu[" + zenb[0] + "][" + zenb[1] + "]\n");
		p.idatzi(this.toString(this.space));
		j = j + 1;
		i = i - 2;
		notify();
		return zenb;
	}

	public synchronized String toString(String space) {
		/*
		 * for (int i = id; i < MaximoaApp.ProzKop; i++) { p.idatzi("\t\t\t"); }
		 */
		p.idatzi(space);
		String agendaStr = "[";
		for (int i = 0; i < this.tam - 1; i++) {
			if (this.agenda[i] != 0) {
				agendaStr += this.agenda[i] + "|";
			} else {
				agendaStr += " |";
			}

		}
		if (this.agenda[this.tam - 1] != 0) {
			agendaStr += this.agenda[this.tam - 1] + "";
		} else {
			agendaStr += " ";
		}
		agendaStr += "]\n";
		return agendaStr;
	}
}

class Prozesua extends Thread {
	int id;
	long[] zenb = new long[2];
	String Space;
	Agenda agen;
	Pantaila p;

	Prozesua(int pid, Agenda a, Pantaila pp, String pspace) {
		agen = a;
		p = pp;
		id = pid;
		Space = pspace;
	}

	public int zenbakiaAuzas() {
		Random rand = new Random();
		return rand.nextInt((10 - 1) + 1) + 1;
	}

	public synchronized long biderkatu() throws InterruptedException {
		return zenb[0] * zenb[1];
	}

	public void run() {
		try {
			long signal = 0;
			while (signal == 0) {
				zenb = agen.hartu(id, Space);
				this.itxaron();
				long prod = this.biderkatu();
				this.itxaron();
				signal = agen.sartu(id, Space, prod);
				this.itxaron();
			}
			p.idatzi("\n* * Produktuaren Emaitza: " + signal + " * *\n");
		} catch (InterruptedException e) {
		}

	}

	public void itxaron() throws InterruptedException {
		sleep(zenbakiaAuzas() * 100);
	}
}

class Pantaila {

	public Pantaila() {

	}

	synchronized public void idatzi(String s) {
		System.out.print(s);
	}

	synchronized public void idatzi(int id, String s) {
		for (int i = id; i < ProduktuaApp.ProzKop; i++) {
			System.out.print("\t\t\t");
		}
		System.out.print(s);
	}
}

