package G5_3_Maximoa;

//////////////////////////////////////////////////
///   Egilea: Fernando Gonzalez                ///
///   Data : 26/03/2023                        ///
//////////////////////////////////////////////////

//   Gai Zenb : 5.2
//   Ariketa Zenb : 3                          
//   Ariketa Izena : Maximoa  
//   Enuntziatua : Array bateko zenbakien artean maximoa aurkitu agendaren eredua erabiliz.
//	 				FSP eredua eman eta Javaz inplementatu, 

import java.util.Random;

public class MaximoaApp {
	public final static int ProzKop = 3;
	public final static int N = 10;

	public static void main(String[] args) {
		Pantaila p = new Pantaila();
		for (int i = 0; i < ProzKop; i++) {
			p.idatzi("P[" + (i) + "]\t\t\t");
		}
		p.idatzi("Agenda\n");
		p.idatzi("================================================================================\n");
		Agenda ag = new Agenda(N, p);
		Prozesua idList[] = new Prozesua[ProzKop];

		String space = "";
		for (int i = 0; i < ProzKop; i++) {
			idList[i] = new Prozesua(i, ag, p, space);
			space += "\t\t\t";
		}
		for (int i = 0; i < ProzKop; i++) {
			idList[i].start();
		}

	}
}

class Agenda {
	int i;
	int j = 0;
	int tam;
	String[] agenda;
	Pantaila p;
	String space;

	// AGENDA[uneko i-ren posizioa][Zenbat prozesuk hartu duten zenbakiak
	// konparatzeko]
	public Agenda(int ptam, Pantaila pp) {
		p = pp;
		tam = ptam;
		i = tam - 1;
		this.agenda = new String[tam];
		for (int i = 0; i < this.tam - 1; i++) {
			int zenb = this.zenbakiaAuzas();
			if (zenb <= 9) {
				this.agenda[i] = " " + zenb;
			} else {
				this.agenda[i] = "" + zenb;
			}

		}
		int zenb = this.zenbakiaAuzas();
		if (zenb <= 9) {
			this.agenda[tam - 1] = " " + zenb;
		} else {
			this.agenda[tam - 1] = "" + zenb;
		}
		p.idatzi("\t" + this.toString(0));

	}

	public int zenbakiaAuzas() {
		Random rand = new Random();
		return rand.nextInt((99 - 1) + 1) + 1;
	}

	// pr[PR].sartu -> [i+1] -> if(i==1 && j==1)then (max->STOP )
	// else AGENDA[i+1][j-1]
	public synchronized int sartu(int pid, String spaceP, int zenb) throws InterruptedException {
		while (!(i < tam))
			wait();
		int signal = 0;
		if (zenb <= 9) {
			this.agenda[i + 1] = " " + zenb;
		} else {
			this.agenda[i + 1] = "" + zenb;
		}
		p.idatzi(pid, "sartu[" + zenb + "]");
		p.idatzi(this.toString(pid));
		if (i == -1 && j == 1) {
			signal = zenb;
		} else {
			i = i + 1;
			j = j - 1;
		}
		notify(); // itxaroten dauden hariei notifikatu haien eragiketak egin dezaten eta hartzeko prest
						// daudenen hariak sartutako zenbakia har dezaten
		return signal;// STOP
	}

//	when(i>0) pr[PR].hartu
	public synchronized int[] hartu(int pid, String spaceP) throws InterruptedException {
		int[] zenb = new int[2];
		while (!(i > 0))
			wait();
		zenb[0] = Integer.valueOf(agenda[i].replace(" ", ""));
		zenb[1] = Integer.valueOf(agenda[i - 1].replace(" ", ""));
		this.agenda[i] = "  ";
		this.agenda[i - 1] = "  ";
		String mezua = "hartu[";
		if (zenb[0] <= 9) {
			mezua += " " + zenb[0];
		} else {
			mezua += "" + zenb[0];
		}
		if (zenb[1] <= 9) {
			mezua += "][ " + zenb[1];
		} else {
			mezua += "][" + zenb[1];
		}
		p.idatzi(pid, mezua + "]");
		p.idatzi(this.toString(pid));
		j = j + 1;
		i = i - 2;
		return zenb;// hartutako bi zenbakiak bueltatu konparazioa egiteko
	}

	public synchronized String toString(int id) {
		p.idatzi("\t\t");
		for (int i = id + 1; i < MaximoaApp.ProzKop; i++) {
			p.idatzi("\t\t\t");
		}
		String agendaStr = "[";
		for (int i = 0; i < this.tam - 1; i++) {
			if (this.agenda[i] != "  ") {
				agendaStr += this.agenda[i] + "|";
			} else {
				agendaStr += "  |";
			}

		}
		if (this.agenda[this.tam - 1] != "  ") {
			agendaStr += this.agenda[this.tam - 1] + "";
		} else {
			agendaStr += "  ";
		}
		agendaStr += "]\n";
		return agendaStr;
	}
}

class Prozesua extends Thread {
	int id;
	int[] zenb = new int[2];
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

	public synchronized int konparatu() throws InterruptedException {
		if (zenb[0] >= zenb[1])
			return zenb[0];
		else
			return zenb[1];
	}

	public void run() {
		try {
			int signal = 0;
			while (signal == 0) {
				zenb = agen.hartu(id, Space);
				this.itxaron();
				int max = this.konparatu();
				this.itxaron();
				signal = agen.sartu(id, Space, max);
				this.itxaron();
			}
			p.idatzi("\n* * MAXIMOA: " + signal + " * *\n");
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
		for (int i = id; i > 0; i--) {
			System.out.print("\t\t\t");
		}
		System.out.print(s);
	}
}
