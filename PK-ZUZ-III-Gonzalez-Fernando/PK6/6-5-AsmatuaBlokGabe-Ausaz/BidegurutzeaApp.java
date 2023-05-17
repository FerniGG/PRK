package A5.AsmaBlokGabeAusaz;


import java.util.Random;


//////////////////////////////////////////////////
///   Egilea: Fernando Gonzalez                ///
///   Data : 2/04/2023                        ///
//////////////////////////////////////////////////

//Gai Zenb : 6
//Ariketa Zenb : 5                        
//Ariketa Izena : AsmatutakoProblemaBlokeoaGabe
//Enuntziatua : 
/*
AZALPENA: ez dakidanez zelan gorde parametro bezala ausaz aukeratutako bidea helmuga bezala
hemen alboko bidea esleituko diot helmuga bezala eta JAVAn bi .jar izango ditut alboko bidea izanda helmuga bezala eta  auzas aukeratutakoa.
Honela egingo dut FSP ondo dabilela ziurtatzeko.
*/

//Bidegurutze bat daukagun non kotxea gurutzera heltzen da eta bere bidea okupatuko du.
//uneko bidea okupatu ostean helmuga bidea aukeratuko du(unekoa ezin da izan) eta okupatuko du libre baldin badago.
//Bide hori okupatu eta gero martzan jarriko da. eta Uneko bidea eta helmuga libratuko du orden horretan, ondoren gurutzetik ateratzeko.
/*azalpena: ariketa honetan exekuzio puntu batera heltzerakoan, ezingo dute eragiketarik egin 
hari guztiak elkarblokeatuta dagoelako.
kasu honetan elkarblokeatuko dira kotxe guztiak haien bidean gelditzen badira edozein helmuga okupatu baino lehen.

No deadlocks/errors
*/
//||BIDEGURUTZEA=  ( forall [i:R] ( k[i]:KOTXEA|| {k[i].unekoa,k[(i+1)%N].helmuga}::BIDEA)).
public class A5RotondaAppAusaz {
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
		this.helmId=(this.unekoId+1)%A5RotondaAppAusaz.N;
		this.helmBidea = Rotonda[this.helmId];
	}
	
	private Bidea auzasBidea() {
		int auk=this.unekoId;
		while(auk==this.unekoId) {
			auk = getRandomNumberInRange(0, A5RotondaAppAusaz.N-1);
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
		sleep((long) (Math.random()* 5000));
	}
	public void run() {
		try {
			while (true) {
				itxaron();
				pant.idatzi(zenb, "gelditu\t");
				itxaron();
				unekoBidea.okupatu(zenb,"uneko",this.unekoId);
				itxaron();
				this.helmBidea=auzasBidea();
				pant.idatzi(zenb, "helm auk"+this.helmId+"\t");
				itxaron();
				unekoBidea.libratu(zenb, "uneko", this.unekoId);
				itxaron();
				pant.idatzi(zenb, "martxan -> " + this.unekoId+"'");
				itxaron();
				helmBidea.okupatu(zenb, "helm", this.helmId);
				itxaron();
				pant.idatzi(zenb, "martxan -> "+ this.helmId );
				itxaron();
				helmBidea.libratu(zenb, "helm", this.helmId);
				itxaron();
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
		A5RotondaAppAusaz.Rotonda[zenbakia]=0;
		pant.idatzi(zenb, unekHelm+" libre "+bideId);
		notify();
	}

	// when (h==0) okupatu->BIDEA[1]
	public synchronized void okupatu(int zenb,String unekHelm, int bideId) throws java.lang.InterruptedException {
		while (okup)
			wait();
		okup = true;
		A5RotondaAppAusaz.Rotonda[zenbakia]=1;
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
		
		for (int i = 0; i <= A5RotondaAppAusaz.N; i++) {
			if (nor == i)
				System.out.print(testua);
			else
				System.out.print("\t\t");
		}
		System.out.print(this.idatziGurutzea()+"\t");
		System.out.println();
	}

	public synchronized String idatziGurutzea() {
		return this.Gurutzea(A5RotondaAppAusaz.Rotonda, kop);
	}

	public synchronized String Gurutzea(int[] eser, int kop) {
		String gurutzea = "[";
		for (int i = 0; i < A5RotondaAppAusaz.N-1; i++) {
			if (eser[i] == 1) {
				gurutzea += "*";
			} else {
				gurutzea += "_";
			}
		}
		if (eser[A5RotondaAppAusaz.N-1] == 1) {
			gurutzea += "*";
		} else {
			gurutzea += "_";
		}
		gurutzea += "]";
		return gurutzea;
	}

	public synchronized void hasieratuGurutzea() {
		String gurutzea ="[0";
		for (int i = 1; i < A5RotondaAppAusaz.N; i++) {
			gurutzea += ","+i;
		}
		gurutzea += "]";
		this.idatzi(A5RotondaAppAusaz.N, gurutzea);
	}

}

