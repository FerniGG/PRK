import java.awt.Image;
import java.util.Random;
import javax.swing.ImageIcon;

public class Mendia {
	int tam_Mendia, tam_IlaraV,tam_IlaraN;
	int eserVIPkop;
	int kv, sv, av, kn, sn, an, km,kh, kmv,e = 0;
	boolean ablokv, ablokn = false;
	Pertsona[] VipIL;
	Pertsona[] NormalIL;
	String state = "open";
	int unekoimg = 0;
	String imagePath;
	Image sprite,mapa;
	public int x_m, x_v, x_n;
	public int y_m, y_v, y_n;


	public Mendia(int ptam_men,int ptam_ilaV, int ptam_IlaN, int px_m, int py_m, int px_v, int py_v, int px_n, int py_n) {
		tam_Mendia = ptam_men;
		tam_IlaraV = ptam_ilaV;
		tam_IlaraN = ptam_IlaN;
		this.VipIL = new Pertsona[tam_IlaraV];
		this.NormalIL = new Pertsona[tam_IlaraN];
		this.eserVIPkop = tam_Mendia * 60 / 100;
		ImageIcon i = new ImageIcon(this.getClass().getResource("Sprites/TIOVIVO/Tiovivo"+unekoimg+"_128px.png"));
		sprite = i.getImage();
		x_m = px_m;
		y_m = py_m;
		x_v = px_v;
		y_v = py_v;
		x_n = px_n;
		y_n = py_n;

	}

	public Image getImage() {
		ImageIcon i = new ImageIcon(this.getClass().getResource("Sprites/TIOVIVO/Tiovivo"+unekoimg+"_128px.png"));
		sprite = i.getImage();
		return this.sprite;
	}
	public Image getMapa() {
		ImageIcon i = new ImageIcon(this.getClass().getResource("Mapa2_" + state + ".png"));
		mapa = i.getImage();
		return this.mapa;
	}

	public synchronized int vip_sartu(Pertsona perts) throws InterruptedException {
		while (!(kv < tam_IlaraV && e==0))
			wait();
		int zenb = sv;
		
		kv++;
		this.VipIL[sv] = perts;
		sv = (sv + 1) % tam_IlaraV;
		notifyAll();
		System.out.println("sartu vip");
		return zenb;
	}

	public synchronized int normal_sartu(Pertsona perts) throws InterruptedException {
		while (!(kn < tam_IlaraN && e==0))
			wait();
		int zenb = sn;
		kn++;
		this.NormalIL[sn] = perts;
		sn = (sn + 1) % tam_IlaraN;
		notifyAll();
		System.out.println("sartu Norm");
		return zenb;
	}

	public synchronized void vip_atera(int pos) throws InterruptedException {
		while (!(pos == av && ((kmv<this.eserVIPkop && this.km+this.kh<this.tam_Mendia)|| ( kn==0 && km+this.kh<this.tam_Mendia  )) && ablokv==false && e==0))
			wait();
		kv--;
		this.ablokv=true;
		VipIL[pos]=null;
		av = (av + 1) % tam_IlaraV;
		this.kh++;
		this.kmv++;
		System.out.println("\tigo vip");
		notifyAll();
	}

	public synchronized void normal_atera(int pos) throws InterruptedException {
		while (!(pos == an &&((this.kv==0 && km+this.kh<this.tam_Mendia) ||(this.kmv>=this.eserVIPkop && this.km+this.kh<this.tam_Mendia)) && ablokn==false && e==0) )
			wait();
		kn--;
		this.ablokn=true;
		NormalIL[pos]=null;
		an = (an + 1) % tam_IlaraN;
		this.kh++;
		
		System.out.println("\tigo norm");
		notifyAll();
	}
	public synchronized void igo_Atrakziora() throws InterruptedException {
		while (!(kh>0))
			wait();
		kh--;
		km++;
		notifyAll();
	}
	public synchronized void vip_desblok() throws InterruptedException {
		while (!(ablokv == true))
			wait();
		ablokv = false;
		notifyAll();
	}

	public synchronized void normal_desblok() throws InterruptedException {
		while (!(ablokn == true))
			wait();
		ablokn = false;
		notifyAll();
	}

	public synchronized void atea_itxi() throws InterruptedException {
		while (!(km == this.tam_Mendia && kh==0 && e == 0))
			wait();
		e = 1;
		this.state="close";

		System.out.println("\t\tatea_itxi "+km);
		notifyAll();
	}
	public synchronized void gelditu() throws InterruptedException {
		while (!(e == 1))
			wait();
		e = 2;
		System.out.println("gelditu");
		this.unekoimg=0;
		notifyAll();
	}
	public synchronized void jaitsiAtrakziotik(Pertsona per) throws InterruptedException {
		while (!( km>0 && e == 2))
			wait();
		km=km-1;
		System.out.println("jaitsi "+this.km);
		notifyAll();
	}
	public synchronized void ateakIreki() throws InterruptedException {
		while (!(km == 0 && e==2))
			wait();
		e = 0;
		this.kmv=0;
		this.state="open";
		System.out.println("atea Ireki");
		notifyAll();
	}
	
	public synchronized void ilara_aurreratu(Pertsona[] ilara) throws InterruptedException {
		for (int i = 0; i < ilara.length; i++) {
			if (ilara[i] != null) {
				ilara[i].x+=25;
			}
		}
	}

	public void martxanJarri() {
		unekoimg=(unekoimg+1)%2;
		
	}

}
