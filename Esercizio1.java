import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Dalia Valeria Barone
 * 1097620
 * daliavaleria.barone@studio.unibo.it
 *
 * la soluzione proposta utilizza una tabella hash, per avere le operazioni di
 * ricerca ed inserimento a tempo costante, come richiesto da consegna
 *
 * in particolare la funzione hash utilizzata è quella standard di java
 * come chiavi si utilizzano le parole, mentre i valori sono le occorrenze
 * le collisioni sono state gestite con delle liste di trabocco, precisamente
 * delle linked list molto semplici, con riferimento solo ai nodi successivi
 *
 */

public class Esercizio1 {
    public static void main(String[] args) {
        if(args.length < 2){
            System.out.println("inserire i file di input");
            return;
        }
        int k = 200;
        StrutturaHash strutturaHash = new StrutturaHash(k);

        try(Scanner scanner = new Scanner(new File(args[0]))){
            while (scanner.hasNext()){
                String[] dati = scanner.nextLine().split(",");

                String chiave = dati[1];
                int valore = Integer.parseInt(dati[0]);

                strutturaHash.aggiungiOccorrenze(chiave,valore);
            }
        }catch (FileNotFoundException e){
            System.out.println(e);
        }

        try(Scanner scanner = new Scanner(new File(args[1]))){
            while (scanner.hasNext()){
                System.out.println(strutturaHash.occorrenzeParola(scanner.nextLine()));
            }
        }catch (FileNotFoundException e){
            System.out.println(e);
        }
    }
}

class Nodo {
    String chiave;
    int valore;
    Nodo successivo;

    public Nodo(String chiave, int valore) {
        this.chiave = chiave;
        this.valore = valore;
    }
    @Override
    public String toString() {
        return chiave + ", " + valore;
    }
}


class StrutturaHash{
    private SimpleLinkedList[] array;
    private int k;

    public StrutturaHash(int k){
        this.k = k;
        array = new SimpleLinkedList[k];
    }

    public void aggiungiOccorrenze(String chiave, int valore) {
        chiave = chiave.toLowerCase();
        int indice = funzioneHash(chiave);

        SimpleLinkedList lista = array[indice];

        if(lista == null){
            array[indice] = new SimpleLinkedList();
            array[indice].add(new Nodo(chiave,valore));
        }else if(lista.get(chiave) == null){
            lista.add(new Nodo(chiave,valore));
        }else{
            Nodo nodo = lista.get(chiave);
            int occurrences = nodo.valore;
            nodo.valore = occurrences + valore;
        }
    }

    public Nodo occorrenzeParola(String chiave) {
        int indice = funzioneHash(chiave.toLowerCase());

        SimpleLinkedList lista = array[indice];

        if(lista == null)
            return null;

        return lista.get(chiave.toLowerCase());
    }

    private int funzioneHash(String chiave){
        int hash = chiave.hashCode() % k;
        return hash < 0 ? -hash : hash;
    }
}

class SimpleLinkedList {

    Nodo testa;
    Nodo coda;

    public SimpleLinkedList(){
    }

    public Nodo get(String chiave){
        Nodo nodo = testa;

        while (nodo != null){
            if(nodo.chiave.equals(chiave))
                return nodo;
            nodo = nodo.successivo;
        }

        return null;
    }

    public void add(Nodo nodo) {
        if(coda == null){
            coda = nodo;
            testa = nodo;
        }else{
            coda.successivo = nodo;
            coda = nodo;
        }
    }
}



