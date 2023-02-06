package kotxeanimatuak0;

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



