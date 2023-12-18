import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

/**REFERENCES I USED: (I forgot to put these in the previous submission)
 * FOR HANDLING THE TICKETS: https://www.youtube.com/watch?v=EFg3u_E6eHU
 * HOW TO IMPLEMENT THE BFS: https://www.youtube.com/watch?v=zaBhtODEL0w
 * */


public class test {
    private static InputReader in;
    private static PrintWriter out;

    private static HashMap<Integer, Kota> cities = new HashMap<>();

    static class Kota{
        ArrayList<Jalan> jalan = new ArrayList<>();

        Kota(){}
    }

    static class Jalan{
        Kota U;
        Kota V;
        int fee;

        Jalan(Kota U, Kota V, int fee){
            this.U = U;
            this.V = V;
            this.fee = fee;

            U.jalan.add(this);
            V.jalan.add(this);
        }
    }

    // TODO: method to create an edge with type==T that connects vertex U and vertex V in a graph
    public static void addEdge(int U, int V, int T) {
        Kota a;
        Kota b;
        if (cities.containsKey(U)) a = cities.get(U);
        else{
            a = new Kota();
            cities.put(U, a);
        }

        if (cities.containsKey(V)) b = cities.get(V);
        else{
            b = new Kota();
            cities.put(V, b);
        }

        new Jalan(a, b, T);
    }

    // TODO: Handle teman X Y K
    public static int canMudik(int X, int Y, int K) {
        HashMap<Kota, Integer> checked = new HashMap<>();
        HashMap<Kota, Boolean> inQueue = new HashMap<>();
        Queue<Kota> queue = new LinkedList<>();

        Kota start = cities.get(X);
        Kota target = cities.get(Y);

        queue.add(start);
        checked.put(start, 0);

        Kota current;
        while (!queue.isEmpty()){
            current = queue.poll();
            inQueue.put(current, false);
            for (Jalan jalan : current.jalan){
                int fee = checked.get(current) + jalan.fee;
                Kota kota;
                if (jalan.U.equals(current)){
                    kota = jalan.V;
                } else kota = jalan.U;

                if (checked.containsKey(kota)){
                    if (fee < checked.get(kota) && checked.get(kota) >= 0){
                        checked.put(kota, fee);
                        if (!inQueue.get(kota)){
                            queue.add(kota);
                            inQueue.put(kota, true);
                        }
                    }
                } else{
                    checked.put(kota, fee);
                    queue.add(kota);
                    inQueue.put(kota, true);
                }
            }
        }

        if (K < checked.get(target)) return 0;
        return 1;
    }

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N = in.nextInt();
        int M = in.nextInt();
        int Q = in.nextInt();

        for (int i=0;i < M;i++) {
            int U = in.nextInt();
            int V = in.nextInt();
            int T = in.nextInt();
            addEdge(U, V, T);
        }
        while(Q-- > 0) {
            int X = in.nextInt();
            int Y = in.nextInt();
            int K = in.nextInt();
            out.println(canMudik(X, Y, K));
        }

        out.flush();
    }

    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit Exceeded caused by slow input-output (IO)
    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

    }
}