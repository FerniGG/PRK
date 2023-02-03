
public class Atea extends Thread {
	 Kontagailua kont;
	 String atea;
	 Simulatu s;
	 public Atea (String zeinAte, Kontagailua k,Simulatu ps){
	kont=k; atea=zeinAte; s=ps;
	 }
	 public void run() {
	 try{
	 for (int i=1;i<=LorategiaApp.MAX;i++){
	 sleep((long)(Math.random()*1000));
	//ausazko denbora itxaron (0 eta 1 segunduren tartean)
	 System.out.print(atea);
	 System.out.print("[");
	 for (int j=1;j<=i;j++) {
		 System.out.print("*");
		 s.HWinterrupt();
	 }
	 for (int k=i;k<LorategiaApp.MAX;k++) {
		 System.out.print(" ");
	 }
	 System.out.println("]");
	 kont.gehitu();
	 }
	 } catch (InterruptedException e) {}
	 }
	}
