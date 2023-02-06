
public class BaiEz1App{
	 public static void main (String args[]) {
		 BaiIdazlea bai = new BaiIdazlea();
		 EzIdazlea ez = new EzIdazlea();
		 bai.start();
		 ez.start();
		  }
		}
		 
		 class BaiIdazlea extends Thread {
			public void run(){
				try {
				while(true){
				System.out.println("bai");
				sleep(1000);
				}
				}catch (InterruptedException e){}
				}
			   }
			   class EzIdazlea extends Thread {
				public void run(){
					try {
						while(true){
							 System.out.println("\t ez");
							 sleep(400);
						}
					}catch (InterruptedException e) {
					 
					}
				}
			}