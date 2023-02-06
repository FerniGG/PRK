public class G3_A5_BaiEzAgianBetiApp{
	public static void main (String args[]) {
		//imagina no repasar el  codigo y no ver esta mierda
		Pantaila p = new Pantaila();
		Idazlea bai = new Idazlea("bai",1000,4,p);
		Idazlea ez = new Idazlea("\tez",400,15,p);
		Idazlea agian = new Idazlea("\t\tagian",2000,15,p);
		Idazlea beti = new Idazlea("\t\t\tbeti",1,60,p);
		bai.start();
		ez.start();
		agian.start();
		beti.start();
		}
		}
		class Idazlea extends Thread {
			private String baiEz;
			private int denbora, kopurua;
			private Pantaila p;
			public Idazlea(String s, int i, int j, Pantaila pant){
			baiEz=s; denbora=i; kopurua=j;
			p=pant;
			}
			public void run() {
				try {
				 int i;
				 for (i=0;i<kopurua;i++) {
				p.margotu(baiEz);
				sleep(denbora);
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
