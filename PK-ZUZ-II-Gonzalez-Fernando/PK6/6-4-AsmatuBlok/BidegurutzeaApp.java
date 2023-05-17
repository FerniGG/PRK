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
	public final static int[] Rotonda= new int[N];

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
		this.helmId=(this.unekoId+1)%A4RotondaApp.N;
		this.helmBidea = Rotonda[this.helmId];
	}
	
	private Bidea auzasBidea() {
		int auk=this.unekoId;
		while(auk==this.unekoId) {
			auk = getRandomNumberInRange(0, A4RotondaApp.N-1);
			if(Rotonda[auk].okup==true) {
				auk=unekoId;
			}
				
		}
		this.helmId=auk;
		return this.Rotonda[auk];
	}
	private static int getRandomNumberInRange(int min, int max) {

		Random r = new Random();
		return r.ints(min, (max + 1)).findFirst().getAsInt();

	}

	public void itxaron() throws InterruptedException {
		sleep((long) (Math.random()* 10000));
	}
	public void run() {
		try {
			while (true) {
				itxaron();
				pant.idatzi(zenb, "gelditu\t");
				unekoBidea.okupatu(zenb,"uneko",this.unekoId);
				itxaron();
				helmBidea.okupatu(zenb, "helm", this.helmId);
				itxaron();
				pant.idatzi(zenb, "martxan -> " + this.unekoId+"'");
				itxaron();
				unekoBidea.libratu(zenb, "uneko", this.unekoId);
				itxaron();
				pant.idatzi(zenb, "martxan -> "+ this.helmId );
				itxaron();
				helmBidea.libratu(zenb, "helm", this.helmId);
				pant.idatzi(zenb, "atera\t");
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
	public synchronized void libratu(int zenb,String unekHelm,int bideId) {
		okup = false;
		//pant.idatzi(zenbakia + " libre ");
		A4RotondaApp.Rotonda[zenbakia]=0;
		pant.idatzi(zenb, unekHelm+" libre "+bideId);
		notify();
	}

	// when (h==0) okupatu->BIDEA[1]
	public synchronized void okupatu(int zenb,String unekHelm, int bideId) throws java.lang.InterruptedException {
		while (okup)
			wait();
		okup = true;
		A4RotondaApp.Rotonda[zenbakia]=1;
		pant.idatzi(zenb, unekHelm+" okup "+bideId);
		notify();
	}
}

class Pantaila {
	int kop;

	public Pantaila(int k) {
		kop = k;
		for (int i = 0; i < kop; ++i)
			System.out.print("K[" + i + "]\t\t");
		System.out.println("\tBideak");
		for (int i = 0; i < kop; ++i)
			System.out.print("================");
		System.out.println("================");
		
	}

	public synchronized void idatzi(int nor, String testua) {
		
		for (int i = 0; i <= A4RotondaApp.N; i++) {
			if (nor == i)
				System.out.print(testua);
			else
				System.out.print("\t\t");
		}
		System.out.print(this.idatziGurutzea()+"\t");
		System.out.println();
	}

	public synchronized String idatziGurutzea() {
		return this.Gurutzea(A4RotondaApp.Rotonda, kop);
	}

	public synchronized String Gurutzea(int[] eser, int kop) {
		String gurutzea = "[";
		for (int i = 0; i < A4RotondaApp.N-1; i++) {
			if (eser[i] == 1) {
				gurutzea += "*";
			} else {
				gurutzea += "_";
			}
		}
		if (eser[A4RotondaApp.N-1] == 1) {
			gurutzea += "*";
		} else {
			gurutzea += "_";
		}
		gurutzea += "]";
		return gurutzea;
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

