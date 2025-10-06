/*
 * 행렬에 적용할 수 있는 두 가지 연산
 * 1. ShiftRow : 모든 행이 아래쪽으로 한 칸씩 밀려난다. 마지막 행은 1번째 행이 된다.
 * 2. Rotate : 행렬의 바깥쪽에 있는 원소들을 시계 방향으로 한 칸 회전 시킨다.
 * >> 첫 행, 첫 열, 끝 행, 끝 열에 포함되는 원소들
 * 주어진 연산을 차례대로 시행한 후 행렬 상태 구하기
 
 * O(1) 시간 복잡도를 가져야 함...ㅠㅅㅠ
 * Deque 이용
 * 좌측, 우측, 중간으로 나누기 ... 중간 원소들은 행 단위로 구별
 * ShiftRow : 왼쪽의 마지막 원소를, 중앙의 마지막 행을, 오른쪽의 마지막 원소를 첫 번째로 이동
 * Rotate : 왼쪽의 첫 번째 원소를 중앙 첫 행의 시작으로, 중앙 첫 행의 마지막 원소를 오른쪽 첫 번째로, 오른쪽 마지막 원소를 중앙 마지막 행의 끝으로, 중앙 마지막 원소를 왼쪽의 끝으로 이동
 */

import java.util.*;

class Solution {
    static int n, m;
    static int[] dirX = {0, 1, 0, -1}; // 우 하 좌 상
    static int[] dirY = {1, 0, -1, 0};
    public int[][] solution(int[][] rc, String[] operations) {
        n = rc.length;
        m = rc[0].length;
        
        // 행렬 쪼개기
        Deque<Integer> left = new ArrayDeque<>();
        Deque<Deque<Integer>> mid = new ArrayDeque<>();
        Deque<Integer> right = new ArrayDeque<>();
        
        for (int i = 0; i < n; i++) {
            mid.add(new ArrayDeque());
            for (int j = 0; j < m; j++) {
                if (j == 0) {
                    left.add(rc[i][j]);
                } else if (j == m - 1) {
                    right.add(rc[i][j]);
                } else {
                    mid.peekLast().add(rc[i][j]);
                }
            }
        }
        
        for (String command : operations) {
            switch (command) {
                case "ShiftRow" : {
                    shiftRow(left, mid, right);
                    break;
                }
                case "Rotate" : {
                    rotate(left, mid, right);
                    break;
                }
            }

        }
        
        // shiftRow(left, mid, right);
        for (int i = 0; i < n; i++) {
            Deque<Integer> tmp = mid.pollFirst();
            for (int j = 0; j < m; j++) {
                if (j == 0) {
                    rc[i][j] = left.poll();
                } else if (j == m - 1) {
                    rc[i][j] = right.poll();
                } else {
                    rc[i][j] = tmp.poll();
                }
            }
        }
        
        return rc;
    }

    
    // 바깥쪽 원소 시계방향 회전
    public void rotate(Deque<Integer> left, Deque<Deque<Integer>> mid, Deque<Integer> right) {
        // System.out.println("Rotate");
        
        // 1. 왼쪽 > 중앙
        mid.peekFirst().addFirst(left.poll());
        // 2. 중앙 > 오른쪽
        right.addFirst(mid.peekFirst().pollLast());
        // 3. 오른쪽 > 중앙
        mid.peekLast().add(right.pollLast());
        // 4. 중앙 > 왼쪽
        left.add(mid.peekLast().poll());
    }
    
    // 한 행씩 내리기
    public void shiftRow(Deque<Integer> left, Deque<Deque<Integer>> mid, Deque<Integer> right) {
        // System.out.println("ShiftRow");
        
        // 각자 마지막 원소를 첫 번째로 이동
        left.addFirst(left.pollLast());
        mid.addFirst(mid.pollLast());
        right.addFirst(right.pollLast());
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
