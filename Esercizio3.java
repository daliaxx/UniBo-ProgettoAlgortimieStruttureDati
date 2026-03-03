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
 */

public class Esercizio3 {

    private static class Item {
        String nome;
        int peso;

        public Item(String nome, int peso) {
            this.nome = nome;
            this.peso = peso;
        }

        public String toString(){
            return nome +" "+ peso;
        }
    }

    public static void main(String[] args) {
        if(args.length < 1){
            System.out.println("inserire il file di input");
            return;
        }

        List<Item> items = new ArrayList<>();

        try(Scanner scanner = new Scanner(new File(args[0]))){
            int n = Integer.parseInt(scanner.nextLine());

            for(int i = 0; i < n; i++){
                String[] dati = scanner.nextLine().split(" ");
                Item item = new Item(dati[0],Integer.parseInt(dati[1]));
                items.add(item);
            }
        }catch (FileNotFoundException e){
            System.out.println(e);
        }

        int disco = 1;

        while (!items.isEmpty()){
            prog_dinamica(disco,items);
            disco++;
        }
    }

    public static void prog_dinamica(int disco, List<Item> items) {
        int capacitaDisco = 650;

        int[][] soluzioni = new int[items.size() + 1][capacitaDisco + 1];

        for (int riga = 1; riga <= items.size(); riga++) {

            int peso = items.get(riga-1).peso, v = items.get(riga-1).peso;

            for (int capacita = 1; capacita <= capacitaDisco; capacita++) {

                soluzioni[riga][capacita] = soluzioni[riga - 1][capacita];
                if (capacita >= peso
                        && v + soluzioni[riga - 1][capacita - peso] > soluzioni[riga][capacita])
                    soluzioni[riga][capacita] = v + soluzioni[riga - 1][capacita - peso];
            }
        }

        int colonna = capacitaDisco;
        List<Integer> files = new ArrayList<>();

        System.out.println("Disco: "+disco);

        for (int riga = items.size(); riga > 0; riga--) {
            if (soluzioni[riga][colonna] != soluzioni[riga - 1][colonna]) {
                int indice = riga - 1;
                files.add(indice);
                colonna -= items.get(indice).peso;
                System.out.println(items.get(indice));
            }
        }

        int spazioLibero = capacitaDisco-soluzioni[items.size()][capacitaDisco];

        for(Integer indice : files){
            items.remove((int)indice);
        }

        System.out.println("Spazio libero: "+spazioLibero);
        System.out.println();
    }
}