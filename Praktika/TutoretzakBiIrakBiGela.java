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
//Zerbait ez badute ulertzen irakaslearen bulegora joaten dira, bulegoan sartu,
//galdera egin eta erantzuna jaso ondoren bulegotik ateratzen dira, berriz ikastera joateko.

//Irakasleak, ikasle batek galdera bat egiten dionean, erantzun baino lehen
//pentsatu egiten du (suposatzen da). Gainera irakaslea, nahiko berezia denez,
//tutoretzetarako ondoko arauak ditu:
//• Bulegoan bi ikasle batera egon daitezke, baina ez gehiago.
//• Ikasle batek egin duen galdera erantzun arte beste ikaslerik ezin du galdetu.
//• Ikaslea bulegotik irten ahalko du (soilik) irakaslearen erantzuna jaso ondoren.
//• Ikasle bakoitzak galdera bakar bat egin dezake tutoretza bakoitzean.
//• Bulegoan sartzeko eta galdera egiteko ez da errespetatzen aurretik zein zegoen.


public class TutoretzakApp {
	public static int N = 2;
	public static int ikMax = 5;

	public static void main(String[] args) {
		Pantaila p = new Pantaila();
		p.idatzi("Irak[" + 0 + "]\t\t");
		p.idatzi("Irak[" + 1 + "]\t\t");
		for (int i = 1; i <= ikMax; i++) {
			p.idatzi("Ik[" + (i) + "]\t\t");
		}
		p.idatzi("Gela1\t\tGela2\n");
		p.idatzi(
				"==========================================================================================================================\n");
		Gela g1 = new Gela(N, p,"");
		Gela g2 = new Gela(N, p,"\t\t");
		Irakaslea ir1 = new Irakaslea(g1, p,"");
		Irakaslea ir2 = new Irakaslea(g2, p,"\t\t");
		Ikaslea ikList[] = new Ikaslea[ikMax];

		String space = "\t\t\t\t";
		for (int i = 0; i < ikMax; i++) {
			ikList[i] = new Ikaslea(i + 1, g1,g2, p, space);
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
//k: gelan daduden ikasle kop
//g: galdera bat egin den edo ez
//e: erantzuna eman den edo ez
class Gela extends Thread {
	int GelaTam;
	Pantaila p;
	int[] Gela;
	int k;
	boolean g, e;
	String space;

	public Gela(int pn, Pantaila pp,String sp) {
		this.GelaTam = pn;
		p = pp;
		this.Gela = new int[pn];
		space=sp;
		for (int i = 0; i < this.GelaTam; i++) {
			this.Gela[i] = 0;
		}
	}

//	when(k<N) 			ik[PR].sartu->GELA[k+1][g][e]
	public synchronized void sartu(int id,int were, String space) throws InterruptedException {
		while (!(this.k < TutoretzakApp.N))
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
		p.idatzi(space + "sartu"+were );
		p.idatzi(this.toString(id));
		notify();

	}

	//							|ik[PR].atera->GELA[k-1][0][0]
	public synchronized void atera(int id,int were, String space) throws InterruptedException {
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
		p.idatzi(space + "atera"+were  );
		p.idatzi(this.toString(id));
		notify();

	}
//							|when(g==1 && e==0)	ir.pentsatu->ir.erantzun->GELA[k][g][1]
	public synchronized void pentsatu(String space) throws InterruptedException {
		while (!(this.g == true && this.e==false))
			wait();
		p.idatzi(space+"pents\n" );
		this.e = true;
		p.idatzi(space+"eran\n" );
		notifyAll();

	}

	//						   	|when(g==0) 		ik[PR].galdetu->GELA[k][1][e]
	public synchronized void galdetu(int id,int were,String space) throws InterruptedException {
		while (!(this.g == false))
			wait();
		this.g = true;
		p.idatzi(space + "gald"+were  );
		p.idatzi(this.toString(id));
		notifyAll();

	}
//							|when(e==1 && g==1) ik[PR].erantzunaJaso->GELA[k][0][0]
	public synchronized void erantsunaJaso(int id,int were, String space) throws InterruptedException {
		while (!(this.g == true && this.e == true))
			wait();
		this.g = false;
		this.e = false;
		p.idatzi(space + "eranJaso\n"+were  );
		notifyAll();

	}

	public synchronized String toString(int id) {
		p.idatzi(space);
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

	public Irakaslea(Gela gg, Pantaila pp,String pspace) {
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
				g.pentsatu(space);
				itxaron();
			}

		} catch (InterruptedException e) {
		}
	}
}

class Ikaslea extends Thread {
	int id;
	Gela g1;
	Gela g2;
	Pantaila p;
	String space;

	public Ikaslea(int pn, Gela pg1, Gela pg2, Pantaila pp, String pspace) {
		this.id = pn;
		g1 = pg1;
		g2 = pg2;
		p = pp;
		space = pspace;
	}

	public void run() {
		try {
			while (true) {
				itxaron();
				if(this.g1.k<this.g1.GelaTam) {
				itxaron();
				g1.sartu(id,1, space);
				itxaron();
				g1.galdetu(id,1,space);
				itxaron();
				g1.erantsunaJaso(id,1, space);
				itxaron();
				g1.atera(id,1, space);
				itxaron();
				}else {
					itxaron();
					g2.sartu(id,2, space);
					itxaron();
					g2.galdetu(id,2,space);
					itxaron();
					g2.erantsunaJaso(id,2, space);
					itxaron();
					g2.atera(id,2, space);
					itxaron();
				}
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
