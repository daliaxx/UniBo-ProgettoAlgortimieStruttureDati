import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Dalia Valeria Barone
 * 1097620
 * daliavaleria.barone@studio.unibo.it
 *
 * la soluzione proposta utilizza l'algoritmo di bfs applicato alla scacchiera
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
 */


public class Esercizio2{

    static class Cella {
        char carattere;
        int i;
        int j;

        public Cella(char carattere, int i, int j) {
            this.carattere = carattere;
            this.i = i;
            this.j = j;
        }

        public Cella(){};
    }

    public static void main(String[] args) {
        if(args.length < 1){
            System.out.println("inserire file di input");
            return;
        }


        try(Scanner scanner = new Scanner(new File(args[0]))){
            int rows = Integer.parseInt(scanner.nextLine());
            int columns = Integer.parseInt(scanner.nextLine());

            Cella[][] grafo = new Cella[rows][columns];
            Cella partenza = null;

            int i = 0;

            while (scanner.hasNext()){
                String positions = scanner.nextLine();
                for(int j = 0; j < columns; j++){
                    Cella cella = new Cella(positions.charAt(j),i,j);

                    grafo[i][j] = cella;

                    if(cella.carattere == 'C'){
                        partenza = cella;
                    }
                }
                i++;
            }

            dfs(grafo, partenza);

        }catch (FileNotFoundException e){
            System.out.println(e);
        }
    }

    private static void dfs(Cella[][] graph, Cella partenza) {
        Stack<Cella> stack = new Stack<>();
        stack.push(partenza);

        int righe = graph.length;
        int colonne = graph[0].length;

        while (!stack.isEmpty()) {
            Cella cella = stack.pop();

            if(cella.carattere == 'X')
                continue;

            int colonna = cella.j;
            int riga = cella.i;

            if(riga-2 > -1 && colonna-1 > -1){
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
        stampaScacchiera(graph);
    }

    private static void stampaScacchiera(Cella[][] scacchiera) {
        boolean risultato = true;

        for(int i = 0; i < scacchiera.length; i++){
            for(int j = 0; j < scacchiera[0].length; j++){
                if(scacchiera[i][j].carattere == '.')
                    risultato = false;

                System.out.print(scacchiera[i][j].carattere);
            }
            System.out.println();
        }
        System.out.println(risultato);
    }
}

