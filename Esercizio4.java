import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Dalia Valeria Barone
 * 1097620
 * daliavaleria.barone@studio.unibo.it
 *
 * la soluzione proposta utilizza l'algoritmo di dijkstra per trovare
 * i camminimi minimi da un nodo X a tutti gli altri
 * dove X va da 0 a N-1, con N numero di vertici
 *
 * per trovare TUTTI i possibili cammini minimi, si utilizza un array dei precedenti
 * che invece di salvarne uno solo, li salva tutti, quindi nell'algoritmo di dijkstra
 * viene fatta un estenzione per salvare non solo i precedenti minori, ma minori o uguali
 *
 * si chiama quindi dijkstra per ogni nodo, e la stampa avviene tramite l'array dei precedenti
 * ciclando al contrario e ricostruendo i cammini, nel caso ce ne fosse piu di uno
 *
 * il costo di dijkstra con lista è O(N^3) con N numero di vertici  O(V * (V + E) log V), con coda di priorità O((V + E) log V) 
 *
 * javac Esercizio4.java ; java -cp . Esercizio4 in4.txt
 */
 

public class Esercizio4 {

    public static void main(String[] args) {
        if(args.length < 1){
            System.out.println("inserire il file di input");
            return;
        }

        Locale.setDefault(Locale.US);

        try(Scanner scanner = new Scanner(new File(args[0]))){
            int numeroVertici = Integer.parseInt(scanner.nextLine());
            int numeroArchi = Integer.parseInt(scanner.nextLine());
//grafo sparso
            Graph graph = new Graph(numeroVertici); //esempio 10 vertici, quindi 10 liste di archi
            while (scanner.hasNext()){
                String riga = scanner.nextLine();

                if(riga.isEmpty())
                    continue;

                String[] edgeData = riga.split(" ");
//leggiamo l'arco (file)
                int fonte = Integer.parseInt(edgeData[0]);
                int destinazione = Integer.parseInt(edgeData[1]);
                double peso = Double.parseDouble(edgeData[2]);
// e laggiungiamo alla lista di adiacenza, sia la fonte che la destinazione
                graph.listaDiAdiacenza.get(fonte).add(new Arco(fonte,destinazione,peso));
                graph.listaDiAdiacenza.get(destinazione).add(new Arco(destinazione,fonte,peso));
            }
//faccio partire timer
            long inizio = System.currentTimeMillis();

            for(int i = 0; i < numeroVertici; i++){
                    stampa(graph, i);
            }

            long fine = System.currentTimeMillis();

            double tempo = (double) (fine - inizio) / 1000;

            System.out.println("tempo di esecuzione: "+tempo);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }
// METODO, data una fonte applica dijstra e per ogni fonte stampa il cammino
    private static void stampa(Graph grafo, int fonte) {

        double[] distanze = dijkstra(grafo, fonte);

        for(int destinazione = 0; destinazione < distanze.length; destinazione++){
            if(destinazione == fonte)
                continue;

            System.out.print(fonte +"  "+ destinazione +"  ");

            ArrayList<Integer> cammino = new ArrayList<>();
            cammino.add(destinazione);

            stampaCammini(distanze[destinazione], fonte, destinazione,cammino);
            System.out.println();
        }
    }

    static HashSet<Integer>[] precedenti;

    private static void stampaCammini(double costoMinimo, int fonte,  //fonte= sorgente, nodoattuale=quello che stiamo considerando che ad ogni iterazione cambia
                                      int nodoAttuale, ArrayList<Integer> cammino) { //ogni chiamata ha un cammino associato

        if(nodoAttuale == fonte){ //abbiamo finito, quindi inverto il cammino e lo stampo
            Collections.reverse(cammino);
            System.out.print(cammino + " costo minimo: "+costoMinimo+"  ");
            return;
        }
// se non ho finito, per ogni diramazione creo un cammino, e hanno in comune una parte di cammino, lo clono e faccio la chiamata ricorsiva con i precedenti
        HashSet<Integer> diramazioni = precedenti[nodoAttuale];

        for(Integer camminoAttuale: diramazioni){

            ArrayList<Integer> nuovoCammino = (ArrayList)cammino.clone();
            nuovoCammino.add(camminoAttuale);

            stampaCammini(costoMinimo, fonte, camminoAttuale, nuovoCammino);
        }
    }
/*"Inizializziamo le distanze di tutti i nodi a infinito, tranne la distanza del nodo sorgente che impostiamo a zero. Usando un array di booleani, 
tracciamo i nodi visitati e con un -array- di HashSet memorizziamo i -predecessori- per gestire i cammini minimi. Utilizziamo una lista per mantenere i nodi da visitare, 
partendo dal nodo sorgente. Nel ciclo principale, estraiamo il nodo con la distanza minima, lo segniamo come visitato e aggiorniamo le distanze dei suoi nodi adiacenti. 
Se troviamo una distanza minore, aggiorniamo la distanza e i predecessori; se la distanza è uguale, aggiungiamo semplicemente il nuovo predecessore. 
Alla fine, ---l'array delle -distanze- contiene i- cammini minimi(valori) -dal nodo sorgente a tutti gli altri nodi----.*/

    private static double[] dijkstra(Graph graph, int source){
        double[] distanze = new double[graph.numeroVertici];
        boolean[] nodiVisitati = new boolean[graph.numeroVertici];

// array che tiene conto dei percorsi minimi precedenti
        precedenti = new HashSet[graph.numeroVertici];
 
        Arrays.fill(distanze,Double.POSITIVE_INFINITY); //di default il nodo che non biamo visitato non è raggiungibile
        distanze[source] = 0;

        for(int i = 0; i < precedenti.length; i++){
            precedenti[i] = new HashSet<>();
        }
// LISTA DI NODI AL POSTO DELLA PRIORITY QUEUE
        List<Nodo> listaDiNodiDaVisitare = new ArrayList<>();
        listaDiNodiDaVisitare.add(new Nodo(source,0)); //aggiungo sorgente, 0 è la distanza 

        while (!listaDiNodiDaVisitare.isEmpty()){  //nodo minimo è il prossimo nodo da visitare,
            //quindi lo levo dai nodi da visitare, e lo metto true nell array dei visitati
            Nodo nodo = trovaMinimo(listaDiNodiDaVisitare);
            listaDiNodiDaVisitare.remove(nodo);
            nodiVisitati[nodo.id] = true;

            //una volta letto il minimo leggo i suoi adiacenti
            ArrayList<Arco> listaAdiacenti = graph.listaDiAdiacenza.get(nodo.id); //vado a vedere nella lista di adiacenza la posizione i esima, i= numero del nodo minimo
            // cicla se mi porta in un nodo già visitato, lo salto
            for(Arco arco: listaAdiacenti){

                if(nodiVisitati[arco.destinazione])
                    continue;
// se non l'ho visitato lo confronto,distanza + nuovo arco, se è migliore l'aggiorno
                double nuovaDistanza = distanze[arco.fonte] + arco.peso;

                if(distanze[arco.destinazione] > nuovaDistanza){
                    distanze[arco.destinazione] = nuovaDistanza;
// lo aggiungo come nodo da visitare con la nuova distanza
                    listaDiNodiDaVisitare.add(new Nodo(arco.destinazione,nuovaDistanza));
                    precedenti[arco.destinazione] = new HashSet<>(); //creo un nuovo hash e azzero i precedenti
                    precedenti[arco.destinazione].add(arco.fonte); 
                }else if(nuovaDistanza == distanze[arco.destinazione]){
                    // AGGIUNGERE TABELLA HASH PER VERIFICARE
                    // CHE NON CI SIANO PERCORSI DUPLICATI
                    // CON UN FOR SCORRO I PERCORSI, QUANDO TROVO UN NODO UGUALE SEGNO CO UN FLAG BOOLEN FALSE E NON LO AGGIUNGO +
                    // QUINDI QUI METTO UN IF SE TRUE AGGIUGNO, ALTRIMENO NON FACCIO NULLA 
                    precedenti[arco.destinazione].add(arco.fonte); //se ci sono due percorsi minimi, li aggiungo entrembi
                }
            }
        }

        return distanze;
    }
//trova nodo minimo, data una lista di nodi
    private static Nodo trovaMinimo(List<Nodo> lista) {
        double distMinima = Double.MAX_VALUE;
        Nodo minimo = null;
// SCORRE TUTTA LA LISTA DI NODI
        for(Nodo attuale: lista){
            //VERIFICA SE LA DISTANZA TROVATA è MUNORE DI QUELLA REGISTRATA
            if(attuale.distanza < distMinima){
                // AGGIORNA LE DISTANZE
                distMinima = attuale.distanza;
                minimo = attuale;
            }
        }
        // RITORNA LA DISTANZA MINIMA 
        return minimo;
    }
//ho rappresentato il grafo usando una lista di adiacenza, dove ogni nodo ha una lista di archi adiacenti. 
    static class Graph { 
        ArrayList<ArrayList<Arco>> listaDiAdiacenza; //prima lista rappresenta la lista dei nodi e la seconda la lista degli archi(nodi adiacenti)
        int numeroVertici;

        public Graph(int numeroVertici){
            this.numeroVertici = numeroVertici;
            listaDiAdiacenza = new ArrayList<>();

            for(int i = 0; i < numeroVertici; i++){
                listaDiAdiacenza.add(new ArrayList<>());
            }
        }
    }

    static class Arco{
        int fonte;
        int destinazione;
        double peso;

        public Arco(int fonte, int destinazione, double peso) {
            this.fonte = fonte;
            this.destinazione = destinazione;
            this.peso = peso;
        }
    }

    static class Nodo{  
        int id;
        double distanza;

        public Nodo(int id, double distanza){
            this.id = id;
            this.distanza = distanza;
        }
    }
}

