package test;
import java.util.*;

public class MinCostWordGraph {

    static class WordCost {
        String word;
        int cost;

        WordCost(String word, int cost) {
            this.word = word;
            this.cost = cost;
        }
    }

    static class Edge {
        int to;
        int cost;

        Edge(int to, int cost) {
            this.to = to;
            this.cost = cost;
        }
    }

    public static int minCostToFormWord(String target, List<WordCost> subwordsWithCost) {
        int n = target.length();
        
        // Build the graph
        Map<Integer, List<Edge>> graph = new HashMap<>();
        for (int i = 0; i <= n; i++) {
            graph.put(i, new ArrayList<>());
        }

        for (int i = 0; i < n; i++) {
            for (WordCost wc : subwordsWithCost) {
                String word = wc.word;
                int len = word.length();
                if (i + len <= n && target.substring(i, i + len).equals(word)) {
                    graph.get(i).add(new Edge(i + len, wc.cost));
                }
            }
        }

        // Dijkstra's Algorithm
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        pq.offer(new int[]{0, 0}); // {position, cost}
        int[] dist = new int[n + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[0] = 0;

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int pos = current[0];
            int costSoFar = current[1];

            if (pos == n) return costSoFar;

            for (Edge edge : graph.get(pos)) {
                if (costSoFar + edge.cost < dist[edge.to]) {
                    dist[edge.to] = costSoFar + edge.cost;
                    pq.offer(new int[]{edge.to, dist[edge.to]});
                }
            }
        }

        return -1;
    }

    public static void main(String[] args) {
        List<WordCost> subwords1 = List.of(
            new WordCost("a", 1),
            new WordCost("p", 2),
            new WordCost("pl", 3),
            new WordCost("e", 1)
        );

        String target1 = "apple";
        
        String target  = "lockdown";
        List<WordCost> subwords = List.of(
            new WordCost("lock", 50),
            new WordCost("down", 50),
            new WordCost("lo", 5),
            new WordCost("ck", 5)
        );
        int result = minCostToFormWord(target, subwords);
        System.out.println("Minimum cost to form '" + target + "': " + result);
    }
}
