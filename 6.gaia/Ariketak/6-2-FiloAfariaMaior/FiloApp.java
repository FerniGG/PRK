package A2.Maior;

import java.util.Random;

//////////////////////////////////////////////////
///   Egilea: Fernando Gonzalez                ///
///   Data : 1/04/2023                        ///
//////////////////////////////////////////////////

//Gai Zenb : 6
//Ariketa Zenb : 2                        
//Ariketa Izena : FilosofoenAfariaMaiordomoa 
//Enuntziatua : . Filosofoen afarirako beste soluzio bat:
//bost filosofoetatik soilik lau filosofo eser daitezke batera.
//MAIORDOMO prozesu bat espezifikatu, proposatutako ereduarekin 
//konposatzean, gehienez lau filosofo eseri ekintza egitea baimentzen 
//duena, altxatu ekintza bat gertatu aurretik. Ziurtatu ez dela elkar-
//blokeaketarik ematen.

//Trazan erakutsi behar da zenbat filosofo dauden eserita 
//(izartxoekin adierazi)
public class FiloApp {
	public final static int N = 5;

	public static void main(String args[]) {
		Pantaila pant = new Pantaila(N);
		Maiordomoa m = new Maiordomoa(N, pant);
		Sardeska sar[] = new Sardeska[N];
		Filosofoa fil[] = new Filosofoa[N];
		for (int i = 0; i < N; ++i)
			sar[i] = new Sardeska(i, pant);

		for (int i = 0; i < N; ++i) {
			fil[i] = new Filosofoa(i, sar[(i - 1 + N) % N], sar[i], m, pant);
			fil[i].start();
		}
	}
}
//filoKop: jada eseritako filosofo kop
class Maiordomoa extends Thread {
	Pantaila pant;
	int filoKop;
	int id;
	String[] eserlekuak= new String[FiloApp.N];

	public Maiordomoa(int pid, Pantaila pp) {
		for(int i=0;i<FiloApp.N;i++) {
			eserlekuak[i]=" ";
		}
		id = pid;
		filoKop = 0;
		pant = pp;
	}

//	when (i<N-1) eseri->MAIOR[i+1]
	public synchronized int eseri(int zenb) throws InterruptedException {
		while (!(this.filoKop < (FiloApp.N - 1)))
			wait();
		filoKop++;
		eserlekuak[zenb]="*";
		pant.idatziEserlekua(FiloApp.N,eserlekuak);
		notify();
		return filoKop;
	}

	/* when (i>0) altxatu->MAIOR[i-1] */
	public synchronized int altxatu(int zenb) throws InterruptedException {
		while (!(this.filoKop > 0))
			wait();
		filoKop--;
		eserlekuak[zenb]=" ";
		pant.idatziEserlekua(FiloApp.N,eserlekuak);
		notify();
		return filoKop;
	}
}

class Filosofoa extends Thread {
	Maiordomoa maior;
	Pantaila pant;
	String space;
	int zenb;
	Sardeska eskubikoa;
	Sardeska ezkerrekoa;

	public Filosofoa(int pzenb, Sardeska ezk, Sardeska esk, Maiordomoa m, Pantaila pp) {
		maior = m;
		zenb = pzenb;
		for (int i = 1; i <= zenb + 1; i++) {
			space += "\t\t";
		}
		pant = pp;
		eskubikoa = esk;
		ezkerrekoa = ezk;
	}

	private static int getRandomNumberInRange(int min, int max) {

		Random r = new Random();
		return r.ints(min, (max + 1)).findFirst().getAsInt();

	}

	public void itxaron() throws InterruptedException {
		sleep(getRandomNumberInRange(1, 10) * 100);
	}
//(eseri->ezker.get ->eskubi.get->jan->ezker.put->eskubi.put->altxatu)
	public void run() {
		try {
			int zenbateserita = 0;
			while (true) {
				zenbateserita = maior.eseri(this.zenb);
				if (zenbateserita < (FiloApp.N - 1)) {
					pant.idatzi(zenb, "pentsatzen");
					itxaron();
					pant.idatzi(zenb, "eseri");
					sleep(200);
					ezkerrekoa.get();
					pant.idatzi(zenb, "ezker.hartu du");
					itxaron();
					eskubikoa.get();
					pant.idatzi(zenb, "eskub.hartu du");
					itxaron();
					pant.idatzi(zenb, "jaten");
					itxaron();
					ezkerrekoa.put();
					pant.idatzi(zenb, "ezker.utzi du");
					itxaron();
					eskubikoa.put();
					pant.idatzi(zenb, "eskub.utzi du");
					itxaron();
					maior.altxatu(zenb);
					pant.idatzi(zenb, "altxatu");
					itxaron();
				}
			}
		} catch (InterruptedException e) {
		}
	}
}

class Sardeska {
	private boolean hartua = false;
	private int zenbakia;
	private Pantaila pant;

	Sardeska(int zenb, Pantaila p) {
		zenbakia = zenb;
		pant = p;
	}

//when (h==1) put->SARD[0]
	public synchronized void put() {
		hartua = false;
		pant.idatzi(zenbakia + " utzia ");
		notify();
	}

	// when (h==0) get->SARD[1]
	public synchronized void get() throws java.lang.InterruptedException {
		while (hartua)
			wait();
		hartua = true;
		pant.idatzi(zenbakia + " hartua");
	}
}

class Pantaila {
	int kop;

	public Pantaila(int k) {
		kop = k;
		System.out.print("Sardeak\t|\t");
		for (int i = 0; i < kop; ++i)
			System.out.print("Fil[" + i + "]\t\t");
		System.out.println("Maior");
		System.out.print("========|========");
		for (int i = 0; i < kop; ++i)
			System.out.print("================");
		System.out.println("========|========");
	}

	public void idatziEserlekua(int n, String[] eserlekuak) {
		String esermezua="[";
		for (int i = 0; i < eserlekuak.length; i++) {
			esermezua+=eserlekuak[i];
		}
		esermezua+="]";
		this.idatzi(n, esermezua);
		
	}

	public synchronized void idatzi(int nor, String testua) {
		System.out.print("\t|\t");
		for (int i = 0; i <= kop; i++) {
			if (nor == i)
				System.out.print(testua);
			else
				System.out.print("\t\t");
		}
		System.out.println();
	}

	public synchronized void idatzi(String testua) {
		System.out.println(testua + "|");
	}

}

