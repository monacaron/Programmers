/*
 * 단품을 조합해서 코스요리 형태로 재구성
 * 이전에 각 손님들이 가장 많이 함께 주문한 단품메뉴들을 코스요리 메뉴로 구성
 * 최소 2가지 이상의 단품메뉴 선택
 * 최소 2명 이상의 손님으로부터 주문된 조합만 후보에 포함
 * 단품 메뉴 : A ~ Z
 * 
 * 각 손님이 주문한 단품메뉴들에서 2개 이상 골라서 조합 만들기
 * 완성된 조합 해시맵에 저장
 * 이미 존재하는 조합의 경우 밸류값 증가
 */

import java.util.*;

class Solution {
    static Map<String, Integer> hm;
    public String[] solution(String[] orders, int[] course) {
        hm = new HashMap<>();
        
        for (String order : orders) {
            char[] arr = order.toCharArray();
            Arrays.sort(arr);
            comb("", 0, 0, course, arr);    
            System.out.println();
        }
        
        Queue<String> queue = new ArrayDeque();
        List<String> list = new ArrayList<>();        
        for (int cnt : course) {
            int max = 2;
            for (String key : hm.keySet()) {
                if (key.length() == cnt) {
                    if (max < hm.get(key)) {
                        queue.clear();
                        queue.offer(key);
                        max = hm.get(key);
                    } else if (max == hm.get(key)) {
                        queue.offer(key);
                    }
                }
            }
            
            while (!queue.isEmpty()) {
                list.add(queue.poll());
            }
        }
        
        String[] ans = list.toArray(new String[list.size()]);
        Arrays.sort(ans);
        return ans;
    }
    
    // 현재 고른 코스 조합, 단품메뉴 인덱스, 코스요리 가짓수 인덱스, 코스요리 가짓수, 단품메뉴
    public void comb(String str, int midx, int cidx, int[] course, char[] order) {
        if (cidx == course.length) {
            return;
        }
        
        if (str.length() == course[cidx]) {
            hm.put(str, hm.getOrDefault(str, 0) + 1);
            cidx++;
        }
        
        for (int i = midx; i < order.length; i++) {
            comb(str + order[i], i + 1, cidx, course, order);
        }   
       
    }
}
