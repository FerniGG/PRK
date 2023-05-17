package A_7_2_d;

//////////////////////////////////////////////////
///   Egilea: Fernando Gonzalez                ///
///   Data : 27/04/2023                        ///
//////////////////////////////////////////////////

//Gai Zenb : 7
//Arkieta Zenb : 2-c                         
//Arkieta Izena : Zebrabidea
//Enuntziatua : Zebrabide batetara iristean:
//- Kotxeak zain geldituko dira oinezkoren bat pasatzen edo pasatzeko zain baldin badago.
//- Oinezkoek, ordea, zain geldituko dira une horretan kotxe bat pasatzen ari bada, harrapatuak ez
//izateko. Gainera ataskorki sor ez dadin, hiru kotxe baino gehiago zebrabidean sartzeko pilatzen
//badira, oinezkoek ere itxarongo dute.
//Modelatu FSPz, aurreko atalean sortzen den elkar-blokeaketa soluzionatuz baina ahalik eta
//gutxien aldatuz. Check Safety erabiliz, ziurtatu konpondu dela arazoa.

//AZALPENA: Aldaketa hauekin bizitasun propietatea ekidituko dugu Lifo ariketan bezala kotxeak sartzean kontagailu bat inkrementatuko da eta
//nire kasuan 3-ra heltzean oinezko guztiak sartuko dira bidean eta guztiak sartu arte kontagailu hori 0 ra hasieratuko dugu.
//honela oinezkoak eta kotxeak txandakatuko dira.

public class BidegurutzeaApp {
	public static final int ataskoak_kont = 3;
	public static final int bizitasuna_kont = 3;
	public static final int kotxeKop = 4;
	public static final int oinezkoKop = 3;

	public static void main(String[] args) {
		Pantaila p = new Pantaila();
		Bidegurutzea b = new Bidegurutzea(kotxeKop + oinezkoKop + 1, p);
		Oinezkoa oList[] = new Oinezkoa[oinezkoKop];
		Kotxea kList[] = new Kotxea[kotxeKop];

		String space = "";
		for (int i = 0; i < kotxeKop; i++) {
			kList[i] = new Kotxea(i, b, p, space);
			space += "\t";
		}
		b.setSpace(space);
		space += "|\t\t\t\t\t\t";
		for (int i = 0; i < oinezkoKop; i++) {
			oList[i] = new Oinezkoa((i+BidegurutzeaApp.kotxeKop), b, p, space);
			space += "\t";
		}
		for (int i = 0; i < oinezkoKop; i++) {
			 oList[i].start();
		}
		for (int i = 0; i < kotxeKop; i++) {
			kList[i].start();
		}
	}

}

/*
 * BIDEGURUTZEA=BIDEGURUTZEA[0][0][0][0][0], //ki= zenbat kotxe iritsi diren
 * //ks= zenbat kotxe sartu diren //oi= zenbat oinezko iritsi diren //os= zenbat
 * kotxe sartu diren //it: Sartutako kotxe totalen kontagailua darama
 * BIDEGURUTZEA[ki:KR][ks:KR][oi:OR][os:OR][it:BR]
 */
class Bidegurutzea {
	int id;
	int ki, ks, oi, os, it = 0;
	int[] k = new int[BidegurutzeaApp.kotxeKop];
	int[] o = new int[BidegurutzeaApp.oinezkoKop];
	int[] b = new int[BidegurutzeaApp.kotxeKop + BidegurutzeaApp.oinezkoKop];
	Pantaila p;
	private String space;

	public Bidegurutzea(int pid, Pantaila pp) {
		id = pid;
		p = pp;
		for (int i = 0; i < this.k.length; i++) {
			this.k[i] = 0;
		}
		for (int i = 0; i < this.o.length; i++) {
			this.o[i] = 0;
		}
		for (int i = 0; i < this.b.length; i++) {
			this.b[i] = 0;
		}

	}

	public void setSpace(String ps) {
		this.space = ps;
		p.idatzi(this.toString(0,"","O"));
	}

	// k[KR1].iritzi->BIDEGURUTZEA[ki+1][ks][oi][os][it]
	public synchronized void k_iritzi(int pid, String pspace) throws InterruptedException {
		while (!(ki < this.k.length))
			wait();
		this.ki++;
		boolean found = false;
		for (int i = 0; i < this.k.length; i++) {
			if (!found) {
				if (this.k[i] == 0) {
					this.k[i] = pid;
					found = true;
				}
			}
		}
		p.idatzi(this.toString(pid,"iritzi","K"));
		notifyAll();

	}

	// o[OR1].iritzi->BIDEGURUTZEA[ki][ks][oi+1][os][it]
	public synchronized void o_iritzi(int pid, String pspace) throws InterruptedException {
		while (!(oi < this.o.length))
			wait();
		this.oi++;
		boolean found = false;
		for (int i = 0; i < this.o.length; i++) {
			if (!found) {
				if (this.o[i] == 0) {
					this.o[i] = pid;
					found = true;
				}
			}
		}
		p.idatzi(this.toString(pid,"iritzi","O"));
		notifyAll();
	}

	// k[KR1].atera->BIDEGURUTZEA[ki][ks-1][oi][os][it]
	public synchronized void k_atera(int pid, String pspace) throws InterruptedException {
		while (!(ks > 0))
			wait();
		this.ks--;
		boolean found = false;
		for (int i = 0; i < this.b.length; i++) {
			if (!found) {
				if (this.b[i] == pid) {
					this.b[i] = 0;
					found = true;
				}
			}
		}
		p.idatzi(this.toString(pid,"atera","K"));
		notifyAll();

	}

	// o[OR1].atera->BIDEGURUTZEA[ki][ks][oi][os-1][it]
	public synchronized void o_atera(int pid, String pspace) throws InterruptedException {
		while (!(os > 0))
			wait();
		this.os--;
		boolean found = false;
		for (int i = 0; i < this.b.length; i++) {
			if (!found) {
				if (this.b[i] == pid) {
					this.b[i] = 0;
					found = true;
				}
			}
		}
		p.idatzi(this.toString(pid,"atera","O"));
		notifyAll();

	}

	/*
	 * when (((oi==0 || ki>=AM) && it<AB) && os==0)
	 * k[KR1].sartu->BIDEGURUTZEA[ki-1][ks+1][oi][os][it+1]
	 */
	public synchronized void k_sartu(int pid, String pspace) throws InterruptedException {
		while (!(((oi == 0 || ki >= BidegurutzeaApp.ataskoak_kont) && it < BidegurutzeaApp.bizitasuna_kont) && os == 0))
			wait();
		this.ki--;
		this.ks++;
		this.it++;
		boolean found = false;
		for (int i = 0; i < this.k.length; i++) {
			if (!found) {
				if (this.k[i] == pid) {
					this.k[i] = 0;
					found = true;
				}
			}
		}
		found = false;
		for (int i = 0; i < this.b.length; i++) {
			if (!found) {
				if (this.b[i] == 0) {
					this.b[i] = pid;
					found = true;
				}
			}
		}
		p.idatzi(this.toString(pid,"sartu","K"));
		notifyAll();

	}

	/*
	 * |when (ks==0 && (ki<AM || it>=AB)) o[OR1].sartu->if (oi-1 == 0) then
	 * BIDEGURUTZEA[ki][ks][oi-1][os+1][0] else BIDEGURUTZEA[ki][ks][oi-1][os+1][it]
	 */
	public synchronized void o_sartu(int pid, String pspace) throws InterruptedException {
		while (!(ks == 0 && (ki < BidegurutzeaApp.ataskoak_kont || it >= BidegurutzeaApp.bizitasuna_kont)))
			wait();
		this.oi--;
		if (oi == 0) {
			this.it = 0;
		}this.os++;
		boolean found = false;
		for (int i = 0; i < this.o.length; i++) {
			if (!found) {
				if (this.o[i] == pid) {
					this.o[i] = 0;
					found = true;
				}
			}
		}
		found = false;
		for (int i = 0; i < this.b.length; i++) {
			if (!found) {
				if (this.b[i] == 0) {
					this.b[i] = pid;
					found = true;
				}
			}
		}
		p.idatzi(this.toString(pid,"sartu","O"));
		notifyAll();

	}

	public String toString(int pid,String action,String zer) {
		String mezua="";	
		if(zer=="O") {
			mezua+=this.space;
		}else {
			for (int i =0;i<BidegurutzeaApp.kotxeKop;i++) {
				if(i==pid) {
					mezua+=action;
				}
				mezua+="\t";
			}
		}
		
		mezua += "Kotxe[";
		for (int i = 0; i < this.k.length - 1; i++) {
			if (this.k[i] == 0) {
				mezua += "_|";
			} else {
				mezua += this.k[i] + "|";
			}
		}
		if (this.k[this.k.length - 1] == 0) {
			mezua += "_]  Kotx[";
		} else {
			mezua += this.k[this.k.length - 1] + "]  Bidea[";
		}

		for (int i = 0; i < this.b.length - 1; i++) {
			if (this.b[i] == 0) {
				mezua += "_|";
			} else {
				mezua += this.b[i] + "|";
			}
		}
		if (this.b[this.b.length - 1] == 0) {
			mezua += "_]  Oin[";
		} else {
			mezua += this.b[this.b.length - 1] + "]  Oin[";
		}

		for (int i = 0; i < this.o.length - 1; i++) {
			if (this.o[i] == 0) {
				mezua += "_|";
			} else {
				mezua += this.o[i] + "|";
			}
		}
		if (this.o[this.o.length - 1] == 0) {
			mezua += "_]";
		} else {
			mezua += this.o[this.o.length - 1] + "]";
		}
		if(zer=="O") {
			mezua+="\t";
			for (int i =BidegurutzeaApp.kotxeKop;i<BidegurutzeaApp.oinezkoKop+BidegurutzeaApp.kotxeKop;i++) {
				if(i==pid) {
					mezua+=action;
				}
				mezua+="\t";
			}
		}else {
			mezua+="\t\t\t\t";
		}
		return mezua +"Pasatako kotxe kop="+this.it+ "\n";
	}
}

//OINEZKOA = (iritzi->sartu->atera->OINEZKOA).
class Oinezkoa extends Thread {
	int id;
	Bidegurutzea bidea;
	Pantaila p;
	String space;

	public Oinezkoa(int pid, Bidegurutzea b, Pantaila pp, String pspace) {
		id = pid;
		bidea = b;
		p = pp;
		space = pspace;
	}

	private void itxaron() throws InterruptedException {
		sleep((long) (Math.random() * 10000));
	}

	public void run() {
		try {
			while (true) {
				itxaron();
				bidea.o_iritzi(id, space);
				itxaron();
				bidea.o_sartu(id, space);
				itxaron();
				bidea.o_atera(id, space);
				itxaron();
			}

		} catch (InterruptedException e) {
		}

	}
}

//KOTXEA = (iritzi->sartu->atera->KOTXEA).
class Kotxea extends Thread {
	int id;
	Bidegurutzea bidea;
	Pantaila p;
	String space;

	public Kotxea(int pid, Bidegurutzea b, Pantaila pp, String pspace) {
		id = pid;
		bidea = b;
		p = pp;
		space = pspace;
	}

	private void itxaron() throws InterruptedException {
		sleep((long) (Math.random() * 10000));
	}

	public void run() {
		try {
			while (true) {
				itxaron();
				bidea.k_iritzi(id, space);
				itxaron();
				bidea.k_sartu(id, space);
				itxaron();
				bidea.k_atera(id, space);
				itxaron();
			}

		} catch (InterruptedException e) {
		}
	}
}

class Pantaila {
	public Pantaila() {

		for (int i = 0; i < BidegurutzeaApp.kotxeKop; i++) {
			idatzi("Kotx_" + i + "\t");
		}
		String space = "\t\t\t\t\t";
		idatzi("Bidegurutzea" + space);

		for (int i = 0; i < BidegurutzeaApp.oinezkoKop; i++) {
			idatzi("Oin_" + (i+BidegurutzeaApp.kotxeKop) + "\t");
		}
		idatzi("\n=====================================================================================\n");
	}

	public synchronized void idatzi(String m) {
		System.out.print(m);
	}
}
