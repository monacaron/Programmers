/*
 * n x m 격자 미로 : (x, y) 출발, (r, c) 탈출
 * 1. 격자 바깥으로는 이동 불가
 * 2. 출발부터 도착까지 이동 거리가 총 k. 같은 격자를 두 번 이상 방문해도 됨
 * 3. 탈출 경로를 문자열로 나타냈을 때, 문자열이 사전 순으로 가장 빠른 경로로 탈출
 * 왼쪽(l), 오른쪽(r), 위쪽(u), 아래쪽(d)
 * 출발(S), 탈출(E)
 * 미로 탈출 경로 구하기 ... 조건대로 미로를 탈출할 수 없는 경우 "impossible"
 
 * 그리디 : 사전 순 루트이므로 d, l, r, u 순서
 * mht = (x, y)에서 (r, c)까지 이동하는데 필요한 최소 거리
 * 이동할 수 있는 거리(k)가 필요 이동 거리(mht)보다 작으면 불가능
 * 필수 이동 거리(mht)가 아닌 여유 이동 거리(k - mht)가 짝수여야만 다른 곳에 갔다가 복귀 가능
 */

import java.util.*;

class Point {
    int a, b; // 좌표 (a, b);
    
    Point(int a, int b) {
        this.a = a;
        this.b = b;
    }
}
class Solution {
    // 사전 순 : d, l, r, u
    static int[] dirX = new int[]{1, 0, 0, -1};
    static int[] dirY = new int[]{0, -1, 1, 0};
    static String[] direct = new String[]{"d", "l", "r", "u"};
    static int N, M; // 격자 크기
    static StringBuilder sb;
    public String solution(int n, int m, int x, int y, int r, int c, int k) {
        // (1, 1) 시작이므로 +1씩
        N = n + 1;
        M = m + 1;
        
        sb = new StringBuilder();
        move(x, y, r, c, k);
        
        return sb.toString();
    }
    
    public void move(int x, int y, int r, int c, int k) {       
        int mht = manhattan(x, y, r, c);
        if (mht > k || (k - mht) % 2 == 1) { // 처음부터 불가능한 경우
            sb.append("impossible");
            return;
        }
        
        int tx = x, ty = y;
        while (k > 0) {
            for (int i = 0; i < dirX.length; i++) {
                int dx = tx + dirX[i];
                int dy = ty + dirY[i];
                
                if (!checkRange(dx, dy)) { // 범위 체크
                    continue;
                }
                
                mht = manhattan(dx, dy, r, c);
                if (mht <= k || (k - mht) % 2 == 0) { // (dx, dy)에서 (r, c)까지 이동 가능한 경우
                    k--;
                    tx = dx;
                    ty = dy;
                    sb.append(direct[i]);
                    break; // 사전 순이므로 다음 경로로 갈 필요 없음
                }
            }
        }
        
        
    }
    
    // (x, y) -> (r, c) 최소 이동거리
    public int manhattan(int x, int y, int r, int c) {
        return Math.abs(x - r) + Math.abs(y- c);
    }
    
    // 좌표의 범위 (1 ~ n, 1 ~ m)
    public boolean checkRange(int x, int y) {
        if (x < 1 || x >= N || y < 1 || y >= M) return false;
        
        return true;
    }
}
