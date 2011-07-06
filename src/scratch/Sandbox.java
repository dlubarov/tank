package scratch;

public class Sandbox {
    void chunkyEdgeSet(int[] dests, long[] metas) {
        int len = dests.length;
        int desiredChunkSize = Math.max(1000, (int) Math.sqrt(len));
        int numChunks = (len + desiredChunkSize - 1) / desiredChunkSize;
        int[][] chunkyDests = new int[10][];
    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 5; ++i) {
            int c;
            do {
                c = System.in.read();
            } while (c != -1);
            System.in.close();
        }
    }
}
