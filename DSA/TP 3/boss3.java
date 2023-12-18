import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

/**TEMAN DISKUSI: UMAR IZZUDIN, TAUFIQUL MAWARID, GITAN SAHL TAZAKHA WIJAYA,
 *                DAN IGNATIUS HENRIYANTO
 * DISCLAIMERS:
 * - IDE SIMULASI DIAJARKAN KE SAYA OLEH GITAN SAHL TAZAKHA WIJAYA
 * - PEMBUATAN KONSEP TIAP QUERY DIDISKUSIKAN BERSAMA TEMAN-TEMAN DISKUSI
 * - SAYA MENYERAH PERIHAL NETWORKING                                      */
/**==========================DOCUMENT INDEX==================================
 * 1. MAIN METHODS
 *    - tambah()
 *    - resign()
 *    - carry()
 *    - boss()
 *    - sebar()
 *    - simulasi()
 *    - networking()
 * 2. GRAPH
 *    - init()
 *    - add()
 * 3. NODE
 *    - init()
 *    - swap()
 *    - addFriend()
 *    - resign()
 *    - goUp()
 *    - goDown()
 * 4. RANK
 *    - init()
 *    - add()
 *    - remove()
 * 5. MAIN
 * 6. CLASS: INPUTREADER
 * ===========================================================================
 * */

public class boss3 {
    private static InputReader in;
    private static PrintWriter out;

    private static int N = 0;
    private static int total = 0;
    private static Node[] nodes;
    private static Rank[] ranks;

    /**================================MAIN METHODS=======================================*/

    /**main*/
    static void tambah(Node U, Node V){
        U.addFriend(V);
        V.addFriend(U);
    }

    /**main*/
    static void carry(Node U){
        if (U.higher.size() != 0) {
            out.println(U.higher.get(0).rank.value);
            //out.println(U.adjacencyList.get(1).rank.value);
            //out.println(U.adjacencyList.get(2).rank.value);
        } else if (U.lower.size() != 0) out.println(U.lower.get(0).rank.value);
        else out.println(0);
    }

    /**main*/
    static void boss(Node U){
        //heap lesgo
        //sebenernya ga make heap bisa sih tapi males ganti
        Graph temp = U.graph;
        if (temp == null){
            temp = new Graph();
            U.graph = temp;
            temp.add(U);
            Node current = U;
            Queue<Node> queue = new LinkedList<>();
            queue.add(current);

            while (!queue.isEmpty()){
                current = queue.poll();
                for (Node node : current.lower){
                    if (node.graph == null) {
                        queue.add(node);
                        node.graph = temp;
                        temp.add(node);
                    }
                }
                for (Node node : current.higher){
                    if (node.graph == null) {
                        queue.add(node);
                        node.graph = temp;
                        temp.add(node);
                    }
                }
            }
        }
        ArrayList<Node> heap = temp.heap;
        if (heap.size() == 1) out.println(0);
        else if (heap.size() == 2) {
            if (heap.get(0).equals(U)) out.println(heap.get(1).rank.value);
            else out.println(heap.get(0).rank.value);
        } else {
            if (!heap.get(0).equals(U)) {
                out.println(heap.get(0).rank.value);
            }
            else {
                out.println(Math.max(heap.get(1).rank.value, heap.get(2).rank.value));
            }
        }

        /**
        if (U.graph == null) out.println(0);
        else {
            if (U.graph.max1 > U.rank.value) out.println(U.graph.max1);
            else out.println(U.graph.max2);
        }*/
    }

    /**main*/
    static void sebar(Node U, Node V){
        //bfs+dijkstra, ide dari Lab7
        Queue<Node> queue = new LinkedList<>();
        int[] map = new int[N+1];
        boolean[] check = new boolean[N+1];
        boolean[] ranked = new boolean[N+1];

        queue.add(U);
        check[U.index] = true;

        Node current;
        boolean found = false;
        while(!queue.isEmpty() && !found){
            current = queue.poll();
            //System.out.println(current.index + " path: " + map[current.index]);
            check[current.index] = true;
            Node temp = current.rank.head;
            while (temp != null && !ranked[temp.rank.value]){
                //System.out.println(temp.index + " rank path: " + map[temp.index]);
                if (!current.equals(temp)){
                    if (!check[temp.index]){
                        queue.add(temp);
                        check[temp.index] = true;
                        if (current.equals(U)) map[temp.index] = map[current.index];
                        else map[temp.index] = map[current.index] + 1;
                    }
                }

                if (temp.equals(V)) {
                    found = true;
                    break;
                }
                temp = temp.next;
            }
            ranked[current.rank.value] = true;

            for (Node node : current.lower){
                if (!check[node.index]){
                    queue.add(node);
                    check[node.index] = true;
                    if (!current.equals(U)){
                        map[node.index] = map[current.index] + 1;
                    }
                    if (node.equals(V)) {
                        found = true;
                        break;
                    }
                }
            }

            for (Node node : current.higher){
                if (!check[node.index]){
                    queue.add(node);
                    check[node.index] = true;
                    if (!current.equals(U)){
                        map[node.index] = map[current.index] + 1;
                    }
                    if (node.equals(V)) {
                        found = true;
                        break;
                    }
                }
            }
        }
        if (!check[V.index]) out.println(-1);
        else out.println(map[V.index]);
    }

    /**main*/
    static void simulasi(){
        //diajarin Gitan
        out.println(total);
    }

    /**main*/
    static void networking(){}

    /**==============================GRAPH DEMI BOSS======================================*/

    static class Graph{
        ArrayList<Node> heap = new ArrayList<>();

        Graph(){}

        void swap(Node root, Node node, int rootPos, int pos){
            heap.set(rootPos, node);
            heap.set(pos, root);
        }

        void add(Node node){
            heap.add(node);
            int pos = heap.size()-1;
            int temp;
            Node root;
            while (pos > 0){
                if (pos%2 == 1) temp = pos/2;
                else temp = (pos/2) - 1;

                root = heap.get(temp);
                if (root.rank.value < node.rank.value){
                    swap(root, node, temp, pos);
                } else break;

                pos = temp;
            }
        }
    }

    /**==============================NODES : WORKERS======================================*/

    static class Node{
        int index;
        Rank rank;
        Graph graph;

        //for rank
        Node prev;
        Node next;
        int sum = 0;

        ArrayList<Node> higher = new ArrayList<>();
        ArrayList<Node> lower = new ArrayList<>();

        /**Node*/
        Node(int index, Rank rank){
            this.index = index;
            this.rank = rank;
            rank.add(this);
        }

        void swap(Node root, Node node, int rootPos, int pos, ArrayList<Node> list){
            list.set(rootPos, node);
            list.set(pos, root);
        }

        /**Node*/
        void addFriend(Node node){
            ArrayList<Node> list;
            if (node.rank.value < rank.value) {
                list = lower;
                if (node.higher.size() == 0 || (node.higher.size() == 1 && node.higher.get(0).equals(this))){
                    total--;
                }
            }
            else list = higher;
            list.add(node);

            //heap
            int pos = list.size()-1;
            int temp;
            Node root;
            while (pos != 0){
                if (pos%2 == 1) temp = pos/2;
                else temp = (pos/2) - 1;

                root = list.get(temp);
                if (root.rank.value < node.rank.value){
                    swap(root, node, temp, pos, list);
                } else break;

                pos = temp;
            }
            sum++;
        }

        /**Node*/
        void resign(){
            if (higher.size() == 0) total--;

            rank.remove(this);
            ArrayList<Node> list;
            for (Node node : higher){
                if (node.rank.equals(rank)) list = node.higher;
                else list = node.lower;
                int pos = -1;
                for (int i=0; i<list.size(); i++){
                    if (list.get(i).equals(this)){
                        list.set(i, list.get(list.size()-1));
                        list.remove(list.size()-1);
                        pos = i;
                        break;
                    }
                }

                if (pos != list.size() && pos != -1){
                    goUp(list.get(pos), pos, list);
                    goDown(list.get(pos), pos, list);
                }


                node.sum--;
            }

            for (Node node : lower){
                list = node.higher;
                int pos = -1;
                for (int i=0; i<list.size(); i++){
                    if (list.get(i).equals(this)){
                        list.set(i, list.get(list.size()-1));
                        list.remove(list.size()-1);
                        pos = i;
                        break;
                    }
                }

                if (pos != list.size() && pos != -1){
                    goUp(list.get(pos), pos, list);
                    goDown(list.get(pos), pos, list);
                }

                if (list.size() == 0) total++;
                node.sum--;
            }
        }

        /**Node*/
        void goUp(Node node, int pos, ArrayList<Node> list){
            int temp;
            Node root;
            while (pos > 0){
                if (pos%2 == 1) temp = pos/2;
                else temp = (pos/2) - 1;

                root = list.get(temp);
                if (root.rank.value < node.rank.value){
                    swap(root, node, temp, pos, list);
                } else break;

                pos = temp;
            }
        }

        /**Node*/
        void goDown(Node node, int pos, ArrayList<Node> list){
            Node left;
            Node right;
            int length = list.size();

            while (pos < (length-1)/2 || (pos == 0 && length > 1)){
                left = list.get(pos*2 + 1);
                if (pos*2 + 2 < length) {
                    right = list.get(pos*2 + 2);
                    if (left.rank.value > right.rank.value){
                        if (node.rank.value < left.rank.value) {
                            swap(left, node, pos*2 + 1, pos, list);
                            pos = pos*2 + 1;
                        } else break;
                    } else{
                        if (node.rank.value < right.rank.value) {
                            swap(right, node, pos*2 + 2, pos, list);
                            pos = pos*2 + 2;
                        } else break;
                    }
                } else{
                    if (node.rank.value < left.rank.value) {
                        swap(left, node, pos*2 + 1, pos, list);
                        pos = pos*2 + 1;
                    } else break;
                }
            }
        }
    }

    /**================================WORKER'S RANK======================================*/

    static class Rank{
        /**Inspirasi: TP2 Node tinggi pada Tree (refer to my TP2)*/
        int value;
        Node head;
        Node tail;
        int sum = 0;

        Rank(int rank){
            value = rank;
        }

        /**rank*/
        void add(Node node){
            sum++;
            if (head == null) head = node;
            else{
                tail.next = node;
                node.prev = tail;
            }
            tail = node;
        }

        /**rank*/
        void remove(Node node){
            sum--;
            if (node.equals(head)){
                if (node.next != null){
                    head = node.next;
                    head.prev = null;
                } else{
                    head = null;
                    tail = null;
                }
            } else{
                node.prev.next = node.next;
                if (node.next != null){
                    node.next.prev = node.prev;
                } else tail = node.prev;
            }
        }
    }

    /**===============================RUN MADAM RUN=======================================*/

    public static void main(String args[]) throws IOException {

        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        N = in.nextInt();
        total = N;
        int M = in.nextInt();
        int Q = in.nextInt();

        nodes = new Node[N+1];
        ranks = new Rank[N+1];
        for (int i = 0; i < N; i++){
            int rank = in.nextInt();
            if (ranks[rank] == null){
                ranks[rank] = new Rank(rank);
            } nodes[i+1] = new Node(i+1, ranks[rank]);
        }

        for (int i = 0; i < M; i++){
            tambah(nodes[in.nextInt()], nodes[in.nextInt()]);
        }

        for (int i = 0; i < Q; i++){
            switch(in.nextInt()){
                case 1: {
                    tambah(nodes[in.nextInt()], nodes[in.nextInt()]);

                    /**
                     for (Node node: nodes[9].adjacencyList){
                     System.out.println(node.rank.value);
                     }
                     System.out.println();
                     //*/
                    break;

                } case 2: {
                    nodes[in.nextInt()].resign();

                    /**
                     for (Node node: nodes[1].adjacencyList){
                     System.out.println(node.rank.value);
                     }
                     System.out.println();
                     //*/
                    break;

                } case 3: {
                    carry(nodes[in.nextInt()]);
                    break;
                } case 4: {
                    boss(nodes[in.nextInt()]);
                    break;
                } case 5: {
                    sebar(nodes[in.nextInt()], nodes[in.nextInt()]);
                    break;
                } case 6: {
                    simulasi();
                    break;
                } case 7: {
                    //networking();
                    out.println(0);
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
