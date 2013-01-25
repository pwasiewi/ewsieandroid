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
