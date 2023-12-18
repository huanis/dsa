import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

public class test {
    private static InputReader in;
    private static PrintWriter out;

    private static Node[] nodes = new Node[400005];
    private static Node root;
    private static int last;

    static class Node{
        int height;
        int index; //index of land
        int pos; //index of node

        Node(int height, int index){
            this.height = height;
            this.index = index;
        }
    }

    public static void add(Node node){
        node.pos = last;
        nodes[last] = node;
        int pos = last;
        while (pos != 0){
            if (pos%2 == 1) pos = pos/2;
            else pos = (pos/2) - 1;

            if (nodes[pos].height > node.height){
                swap(nodes[pos], node);
            } else break;
        }

        if (node.pos == 0) root = node;

        last++;
    }

    public static void deHeight(Node node, int y){
        int pos = node.pos;
        node.height = y;

        while (pos != 0){
            if (pos%2 == 1) pos = pos/2;
            else pos = pos/2 - 1;

            if (nodes[pos].height == node.height && node.index < nodes[pos].height){
                swap(nodes[pos], node);
            }
            else if (nodes[pos].height > node.height){
                swap(nodes[pos], node);
            } else break;
        }


        if (nodes[1] != null && nodes[0].height == nodes[1].height && nodes[0].index > nodes[1].index){
            swap(nodes[0], nodes[1]);
        }
        if (nodes[2] != null && nodes[0].height == nodes[2].height && nodes[0].index > nodes[2].index){
            swap(nodes[0], nodes[2]);
        }
        root = nodes[0];
    }

    public static void inHeight(Node node, int y){
        int pos = node.pos;
        node.height = y;

        Node left;
        Node right;
        int sub;

        while (pos < last/2){
            left = nodes[pos*2 + 1];
            right = nodes[pos*2 + 2];

            if (left == null && right == null) break;
            else if (left == null){
                if (right.height < y || (right.height == y && node.index > right.index)) swap(node, right);
                else break;
            } else if (right == null){
                if (left.height < y || (left.height == y && node.index > left.index)) swap(node, left);
                else break;
            } else{
                sub = right.height - left.height;
                if (sub == 0){
                    if (node.height == right.height){
                        if (right.index > left.index && node.index > left.index){
                            swap(node, left);
                        } else if (right.index < node.index) swap(node, right);
                        else break;
                    } else if (node.height > right.height){
                        if (right.index > left.index){
                            swap(node, left);
                        } else swap(node, right);
                    } else break;

                }
                else if (sub < 0){
                    if (right.height < y || (right.height == y && node.index > right.index)) swap(node, right);
                    else break;
                } else{
                    if (left.height < y || (left.height == y && node.index > left.index)) swap(node, left);
                    else break;
                }
            }
            pos = node.pos;
        }
        if (nodes[1] != null && nodes[0].height == nodes[1].height && nodes[0].index > nodes[1].index){
            swap(nodes[0], nodes[1]);
        }
        if (nodes[2] != null && nodes[0].height == nodes[2].height && nodes[0].index > nodes[2].index){
            swap(nodes[0], nodes[2]);
        }
        root = nodes[0];
    }

    public static void swap(Node root, Node node){
        int pos = root.pos;
        nodes[pos] = node;
        nodes[node.pos] = root;
        root.pos = node.pos;
        node.pos = pos;
    }

    static class Pointer{
        Node node;

        Pointer(Node node){
            this.node = node;
        }
    }

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        List<Pointer> tanah = new ArrayList<>();

        int N = in.nextInt();
        for (int i = 0; i < N; i++) {
            int height = in.nextInt();
            Node temp = new Node(height, tanah.size());
            tanah.add(new Pointer(temp));
            add(temp);
        }

        int Q = in.nextInt();
        while(Q-- > 0) {
            String query = in.next();
            if (query.equals("A")) { //safe
                // TODO: Handle query A
                int height = in.nextInt();
                Node temp = new Node(height, tanah.size());
                tanah.add(new Pointer(temp));
                add(temp);

                /**cek add node:
                 int n = 1;
                 int j = n;
                 for (Node node : nodes){
                    j--;
                    if (node == null) break;
                    if (j == 0) {
                        System.out.println(node.height);
                        n *=2;
                        j = n;
                    } else System.out.print(node.height + " ");
                 }
                 System.out.println();
                 */
            } else if (query.equals("U")) {
                // TODO: Handle query U
                int x = in.nextInt();
                int y = in.nextInt();

                Node node = tanah.get(x).node;
                if (y < node.height) {
                    deHeight(node, y);
                }
                else if (y > node.height) inHeight(node, y);

                /**
                int n = 1;
                int j = n;
                for (Node i : nodes){
                    j--;
                    if (i == null) break;
                    if (j == 0) {
                        System.out.println(i.height);
                        n *=2;
                        j = n;
                    } else System.out.print(i.height + " ");
                }
                System.out.println();
                System.out.println(root.index + " root");
                System.out.println();
                 */


            } else {
                // TODO: Handle query R
                int i = root.index;
                Node node = root;
                // TODO: Conditionals
                if (tanah.size() > 1){
                    if (i == 0){
                        Node next = tanah.get(i+1).node;
                        inHeight(node, next.height);
                    } else if (i == tanah.size()-1){
                        Node prev = tanah.get(i-1).node;
                        if (prev.height == node.height){
                            i = i-1;
                        } else inHeight(node, prev.height);
                    } else {
                        Node prev = tanah.get(i-1).node;
                        Node next = tanah.get(i+1).node;
                        if (prev.height == node.height){
                            i = i-1;
                        }
                        if (prev.height > next.height){
                            inHeight(node, prev.height);
                            inHeight(next, prev.height);
                        } else{
                            inHeight(node, next.height);
                            inHeight(prev, next.height);
                        }
                    }
                }

                //System.out.println(nodes[0].index + " " + nodes[1].index + " " + nodes[2].index);
                /**
                int n = 1;
                int j = n;
                for (Node k : nodes){
                    j--;
                    if (k == null) break;
                    if (j == 0) {
                        System.out.println(k.height);
                        n *=2;
                        j = n;
                    } else System.out.print(k.height + " ");
                }
                System.out.println();
                System.out.println();
                */
                out.println(node.height + " " + i);
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