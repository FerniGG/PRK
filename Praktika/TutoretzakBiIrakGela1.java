package G5_6_Tutoretzak;

import java.util.Random;

//////////////////////////////////////////////////
///   Egilea: Fernando Gonzalez                ///
///   Data : 25/03/2023                        ///
//////////////////////////////////////////////////

//Gai Zenb : 5.2
//Ariketa Zenb : 6                         
//Ariketa Izena : Tutoretzak  
//Enuntziatua : Tutoretzak (FSPz modelatu eta Java-z inplementatu):
//Ikasleek azterketa garaian ikasten egoten dira (suposatzen da).
//Zerbait ez badute ulertzen irakasleen bulegora joaten dira, bulegoan sartu,
//galdera egin eta erantzuna jaso ondoren bulegotik ateratzen dira, berriz ikastera joateko.

//Bi irakaslek, ikasle batek galdera bat egiten dionean, erantzun baino lehen
//pentsatu egiten du (suposatzen da). Gainera irakaslea, nahiko berezia denez,
//tutoretzetarako ondoko arauak ditu:
//• Bulegoan bi ikasle batera egon daitezke, baina ez gehiago.
//• Ikasle batek egin duen galdera erantzun arte beste ikaslerik ezin du galdetu.
//• Ikaslea bulegotik irten ahalko du (soilik) irakaslearen erantzuna jaso ondoren.
//• Ikasle bakoitzak galdera bakar bat egin dezake tutoretza bakoitzean.
//• Bulegoan sartzeko eta galdera egiteko ez da errespetatzen aurretik zein zegoen.


public class TutoretzakApp {
	public static int N = 4;
	public static int irakMax = 2;
	public static int ikMax = 6;

	public static void main(String[] args) {
		Pantaila p = new Pantaila();
		p.idatzi("Irak[" + 0 + "]\t\t");
		p.idatzi("Irak[" + 1 + "]\t\t");
		for (int i = 1; i <= ikMax; i++) {
			p.idatzi("Ik[" + (i) + "]\t\t");
		}
		p.idatzi("Gela\n");
		p.idatzi(
				"=====================================================================================================\n");
		Gela g = new Gela(N,irakMax, p);
		p.idatzi(g.toString(-1));
		Irakaslea ir1 = new Irakaslea("",g, p);
		Irakaslea ir2 = new Irakaslea("\t\t",g, p);
		Ikaslea ikList[] = new Ikaslea[ikMax];

		String space = "\t\t\t\t";
		for (int i = 0; i < ikMax; i++) {
			ikList[i] = new Ikaslea(i + 1, g, p, space);
			space += "\t\t";
		}
		ir1.start();
		ir2.start();
		for (int i = 0; i < ikMax; i++) {
			ikList[i].start();
		}

	}

}

//Gela
//i:gela barruan dauden irakasle kopurua
//k: gelan daduden ikasle kop
//g: galdera bat egin den edo ez
//e: erantzuna eman den edo ez
class Gela extends Thread {
	int GelaTam;
	int irakKop;
	Pantaila p;
	int[] Gela;
	int k;
	int g, e;

	public Gela(int pn,int irakkop, Pantaila pp) {
		this.GelaTam = pn;
		irakKop=irakkop;
		p = pp;
		this.Gela = new int[this.GelaTam];
		for (int i = 0; i < this.GelaTam; i++) {
			this.Gela[i] = 0;
		}
	}

//	when(k<N) 			ik[PR].sartu->GELA[i][k+1][g][e]
	public synchronized void sartu(int id, String space) throws InterruptedException {
		while (!(this.k < this.GelaTam))
			wait();
		k++;
		boolean found = false;
		for (int i = 0; i < this.GelaTam; i++) {
			if (!found) {
				if (this.Gela[i] == 0) {
					this.Gela[i] = id;
					found = true;
				}
			}
		}
		p.idatzi(space + "sartu" );
		p.idatzi(this.toString(id));
		notify();

	}

	//							|ik[PR].atera->GELA[i][k-1][0][0]
	public synchronized void atera(int id, String space) throws InterruptedException {
		while (!(this.k > 0))
			wait();
		k--;
		boolean found = false;
		for (int i = 0; i < this.GelaTam; i++) {
			if (!found) {
				if (this.Gela[i] == id) {
					this.Gela[i] = 0;
					found = true;
				}
			}
		}
		p.idatzi(space + "atera" );
		p.idatzi(this.toString(id));
		notify();

	}
//							|when(g>0 && e<g)	ir[IR].pentsatu->ir[IR].erantzun->GELA[i][k][g][e+1]
	public synchronized void pentsatu(String space) throws InterruptedException {
		while (!(this.g >0 && this.e<this.g))
			wait();
		p.idatzi(space+"Pentsatu\n" );
		this.e++;
		p.idatzi(space+"Erantzun\n" );
		notifyAll();

	}

	//						   	|when(g<i) 		ik[PR].galdetu->GELA[i][k][g+1][e]
	public synchronized void galdetu(int id, String space) throws InterruptedException {
		while (!(this.g < this.irakKop))
			wait();
		this.g++;
		p.idatzi(space + "galdetu" );
		p.idatzi(this.toString(id));
		notifyAll();

	}
//							|when(e>1 && g>1) ik[PR].erantzunaJaso->GELA[i][k][g-1][e-1]
	public synchronized void erantsunaJaso(int id, String space) throws InterruptedException {
		while (!(this.g>0 && this.e>0))
			wait();
		this.g --;
		this.e --;
		p.idatzi(space + "erantzunaJaso\n" );
		notifyAll();

	}

	public synchronized String toString(int id) {
		for (int i = id; i < TutoretzakApp.ikMax + 1; i++) {
			p.idatzi("\t\t");
		}
		String mezua = "[";
		for (int i = 0; i < this.GelaTam; i++) {
			if (this.Gela[i] == 0) {
				mezua += "__";
			} else {
				mezua += "" + this.Gela[i];
			}
			if (i != this.GelaTam - 1)
				mezua += "|";

		}
		mezua += "]\n";
		return mezua;
	}



}

class Irakaslea extends Thread {
	int id = 0;
	Gela g;
	Pantaila p;
	String space;
	public Irakaslea(String pspace,Gela gg, Pantaila pp) {
		g = gg;
		p = pp;
		space=pspace;
	}

	private static int getRandomNumberInRange(int min, int max) {

		Random r = new Random();
		return r.ints(min, (max + 1)).findFirst().getAsInt();

	}

	public void itxaron() throws InterruptedException {
		sleep(getRandomNumberInRange(1, 30) * 100);
	}

	public void run() {
		try {
			while (true) {
				itxaron();
				g.pentsatu(this.space);
				itxaron();
			}

		} catch (InterruptedException e) {
		}
	}
}

class Ikaslea extends Thread {
	int id;
	Gela g;
	Pantaila p;
	String space;

	public Ikaslea(int pn, Gela pg, Pantaila pp, String pspace) {
		this.id = pn;
		g = pg;
		p = pp;
		space = pspace;
	}

	public void run() {
		try {
			while (true) {
				itxaron();
				g.sartu(id, space);
				itxaron();
				g.galdetu(id,space);
				itxaron();
				g.erantsunaJaso(id, space);
				itxaron();
				g.atera(id, space);
				itxaron();
			}

		} catch (InterruptedException e) {
		}
	}

	private static int getRandomNumberInRange(int min, int max) {

		Random r = new Random();
		return r.ints(min, (max + 1)).findFirst().getAsInt();

	}

	public void itxaron() throws InterruptedException {
		sleep(getRandomNumberInRange(1, 20) * 100);
	}
}

class Pantaila {
	public Pantaila() {

	}

	public synchronized void idatzi(String mezua) {
		System.out.print(mezua);
	}
}
