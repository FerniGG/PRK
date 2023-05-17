import java.awt.Component;
import java.awt.Image;
import java.time.zone.ZoneOffsetTransitionRule;
import java.util.Random;

import javax.swing.ImageIcon;

public class Pertsona extends Thread {
	public int id;
	public String mota;
	public int vipNahi = -1;
	public int ilarapos;
	public int argazkiaNahi = -1;
	public int x, y;
	public boolean heldux = false;
	public boolean helduy = false;
	public String direc = "right";
	public String action="look";
	public Framea framea;
	public Image sprite;
	public Mendi_Koord mendi_kord;
	public Argazki_Koord arg_koord;
	public Mendia mendia;
	public Argazki_Denda denda;

	public Pertsona(int pid, Mendi_Koord mk, Mendia pmendia, Argazki_Koord parg_koord,Argazki_Denda pdenda) {
		id = pid;
		mota = "KOORD";
		ImageIcon i = new ImageIcon(this.getClass().getResource("Sprites/" + mota + "/boy_look_down.png"));
		sprite = i.getImage();
		this.x = 25;
		this.y = this.getRandomNumberInRange(100, 500);
		mendi_kord = mk;
		arg_koord = parg_koord;
		mendia = pmendia;
		denda = pdenda;

	}

	public void setFramea(Framea pf) {
		this.framea = pf;
	}

	private static int getRandomNumberInRange(int min, int max) {

		Random r = new Random();
		return r.ints(min, (max + 1)).findFirst().getAsInt();

	}

	private static int getRandomNumberInRange01() {

		Random r = new Random();
		return r.ints(0, (1 + 1)).findFirst().getAsInt();

	}

	public Image getImage() {
		ImageIcon i = new ImageIcon(this.getClass().getResource("Sprites/" + mota + "/boy_"+action+"_" + direc + ".png"));
		sprite = i.getImage();
		
		return this.sprite;
	}

	public void itxaron() throws InterruptedException {
		sleep((long) (Math.random() * 1000));
	}

	public void itxaron_txiki() throws InterruptedException {
		sleep((long) (Math.random() * 10));
	}

	public void run() {
		try {
			int xh = 0;
			int yh = 0;
			while (true) {
				itxaron();
				if (vipNahi == -1) {
					this.vipNahi = getRandomNumberInRange01();
				}
				if (vipNahi == 1) {
					xh = 300;
					yh = 230;
					this.mugituTo(xh, yh);
					
					ilarapos = mendia.vip_sartu(this);
					mota="VIP";
					xh = mendia.x_v - 25 * mendia.kv;
					yh = mendia.y_v;
					this.mugituTo(xh, yh);
					this.direc="right";
					itxaron();
					mendia.vip_atera(ilarapos);
					xh = mendia.x_m + 64;
					yh = mendia.y_m + 64;
					this.mugituTo(xh, yh);
					mendia.igo_Atrakziora();
					mendia.ilara_aurreratu(mendia.VipIL);
					itxaron();
					mendia.vip_desblok();
					itxaron();
					mendia.jaitsiAtrakziotik(this);
				} else {
					xh = 300;
					yh = 230;
					this.mugituTo(xh, yh);
					ilarapos = mendia.normal_sartu(this);
					mota="NORMAL";
					xh = mendia.x_n - 25 * mendia.kn;
					yh = mendia.y_n;
					this.mugituTo(xh, yh);
					this.direc="right";
					itxaron();
					mendia.normal_atera(ilarapos);
					xh = mendia.x_m + 64;
					yh = mendia.y_m + 64;
					this.mugituTo(xh, yh);
					mendia.igo_Atrakziora();
					mendia.ilara_aurreratu(mendia.NormalIL);
					itxaron();
					mendia.normal_desblok();
					itxaron();
					mendia.jaitsiAtrakziotik(this);
				}
				this.argazkiaNahi = getRandomNumberInRange01();
				if (argazkiaNahi == 1) {
					denda.dendan_sartu(this);
					xh = arg_koord.x - denda.k*25;
					yh = arg_koord.y-100 ;
					this.mugituTo(xh, yh);
					this.direc="down";
					denda.ilara_aurreratu(denda.Denda);
					itxaron();
					denda.erakusmahaiara_hurbildu(this);
					xh = arg_koord.x;
					yh = arg_koord.y-50;
					this.mugituTo(xh, yh);

					this.direc="down";
					itxaron();
					denda.argazkiaEskatu();
					itxaron();
					denda.argazkiaHartu(this);
					itxaron();
					this.action="look";
					denda.denda_Atera(this);
				}
				
				xh = 25;
				yh = this.getRandomNumberInRange(100, 500);
				this.mugituTo(xh, yh);
				mota = "KOORD";
			}
		} catch (InterruptedException e) {
		}
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

}