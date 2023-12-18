import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

class oop3 {

    private static InputReader in;
    private static PrintWriter out;

    // sum of batch
    private static int C;
    // sum of students in each batch
    private static int N;
    // how many days for training
    private static int E;

    // list of students
    private static ArrayList<String> names = new ArrayList<>();
    private static Map<String, Integer> kompe = new HashMap<>();
    private static Map<String, String> students = new HashMap<>();

    //OOP
    private static Map<String, Student> con = new HashMap<>();

    private static Student student;

    private static int max = 0;
    private static int len = 0;

    private static void panutan(int q){
        int b = 0;
        int s = 0;
        len = names.size();
        for (int i=0; i<len;i++){
            String name = names.get(i);
            out.print(name + " ");
            if (i<q){
                switch (con.get(name).code){
                    case "B": b++; break;
                    case "S": s++; break;
                }
            }

        }
        out.println();
        out.println(b + " " + s);

    }

    private static void kompetitif(){
        boolean had = false;
        String p = "";
        for (String i: names){
            out.print(i + " ");
            if (kompe.get(i) == max && !had){
                had = true;
                p = i + " " + max;
            }
        }
        out.println();
        out.println(p);
    }

    private static void evaluasi(){
        Queue<String> temp = new ArrayDeque<>();
        for (int i = 0; i <= len; i++) {
            String name = names.get(i);
            student = con.get(name);
            if (i < con.get(name).index) {
                student.evaluate = true;
            }
            if (!student.evaluate) temp.add(name);
            out.print(name + " ");
        }
        out.println();

        boolean none = true;
        while (!temp.isEmpty()){
            none = false;
            out.print(temp.poll() + " ");
        }
        if (none) out.print("TIDAK ADA");
        out.println();
    }

    private static void duo(){
        String name;
        Queue<String> B = new ArrayDeque<>();
        Queue<String> S = new ArrayDeque<>();
        for (int i = 0; i <= len; i++) {
            name = names.get(i);
            student = con.get(name);
            if (student.code.equals("B")) B.add(name);
            else S.add(name);
            out.print(name + " ");
        }
        out.println();

        boolean extra = false;
        while (!B.isEmpty() || !S.isEmpty()){
            if (!B.isEmpty() && !S.isEmpty()) out.println(B.poll() + " " + S.poll());
            else if (extra){
                if (B.isEmpty()) out.print(S.poll() + " ");
                else out.print(B.poll() + " ");
            } else {
                out.print("TIDAK DAPAT: ");
                extra = true;
            }
        }

        if (extra) out.println();
    }

    private static int[][] dp;
    private static boolean[][] hascal;

    private static void deploy(int Q){
        String name;
        if (N > 0) {
            for (int k = 0; k <= len; k++) {
                name = names.get(k);
                out.print(name + " ");
            }
            out.println();
        }
        len = names.size();

        dp = new int[Q+1][len+1];
        hascal = new boolean[Q+1][len+1];
        out.println(recursion(students.get(names.get(0)), 1, Q, 0));
    }

    private static int recursion(String prevCode, int index, int regu, int start){
        //inspirasi: pembahasan lab 3
        if (index == len){
            if (--regu == 0 && prevCode.equals(students.get(names.get(len-1)))) {
                return 1;
            }
            return 0;
        }
        if (hascal[regu][start]){
            return dp[regu][start];
        }
        String code = students.get(names.get(index));
        int res=0;
        if (regu != 0 && (len-start/regu)>1) {
            if (prevCode.equals(code) && index < len - 2) {
                res = recursion(students.get(names.get(index + 1)), index + 2, regu - 1, index+1);
            }
            res += recursion(prevCode, index + 1, regu, start);
        }
        res = (int) (res % 1000000007L);
        dp[regu][start] = res;
        hascal[regu][start] = true;
        return res;
    }

    public static void main(String args[]) throws IOException {

        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        //banyak batch yang akan diambil
        C = in.nextInt();

        String name;
        String code;
        for (int i=0; i<C; i++){
            //restart for next batch
            max =0;
            names.clear();
            kompe.clear();
            students.clear();

            //oop
            con.clear();
            //banyak siswa pada batch i
            N = in.nextInt();
            for (int j=0; j<N; j++){
                name = in.next();
                code = in.next();
                names.add(name);

                //students: {kode unik:spesialisasi}
                students.put(name, code);

                //con: {kode unik:student object}
                con.put(name, new Student(name, code, j));

                //initialize kompetitif (0 kali ditunjuk)
                kompe.put(name, 0);
            }
            //mau ganti jadi names.size() aja tapi takutnya
            //nanti ada error. Ga berani ganti kak
            len = names.size()-1;

            //banyak hari untuk pelatihan
            E = in.nextInt();
            for (int j=0; j<E; j++){
                //cara sort ranking terinspirasi oleh Taufiqul Mawarid dan Umar Izzuddin
                //yang ditunjuk menjadi ranking atas akan dimasukkan ke dalam stack
                //yang ditunjuk menjadi ranking bawah akan dimasukkan ke dalam queue

                //alasannya saya boleh saya gambar di one note ga kak?
                Stack<String> zero = new Stack<>();
                Queue<String> one = new ArrayDeque<>();

                //jika murid index k ditunjuk maka check[k] = true
                //ini untuk membedakan yang sudah ditunjuk pada hari itu dengan yang belum
                boolean[] check = new boolean[len+1];

                //banyak pelatihan pada hari j
                int P = in.nextInt();

                //jika ada yang ditunjuk ke ranking atas, count0++
                //Jika ada yang dijuntuk ke ranking atas, count1++
                int count0 = 0;
                int count1 = 0;

                for (int k=0; k<P; k++){
                    name = in.next();

                    //ditunjuk++
                    kompe.put(name, kompe.get(name)+1);

                    //jika lebih besar dari max, banyak ditunjuk akan menjadi max
                    max = Integer.max(max, kompe.get(name));
                    check[con.get(name).index] = true;
                    if (in.nextInt() == 0){
                        zero.push(name);

                        //oop
                        //yang ini juga boleh dijelasin pake one note aja ga kak?
                        //pengidentifikasi. Untuk tahap pemasukan ke array
                        con.get(name).count = count0++;
                        con.get(name).v = 0;
                    } else{
                        one.add(name);

                        //oop
                        con.get(name).count = count1++;
                        con.get(name).v = 1;
                    }
                }
                ArrayList<String> temp = new ArrayList<>();
                //insert new ranking system
                //how to efisien dis?
                //let's try oop maybe?
                //oh my god
                //ya Allah ya Rabb
                //berkahilah kodeku
                //dan kode kawan kawanku
                int dex = 0;
                while (!zero.isEmpty()){
                    name = zero.pop();
                    student = con.get(name);
                    count0--;
                    if (student.v == 0 && count0 == student.count) {
                        temp.add(name);
                        if (j != E-1) {
                            if (dex < student.index) {
                                con.get(name).evaluate = true;
                            }
                            con.get(name).index = dex++;
                            out.print(name + " ");
                        }
                    }
                }
                for (int k = 0; k <= len; k++){
                    if (!check[k]){
                        name = names.get(k);
                        student = con.get(name);
                        temp.add(name);
                        if (j != E-1) {
                            if (dex < student.index) {
                                con.get(name).evaluate = true;
                            }
                            student.index = dex++;
                            out.print(name + " ");
                        }
                    }
                }
                count1 = 0;
                while (!one.isEmpty()){
                    name = one.poll();
                    student = con.get(name);
                    if (student.v == 1 && count1 == student.count) {
                        temp.add(name);
                        if (j != E-1) {
                            if (dex < con.get(name).index) {
                                student.evaluate = true;
                            }
                            con.get(name).index = dex++;
                            out.print(name + " ");
                        }
                    }
                    count1++;
                }
                names = temp;
                //done
                if (j != E-1) {
                    out.println();
                }
            }

            switch (in.next()){
                case "PANUTAN":
                    panutan(in.nextInt());
                    break;

                case "KOMPETITIF":
                    kompetitif();
                    break;

                case "EVALUASI":
                    evaluasi();
                    break;

                case "DUO":
                    duo();
                    break;

                case "DEPLOY":
                    deploy(in.nextInt());
                    break;
            }
        }

        out.println();
        out.flush();
    }

    static class Student {
        public String name;
        public String code;
        public int index;
        public int count;
        public int v;
        public boolean evaluate;

        public Student(String name, String code, int index){
            this.name = name;
            this.code = code;
            this.index = index;
        }
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