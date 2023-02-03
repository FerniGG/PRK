import javax.swing.JFrame;

/**
*
* @author fede
*/

public class Main extends JFrame {
   public Main(){
       add(new Board());
       setTitle("Pilotan jolasten");
       setDefaultCloseOperation(EXIT_ON_CLOSE);
       setSize(600,600);
       setLocationRelativeTo(null);
       setVisible(true);
       setResizable(false);
   }

   public static void main(String[] args) {
       new Main();
   }

}
