/*
 * 두 사람 사이에 선물을 주고받은 기록이 있다면, 더 많은 선물을 준 사람이 다음 달에 선물을 하나 받음
 * 두 사람이 선물을 주고받은 기록이 하나도 없거나 수가 같다면, 선물 지수가 더 큰 사람이 하나 더 받습니다.
 * 선물 지수 = 이번 달까지 자신이 친구들에게 준 선물의 수 - 받은 선물의 수
 * 두 사람의 선물 지수도 같은 경우, 다음 달에 선물을 주고받지 않습니다.
 * 다음 달에 선물을 가장 많이 받을 친구 구하기
 
 * 1. 이름 -> 번호
 * 2. 번호간 선물 횟수 기록
 * 3. 선물 지수 기록
 * 3. 선물 주고받기
 * 4. 가장 많은 받은 번호 찾기
 */

import java.util.*;

class Solution {
    public int solution(String[] friends, String[] gifts) {
        int answer = 0;
        
        int n = friends.length;
        HashMap<String, Integer> hm = new HashMap<>(); // 이름 : 번호
        for (int i = 1; i <= n; i++) {
            hm.put(friends[i - 1], i);
        }
        
        int[] giftScore = new int[n + 1]; // 선물 지수 = 준 선물 - 받은 선물
        int[][] map = new int[n + 1][n + 1]; // 줌(행), 받음(열)
        
        for (int i = 0; i < gifts.length; i++) {
            String[] input = gifts[i].split(" ");
            int A = nameToIndex(input[0], hm); // A 줌
            int B = nameToIndex(input[1], hm); // B 받음
            
            map[A][B]++;
            giftScore[A]++;
            giftScore[B]--;
        }
        
        int[] result = new int[n + 1]; // 다음 달 받을 선물의 개수
        for (int i = 1; i < n; i++) {
            for (int j = i + 1; j <= n; j++) {                
                // 선물 기록이 있으며 차이가 있는 경우
                if (map[i][j] > map[j][i]) {
                    result[i]++;
                } else if (map[i][j] < map[j][i]) {
                    result[j]++;
                } 
                else { // 선물 기록이 없거나 같은 경우
                    if (giftScore[i] > giftScore[j]) {
                            result[i]++;
                        } else if (giftScore[i] < giftScore[j]) {
                            result[j]++;
                        }
                }
            }
        }
        
        for (int k : result) {
            answer = Math.max(answer, k);
        }
        return answer;
    }
    
    public int nameToIndex(String name, HashMap<String, Integer> hm) {
        return hm.get(name);
    }
}
