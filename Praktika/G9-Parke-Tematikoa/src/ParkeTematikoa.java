import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ParkeTematikoa {
	public static int PertsonKop =13;
	public static int ParkeEdukiaMax = 4;
	private static int DendariKop = 1;
	private static int MendiEser = 6;
	private static int VipEduki = 5;
	private static int NormalEduki = 7;

	public static Mendia mendia;
	public static Mendi_Koord mendi_koord;
	public static Argazki_Koord arg_koord;
	public static Argazki_Denda denda;
	private static Clip clip;
	public static ArrayList<Pertsona> pList = new ArrayList<Pertsona>();

	public static void main(String args[]) throws InterruptedException, LineUnavailableException, UnsupportedAudioFileException, IOException {
		Pertsona p = null;
		File music= null;
		/*int aukera=1;
		if(aukera==1) {
		 music = new File("1998 46-Foot Custom Carousel.wav");
		}else {
		 music = new File("Y era un domingo en la tarde fui a los coches de choque.wav");
		}
		  AudioInputStream audioinput = AudioSystem.getAudioInputStream(music);
		  Clip clip=AudioSystem.getClip();
		  clip.open(audioinput);*/
		int x=535;
		int y=200;
		 mendia= new Mendia(MendiEser,6,6,670,300,525, 200,525, 250);	
		 
		 mendi_koord = new Mendi_Koord(599, 150,mendia);
		 denda= new Argazki_Denda(DendariKop);
		arg_koord= new Argazki_Koord(410, 520, denda);
		for(int i=0;i<PertsonKop;i++) {
		 p=new Pertsona(i, mendi_koord, mendia,arg_koord, denda);
		pList.add(p);
		}
		
		
		Framea framea = new Framea();
 
		framea.setVisible(true);
		mendi_koord.start();
		arg_koord.start();
		for (int i = 0; i < pList.size(); i++) {
			pList.get(i).start();
		}
		
	
	//	clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	private static int getRandomNumberInRange01() {

		Random r = new Random();
		return r.ints(0, (1 + 1)).findFirst().getAsInt();

	}
}
