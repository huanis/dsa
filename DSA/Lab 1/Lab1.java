import java.io.*;
import java.util.*;

public class Lab1 {
    private static InputReader in = new InputReader(System.in);
    private static PrintWriter out = new PrintWriter(System.out);

    /**
     * The main method that reads input, calls the function 
     * for each question's query, and output the results.
     * @param args Unused.
     * @return Nothing.
     */
    public static void main(String[] args) {

        int N = in.nextInt();   // banyak bintang
        int M = in.nextInt();   // panjang sequence

        List<String> sequence = new ArrayList<>();
        for (int i = 0; i < M; i++) {
            String temp = in.next();
            sequence.add(temp);
        }

        int maxMoney = getMaxMoney(N, M, sequence);
        out.println(maxMoney);
        out.close();
    }

    public static int getMaxMoney(int N, int M, List<String> sequence) {
        int result = 0;
        int temp = 0;

        // TODO
        int[] res = new int[N-1];
        int index = -1;
        for (String i : sequence){
            if (i.equals("*")){
                if (index > -1) {
                    temp += res[index];
                    if (temp < res[index]){
                        temp = res[index];
                    }
                    if (temp > result){
                        result = temp;
                    }
                    else if (index == 0) {
                        result = res[0];
                    }
                    else if (result <= res[index]){
                            result = res[index];
                            temp = result;
                    }
                }
                index++;
            }
            else{
                res[index] += Integer.parseInt(i);
            }
        }

        /* 80/100
        int[] res = new int[N-1];
        int index = -1;
        for (String i : sequence){
            if (i.equals("*")){
                index++;
            }else{
                res[index] += Integer.parseInt(i);
            }
        }

        result = res[0];
        for (int i = 0; i < N-1; i++){
            int newMax = 0;
            for (int j = i; j < N-1; j++){
                newMax += res[j];
                if (newMax > result) result = newMax;
            }
        }*/

        return result;
    }

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