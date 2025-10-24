/*
 * 시청자들이 가장 많이 보는 구간에 공익광고 넣기
 * 동영상 재생시간 = 종료 시각 - 시작 시각 ~ 종료 시각은 포함 안됨
 * 동영상 재생시간과 공익광고 재생시간이 주어질 때, 광고가 들어갈 시작 시각 구하기 -> 누적 재생 시간이 가장 높은 곳
 * 여러 개라면 가장 빠른 시각으로 ~
 
 * 1. 문자열 -> 시간
 * 2. A - B - C : A의 끝과 B의 시작, B의 끝과 C의 시작
 */

import java.util.*;

class Solution {
    public String solution(String play_time, String adv_time, String[] logs) {
        String answer = "";
        
        int pt = getTime(play_time); // 전체 길이
        int at = getTime(adv_time); // 광고 길이
        
        int len = pt + 1;
        long[] arr = new long[len]; // 재생 누적 정보
        
        for (String log : logs) {
            String[] split = log.split("-");
            int start = getTime(split[0]);
            int end = getTime(split[1]);
            
            arr[start]++; // 시작
            arr[end]--; // 끝
        }
        
        // 1차 누적합 : 초단위 시청자 ~ arr[i] = i초에 시청중인 시청자 수
        for(int i = 1; i < len; i++) {
            arr[i] += arr[i - 1];
        }
        
        // 2차 누적합 : 0초부터 i초까지 누적 재생 시간 ~ sum(초단위 시청자들 합)
        for(int i = 1; i < len; i++) {
            arr[i] += arr[i - 1];
        }
        
        // 초기 : 0 ~ adv_time까지 누적 재생 시간
        long max = arr[at];
        int start = 0;
        
        for (int i = 1; i <= pt - at; i++) { // 1 ~ 시작 가능한 시간까지
            // arr[i + at - 1] = 0초부터 i + at - 1초까지
            // arr[i - 1] = 0초부터 i - 1초까지
            // cur = i초부터 i + at초까지 (종료 시각 제외)
            // A-1 (여기까지 arr[i - 1]) A ... B - 1 (여기까지 arr[i + at - 1]) B
            long cur = arr[i + at - 1] - arr[i - 1]; // 종료 시각은 카운트 안되므로 -1
            
            if (max < cur) {
                max = cur;
                start = i;
            }
        }
        
        return getString(start);
    }
    
    public int getTime(String str) {
        String[] split = str.split(":");
        
        return 3600 * stoi(split[0]) + 60 * stoi(split[1]) + stoi(split[2]);
    }
    
    public String getString(int time) {
        StringBuilder sb = new StringBuilder();
        
        sb.append(String.format("%02d:", time/3600));
        time %= 3600;
        sb.append(String.format("%02d:", time/60));
        time %= 60;
        sb.append(String.format("%02d", time));
        
        return sb.toString();
    }
    
    public int stoi(String str) {
        return Integer.parseInt(str);
    }
}
