public class BaiEz3App{
    public static void main (String args[]) {
    Idazlea3 bai = new Idazlea3("bai",1000,4);
    Idazlea3 ez = new Idazlea3("\tez",400,20);
    bai.start();
    ez.start();
    }
    }
class Idazlea3 extends Thread {
    private String baiEz;
    private int denbora, kopurua;
    
    public Idazlea3(String s, int i, int j){
        baiEz=s;
        denbora=i;
        kopurua=j;
    }
    
    public void run() {
        try{
            int i;
            for (i=0;i<kopurua;i++) {
                System.out.println(baiEz);
                sleep(denbora);
            }
    System.out.println(" Bukatuta ("+baiEz+")");
        }catch (InterruptedException e) {}
    }
}