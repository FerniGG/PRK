
public class Kontagailua {
	 int balioa=0;
	 Kontagailua() {
		 String kont_izarra="[";
		 for (int k=balioa;k<2*LorategiaApp.MAX;k++) {
			 kont_izarra+=" ";
		 }
		kont_izarra+="]";
	System.out.println("\t\t\t\t"+kont_izarra);
	 }
	 void gehitu() {
	 int lag;
	 lag=balioa; //balioa irakurri

	 balioa=lag+1; //balioa idatzi
	 System.out.print("\t\t\t\t");
	 System.out.print("[");
	 for (int j=1;j<=balioa;j++) {
		 System.out.print("*");
		 Simulatu.HWinterrupt();
	 }
	 for (int k=balioa;k<2*LorategiaApp.MAX;k++) {
		 System.out.print(" ");
	 }
	 System.out.println("]");
	
	 }
	} 