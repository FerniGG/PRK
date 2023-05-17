package A4.AsmaBlok;

import java.util.Random;


//////////////////////////////////////////////////
///   Egilea: Fernando Gonzalez                ///
///   Data : 2/04/2023                        ///
//////////////////////////////////////////////////

//Gai Zenb : 6
//Ariketa Zenb : 4                        
//Ariketa Izena : AsmatutakoProblemaBlokeoaGabe
//Enuntziatua : Bidegurutze bat daukagun non kotxea gurutzera heltzen da.
//				Bidegurutzera sartzerakoanan helmuga bidea aukeratuko du(unekoa ezin da izan).
//				Bide hori libre badago martzan jarriko da. eta Uneko bidea libratuko du.
// 				Bidegurutzera heltzerakoan helmuga bidea aukeratuko du berriro.

public class A4RotondaApp {
	public final static int N = 4;

	public static void main(String args[]) {
		Pantaila pant = new Pantaila(N);
		Bidea bide[] = new Bidea[N];
		Kotxea k[] = new Kotxea[N];
		for (int i = 0; i < N; ++i)
			bide[i] = new Bidea(i,pant);

		for (int i = 0; i < N; ++i) {
			k[i] = new Kotxea(i,  bide[i],i, bide,pant);
			k[i].start();
		}
	}
}

/*KOTXEA = (gelditu->unekoa.okupatu ->helmuga.okupatu->MARTXAN),
MARTXAN = (martxan->unekoa.libratu->helmuga.libratu->atera->KOTXEA).*/
class Kotxea extends Thread {
	Pantaila pant;
	String space;
	int zenb;
	int unekoId;
	int helmId;
	Bidea[] Rotonda;
	Bidea helmBidea;
	Bidea unekoBidea;

	public Kotxea(int pzenb, Bidea uneko, int punekoId, Bidea[] proto,Pantaila pp) {
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
			auk = getRandomNumberInRange(0, A4RotondaApp.N-1);
				
		}
		this.helmId=auk;
		return this.Rotonda[auk];
	}
	private static int getRandomNumberInRange(int min, int max) {

		Random r = new Random();
		return r.ints(min, (max + 1)).findFirst().getAsInt();

	}

	public void itxaron() throws InterruptedException {
		sleep((long) (Math.random()* 5000));
	}
	public void run() {
		try {
			while (true) {
				itxaron();
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

	Bidea(int zenb,Pantaila p) {
		zenbakia = zenb;
		pant = p;
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
		System.out.println("");
		for (int i = 0; i < kop; ++i)
			System.out.print("================");
		System.out.println("================");
		
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
		for (int i = 0; i < A4RotondaApp.N-1; i++) {
			if (eser[i] == 1) {
				gurutzea += "*,";
			} else {
				gurutzea += "_,";
			}
		}
		if (eser[A4RotondaApp.N-1] == 1) {
			gurutzea += "*";
		} else {
			gurutzea += "_";
		}
		gurutzea += "]  Kop=" + kop;
		this.idatzi(A4RotondaApp.N, gurutzea);
	}

	public synchronized void hasieratuGurutzea() {
		String gurutzea ="[0";
		for (int i = 1; i < A4RotondaApp.N; i++) {
			gurutzea += ","+i;
		}
		gurutzea += "]";
		this.idatzi(A4RotondaApp.N, gurutzea);
	}

}

