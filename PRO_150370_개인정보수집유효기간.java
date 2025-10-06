/*
 * 1 ~ n번의 개인정보
 * 약관 종류는 여러 가지가 있으며, 각 약관마다 유효기간이 정해져 있음
 * 수집된 개인정보는 유효기간 전까지만 보관 가능하며, 지났다면 반드시 파기
 * 모든 달은 28일까지 존재
 * 파기해야 할 개인정보의 번호를 오름차순으로 구하기
 
 * terms :  약관 종류(A ~ Z), 유효 기간(1 ~ 100)
 
 * 1. 약관 종류, 기간 배열 저장
 * 2. 약관 종류에 따라 만료 기간 계산
 * 3. 오늘 날짜 이후면 삭제 리스트 추가
 */

import java.util.*;

class Solution {
    public int[] solution(String today, String[] terms, String[] privacies) {        
        
        // 약관 정보 저장
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < terms.length; i++) {
            String[] term = terms[i].split(" ");
            map.put(term[0], Integer.parseInt(term[1]));
        }
        
        
        // 만료 확인
        List<Integer> list = new ArrayList<>();
        
        int td = getSum(today);
        for (int i = 0; i < privacies.length; i++) {
            String[] privacy = privacies[i].split(" ");
            int sd = getSum(privacy[0]);
            
            int period = map.get(privacy[1]);
            
            sd += period * 28;
            
            // 날짜 비교
            if (td - sd >= 0) {
                list.add(i + 1);
            } 
        }
        
        
        int[] answer = new int[list.size()];
        for(int i = 0; i < list.size(); i++) {
            answer[i] = list.get(i);
        }
        
        return answer;
    }
    
    // String to Sum of Day
    public int getSum (String privacy) {
        String[] input = privacy.split("\\.");
        
        int year = Integer.parseInt(input[0]);
        int month = Integer.parseInt(input[1]);
        int day = Integer.parseInt(input[2]);
        return year * 12 * 28 + month * 28 + day;
    }
    
    
    
}
