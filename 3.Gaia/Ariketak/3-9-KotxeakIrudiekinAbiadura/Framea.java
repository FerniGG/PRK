package kotxeanimatuak0;

import javax.swing.JFrame;

public class Framea extends JFrame {

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

