
import java.util.ArrayList;

import javax.swing.JFrame;

public class Framea extends JFrame {
	
	Panela panela ;
	
	public Framea() {
		super();
		panela=new Panela();
		
		initialize();
	}
	
	private void initialize() {
		this.setTitle("Parke_Tematikoa_Fernando_Gonzalez");
		this.add(panela);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(800,625);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setResizable(false);
	}
}

