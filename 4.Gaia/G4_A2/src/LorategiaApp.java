public class LorategiaApp{
public final static int MAX = 6;
public static void main (String args[]) {
System.out.println("LORATEGIA: return sakatu hasteko");
try{int c = System.in.read();}catch(Exception ex){}
System.out.println("Aurre \t\tAtze \t\tGuztira");
Kontagailua k = new Kontagailua();
Simulatu s = new Simulatu();
Atea aurrekoa = new Atea("",k,s);
Atea atzekoa = new Atea("\t\t",k,s);
aurrekoa.start();
atzekoa.start();
}
}