import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Dalia Valeria Barone
 * 1097620
 * daliavaleria.barone@studio.unibo.it
 *
 * la soluzione proposta utilizza l'algoritmo di di ricerca in profondità (DFS) applicato alla scacchiera
 * presa in input, la cella del cavallo è la partenza (source), da cui si va a
 * visitare l'intero grafo, vale a dire la scacchiera
 * per trovare gli adiacenti di un nodo ci si muove nella scacchiera con le mosse
 * del cavallo a partire dalla cella a cui si sta puntando
 *
 * il costo computazionale è O(E+V) in quanto si visita ogni nodo ed ogni arco
 * con E si intende il numero di archi e V il numero di nodi / vertici
 *
 * durante la stampa, che costa O(V) si controlla se il cavallo ha visitato o meno
 * ogni cella
 * 
 * javac Esercizio2.java ; java -cp . Esercizio2 in2.txt
 */


public class Esercizio2{

    static class Cella {
        char carattere;   // Carattere che rappresenta lo stato della cella ('.', 'X', 'C')
        int i;           // Indice di riga
        int j;          // Indice di colonna

         // Costruttore per inizializzare una cella con un carattere e la sua posizione
        public Cella(char carattere, int i, int j) {
            this.carattere = carattere;
            this.i = i;
            this.j = j;
        }

        public Cella(){};
    }

    /* Metodo principale, Ho creato una matrice di dimensioni N*M di oggetti Cella, che viene popolata leggendo i dati da un file di input. 
    Quando durante la lettura incontro il carattere 'C', salvo quella posizione come la cella di partenza. Questa cella di partenza sarà 
    il nodo sorgente da cui applicheremo l'algoritmo di ricerca in profondità (DFS) per visitare tutto il grafo.
    */
    public static void main(String[] args) {
        // Controlla che sia stato fornito un file di input come argomento
        if(args.length < 1){
            System.out.println("inserire file di input");
            return;
        }

        // Prova ad aprire il file di input
        try(Scanner scanner = new Scanner(new File(args[0]))){

            // Legge il numero di righe e colonne della scacchiera
            int rows = Integer.parseInt(scanner.nextLine());
            int columns = Integer.parseInt(scanner.nextLine());

            // Inizializza la matrice di celle della scacchiera e la cella di partenza del cavallo
            Cella[][] grafo = new Cella[rows][columns];
            Cella partenza = null;

            int i = 0;
            // Legge la configurazione della scacchiera dal file

            //ciclo sul file di input, creo una cella per ogni posizione
            while (scanner.hasNext()){
                String positions = scanner.nextLine();
                for(int j = 0; j < columns; j++){
                    Cella cella = new Cella(positions.charAt(j),i,j);

                    grafo[i][j] = cella;

                    // Trova la posizione iniziale del cavallo
                    if(cella.carattere == 'C'){
                        partenza = cella;
                    }
                }
                i++;
            }
            // Esegue la ricerca in profondità a partire dalla cella di partenza
            dfs(grafo, partenza);

        }catch (FileNotFoundException e){
            System.out.println(e);
        }
    }

    // Metodo per eseguire la ricerca in profondità (DFS)
    /*Una volta ottenuta la matrice che rappresenta il grafo e identificata la cella sorgente, applico l'algoritmo DFS utilizzando uno stack. 
    Inserisco quindi come primo nodo da visitare la cella di partenza (dove si trova il cavallo). Finché lo stack non è vuoto, continuo a iterare
    sui nodi adiacenti da visitare. --Gli adiacenti sono calcolati applicando tutte le possibili mosse del cavallo a partire da una posizione. --*/

    private static void dfs(Cella[][] graph, Cella partenza) {
        Stack<Cella> stack = new Stack<>();
        stack.push(partenza);

        int righe = graph.length;      
        int colonne = graph[0].length; 

/*Ogni volta che trovo una posizione valida ( carattere '.'), la considero raggiungibile. Se invece trovo una 'X', significa che la cella è occupata 
e non posso visitarla. Se trovo una 'C', significa che la cella è già stata visitata. Per questo motivo, non mantengo un array di nodi visitati 
come si fa di solito con gli algoritmi di visita, ma modifico direttamente il grafo durante la visita, "marchiando" i nodi visitati con il segno 'C'. */

        // Esegue la DFS finché ci sono celle da visitare nella pila
        while (!stack.isEmpty()) {
            Cella cella = stack.pop();// Estrae la cella dalla cima della pila

            // Salta le celle occupate da un pezzo
            if(cella.carattere == 'X')
                continue;

            int colonna = cella.j; // Colonna corrente
            int riga = cella.i;    // Riga corrente

            // Controlla e visita le celle raggiungibili con le mosse del cavallo
            if(riga-2 > -1 && colonna-1 > -1){ //mossa del cavallo
                Cella cella1 = graph[riga-2][colonna-1];

                if(cella1.carattere == '.'){
                    stack.push(cella1);
                    cella1.carattere = 'C';
                }
            }

            if(riga+2 < righe && colonna-1 > -1){
                Cella cella1 = graph[riga+2][colonna-1];
                if(cella1.carattere == '.'){
                    stack.push(cella1);
                    cella1.carattere = 'C';
                }
            }


            if(riga+2 < righe && colonna+1 < colonne){
                Cella cella1 = graph[riga+2][colonna+1];
                if(cella1.carattere == '.'){
                    stack.push(cella1);
                    cella1.carattere = 'C';
                }
            }



            if(riga-1 > -1 && colonna+2 < colonne){
                Cella cella1 = graph[riga-1][colonna+2];
                if(cella1.carattere == '.'){
                    stack.push(cella1);
                    cella1.carattere = 'C';
                }
            }

            if(riga-2 > -1 && colonna+1 < colonne){
                Cella cella1 = graph[riga-2][colonna+1];
                if(cella1.carattere == '.'){
                    stack.push(cella1);
                    cella1.carattere = 'C';
                }
            }

            if(riga+1 < righe && colonna-2 > -1){
                Cella cella1 = graph[riga+1][colonna-2];
                if(cella1.carattere == '.'){
                    stack.push(cella1);
                    cella1.carattere = 'C';
                }
            }

            if(riga+1 < righe && colonna+2 < colonne){
                Cella cella1 = graph[riga+1][colonna+2];
                if(cella1.carattere == '.'){
                    stack.push(cella1);
                    cella1.carattere = 'C';
                }
            }

            if(riga-1 > -1 && colonna-2 > -1){
                Cella cella1 = graph[riga-1][colonna-2];
                if(cella1.carattere == '.'){
                    stack.push(cella1);
                    cella1.carattere = 'C';
                }
            }


        }
        stampaScacchiera(graph); // Stampa la scacchiera dopo la DFS
    }

    // Metodo per stampare la scacchiera e verificare se tutte le celle libere sono raggiunte
    private static void stampaScacchiera(Cella[][] scacchiera) {
        boolean risultato = true; // Variabile per determinare se tutte le celle libere sono raggiunte


        for(int i = 0; i < scacchiera.length; i++){
            for(int j = 0; j < scacchiera[0].length; j++){
                if(scacchiera[i][j].carattere == '.')
                    risultato = false; // Se c'è ancora una cella libera, il risultato è false

                System.out.print(scacchiera[i][j].carattere); // Stampa il carattere della cella
            }
            System.out.println();
        }
        System.out.println(risultato);
    }
}

