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
		for (int i = 1; i <= ikMax; i++) {
			p.idatzi("Ik[" + (i) + "]\t\t");
		}
		p.idatzi("Gela\n");
		p.idatzi(
				"=====================================================================================================\n");
		Gela g = new Gela(N, p);
		p.idatzi(g.toString(0));
		Irakaslea ir = new Irakaslea(g, p);
		Ikaslea ikList[] = new Ikaslea[ikMax];

		String space = "\t\t";
		for (int i = 0; i < ikMax; i++) {
			ikList[i] = new Ikaslea(i + 1, g, p, space);
			space += "\t\t";
		}
		ir.start();
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

	public Gela(int pn, Pantaila pp) {
		this.GelaTam = pn;
		p = pp;
		this.Gela = new int[pn];
		for (int i = 0; i < this.GelaTam; i++) {
			this.Gela[i] = 0;
		}
	}

//	when(k<N) 			ik[PR].sartu->GELA[k+1][g][e]
	public synchronized void sartu(int id, String space) throws InterruptedException {
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
		p.idatzi(space + "sartu" );
		p.idatzi(this.toString(id));
		notify(); //sartzeko itxaroten dauden ikasleei abisatzen die, baina hauek bakarrik sartu ahalko dir

	}

	//							|ik[PR].atera->GELA[k-1][0][0]
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
		notifyAll(); //sartzeko itxaroten dauden Ikasleei eta galduera 
		//egin nahi duten ikasleei abisatu hauek sar edo galdetu dezateen.

	}
//							|when(g==1 && e==0)	ir.pentsatu->ir.erantzun->GELA[k][g][1]
	public synchronized void pentsatu() throws InterruptedException {
		while (!(this.g == true && this.e==false))
			wait();
		p.idatzi("Pentsatu\n" );
		p.idatzi("Erantzun\t" );
		this.e = true;
		p.idatzi(this.toString(1));
		
		notifyAll();//erantzuna itxaroten dagoen ikasleari abisatu.

	}

	//						   	|when(g==0) 		ik[PR].galdetu->GELA[k][1][e]
	public synchronized void galdetu(int id, String space) throws InterruptedException {
		while (!(this.g == false))
			wait();
		this.g = true;
		p.idatzi(space + "galdetu" );
		p.idatzi(this.toString(id));
		notifyAll();//Itxaronten dagoen hariei irakasleai konkretuki abisatu.

	}
//							|when(e==1 && g==1) ik[PR].erantzunaJaso->GELA[k][0][0]
	public synchronized void erantsunaJaso(int id, String space) throws InterruptedException {
		while (!(this.g == true && this.e == true))
			wait();
		this.g = false;
		this.e = false;
		p.idatzi(space + "erantzunaJaso\n" );
		notifyAll(); // itxaroten dauden ikasleei abisatzen die, konkretuki galdera egin nahi dutenei.

	}

	public synchronized String toString(int id) {
		for (int i = id; i < TutoretzakApp.ikMax + 1; i++) {
			p.idatzi("\t\t");
		}
		String mezua = "[";
		for (int i = 0; i < this.GelaTam; i++) {
			if (this.Gela[i] == 0) {
				mezua += "_";
			} else {
				mezua += "" + this.Gela[i];
			}
			if (i != this.GelaTam - 1)
				mezua += "|";

		}
		mezua += "]  ";
		if(this.g==false) {
			mezua+="g= -  ";
		}else {
			mezua+="g= G  ";
		}
		if(this.e==false) {
			mezua+="e= -  ";
		}else {
			mezua+="e= E  ";
		}
		return mezua+"\n";
	}



}

class Irakaslea extends Thread {
	int id = 0;
	Gela g;
	Pantaila p;

	public Irakaslea(Gela gg, Pantaila pp) {
		g = gg;
		p = pp;
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
				g.pentsatu();
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
		sleep((long) (Math.random() *  1000));
	}
	
}

class Pantaila {
	public Pantaila() {

	}

	public synchronized void idatzi(String mezua) {
		System.out.print(mezua);
	}
}

