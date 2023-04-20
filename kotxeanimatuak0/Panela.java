package kotxeanimatuak0;

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

public class Panela extends JPanel implements ActionListener {
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