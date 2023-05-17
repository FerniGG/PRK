import java.awt.Image;
import java.util.Timer;

import javax.swing.ImageIcon;

public class Mendi_Koord extends Thread{
	public String mota = "KOORD";
	public int x;
	public int y;
	private Framea framea;
	private Image sprite;
	private Mendia mendia;
	public Mendi_Koord(int px,int py,Mendia m) {
		x=px;
		y=py;
		mendia=m;
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
		ImageIcon i = new ImageIcon(this.getClass().getResource("Sprites/" + mota + "/boy_look_down.png"));
		sprite = i.getImage();
		return this.sprite;
	}

	public void itxaron() throws InterruptedException {
		sleep((long) (Math.random() * 1000));
	}
	public void itxaron_Txiki() throws InterruptedException {
		sleep((long) (Math.random() * 100));
	}


	public void run() {
		try {

			while (true) {
				itxaron();
				mendia.atea_itxi();
				itxaron();
				long t= System.currentTimeMillis();
				long end = t+5000;
				while(System.currentTimeMillis() < end) {
				  mendia.martxanJarri();
				  itxaron();
				}
				mendia.gelditu();
				itxaron();
				mendia.ateakIreki();
			}

		} catch (InterruptedException e) {
		}
	}
}
