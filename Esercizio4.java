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
 * il costo di dijkstra con lista è O(N^3) con N numero di vertici
 *
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

            Graph graph = new Graph(numeroVertici);

            while (scanner.hasNext()){
                String riga = scanner.nextLine();

                if(riga.isEmpty())
                    continue;

                String[] edgeData = riga.split(" ");

                int fonte = Integer.parseInt(edgeData[0]);
                int destinazione = Integer.parseInt(edgeData[1]);
                double peso = Double.parseDouble(edgeData[2]);

                graph.listaDiAdiacenza.get(fonte).add(new Arco(fonte,destinazione,peso));
                graph.listaDiAdiacenza.get(destinazione).add(new Arco(destinazione,fonte,peso));
            }

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

    private static void stampaCammini(double costoMinimo, int fonte,
                                      int nodoAttuale, ArrayList<Integer> cammino) {

        if(nodoAttuale == fonte){
            Collections.reverse(cammino);
            System.out.print(cammino + " costo minimo: "+costoMinimo+"  ");
            return;
        }

        HashSet<Integer> diramazioni = precedenti[nodoAttuale];

        for(Integer camminoAttuale: diramazioni){

            ArrayList<Integer> nuovoCammino = (ArrayList)cammino.clone();
            nuovoCammino.add(camminoAttuale);

            stampaCammini(costoMinimo, fonte, camminoAttuale, nuovoCammino);
        }
    }

    private static double[] dijkstra(Graph graph, int source){
        double[] distanze = new double[graph.numeroVertici];
        boolean[] nodiVisitati = new boolean[graph.numeroVertici];

        precedenti = new HashSet[graph.numeroVertici];

        Arrays.fill(distanze,Double.POSITIVE_INFINITY);
        distanze[source] = 0;

        for(int i = 0; i < precedenti.length; i++){
            precedenti[i] = new HashSet<>();
        }

        List<Nodo> listaDiNodiDaVisitare = new ArrayList<>();
        listaDiNodiDaVisitare.add(new Nodo(source,0));

        while (!listaDiNodiDaVisitare.isEmpty()){
            Nodo nodo = trovaMinimo(listaDiNodiDaVisitare);
            listaDiNodiDaVisitare.remove(nodo);
            nodiVisitati[nodo.id] = true;

            ArrayList<Arco> listaAdiacenti = graph.listaDiAdiacenza.get(nodo.id);

            for(Arco arco: listaAdiacenti){

                if(nodiVisitati[arco.destinazione])
                    continue;

                double nuovaDistanza = distanze[arco.fonte] + arco.peso;

                if(distanze[arco.destinazione] > nuovaDistanza){
                    distanze[arco.destinazione] = nuovaDistanza;

                    listaDiNodiDaVisitare.add(new Nodo(arco.destinazione,nuovaDistanza));
                    precedenti[arco.destinazione] = new HashSet<>();
                    precedenti[arco.destinazione].add(arco.fonte);
                }else if(nuovaDistanza == distanze[arco.destinazione]){
                    precedenti[arco.destinazione].add(arco.fonte);
                }
            }
        }

        return distanze;
    }

    private static Nodo trovaMinimo(List<Nodo> lista) {
        double distMinima = Double.MAX_VALUE;
        Nodo minimo = null;

        for(Nodo attuale: lista){
            if(attuale.distanza < distMinima){
                distMinima = attuale.distanza;
                minimo = attuale;
            }
        }
        return minimo;
    }

    static class Graph {
        ArrayList<ArrayList<Arco>> listaDiAdiacenza;
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

