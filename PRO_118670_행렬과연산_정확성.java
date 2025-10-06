/*
 * 행렬에 적용할 수 있는 두 가지 연산
 * 1. ShiftRow : 모든 행이 아래쪽으로 한 칸씩 밀려난다. 마지막 행은 1번째 행이 된다.
 * 2. Rotate : 행렬의 바깥쪽에 있는 원소들을 시계 방향으로 한 칸 회전 시킨다.
 * >> 첫 행, 첫 열, 끝 행, 끝 열에 포함되는 원소들
 * 주어진 연산을 차례대로 시행한 후 행렬 상태 구하기

 * 해당 풀이는 효율성 조건을 만족하지 못함
 */

import java.util.*;

class Solution {
    static int n, m;
    static int[] dirX = {0, 1, 0, -1}; // 우 하 좌 상
    static int[] dirY = {1, 0, -1, 0};
    public int[][] solution(int[][] rc, String[] operations) {
        n = rc.length;
        m = rc[0].length;
        
        for (String command : operations) {
            switch (command) {
                case "ShiftRow" : {
                    shiftRow(rc);
                    break;
                }
                case "Rotate" : {
                    rotate(rc);
                    break;
                }
            }
            
            // print(rc);
        }
        
        return rc;
    }

    
    // 바깥쪽 원소 시계방향 회전
    // 상단 : map[0][i]
    // 우단 : map[i][n - 1]
    // 하단 : map[n - 1][i]
    // 좌단 : map[i][0]
    public void rotate(int[][] map) {
        // System.out.println("Rotate");
        
        int d = 0;
        int now = map[0][0], next = 0;
        for (int i = 1; i < m; i++) {
            next = map[0][i];
            map[0][i] = now;
            now = next;
        }
        
        for (int i = 1; i < n; i++) {
            next = map[i][m - 1];
            map[i][m - 1] = now;
            now = next;
        }
        
        for (int i = 1; i < m; i++) {
            next = map[n - 1][m - 1 - i];
            map[n - 1][m - 1 - i] = now;
            now = next;
        }
        
        for (int i = 1; i < n; i++) {
            next = map[n - 1 - i][0];
            map[n - 1 - i][0] = now;
            now = next;
        }
            
    }
    
    // 한 행씩 내리기
    public void shiftRow(int[][] map) {
        // System.out.println("ShiftRow");
        
        int[] end = map[n - 1].clone();
        for (int i = n - 1; i > 0; i--) {
            map[i] = map[i - 1].clone();
        }
        
        map[0] = end.clone();
    }
    
    public boolean checkRange(int x, int y) {
        if (x < 0 || x >= n || y < 0 || y >= n) return false;
        
        return true;
    }
    
    public void print(int[][] map) {
        for (int[] arr : map) {
            for (int k : arr) {
                System.out.printf("%d ", k);
            }
            System.out.println();
        }
        System.out.println();
    }
}
