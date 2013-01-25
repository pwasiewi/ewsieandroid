import java.util.HashMap;
import java.util.Map;

public class JKomunikaty {
  public static void main (String [] args) {
    int[] tablica = new int[10];
    for(int i=0; i<10; i++)
      tablica[i] = i+1;
    for(int x: tablica)
      System.out.print(x + " ");
    System.out.println();
    Komunikat km = new Komunikat(1,"kolko");
    System.out.println(km.index + " - " + km.id);
    System.out.println(Komunikaty.KOLKO);
    System.out.println(Komunikaty.ZAPISZ);
    System.out.println(Komunikaty.USTAWIENIA);

    Map<Integer, String> mapa = new HashMap<Integer, String>();
    mapa.put(1,"kolko");
    mapa.put(2,"zapisz");
    mapa.put(3,"ustawienia");
    System.out.println(mapa.get(2));
    System.out.println(mapa.keySet());
    System.out.println(mapa.values());
    System.out.println(mapa.size());

    Komunikaty wybor = Komunikaty.KOLKO;
    switch(wybor){
      case KOLKO:
        System.out.print(wybor.tekst());
        break;
      case ZAPISZ:
        System.out.print(wybor.tekst());
        break;
      default:
        System.out.print(wybor.tekst());
        break;
    }
  }
}

enum Komunikaty {
  KOLKO("kolko"), ZAPISZ("zapisz"), USTAWIENIA("ustawienia") ;
  private String id;
  private Komunikaty(String a){
    this.id = a; 
  }
  public String tekst(){
    return this.id;
  }
}

class Komunikat {
  int index;
  String id;
  Komunikat(int a, String b){
    index = a;
    id = b;
  }
}
