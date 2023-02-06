import java.awt.Image;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import java.awt.Color;
// Timer Imports
import java.awt.Toolkit;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent; 
import javax.swing.JFrame;

 
public class KotxeAnimatuak  {
	
	public static void main (String args[]) {
	
		Framea framea = new Framea();
		framea.setVisible(true);
		
		Kotxea k1 = new Kotxea(1, 70, framea);
		Kotxea k2 = new Kotxea(2, 50, framea);
						
		k1.start();
		k2.start();
	}
}

class Kotxea extends Thread {
	private int kotxea;
	private int abiadura;
	private int denbora;
	private Framea framea;
	
	public Kotxea(int kotx, int abiad,  Framea f){
		kotxea=kotx;
		abiadura=abiad;
		framea=f;
	}
	
	public void run() {
		try {sleep(1000);}
		catch (InterruptedException e) {}
		for (int x=0;x<540;x++) {
			framea.panela.setX(kotxea,x);
			if(kotxea==1){
				abiadura*=1.2;
			}else{
				abiadura/=1.2;
			}
			denbora=(int)(1000/abiadura);
			try {sleep(denbora);}
			catch (InterruptedException e) {}
		}
		System.out.println(" Bukatuta ("+kotxea+")");
	}
}

class Framea extends JFrame {

	Panela panela = new Panela();
	
	public Framea() {
		super();
		initialize();
	}
	
	private void initialize() {
		this.setTitle("Kotxeak");
		this.add(panela);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(600,300);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setResizable(false);
	}
}



class Panela extends JPanel implements ActionListener {
    private Image image1,image2;
    private Timer timer;
    private int x1, x2, y1=40, y2=80;

	public void setX(int kotxea, int balioa){
		if (kotxea==1)	{x1=balioa;}
		else			{x2=balioa;}
	}
	
    public Panela(){
    	ImageIcon ii = new ImageIcon(this.getClass().getResource("kotxe1.png"));
        image1 = ii.getImage();
    	ImageIcon ii2 = new ImageIcon(this.getClass().getResource("kotxe2.png"));
        image2 = ii2.getImage();
        this.setBackground(Color.white);
       
        timer = new Timer(15, this); // 15ms-ro actionPerformed metodoari deitzen dio
        timer.start();
    }

    public void paint(Graphics g){
        super.paint(g);
        g.drawImage(image1, x1 , y1, this);
        g.drawImage(image2, x2 , y2, this);
    }

    public void actionPerformed(ActionEvent e){ 
        repaint(); // panela bir-margotu (re-paint)
    }
}



