/*
 * 5개 대기실. 각 대기실은 5 x 5
 * 응시자들끼리는 맨해튼 거리 2 이하로 앉으면 안됨
 * 자리 사이가 파티션으로 막혀있는 경우에는 허용
 * 모두 규칙을 지키면 1, 한 명이라도 어기면 0
 * 어기는 경우 1. 세로 직선 맨해튼 2
 * 어기는 경우 2. 가로 직선 맨해튼 2
 * 어기는 경우 3. 대각선 맨해튼 2
 */

import java.util.*;

class Solution {
    public int[] solution(String[][] places) {
        int[] answer = new int[5];
        
        char[][] map = new char[5][5];
        
        int idx = 0;
        for (String[] place : places) {
            boolean flag = true;
            
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    if (place[i].charAt(j) == 'P') {
                        // 1-1. 가로 1칸
                        if (j + 1 < 5 && place[i].charAt(j + 1) == 'P') {
                            flag = false;
                            break;
                        }
                        // 1-2. 가로 2칸
                        if (j + 2 < 5 && place[i].charAt(j + 2) == 'P' && place[i].charAt(j + 1) == 'O') {
                            flag = false;
                            break;
                        }
                        
                        // 2-1. 세로 1칸
                        if (i + 1 < 5 && place[i + 1].charAt(j) == 'P') {
                            flag = false;
                            break;
                        }
                        
                        // 2-2. 세로 2칸
                        if (i + 2 < 5 && place[i + 2].charAt(j) == 'P' && place[i + 1].charAt(j) == 'O') {
                            flag = false;
                            break;
                        }
                        
                        // 3. 대각선
                        if (!check(i, j, place)) {
                            flag = false;
                            break;
                        }
                    }
                }
                
                if (!flag) {
                    break;
                }
                
            }    

            if (flag) {
                answer[idx] = 1;
            }
            
            idx++;
        }
        
        
        return answer;
    }
    
    public boolean check (int x, int y, String[] place) {
        if (x - 1 >= 0 && y - 1 >= 0 && place[x - 1].charAt(y - 1) == 'P') { // 11
            if (place[x - 1].charAt(y) != 'X' || place[x].charAt(y - 1) != 'X') {
                return false;
            }
        }
        
        if (x - 1 >= 0 && y + 1 < 5 && place[x - 1].charAt(y + 1) == 'P') { // 1
            if (place[x - 1].charAt(y) != 'X' || place[x].charAt(y + 1) != 'X') {
                return false;
            }
        }
        
        if (x + 1 < 5 && y - 1 >= 0 && place[x + 1].charAt(y - 1) == 'P') { // 7
            if (place[x].charAt(y - 1) != 'X' || place[x + 1].charAt(y) != 'X') {
                return false;
            }
        }
        
        if (x + 1 < 5 && y + 1 < 5 && place[x + 1].charAt(y + 1) == 'P') { // 5
            if (place[x].charAt(y + 1) != 'X' || place[x + 1].charAt(y) != 'X') {
                return false;
            }
        }
            
        return true;
    }
}
