/*
 * 노드 : 1 ~ n까지 n개의 지점
 * 간선 : 양방향. 예상 택시 요금
 * 출발 지점에서 A와 B의 집으로 가는 데 필요한 최소 요금 = 합승 + A따로 + B따로
 * 
 * (출발 ~ 합승끝) + (합승끝 ~ A) + (합승끝 ~ B)
 * 다익스트라로 x -> y 최소 비용 구하기
 */

import java.util.*;

class Node {
    int idx, cost;
    
    Node(int idx, int cost) {
        this.idx = idx;
        this.cost = cost;
    }
}

class Solution {
    List<Node>[] graph;
    // 노드 개수, 출발점, A집, B집, 간선 정보
    public int solution(int n, int s, int a, int b, int[][] fares) {
        graph = new ArrayList[n + 1]; // 1 ~ n
        for (int i = 1; i <= n; i++) {
            graph[i] = new ArrayList<>();
        }
        
        for (int[] fare : fares) {
            int x = fare[0];
            int y = fare[1];
            int cost = fare[2];
            
            graph[x].add(new Node(y, cost));
            graph[y].add(new Node(x, cost));
        }
        
        int[][] dist = new int[n + 1][n + 1];
        for (int i = 1; i <= n; i++) {
            dist[i] = dijkstra(i, n);
        }
        
        // 1. 합승 x
        int answer = dist[s][a] + dist[s][b];
        
        // 2. 합승 o
        for (int i = 1; i <= n; i++) {
            answer = Math.min(answer, dist[s][i] + dist[i][a] + dist[i][b]);
        }
        
        // for (int[] d: dist) {
        //     for (int k : d) {
        //         System.out.print(k + " ");
        //     }
        //     System.out.println();
        // }
        // System.out.println();
        
        return answer;
    }
    
    public int[] dijkstra(int start, int n) {
        int[] dist = new int[n + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[start] = 0;
        
        PriorityQueue<Node> pq = new PriorityQueue<>((o1, o2) -> o1.cost - o2.cost);
        pq.offer(new Node(start, 0));
        
        while (!pq.isEmpty()) {
            Node now = pq.poll();
            int idx = now.idx;
            int cost = now.cost;
            
            if (dist[idx] < cost) continue;            

            for (Node next : graph[idx]) {
                int nextIdx = next.idx;
                int nextCost = next.cost + cost;
                
                if (dist[nextIdx] <= nextCost) continue;
                
                dist[nextIdx] = nextCost;
                pq.offer(new Node(nextIdx, nextCost));
            }
        }
        
        return dist;
    }
}
