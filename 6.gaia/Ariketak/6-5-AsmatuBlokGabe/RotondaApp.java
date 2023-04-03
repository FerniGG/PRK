package A5.AsmaBlokGabe;

import java.util.Random;

//////////////////////////////////////////////////
///   Egilea: Fernando Gonzalez                ///
///   Data : 2/04/2023                        ///
//////////////////////////////////////////////////

//Gai Zenb : 6
//Ariketa Zenb : 5                        
//Ariketa Izena : AsmatutakoProblemaBlokeoaGabe
//Enuntziatua : Bidegurutze bat daukagun non kotxea gurutzera heltzen da.
//				Gelditu baino lehen udaltzain bat esango dio sartu daitekeen edo ez
//				sartzerakoan helmuga bidea aukeratuko du(unekoa ezin da izan).
//				Bide hori libre badago martzan jarriko da. eta Uneko bidea libratuko du.
// 				Bidegurutzera heltzerakoan helmuga bidea aukeratuko du berriro.

public class RotondaApp {
	public final static int N = 5;

	public static void main(String args[]) {
		Pantaila pant = new Pantaila(N);
		Udaltzaina u = new Udaltzaina(N, pant);
		Bidea bide[] = new Bidea[N];
		Kotxea k[] = new Kotxea[N];
		for (int i = 0; i < N; ++i)
			bide[i] = new Bidea(i,u,pant);

		for (int i = 0; i < N; ++i) {
			k[i] = new Kotxea(i,  bide[i],i, u, bide,pant);
			k[i].start();
		}
	}
}

class Udaltzaina extends Thread {
	Pantaila pant;
	int[] bideak = new int[RotondaApp.N];
	int kotxeKop;
	int id;

	public Udaltzaina(int pid, Pantaila pp) {
		id = pid;
		kotxeKop = 0;
		pant = pp;
	}
//(when (i<N-1) gelditu->UDALTZAINA[i+1])
	public synchronized void gelditu(int zenb) throws InterruptedException {
		while (!(this.kotxeKop < (RotondaApp.N - 1)))
			wait();
		kotxeKop++;
		bideak[zenb] = 1;
		pant.idatziGurutzea(bideak, kotxeKop);
		notify();
	}
//(when (i>0) atera->UDALTZAINA[i-1])
	public synchronized void jarraitu(int zenb) throws InterruptedException {
		while (!(this.kotxeKop > 0))
			wait();
		kotxeKop--;
		bideak[zenb] = 0;
		pant.idatziGurutzea(bideak, kotxeKop);
		notify();
	}
}

class Kotxea extends Thread {
	Udaltzaina udal;
	Pantaila pant;
	String space;
	int zenb;
	int unekoId;
	int helmId;
	Bidea[] Rotonda;
	Bidea helmBidea;
	Bidea unekoBidea;

	public Kotxea(int pzenb, Bidea uneko, int punekoId,Udaltzaina m, Bidea[] proto,Pantaila pp) {
		udal = m;
		Rotonda = proto;
		zenb = pzenb;
		for (int i = 1; i <= zenb + 1; i++) {
			space += "\t\t";
		}
		pant = pp;
		unekoId=punekoId;
		unekoBidea = uneko;
	}
	
	private Bidea auzasBidea() {
		int auk=this.unekoId;
		while(auk==this.unekoId) {
			auk = getRandomNumberInRange(0, RotondaApp.N-1);
		}
		this.helmId=auk;
		return this.Rotonda[auk];
	}

	private static int getRandomNumberInRange(int min, int max) {

		Random r = new Random();
		return r.ints(min, (max + 1)).findFirst().getAsInt();

	}

	public void itxaron() throws InterruptedException {
		sleep(getRandomNumberInRange(1, 10) * 100);
	}
	public void run() {
		try {
			while (true) {
				itxaron();
				udal.gelditu(this.zenb);
				pant.idatzi(zenb, "gelditu");
				itxaron();
				unekoBidea.okupatu();
				pant.idatzi(zenb, "uneko okup "+this.unekoId);
				itxaron();
				this.helmBidea = auzasBidea();
				itxaron();
				pant.idatzi(zenb, "auk helm "+this.helmId);
				helmBidea.okupatu();
				itxaron();
				pant.idatzi(zenb, "helm okup"+this.helmId);
				itxaron();
				pant.idatzi(zenb, "martxan " + zenb + "->" + this.helmId );
				itxaron();
				unekoBidea.libratu();
				pant.idatzi(zenb, "uneko libre");
				itxaron();
				helmBidea.libratu();
				pant.idatzi(zenb, "helm libre"+this.helmId);
				itxaron();
				udal.jarraitu(zenb);
				pant.idatzi(zenb, "atera");
				itxaron();

			}
		} catch (InterruptedException e) {
		}
	}
}

class Bidea {
	public boolean okup = false;
	private int zenbakia;
	private Pantaila pant;
	private Udaltzaina udal;

	Bidea(int zenb,Udaltzaina pudal, Pantaila p) {
		zenbakia = zenb;
		pant = p;
		udal=pudal;
	}

	// when (h==1) libratu->BIDEA[0]
	public synchronized void libratu() {
		okup = false;
		pant.idatzi(zenbakia + " libre ");
		notify();
	}

	// when (h==0) okupatu->BIDEA[1]
	public synchronized void okupatu() throws java.lang.InterruptedException {
		while (okup)
			wait();
		okup = true;
		pant.idatzi(zenbakia + " okup  ");
		notify();
	}
}

class Pantaila {
	int kop;

	public Pantaila(int k) {
		kop = k;
		System.out.print("Bideak\t|\t");
		for (int i = 0; i < kop; ++i)
			System.out.print("K[" + i + "]\t\t");
		System.out.println("Udaltzaina");
		this.hasieratuGurutzea();
		System.out.print("========|========");
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

	public synchronized void idatziGurutzea(int[] eser, int kop) {
		String gurutzea = "[";
		for (int i = 0; i < RotondaApp.N-1; i++) {
			if (eser[i] == 1) {
				gurutzea += "*,";
			} else {
				gurutzea += "_,";
			}
		}
		if (eser[RotondaApp.N-1] == 1) {
			gurutzea += "*";
		} else {
			gurutzea += "_";
		}
		gurutzea += "]  Kop=" + kop;
		this.idatzi(RotondaApp.N, gurutzea);
	}

	public synchronized void hasieratuGurutzea() {
		String gurutzea ="[0";
		for (int i = 1; i < RotondaApp.N; i++) {
			gurutzea += ","+i;
		}
		gurutzea += "]";
		this.idatzi(RotondaApp.N, gurutzea);
	}

}
