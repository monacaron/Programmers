/*
 * 4 x 4 ~ 16장
 * 8가지 그림이 각기 2장씩 무작위 배치
 * 2장을 골랐을 때, 같은 그림이면 화면에서 사라지고, 다른 그림이면 다시 뒤집기
 * 상하좌우 키보드 : 해당 방향으로 한 칸 커서 이동
 * Ctrl + 키보드 : 누른 키 방향에 있는 가장 가까운 카드로 한 번에 이동
 * 해당 방향에 카드가 하나도 없다면 그 방향의 가장 마지막 칸으로 이동
 * 이동 가능한 카드 or 빈 공간이 없다면 커서 움직임 xxx
 * Enter : 뒤집기... 같은 그림이 2개가 되면 카드 삭제. 다르면 다시 뒤집기
 * 몇 장 제거된 상태에서 카드 앞면의 그림을 알고 있다면, 남은 카드를 모두 제거하는데 필요한 키 조작 횟수의 최솟값 구하기
 
 * 1. 카드 삭제 순서 순열
 * 2. 삭제 시뮬레이션
 */

import java.util.*;

class Point {
    int x, y;
    
    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

class Solution {
    static int answer = (int)1e9;
    static Point[][] cards;
    static int[] dirX = {-1, 1, 0, 0};
    static int[] dirY = {0, 0, -1, 1};
    static int[][] map;
    public int solution(int[][] board, int r, int c) {
        cards = new Point[6 + 1][2]; // 카드 종류 : 1 ~ 6
        
        // 카드 정보 저장
        int cnt = 0;
        map = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == 0) { // 빈 칸
                    continue;
                }
                
                int k = board[i][j];
                cnt = Math.max(cnt, k);
                map[i][j] = k;
                
                if (cards[k][0] == null) {
                    cards[k][0] = new Point(i, j);
                } else {
                    cards[k][1] = new Point(i, j);
                }
                
            }
        }
                
        perm(0, cnt, 0, new boolean[cnt + 1], r, c, new int[cnt]);
        
        
        return answer;
    }
    
    
    // 다익스트라 + bfs
    public int bfs(int tx, int ty, int r, int c) {
        int x = r;
        int y = c;
        
        PriorityQueue<int[]> queue = new PriorityQueue<>((o1, o2) -> o1[2] - o2[2]);
        queue.offer(new int[]{x, y, 0});
        
        int[][] dist = new int[4][4];
        for (int[] row : dist) Arrays.fill(row, (int)1e9);
        dist[r][c] = 0;

        while (!queue.isEmpty()) {
            int[] now = queue.poll();
            
            x = now[0];
            y = now[1];
            int cost = now[2];

            if (x == tx && y == ty) {
                return cost;
            }
            
            // 현재 비용이 이미 저장된 최소 비용보다 크면 pass
            if (cost > dist[x][y]) continue;
            
            // 2. 한 칸 이동
            for (int i = 0; i < 4; i++) {
                int dx = x + dirX[i];
                int dy = y + dirY[i];
                
                // 범위 벗어남 or 최솟값 갱신 불가
                if (!checkRange(dx, dy) || cost + 1 >= dist[dx][dy]) {
                    continue;
                }

                dist[dx][dy] = cost + 1;                
                queue.offer(new int[]{dx, dy, cost + 1});
            }
            
            // 3. Ctrl 이동
            for (int i = 0; i < 4; i++) {
                int dx = x + dirX[i];
                int dy = y + dirY[i];                
                
                // 3. Ctrl 이동
                while (checkRange(dx, dy) && map[dx][dy] == 0) {
                    dx += dirX[i];
                    dy += dirY[i];
                }
                
                // 마지막 카드도 비어있는 경우 마지막 카드로 커서 이동
                if (!checkRange(dx, dy)) {
                    dx -= dirX[i];
                    dy -= dirY[i];
                }
                
                // 최종 좌표가 이전과 같으면 pass
                if (x == dx && y == dy) {
                    continue;
                }

                if (cost + 1 >= dist[dx][dy]) {
                    continue;
                }
                
                dist[dx][dy] = cost + 1;
                queue.offer(new int[]{dx, dy, cost + 1});
            }
        }
        
        return 0;
    }
    
//     public int bfs(int tx, int ty, int r, int c) {
//         int x = r;
//         int y = c;
        
//         PriorityQueue<int[]> queue = new PriorityQueue<>((o1, o2) -> o1[2] - o2[2]);
//         boolean[][] visited = new boolean[4][4];
//         // int[][] dist = new int[4][4];
//         // for (int[] row : dist) Arrays.fill(row, (int)1e9);
//         // dist[r][c] = 0;
        
//         queue.offer(new int[]{x, y, 0});
//         visited[x][y] = true;

//         while (!queue.isEmpty()) {
            
//             int[] now = queue.poll();
            
//             x = now[0];
//             y = now[1];

//             if (x == tx && y == ty) {
//                 return now[2];
//             }
            
//             // 2. 한 칸 이동
//             for (int i = 0; i < 4; i++) {
//                 int dx = x + dirX[i];
//                 int dy = y + dirY[i];
                
//                 // 범위 벗어남 or 지나온 길
//                 if (!checkRange(dx, dy) || visited[dx][dy]) {
//                     continue;
//                 }

//                 visited[dx][dy] = true;
//                 queue.offer(new int[]{dx, dy, now[2] + 1});
//             }
            
//             // 3. Ctrl 이동
//             for (int i = 0; i < 4; i++) {
//                 int dx = x + dirX[i];
//                 int dy = y + dirY[i];                
                
//                 // 3. Ctrl 이동
//                 while (checkRange(dx, dy) && map[dx][dy] == 0) {
//                     dx += dirX[i];
//                     dy += dirY[i];
//                 }
                
//                 // 마지막 카드도 비어있는 경우 마지막 카드로 커서 이동
//                 if (!checkRange(dx, dy)) {
//                     dx -= dirX[i];
//                     dy -= dirY[i];
//                 }
                
//                 // 최종 루트가 방문한 곳인지?
//                 if (visited[dx][dy]) {
//                     continue;
//                 }

//                 visited[dx][dy] = true;
//                 queue.offer(new int[]{dx, dy, now[2] + 1});
//             }
//         }
        
//         return 0;
//     }
    
    public void perm(int idx, int cnt, int sum, boolean[] visited, int r, int c, int[] selected) {
        if (answer < sum) {
            return;
        }
        
        if (idx == cnt) {
            answer = sum;
            return;
        }
        
        for (int i = 1; i <= cnt; i++) { // 카드 선택
            if (visited[i]) { // 해당 카드 삭제 완료
                continue;
            }
            
            int r1 = cards[i][0].x, c1 = cards[i][0].y;
            int r2 = cards[i][1].x, c2 = cards[i][1].y;
            int move1 = bfs(r1, c1, r, c) + bfs(r2, c2, r1, c1) + 2; // 첫 번째 카드 -> 두 번째 카드
            int move2 = bfs(r2, c2, r, c) + bfs(r1, c1, r2, c2) + 2; // 두 번째 카드 -> 첫 번째 카드
            
            visited[i] = true;
            map[r1][c1] = 0;
            map[r2][c2] = 0;
            selected[idx] = i;

            // 이렇게 하면 시간 더 짧게 정답인정됨 ... 근데 다음 카드 시작 지점에 따라 답이 바뀔 것 같은데 왜 맞는지 의문 ㅠㅅㅠ
            // if (move1 > move2) {
            //     perm(idx + 1, cnt, sum + move2, visited, r1, c1, selected);
            // } else {
            //     perm(idx + 1, cnt, sum + move1, visited, r2, c2, selected);  
            // }
            
            perm(idx + 1, cnt, sum + move1, visited, r2, c2, selected);        
            perm(idx + 1, cnt, sum + move2, visited, r1, c1, selected);
            
            map[r1][c1] = i;
            map[r2][c2] = i;
            visited[i] =false;
        }
    }
    
    public boolean checkRange(int x, int y) {
        if (x < 0 || x >= 4 || y < 0 || y >= 4) {
            return false;
        }
        
        return true;
    }
}
