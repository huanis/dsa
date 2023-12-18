import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

/**         A COMBINATION BETWEEN NH2.JAVA WITH IMANNUEL'S IDEA            */
/**==========================================================================
 * Idea for relative height was given to me by Gitan. He was taught by Nuel*/
/**==========================DOCUMENT INDEX==================================
 * 1. RAIDEN METHODS
 * 2. CLASS: DATARAN
 *    - init
 *    - printTinggi()
 * 3. CLASS: PULAU
 *    - init
 *    - getTinggi()
 *    - add()
 *    - unifikasi()
 *    - pisah()
 *    - crumble()
 *    - stabilize()
 *    - quake()
 *    - rise()
 *    - sweeping()
 * 4. CLASS: NODE
 *    - init
 *    - add()
 *    - remove()
 * 5. CLASS: TREE
 *    - init()
 *    - search()
 *    - recSearch()
 *    - getTinggi()
 *    - printTinggi()
 *    - recTinggi()
 *    - add()
 *    - insert()
 *    - stabilize()
 *    - recStab()
 *    - crumble()
 *    - recCrumble()
 *    - quake()
 *    - recQuake()
 *    - rise()
 *    - recRise()
 *    - sweeping()
 *    - recSweep()
 * 6. MAIN
 * 7. CLASS: INPUTREADER
 * ===========================================================================
 * */

public class last1 {
    private static InputReader in;
    private static PrintWriter out;

    private static Map<String, Dataran> temples = new HashMap<>();
    private static Dataran raiden;

    /**===============================RAIDEN STUFF=======================================*/

    /**raiden*/
    private static void teleportasi(Dataran V){ // safe
        raiden = V;
        raiden.printTinggi();
    }

    /**raiden*/
    private static void gerak(String D, int S){ // safe
        if (D.equals("KIRI")){
            while (raiden.prev != null && S != 0){
                raiden = raiden.prev;
                S--;
            }
        } else{
            while (raiden.next != null && S != 0){
                raiden = raiden.next;
                S--;
            }
        }
        raiden.printTinggi();
    }

    /**raiden*/
    private static void tebas(String D, int S){
        Dataran check = raiden;
        Pulau pulau = raiden.pulau;
        long tinggi = -1;
        if (D.equals("KIRI")){
            while ((check.iprev != null && S!=0) || (pulau.prev != null && S!=0)){
                if (check.iprev == null){
                    if (tinggi == -1) tinggi = pulau.getTinggi(raiden.node);
                    pulau = pulau.prev;
                    Node node = pulau.tree.search(tinggi);
                    if (node != null && node.tail != null) {
                        check = node.tail;
                        S--;
                    }
                } else{
                    check = check.iprev;
                    S--;
                }
            }
        } else{
            while ((check.inext != null && S!=0) || (pulau.next != null && S!=0)){
                if (check.inext == null){
                    if (tinggi == -1) tinggi = pulau.getTinggi(raiden.node);
                    pulau = pulau.next;
                    Node node = pulau.tree.search(tinggi);
                    if (node != null && node.head != null){
                        check = node.head;
                        S--;
                    }
                } else{
                    check = check.inext;
                    S--;
                }
            }
        }
        if (raiden.equals(check)) {
            out.println(0);
        }
        else {
            raiden = check;
            if (D.equals("KIRI")) raiden.next.printTinggi();
            else raiden.prev.printTinggi();
        }
    }

    /**raiden*/
    private static void stabilize(){
        if (raiden.kuil) out.println(0);
        else{
            Dataran prev;
            if (raiden.prev.node.tinggi < raiden.node.tinggi) prev = raiden.prev;
            else prev = raiden;

            Dataran baru = new Dataran();
            prev.pulau.stabilize(prev, baru, prev.node.tinggi);

            baru.prev = raiden;
            if (raiden.next != null) raiden.next.prev = baru;
            baru.next = raiden.next;
            raiden.next = baru;
        }
    }

    /**raiden*/
    private static void crumble(){
        if (raiden.kuil) out.println(0);
        else{
            Dataran temp = raiden;
            raiden.pulau.crumble(raiden, raiden.node.tinggi);

            raiden = raiden.prev;
            raiden.next = temp.next;
            if (temp.next != null) temp.next.prev = raiden;
        }
    }


    /**===============================DATARAN UY=======================================*/

    static class Dataran{
        Node node;
        Pulau pulau;
        boolean kuil = false;

        Dataran prev;
        Dataran next;

        Dataran iprev;
        Dataran inext;
        Dataran(){}

        /**dataran*/
        void printTinggi(){
            pulau.tree.printTinggi(node);
        }
    }

    /**===============================DARI PULAU KE PULAU=======================================*/


    static class Pulau{
        Tree tree = new Tree();

        int sum;

        Pulau prev;
        Pulau next;

        Dataran head;
        Dataran tail;

        Pulau(){}

        long getTinggi(Node node){
            return tree.getTinggi(node);
        }

        /**pulau*/
        void add(Dataran d, long tinggi){
            d.pulau = this;
            if (head == null) {
                d.kuil = true;
                head = d;
            }
            else {
                tail.next = d;
                d.prev = tail;
            } tail = d;

            sum++;
            tree.add(d, tinggi);
        }

        /**pulau*/
        public void unifikasi(Pulau V){ //safe
            Pulau current = this;
            int total = sum + V.sum;
            while (current.next != null){
                current = current.next;
                total += current.sum;
            }
            current.tail.next = V.head;
            V.head.prev = current.tail;
            current.next = V;
            V.prev = current;

            //tree
            current = V;

            while (current.next != null){
                current = current.next;
                total += current.sum;
            }

            out.println(total);
        }

        /**pulau*/
        public void pisah(){ //safe
            head.prev.next = null;
            head.prev = null;

            Pulau current = prev;
            int kiri = current.sum;
            while(current.prev != null){
                current = current.prev;
                kiri += current.sum;
            }

            int kanan = sum;
            if (next != null){
                current = next;
                kanan += next.sum;
                while(current.next != null){
                    current = current.next;
                    kanan += current.sum;
                }
            }

            prev.next = null;
            prev = null;

            out.println(kiri + " " + kanan);
        }

        /**pulau*/
        public void crumble(Dataran d, long tinggi){
            if (d.equals(tail)) tail = d.prev;
            sum--;
            tree.crumble(d, tinggi);
        }

        /**pulau*/
        public void stabilize(Dataran prev, Dataran d, long tinggi){
            if (raiden.equals(tail)) tail = d;
            sum++;
            d.pulau = this;
            tree.stabilize(prev, d, tinggi);
        }

        /**pulau*/
        public void quake(long H, long X){
            int sum = tree.quake(H, X);
            Pulau current = this;
            while (current.next != null){
                current = current.next;
                sum += current.tree.quake(H, X);
            }
            out.println(sum);
        }

        /**pulau*/
        public void rise(int H, int X){
            int sum = tree.rise(H, X);
            Pulau current = this;
            while (current.next != null){
                current = current.next;
                sum += current.tree.rise(H, X);
            }
            out.println(sum);
        }

        /**pulau*/
        public void sweeping(long L){
            int sum = tree.sweeping(L);
            Pulau current = this;
            while (current.next != null){
                current = current.next;
                sum += current.tree.sweeping(L);
            }
            out.println(sum);
        }

    }

    /**===============================LEAF. JUST LEAF=======================================*/


    static class Node{
        long relatif;
        long tinggi; //awal

        Node left;
        Node right;

        int sum;
        int sumLeft = 0;
        int sumRight = 0;

        Dataran head;
        Dataran tail;

        Node(Dataran dataran, long tinggi, long relatif){
            dataran.node = this;
            head = dataran;
            tail = dataran;
            this.tinggi = tinggi;
            this.relatif = relatif;
            sum = 1;
        }

        /**node*/
        void add(Dataran d){
            d.node = this;
            if (head == null){
                head = d;
            } else{
                tail.inext = d;
                d.iprev = tail;
            }
            tail = d;
            sum++;
        }

        /**node*/
        void add(Dataran prev, Dataran d){
            d.node = this;
            d.iprev = prev;
            if (prev.inext != null){
                prev.inext.iprev = d;
            } else tail = d;
            d.inext = prev.inext;
            prev.inext = d;
            sum++;
        }

        /**node*/
        void remove(Dataran d){
            if (sum == 1){
                head = null;
                tail = null;
            } else if (head.equals(d)){
                head = head.inext;
                head.iprev = null;
            } else{
                if (tail.equals(d)){
                    tail = tail.iprev;
                    tail.inext = null;
                } else{
                    d.iprev.inext = d.inext;
                    d.inext.iprev = d.iprev;
                }
            }
            sum--;
        }
    }

    /**===============================LA TREE FOR THA LEAVES=======================================*/


    static class Tree{
        Node root;

        Tree(){}

        /**tree*/
        Node search(long tinggi){
            return recSearch(root, tinggi);
        }

        /**tree*/
        Node recSearch(Node root, long relatif){
            if (root == null) return null;
            relatif -= root.relatif;
            if (relatif == 0) return root;
            if (relatif > 0) return recSearch(root.right, relatif);
            else return recSearch(root.left, relatif);
        }

        /**tree*/
        long getTinggi(Node node){
            return recTinggi(root, node.tinggi, 0);
        }

        /**tree*/
        void printTinggi(Node node){
            long tinggi = recTinggi(root, node.tinggi, 0);
            out.println(tinggi);
        }

        /**tree*/
        long recTinggi(Node root, long tinggi, long relatif){
            relatif += root.relatif;
            if (root.tinggi == tinggi) return relatif;
            if (root.tinggi < tinggi) return recTinggi(root.right, tinggi, relatif);
            return recTinggi(root.left, tinggi, relatif);
        }

        /**tree*/
        void add(Dataran d, long tinggi){
            root = insert(root, d, tinggi, tinggi);
        }

        /**tree*/
        Node insert(Node root, Dataran d, long tinggi, long relatif){
            if (root == null) return new Node(d, tinggi, relatif);
            long r = relatif - root.relatif;
            if (r == 0) root.add(d);
            else if (r > 0) {
                root.sumRight++;
                root.right = insert(root.right, d, tinggi, r);
            }
            else {
                root.sumLeft++;
                root.left = insert(root.left, d, tinggi, r);
            }
            return root;
        }

        /**tree*/
        void stabilize(Dataran prev, Dataran d, long tinggi){
            //connect linkedlist in stabilize method outside tree
            recStab(root, prev, d, tinggi, 0);
        }

        /**tree*/
        void recStab(Node root, Dataran prev, Dataran d, long tinggi, long relatif){
            relatif += root.relatif;
            if (root.tinggi == tinggi) {
                root.add(prev, d);
                out.println(relatif);
            }
            else if (root.tinggi < tinggi){
                root.sumRight++;
                recStab(root.right, prev, d, tinggi, relatif);
            } else{
                root.sumLeft++;
                recStab(root.left, prev, d, tinggi, relatif);
            }
        }

        /**tree*/
        void crumble(Dataran d, long tinggi){
            recCrumb(root, d, tinggi, 0);
        }

        /**tree*/
        void recCrumb(Node root, Dataran d, long tinggi, long relatif){
            relatif += root.relatif;
            if (root.tinggi == tinggi) {
                root.remove(d);
                out.println(relatif);
            }
            else if (root.tinggi < tinggi){
                root.sumRight--;
                recCrumb(root.right, d, tinggi, relatif);
            } else{
                root.sumLeft--;
                recCrumb(root.left, d, tinggi, relatif);
            }
        }

        /**tree*/
        int quake(long H, long X){
            return recQuake(root, false, 0, H, X);
        }

        /**tree*/
        int recQuake(Node root, boolean found, long relatif, long H, long X){
            if (root == null) return 0;
            relatif += root.relatif;
            if (relatif == H){
                if (found) root.relatif += X;
                if (root.left != null) root.left.relatif -= X;
                return root.sumLeft;
            } else if (relatif > H){
                if (found) root.relatif += X;
                return recQuake(root.left, false, relatif, H, X);
            } else{
                if (!found) root.relatif -= X;
                return root.sum + root.sumLeft + recQuake(root.right, true, relatif, H, X);
            }
        }

        /**tree*/
        int rise(long H, long X){
            return recRise(root, false, 0, H, X);
        }

        /**tree*/
        int recRise(Node root, boolean found, long relatif, long H, long X){
            if (root == null) return 0;
            relatif += root.relatif;
            if (relatif == H){
                if (found) root.relatif -= X;
                if (root.right != null) root.right.relatif += X;
                return root.sumRight;
            } else if (relatif < H){
                if (found) root.relatif -= X;
                return recRise(root.right, false, relatif, H, X);
            } else{
                if (!found) root.relatif += X;
                return root.sum + root.sumRight + recRise(root.left, true, relatif, H, X);
            }
        }

        /**tree*/
        int sweeping(long L){
            return recSweep(root, 0, L);
        }

        /**tree*/
        int recSweep(Node root, long relatif, long L){
            if (root == null) return 0;
            relatif += root.relatif;
            if (relatif == L){
                return root.sumLeft;
            } else if (relatif > L){
                return recSweep(root.left, relatif, L);
            } else{
                return root.sum + root.sumLeft + recSweep(root.right, relatif, L);
            }
        }
    }

    /**===============================RUN MADAM RUN=======================================*/

    public static void main(String args[]) throws IOException {

        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N = in.nextInt();
        for (int i = 0; i < N; i++){
            String name = in.next();
            int many = in.nextInt();
            int tinggi = in.nextInt();
            Pulau pulau = new Pulau();
            Dataran d = new Dataran();
            temples.put(name, d);
            pulau.add(d, tinggi);

            for (int j = 1; j < many; j++){
                pulau.add(new Dataran(), in.nextInt());
            }
        }

        String K = in.next();
        int index = in.nextInt();

        raiden = temples.get(K);
        for (int i = 1; i < index; i++) raiden = raiden.next;

        int Q = in.nextInt();
        for (int i = 0; i < Q; i++){
            String cmd = in.next();
            switch (cmd) {
                case "UNIFIKASI": {
                    Pulau U = temples.get(in.next()).pulau;
                    Pulau V = temples.get(in.next()).pulau;
                    U.unifikasi(V);
                    break;
                }
                case "PISAH": {
                    Pulau U = temples.get(in.next()).pulau;
                    U.pisah();
                    break;
                }
                case "GERAK": {
                    String D = in.next();
                    int S = in.nextInt();
                    gerak(D, S);
                    break;
                }
                case "TEBAS": {
                    String D = in.next();
                    int S = in.nextInt();
                    tebas(D, S);
                    break;
                }
                case "TELEPORTASI": {
                    Dataran V = temples.get(in.next());
                    teleportasi(V);
                    break;
                }
                case "RISE": {
                    Pulau U = temples.get(in.next()).pulau;
                    int H = in.nextInt();
                    int X = in.nextInt();
                    U.rise(H, X);
                    break;
                }
                case "QUAKE": {
                    Pulau U = temples.get(in.next()).pulau;
                    int H = in.nextInt();
                    int X = in.nextInt();
                    U.quake(H, X);
                    break;
                }
                case "CRUMBLE": {
                    crumble();
                    break;
                }
                case "STABILIZE": {
                    stabilize();
                    break;
                }
                case "SWEEPING": {
                    Pulau U = temples.get(in.next()).pulau;
                    long L = in.nextLong();
                    U.sweeping(L);
                    break;
                }
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

        public long nextLong() {
            return Long.parseLong(next());
        }
    }
}
