import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

//1 gedung 1 objek
// TODO - class untuk Lantai
class Lantai {
    public String name;
    public Lantai before;
    public Lantai after;

    public Lantai(String name){
        this.name = name;
        this.before = null;
        this.after = null;
    }

    public String getValue(){
        return name;
    }

}


// TODO - class untuk Gedung
class Gedung {
    public String name;
    public Lantai pointer;
    public Lantai head;
    public Lantai tail;

    public Gedung(String name) {
        this.name = name;
        head = new Lantai("head");
        tail = new Lantai("tail");
        pointer = head;

        head.after = tail;
        tail.before = head;
    }

    public void bangun(String input){
        // TODO - handle BANGUN
        Lantai floor = new Lantai(input);
        floor.before = pointer;
        floor.after = pointer.after;
        pointer.after.before = pointer.after = floor;
        pointer = floor;
    }

    public String lift(String input){
        // TODO - handle LIFT

        //pointer.after == tail
        //pointer.before == head
        if (pointer.after.equals(tail) && pointer.before.equals(head)) {
            return pointer.getValue();
        }
        else if (input.equals("ATAS")){
            if (pointer.after.equals(tail)) return pointer.getValue();
            else{
                pointer = pointer.after;
                return pointer.getValue();
            }
        } else{
            if (pointer.before.equals(head)) return pointer.getValue();
            else{
                pointer = pointer.before;
                return pointer.getValue();
            }
        }

    }

    public String hancurkan(){
        // TODO - handle HANCURKAN
        Lantai destroyed = pointer;
        pointer.after.before = pointer.before;
        pointer.before.after = pointer.after;
        if (pointer.after.equals(tail) && pointer.before.equals(head)) pointer = head;
        else if (pointer.before == head) pointer = pointer.after;
        else pointer = pointer.before;
        return destroyed.getValue();
    }

    public void timpa(Gedung input){
        // TODO - handle TIMPA
        if (input.tail.before.equals(input.head)) return;
        tail.before.after = input.head.after;
        input.head.after.before = tail.before;
        tail.before = input.tail.before;
        input.tail.before.after = tail;
    }

    public String sketsa(){
        // TODO - handle SKETSA
        StringBuilder s = new StringBuilder();
        Lantai current = head;
        if (current.after.equals(tail)) return "";
        while (!current.after.equals(tail)){
            current = current.after;
            s.append(current.getValue());
        }
        return s.toString();
    }


}

public class test {
    private static InputReader in;
    public static PrintWriter out;

    //hashmaps for buildings. Key: name
    public static HashMap<String, Gedung> buildings = new HashMap<>();

    public static void main(String[] args) throws IOException {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // N operations
        int N = in.nextInt();
        String cmd;

        // TODO - handle inputs
        for (int zz = 0; zz < N; zz++) {

            cmd = in.next();


            if(cmd.equals("FONDASI")){
                String A = in.next();
                buildings.put(A, new Gedung(A));

            }
            else if(cmd.equals("BANGUN")){
                String A = in.next();
                String X = in.next();
                // TODO
                buildings.get(A).bangun(X);

            }
            else if(cmd.equals("LIFT")){
                String A = in.next();
                String X = in.next();
                // TODO
                out.println(buildings.get(A).lift(X));

            }
            else if(cmd.equals("SKETSA")){
                String A = in.next();
                // TODO
                out.println(buildings.get(A).sketsa());

            }
            else if(cmd.equals("TIMPA")){
                String A = in.next();
                String B = in.next();
                // TODO
                buildings.get(A).timpa(buildings.get(B));

            }
            else if(cmd.equals("HANCURKAN")){
                String A = in.next();
                // TODO
                out.println(buildings.get(A).hancurkan());
            }
        }

        // don't forget to close/flush the output
        out.close();
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