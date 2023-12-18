import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
import static java.lang.Math.min;
import static java.lang.Math.max;


class Lab2 {

    private static InputReader in;
    private static PrintWriter out;

    private static HashMap<String, Integer> gangs = new HashMap<String, Integer>();
    private static Deque<String> gangsQ = new ArrayDeque<String>();
    private static Deque<Integer> intQ = new ArrayDeque<Integer>();
    private static int queue = 0;
    private static int served = 0;
    // TODO
    static private int handleDatang(String Gi, int Xi) {
        gangsQ.addLast(Gi);
        intQ.addLast(Xi);
        queue += Xi;
        return queue;
    }

    // TODO
    static private String handleLayani(int Yi) {
        String terakhir = "";

        while (Yi > 0 && queue > 0){
            int q = 0;
            if (intQ.isEmpty()) {
                queue = 0;
                break;
            }
            terakhir = gangsQ.peekFirst();
            if (intQ.peekFirst() > Yi){
                q = Yi;
                intQ.addFirst(intQ.pollFirst() - Yi);
                queue -= Yi;
                Yi = 0;

            } else if (intQ.peekFirst() <= Yi){
                q = intQ.pollFirst();
                gangsQ.pollFirst();
                queue -= q;
                Yi -= q;
            }
            if(gangs.containsKey(terakhir)){
                gangs.put(terakhir, gangs.get(terakhir) + q);
            } else gangs.put(terakhir, q);
        }

        return terakhir;
    }

    // TODO
    static private int handleTotal(String Gi) {
        try {
            return gangs.get(Gi);
        } catch (NullPointerException e){
            return 0;
        }

    }

    public static void main(String args[]) throws IOException {

        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N;

        N = in.nextInt();

        for(int tmp=0;tmp<N;tmp++) {
            String event = in.next();

            if(event.equals("DATANG")) {
                String Gi = in.next();
                int Xi = in.nextInt();

                out.println(handleDatang(Gi, Xi));
            } else if(event.equals("LAYANI")) {
                int Yi = in.nextInt();

                out.println(handleLayani(Yi));
            } else {
                String Gi = in.next();

                out.println(handleTotal(Gi));
            }
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
