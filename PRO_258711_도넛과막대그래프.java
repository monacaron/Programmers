/*
 * 도넛, 막대, 8자 모양 그래프
 * 1개 이상의 정점과, 정점들을 연결하는 단방향 간선들로 이루어짐
 * 크기가 n인 도넛 모양 그래프는 n개의 정점, n개의 간선으로 이루어짐
 * 도넛 : 아무 한 정점에서 출발해 이용한 적 없는 간선을 계속 따라가면 모든 정점들을 한 번씩 방문한 뒤 출발 정점으로 복귀 ~ 일방향
 * 크기가 n인 막대 모양 그래프는 n개의 정점과 n - 1개의 간선으로 이루어짐
 * 막대 : 임의의 한 정점에서 출발해 간선을 계속 따라가면 나머지 n - 1개의 정점을 한 번씩 방문하게 되는 정점이 단 하나 존재 ~ 일자형
 * 크기가 n인 8자 모양 그래프는 2n + 1개의 정점과 2n + 2개의 간선 존재
 * 간선 정보를 통해 시작 정점의 번호와 태초 도넛, 막대, 8자 그래프의 수 구하기
 * return {태초 정점의 번호, 도넛, 막대, 8자}
 
 * 1. 태초 정점 : 입력 간선 수 0, 출력 간선 수 >= 2
 * 2. 도넛, 막대, 8자 그래프
 * 막대 : 출력 간선 수 = 0인 정점 존재 = 마지막 정점
 * 8자 : 입력 간선 수 >= 2, 출력 간선 수 2인 정점 존재 = 교차점
 * 도넛 : 입력 간선 수 = 1, 출력 간선 수 1 ... 그 외 그래프
 */

import java.util.*;

class Solution {
    final int size = (int)1e6;
    static int origin, donut, stick, eight;
    public int[] solution(int[][] edges) {
        List<Integer>[] list = new ArrayList[size + 1];
        
        int[] outCount = new int[size + 1]; // 출력 간선 수        
        int[] inCount = new int[size + 1]; // 입력 간선 수
        
        for (int i = 0; i < edges.length; i++) {
            // a -> b
            int a = edges[i][0];
            int b = edges[i][1];
            
            if (list[a] == null) {
                list[a] = new ArrayList<>();
            }

            list[a].add(b);
            outCount[a]++;
            inCount[b]++;
        }

        for (int i = 1; i <= size; i++) {
            if (inCount[i] == 0 && outCount[i] >= 2) { // 태초 정점 : 입력 0, 출력 >= 2
                origin = i;
            }
        }

        for (int next : list[origin]) { // 태초 정점과 연결된 정점이 각 그래프의 시작 정점
            if (outCount[next] == 0) { // 연결된 정점이 막대 그래프의 끝 정점인 경우
                stick++;
                continue;
            }
            
            int v = list[next].get(0); // 다음 정점
            while(true) {
                if (outCount[v] == 0) { // 막대 그래프의 끝 정점
                    stick++;
                    break;
                }
                
                if (outCount[v] == 2) { // 교차점
                    eight++;
                    break;
                }              
                
                if (next == v) { // 8자 그래프가 아니면서 시작점에 도착한 경우 ~ 도넛
                    donut++;
                    break;
                }
                
                v = list[v].get(0);
            }
        }
        
        return new int[]{origin, donut, stick, eight};
    }
  
}
