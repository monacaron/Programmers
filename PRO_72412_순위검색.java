/*
 * 개발 언어 : cpp, java, python
 * 지원 직군 : backend, frontend
 * 경력 구분 : junior, senior
 * 소울 푸드 : chicken, pizza
 * 질문 : 조건을 만족하는 사람 중 코테 점수 x점 이상 받은 사람은 모두 몇 명인가 ?
 
 * 1. 지원서 정보 전처리 : 개발언어 직군 경력 소울푸드 점수
 * 2. 쿼리 조건 확인 : [개발언어 and 직군 and 경력 and 소울푸드] 점수 ... '-'는 아무거나
 */

import java.util.*;

class Solution {
    static Map<String, Integer> keyMap = new HashMap<>();
    static Map<String, List<Integer>> scoreMap = new HashMap<>();
    static int sum;
    public int[] solution(String[] info, String[] query) {
        int[] answer = new int[query.length];
        
        keyMap.put("cpp", 0);
        keyMap.put("java", 1);
        keyMap.put("python", 2);
        keyMap.put("backend", 0);
        keyMap.put("frontend", 1);
        keyMap.put("junior", 0);
        keyMap.put("senior", 1);
        keyMap.put("chicken", 0);
        keyMap.put("pizza", 1);
        
        
        for (int i = 0; i < info.length; i++) {
            applicant(info[i].split(" "));
        }
        
        for (String key : scoreMap.keySet()) {
            Collections.sort(scoreMap.get(key));
        }
        
        for(int i = 0; i < query.length; i++) {
            String[] q1 = query[i].split(" and ");
            String[] q2 = q1[3].split(" ");
            q1[3] = q2[0];
            int score = Integer.parseInt(q2[1]);
            
            sum = 0;
            func(0, "", q1, score);
            answer[i] = sum;
        }
        
        return answer;
    }
    
    public void func(int idx, String key, String[] question, int score) {
        if (idx == 4) {
            List<Integer> list = scoreMap.get(key);
            
            if (list == null) {
                return;
            }
            
//             int left = 0;
//             int right = list.size();
            
//             while (left < right) {
//                 int mid = (left + right) / 2;
                    
//                 if (list.get(mid) >= score) {
//                     right = mid;
//                 } else {
//                     left = mid + 1;
//                 }
//             }
            
            int left = 0;
            int right = list.size() - 1;
            
            while (left <= right) {
                int mid = (left + right) / 2;
                    
                if (list.get(mid) >= score) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
            
            sum += list.size() - left;
            return;
        }
        
        if (question[idx].equals("-")) { // "-"
            func(idx + 1, key + 0, question, score);
            func(idx + 1, key + 1, question, score);
            if (idx == 0) {
                func(idx + 1, key + 2, question, score);
            }
        } else { // 그 외
            func(idx + 1, key + keyMap.get(question[idx]), question, score);
        }
        
    }
    
    public void applicant(String[] info) {
        // 지원 정보 -> key
        String key = "";
        key += keyMap.get(info[0]);
        key += keyMap.get(info[1]);
        key += keyMap.get(info[2]);
        key += keyMap.get(info[3]);
        int score = Integer.parseInt(info[4]);
        
        // System.out.printf("%s %d\n", key, score);
        
        // 해당 key의 점수를 list로 저장
        if (!scoreMap.containsKey(key)) {
            List<Integer> list = new ArrayList<>();
            list.add(score);
            scoreMap.put(key, list);
        } else {
            scoreMap.get(key).add(score);
        }
    }
}
