

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
import java.util.ArrayList;
import java.awt.event.ActionEvent; 

public class Panela extends JPanel implements ActionListener {
    private Timer timer;
    String imagepath="";
    private ImageIcon background = new ImageIcon(this.getClass().getResource("Mapa2_open.png"));
    private Image background_Image=background.getImage();;

    public Panela(){
        this.setBackground(Color.white);
       
        timer = new Timer(15, this); // 15ms-ro actionPerformed metodoari deitzen dio
        timer.start();
    }

    public void paint(Graphics g){
        super.paint(g);
        this.background_Image=ParkeTematikoa.mendia.getMapa();
        g.drawImage(this.background_Image,0,0 , this);
        for(int i=0;i<ParkeTematikoa.pList.size();i++) {
        	 g.drawImage(ParkeTematikoa.pList.get(i).getImage(), ParkeTematikoa.pList.get(i).x , ParkeTematikoa.pList.get(i).y , this);
    	}
        g.drawImage(ParkeTematikoa.mendi_koord.getImage(), ParkeTematikoa.mendi_koord.getKoordX() , ParkeTematikoa.mendi_koord.getKoordY() , this);        
        g.drawImage(ParkeTematikoa.mendia.getImage(), ParkeTematikoa.mendia.x_m , ParkeTematikoa.mendia.y_m , this);
        g.drawImage(ParkeTematikoa.arg_koord.getImage(), ParkeTematikoa.arg_koord.x , ParkeTematikoa.arg_koord.y , this);
    }


    public void actionPerformed(ActionEvent e){ 
        repaint(); // panela bir-margotu (re-paint)
    }
}