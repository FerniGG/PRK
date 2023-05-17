import java.awt.Image;
import java.util.Random;

import javax.swing.ImageIcon;

public class Argazki_Koord extends Thread{
	public String mota = "KOORD";
	public int x;
	public int y;
	public boolean heldux = false;
	public boolean helduy = false;
	public String action="look";
	public String direc="up";
	private Framea framea;
	private Image sprite;
	private Argazki_Denda denda;
	public Argazki_Koord(int px,int py,Argazki_Denda pden) {
		x=px;
		y=py;
		denda=pden;
	}

	public int getKoordX() {
		return this.x;
	}

	public int getKoordY() {
		return this.y;
	}

	public void setKoordX(int x) {
		this.x = x;
	}

	public void setKoordY(int y) {
		this.y = y;
	}

	public void setKoord(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Image getImage() {
		ImageIcon i = new ImageIcon(this.getClass().getResource("Sprites/" + mota + "/boy_"+action+"_"+direc+".png"));
		sprite = i.getImage();
		return this.sprite;
	}

	public void itxaron() throws InterruptedException {
		sleep((long) (Math.random() * 500));
	}
	public void itxaron_txiki() throws InterruptedException {
		sleep((long) (Math.random() * 100));
	}
	private static int getRandomNumberInRange01() {

		Random r = new Random();
		return r.ints(0, (1 + 1)).findFirst().getAsInt();

	}
	public void mugituTo(int hx, int hy) throws InterruptedException {
		this.heldux = false;
		this.helduy = false;
		while (this.heldux == false || this.helduy == false) {
			int aukera = getRandomNumberInRange01();
			if (aukera == 1) {
				heldux = mugituToX(hx);
				//helduy = mugituToY(hy);
			} else {
				helduy = mugituToY(hy);
				//heldux = mugituToX(hx);
			}
			itxaron_txiki();
		}
	}

	public boolean mugituToX(int hx) {
		int step = 1;
		if (this.x == hx) {
			return true;
		} else if (this.x < hx) {
			this.x += step;
			this.direc="right";
		} else {
			this.x -= step;
			this.direc="left";
		}
		return false;
	}

	public boolean mugituToY(int hy) {
		int step = 1;
		if (this.y == hy) {
			return true;
		} else if (this.y < hy) {
			this.y += step;
			this.direc="down";
		} else {
			this.y -= step;
			this.direc="up";
		}
		return false;
	}


	public void run() {
		try {

			while (true) {
				itxaron();
				denda.argazkiaInprimatu();
				this.mugituTo(x, y+10);
				this.action="pick";
				this.direc="down";
				this.mugituTo(x, y-10);
				this.direc="up";
				itxaron();
				denda.argazkiaEman(this);
				this.action="pick";
				itxaron();
				this.action="look";
			}

		} catch (InterruptedException e) {
		}
	}
}

