
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
import java.awt.event.ActionEvent; // Para poder usar actionPerformed, necesitamos este tipo

/**
 *
 * @author fede
 */
public class Board extends JPanel implements ActionListener {
    private Image image;
    private Timer timer;
    private int x, y, dx, dy;
    private int a, b, da, db;
    private static int SPEED1 = 2;
    private static int SPEED2 = 3;
    private static int SPEED3 = 10;
    private static int SPEED4 = 15;
    public Board(){
        ImageIcon ii = new ImageIcon(this.getClass().getResource("elef.png"));
        image = ii.getImage();
        this.setBackground(Color.white);
        x = 200;
        y = 200;
        dx = SPEED1;
        dy = SPEED2;
        
        a = 10;
        b = 10;
        da = SPEED3;
        db = SPEED4;
        // Timer
        timer = new Timer(15, this); // cada 15ms llama actionPerformed
        timer.start();
    }

    public void paint(Graphics g){
        super.paint(g);

        //Graphics2D g2d = (Graphics2D)g; // Convertimos a g de Graphics a Graphics2D
        //g2d.drawImage(image, x, y, this);
        g.drawImage(image, x, y, this);

        g.fillOval(Math.round(600-a), Math.round(600-b), 20, 20);


        // Timer
        //Toolkit.getDefaultToolkit().sync(); // fuerza sincronizacion, basicamente  	//// kendu ahal da

        //g.dispose();																//// kendu ahal da
    }

    public void actionPerformed(ActionEvent e){ // Se ejecuta cada 5ms
        x+=dx;
        y+=dy;
        if(x > 600-image.getWidth(null) || x < 0)
            dx *= -1;
        if(y > 600-image.getHeight(null) || y < 0)
            dy *= -1;
        
        a+=da;
        b+=db;
        if(a > 600-20 || a < 0)
            da *= -1;
        if(b > 600-20 || b < 0)
            db *= -1;
        System.out.println(" x:"+x+" y:"+y);
        System.out.println("\t\t\t a:"+a+" b:"+b);

        repaint(); // "re-pintamos" el panel
    }
}