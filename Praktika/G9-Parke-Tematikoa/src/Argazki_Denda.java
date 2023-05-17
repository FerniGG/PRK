
public class Argazki_Denda {
	int DendaTam;
	Pertsona[] Denda;
	int  k;
	boolean okup,g, e, i;

	public Argazki_Denda(int pn) {
		this.DendaTam = pn;
		this.Denda = new Pertsona[pn];
		for (int i = 0; i < this.DendaTam; i++) {
			this.Denda[i] = null;
		}
	}
	public synchronized void dendan_sartu(Pertsona per) throws InterruptedException {
		while (!(k<ParkeTematikoa.PertsonKop))
			wait();
		k++;
		boolean found = false;
		for (int i = 0; i < this.DendaTam; i++) {
			if (!found) {
				if (this.Denda[i] == null) {
					this.Denda[i] = per;
					found = true;
				}
			}
		}
		System.out.println("\t\t\terakusmahira hurbildu");
		notifyAll();

	}
	
		
//	when(k<N) 			ik[PR].sartu->GELA[k+1][g][e]
	public synchronized void erakusmahaiara_hurbildu(Pertsona per) throws InterruptedException {
		while (!(this.okup ==false))
			wait();
		okup=true;
		k--;
		boolean found = false;
		for (int i = 0; i < this.DendaTam; i++) {
			if (!found) {
				if (this.Denda[i] == null) {
					this.Denda[i] = per;
					found = true;
				}
			}
		}
		System.out.println("\t\t\terakusmahira hurbildu");
		notify();

	}

	// |ik[PR].atera->GELA[k-1][0][0]
	public synchronized void denda_Atera(Pertsona per) throws InterruptedException {
		while (!(this.okup == true))
			wait();
		okup=false;
		System.out.println("\t\t\tDendatik atera");
		
		notify();

	}

//	|when(g==1 && e==0)	ir.pentsatu->ir.erantzun->GELA[k][g][1]
	public synchronized void argazkiaInprimatu() throws InterruptedException {
		while (!(this.g == true && this.e == false && this.i == false))
			wait();
		System.out.println("\t\t\t\targazkia imp");
		this.i = true;
		notifyAll();

	}// |when(g==1 && e==0) ir.pentsatu->ir.erantzun->GELA[k][g][1]

	public synchronized void argazkiaEman(Argazki_Koord arg) throws InterruptedException {
		while (!(this.g == true && this.e == false && this.i == true))
			wait();
		this.e = true;
		this.i = false;
		System.out.println("\t\t\t\targazkia eman");
		notifyAll();

	}

	// |when(g==0) ik[PR].galdetu->GELA[k][1][e]
	public synchronized void argazkiaEskatu() throws InterruptedException {
		while (!(this.g == false))
			wait();
		this.g = true;
		System.out.println("\t\t\targazkia eskatu");
		notifyAll();

	}

//							|when(e==1 && g==1) ik[PR].erantzunaJaso->GELA[k][0][0]
	public synchronized void argazkiaHartu(Pertsona per) throws InterruptedException {
		while (!(this.g == true && this.e == true))
			wait();
		this.g = false;
		this.e = false;
		per.action="pick";
		System.out.println("\t\t\targazkia hartu");
		notifyAll();

	}
	public synchronized void ilara_aurreratu(Pertsona[] ilara) throws InterruptedException {
		for (int i = 0; i < ilara.length; i++) {
			if (ilara[i] != null) {
				ilara[i].x+=25;
			}
		}
	}

}
