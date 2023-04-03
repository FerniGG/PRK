package A1.Asimetria;

//////////////////////////////////////////////////
///   Egilea: Fernando Gonzalez                ///
///   Data : 1/04/2023                        ///
//////////////////////////////////////////////////

//Gai Zenb : 6
//Ariketa Zenb : 1                          
//Ariketa Izena : FilosofoenAfariaAsimetria  
//Enuntziatua : Filosofoen afaria asimetriarekin Java-z inplementatu 

import java.util.Random;

public class FiloApp {
	public final static int N = 5;

	public static void main(String args[]) {
		Pantaila pant = new Pantaila(N);
		Sardeska sar[] = new Sardeska[N];
		Filosofoa fil[] = new Filosofoa[N];
		for (int i = 0; i < N; ++i)
			sar[i] = new Sardeska(i, pant);

		for (int i = 0; i < N; ++i) {
			fil[i] = new Filosofoa(i, sar[(i - 1 + N) % N], sar[i], pant);
			fil[i].start();
		}
	}
}


class Filosofoa extends Thread {
	Pantaila pant;
	String space;
	int zenb;
	Sardeska eskubikoa;
	Sardeska ezkerrekoa;

	public Filosofoa(int pzenb, Sardeska ezk, Sardeska esk,  Pantaila pp) {
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
		sleep(getRandomNumberInRange(1, 10)*100);
	}
/*(eseri->
		if (I%2==1) then (ezker.get ->eskubi.get->JAN)
 		else (eskubi.get->ezker.get ->JAN)
 )*/
	public void run() {
		try {
			while (true) {
				if (this.zenb % 2 == 0) {
					pant.idatzi(zenb, "pentsatzen");
					itxaron();
						pant.idatzi(zenb, "eseri");
						itxaron();
						eskubikoa.get();
						pant.idatzi(zenb, "eskub.hartu du");
						itxaron();
						ezkerrekoa.get();
						pant.idatzi(zenb, "ezker.hartu du");
						itxaron();
						pant.idatzi(zenb, "jaten");
						itxaron();
						eskubikoa.put();
						pant.idatzi(zenb, "eskub.utzi du");
						itxaron();
						ezkerrekoa.put();
						pant.idatzi(zenb, "ezker.utzi du");
						itxaron();
						pant.idatzi(zenb, "altxatu");
						itxaron();
				} else {
						pant.idatzi(zenb, "pentsatzen");
						itxaron();
						pant.idatzi(zenb, "eseri");
						itxaron();
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
//when (h==1) put->SARD[0]).
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
		System.out.print("\n========|========");
		for (int i = 0; i < kop; ++i)
			System.out.print("================");
		System.out.println("========|========");
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

