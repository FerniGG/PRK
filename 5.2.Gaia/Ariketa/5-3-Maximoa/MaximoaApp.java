//////////////////////////////////////////////////
///   Egilea: Fernando Gonzalez                ///
///   Data : 26/03/2023                        ///
//////////////////////////////////////////////////

//   Gai Zenb : 5.2
//   Ariketa Zenb : 3                          
//   Ariketa Izena : Maximoa  
//   Enuntziatua : Array bateko zenbakien artean maximoa aurkitu agendaren eredua erabiliz.
//	 				FSP eredua eman eta Javaz inplementatu, 

import java.util.Random;

public class MaximoaApp {
	public final static int ProzKop = 3;
	public final static int N = 10;

	public static void main(String[] args) {
		Pantaila p = new Pantaila();
		for (int i = 0; i < ProzKop; i++) {
			p.idatzi("P[" + (i+1) + "]\t\t\t");
		}
		p.idatzi("Agenda\n");
		p.idatzi("=======================================================\n");
		Agenda ag = new Agenda(N, p);
		Prozesua idList[] = new Prozesua[ProzKop];

		String space = "";
		for (int i = 0; i < ProzKop; i++) {
			idList[i] = new Prozesua(i+1, ag, p, space);
			space += "\t\t\t";
		}
		ag.setSpace(space);
		for (int i = 0; i < ProzKop; i++) {
			idList[i].start();
		}

		

	}
}

class Agenda {
	int i;
	int j=0;
	int tam;
	int[] agenda;
	Pantaila p;
	String space;
	//AGENDA[uneko i-ren posizioa][Zenbat prozesuk hartu duten zenbakiak konparatzeko]
	public Agenda(int ptam, Pantaila pp) {
		p = pp;
		tam=ptam;
		i=tam-1;
		this.agenda = new int[tam];
		for (int i = 0; i < this.tam-1; i++) {
			int zenb=this.zenbakiaAuzas();
			this.agenda[i] = zenb;
			
		}
		int zenb=this.zenbakiaAuzas();
		this.agenda[this.tam-1] = zenb;
		//p.idatzi(this.toString(0));

	}
	public int zenbakiaAuzas() {
		Random rand = new Random();
		return rand.nextInt((100 - 1) + 1) + 1;
	}

	public void setSpace(String spce) {
		this.space = spce;
		p.idatzi(this.toString(spce));
	}

	//pr[PR].sartu -> [i+1] -> if(i==1 && j==1)then (max->STOP  )
		//else AGENDA[i+1][j-1]
	public synchronized int sartu(int pid, String spaceP,int zenb) throws InterruptedException {
		while (!(i < tam))
			wait();
		int signal=0;
		agenda[i+1]=zenb;
		p.idatzi(pid,"sartu["+zenb+"]\n" );
		p.idatzi(this.toString(this.space));
		if (i==-1  && j==1) {
			signal=zenb;
		}
		else{
			i=i+1;
			j=j-1;
			}
		notify();
		return signal;//STOP
	}

//	when(i>0) pr[PR].hartu
	public synchronized int[] hartu(int pid, String spaceP) throws InterruptedException {
		int[] zenb= new int[2];
		while (!(i>0))
			wait();
		zenb[0]=agenda[i];
		zenb[1]=agenda[i-1];
		this.agenda[i]=0;
		this.agenda[i-1]=0;
		p.idatzi(pid, "hartu["+zenb[0]+"]["+zenb[1]+"]\n" );
		p.idatzi(this.toString(this.space));
		j=j+1;
		i=i-2;
		notify();
		return zenb;
	}

	public synchronized String toString(String space) {
		/*for (int i = id; i < MaximoaApp.ProzKop; i++) {
			p.idatzi("\t\t\t");
		}*/
		p.idatzi(space);
		String agendaStr="[";
		for (int i = 0; i < this.tam-1; i++) {
			if (this.agenda[i]!=0) {
				agendaStr+=this.agenda[i]+"|";
			}else {
				agendaStr+=" |";
			}
			
		}
		if (this.agenda[this.tam-1]!=0) {
			agendaStr+=this.agenda[this.tam-1]+"";
		}else {
			agendaStr+=" ";
		}
		agendaStr+="]\n";
		return agendaStr;
	}
}

class Prozesua extends Thread {
	int id;
	int[] zenb = new int[2];
	String Space;
	Agenda agen;
	Pantaila p;

	Prozesua(int pid, Agenda a, Pantaila pp, String pspace) {
		agen = a;
		p = pp;
		id = pid;
		Space = pspace;
	}

	public int zenbakiaAuzas() {
		Random rand = new Random();
		return rand.nextInt((10 - 1) + 1) + 1;
	}
	public synchronized int konparatu() throws InterruptedException {
		if(zenb[0]>=zenb[1])
			return zenb[0];
		else
			return zenb[1];
	}


	public void run() {
		try {
			int signal=0;
			while (signal==0) {
				zenb=agen.hartu(id, Space);
				this.itxaron();
				int max=this.konparatu();
				this.itxaron();
				signal=agen.sartu(id, Space, max);
				this.itxaron();
			}
			p.idatzi("\n* * MAXIMOA: "+signal+" * *\n");
		} catch (InterruptedException e) {
		}

	}

	public void itxaron() throws InterruptedException {
		sleep(zenbakiaAuzas() * 100);
	}
}

class Pantaila {

	public Pantaila() {

	}

	synchronized public void idatzi(String s) {
		System.out.print(s);
	}
	synchronized public void idatzi(int id,String s) {
		for (int i = id; i < MaximoaApp.ProzKop; i++) {
			System.out.print("\t\t\t");
		}
		System.out.print(s);
	}
}

