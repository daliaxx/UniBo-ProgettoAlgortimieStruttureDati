import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Dalia Valeria Barone
 * 1097620
 * daliavaleria.barone@studio.unibo.it
 *
 * Questo programma implementa il problema dello zaino 0/1 (Knapsack 0/1) attraverso l'uso della programmazione dinamica.
 * L'algoritmo è progettato per massimizzare l'uso dello spazio disponibile su un disco, considerando un insieme di file,
 * ognuno con un proprio peso (o dimensione). L'obiettivo è selezionare i file da inserire nel disco in modo che il
 * peso totale sia massimo e non superi la capacità del disco.
 *
 * La tabella delle soluzioni è costruita iterando su ogni elemento (file). Per ogni file, si decide se includerlo nel disco
 * o meno, basandosi sulle soluzioni dei sottoproblemi precedenti:
 *
 * Definiamo sol[i][j] come il massimo spazio utilizzato utilizzando i primi i file e una capacità di disco j.
 * - Se il file i-esimo è escluso, sol[i][j] sarà uguale a sol[i-1][j], cioè la soluzione del sottoproblema precedente
 *   senza considerare il file corrente.
 * - Se il file i-esimo è incluso, allora sol[i][j] sarà uguale al massimo tra sol[i-1][j] e sol[i-1][j-peso(i)] + peso(i),
 *   dove peso(i) è il peso del file i-esimo. Questo assicura che il file sia incluso solo se c'è abbastanza spazio rimasto
 *   nel disco dopo aver considerato i file precedenti.
 *
 * La soluzione ottimale al problema è trovata considerando tutte le combinazioni di inclusione/esclusione per ogni file,
 * risultando in una complessità computazionale di O(N*P), dove N è il numero di file e P è la capacità del disco.
 *
 * Il processo termina con l'identificazione della combinazione di file che massimizza l'uso dello spazio, garantendo
 * che lo spazio totale utilizzato sia il più vicino possibile alla capacità massima del disco, minimizzando così
 * lo spazio libero residuo.
 * javac Esercizio3.java ; java -cp . Esercizio3 in3.txt
 */

public class Esercizio3 {

    // Classe interna statica per rappresentare un file con nome e peso
    private static class Item {
        String nome;  // Nome del file
        int peso;     // Peso del file in MB

        // Costruttore per inizializzare nome e peso del file, conterrà
        public Item(String nome, int peso) {
            this.nome = nome;
            this.peso = peso;
        }

        // Metodo per convertire l'oggetto Item in una stringa
        public String toString(){
            return nome + " " + peso;
        }
    }

    // Metodo principale del programma
    public static void main(String[] args) {
        // Controlla se è stato passato almeno un argomento (il nome del file di input)
        if(args.length < 1){
            System.out.println("inserire il file di input");
            return;  // Esce dal programma se non c'è il file di input
        }

        //inizializziamo la lista di items, --che memorizza i file letti dal file di input--
        List<Item> items = new ArrayList<>();  

        // Prova a leggere il file di input
        try(Scanner scanner = new Scanner(new File(args[0]))){
            int n = Integer.parseInt(scanner.nextLine());  // Legge il numero di file

            // Legge ogni riga del file di input e crea un oggetto Item per ogni file
            for(int i = 0; i < n; i++){
                String[] dati = scanner.nextLine().split(" ");
                Item item = new Item(dati[0], Integer.parseInt(dati[1]));
                items.add(item);  // Aggiunge l'oggetto Item alla lista
            }
        }catch (FileNotFoundException e){
            System.out.println(e);  
        }

        int disco = 1;  // Inizializza il numero del disco a 1

        //applichimo la tecnica dello zaino
        // Continua a riempire i dischi finché ci sono file nella lista
        while (!items.isEmpty()){
            prog_dinamica(disco, items);  // Chiamata al metodo per riempire il disco
            disco++;                     // Incrementa il numero del disco
        }
    }
////////////////
    // Metodo per riempire un disco usando programmazione dinamica
    public static void prog_dinamica(int disco, List<Item> items) {
        int capacitaDisco = 650;  

        // Matrice per memorizzare le soluzioni dei sottoproblemi, struttura dati con tutte le soluzioni possibil, n x c
        int[][] soluzioni = new int[items.size() + 1][capacitaDisco + 1];

        // Popola la matrice delle soluzioni
        for (int riga = 1; riga <= items.size(); riga++) {                  //itera attraverso ciascun file.
            int peso = items.get(riga-1).peso, v = items.get(riga-1).peso;  // Peso e valore del file corrente

            for (int capacita = 1; capacita <= capacitaDisco; capacita++) {
                soluzioni[riga][capacita] = soluzioni[riga - 1][capacita];
                // Escludi il file dalla soluzione, copi la soluzione dalla riga precedente

                //confronta la soluzione precedente senza il file e la nuova soluzione che include il file, scegliendo quella che utilizza al meglio lo spazio disponibile.

                /*Includere l'iesimo oggetto: In questo caso, si sottrae il peso dell'iesimo oggetto dal peso disponibile nello zaino (j) capacita
                e si va a sommare il valore dell'iesimo oggetto al valore massimo ottenibile con i primi i1 oggetti e con uno spazio nello zaino 
                pari a j-peso di i (valore presente nella cella (i-1, j-W_i)). Si sceglie il valore massimo tra le due opzioni per la cella (i, j). */
                // Se il file corrente può essere incluso e migliora la soluzione, aggiorna la matrice
                if (capacita >= peso && v + soluzioni[riga - 1][capacita - peso] > soluzioni[riga][capacita])
                    soluzioni[riga][capacita] = v + soluzioni[riga - 1][capacita - peso];
                    //calcola il valore ottenibile includendo il file corrente. Somma il valore del file corrente v con la soluzione del sottoproblema
                }
        }

        int colonna = capacitaDisco;  // Inizializza la colonna alla capacità massima del disco
        List<Integer> files = new ArrayList<>();  // Lista per tenere traccia dei file inclusi nel disco

        System.out.println("Disco: " + disco);  // Stampa il numero del disco


        /*La soluzione ottimale è rappresentata dagli oggetti selezionati durante la ricostruzione del percorso
        dalla cella (N, C) alla cella (0, 0). Il valore massimo presente nella cella (N, C) rappresenta il valore
        totale massimo ottenibile con questa combinazione di oggetti, sottraendo questo valore alla capacita
        massima otteniamo lo spazio libero nel disco dopo aver inserito i files. */

        // Soluzione finale, Parto dalla fine e voglio capire se l,ogetto è stato selezionato, se la soluzione della riga precedemte è diversa, oggetto è stato selezionato
        //Trova i file inclusi nel disco usando il backtracking sulla matrice delle soluzioni
        for (int riga = items.size(); riga > 0; riga--) {
            if (soluzioni[riga][colonna] != soluzioni[riga - 1][colonna]) {
                int indice = riga - 1;
                files.add(indice);  
                colonna -= items.get(indice).peso;  // Aggiorna la colonna sottraendo il peso del file
                System.out.println(items.get(indice));  // Stampa il file incluso nel disco
            }
        }

        int spazioLibero = capacitaDisco - soluzioni[items.size()][capacitaDisco];  // Calcola lo spazio libero sul disco

        // Rimuove i file inclusi nel disco dalla lista principale
        for(Integer indice : files){
            items.remove((int)indice);
        }

        System.out.println("Spazio libero: " + spazioLibero);  // Stampa lo spazio libero sul disco
        System.out.println();  // Stampa una riga vuota per separare i dischi
    }
}
