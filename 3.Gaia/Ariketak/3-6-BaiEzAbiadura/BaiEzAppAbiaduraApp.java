public class BaiEzAppAbiaduraApp{
	public static void main (String args[]) {
		Pantaila p = new Pantaila();
		Idazlea bai = new Idazlea("bai",1000,30,p);
		Idazlea ez = new Idazlea("\tez",200,13,p);
		bai.start();
		ez.start();
		}
		}

		class Idazlea extends Thread {
			private String baiEz;
			private int kopurua;
			private double denbora;
			private Pantaila p;
			public Idazlea(String s, float i, int j, Pantaila pant){
			baiEz=s; denbora=i; kopurua=j;
			p=pant;
			}
			public void run() {
				try {
				 int i;
				 for (i=0;i<kopurua;i++) {
				p.margotu(baiEz);
		
				sleep((int)denbora);
				 if(this.baiEz.compareTo("bai")==0) {
					 this.denbora=this.denbora/1.2;
				 }
				 else {
					 this.denbora=this.denbora*1.2;
				 }
				 }
				 p.margotu(baiEz+" bukatu da");
				
				}catch (InterruptedException e) {}
				}
		
		
			}

class Pantaila {
			public Pantaila() {
				
			}
		
			public void margotu(String s){
				System.out.println(s);
				}
			
				}
		
