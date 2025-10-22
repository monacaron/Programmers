/* 
 * 선A, 후B
 * 발판이 있는 곳에만 캐릭터가 서있을 수 있음
 * 다른 곳으로 이동하면, 밟고 있던 발판은 사라짐
 * 자기 차례에 자신의 캐릭터를 상하좌우 중 인접한 발판으로 이동
 * 움직일 수 없는 경우 패배
 * 두 캐릭터가 같은 발판 위에 있을 때, 상대 캐릭터가 다른 발판으로 이동하면 패배
 * 양 플레이어는 항상 최적의 플레이
 * 이길 수 있으면 최대한 빨리 승리
 * 질 수 밖에 없으면 최대한 오래 버티기
 
 * min-max 알고리즘
 * 다음 위치로 이동했을 때, 무조건 이기는지, 지는지? -> 이후 판가름이 날 때까지 소요된 턴의 횟수가 중요
 * A(위치 이동) -> B -> A(패배) ~ 2턴 소요 후 패배
 * A(위치 이동) -> B -> A -> B(패배) ~ 3턴 소요 후 승리
 * 즉 마지막에 이동 불가한 사람이 누군지를 턴 횟수로 판별 가능
 * 횟수가 홀수 -> 승리
 * 횟수가 짝수 -> 패배
 * 하나라도 홀수라면 해당 경우 채택 ... 이길 가능성이 있으면 지는 경우는 무시
 * 모두 홀수라면 가장 작은 경우 채택 (빨리 끝내기)
 * 모두 짝수라면 가장 큰 경우 채택 (오래 끌기)
 */

class Solution {
    static int[] dirX = {-1, 1, 0, 0}; // 상하좌우
    static int[] dirY = {0, 0, -1, 1}; // 상하좌우
    static int n, m; // 보드 크기
    public int solution(int[][] board, int[] aloc, int[] bloc) {
        
        n = board.length;
        m = board[0].length;
        
        return minMax(aloc[0], aloc[1], bloc[0], bloc[1], board);
    }
    
    // 이번 캐릭터 좌표(x, y), 다음 캐릭터 좌표(nx, ny)
    public int minMax(int x, int y, int nx, int ny, int[][] board) {
        // System.out.printf("(%d, %d)\n", x, y);
        // print(board);
        int ret = 0; // 결과값

        // 다른 플레이어가 이동해서 현재 발판이 사라진 경우 패배
        if (board[x][y] == 0) return 0;
        
        for (int i = 0; i < 4; i++) {
            int dx = x + dirX[i];
            int dy = y + dirY[i];

            // 범위 밖 or 발판 없음
            if (!checkRange(dx, dy) || board[dx][dy] == 0) {
                continue;
            }
            
            board[x][y] = 0;
            int pos = minMax(nx, ny, dx, dy, board) + 1; // 다음 턴으로 진행
            board[x][y] = 1;
            
            // 1. 승리 분기가 존재하는 경우
            if (ret % 2 == 0 && pos % 2 == 1) {
                ret = pos;
            }
            // 2. 모든 분기가 패배인 경우, 가장 긴 횟수 선택
            else if (ret % 2 == 0 && pos % 2 == 0) {
                ret = Math.max(ret, pos);
            }
            // 3. 모든 분기가 승리인 경우, 가장 짧은 횟수 선택
            else if (ret % 2 == 1 && pos % 2 == 1) {
                ret = Math.min(ret, pos);
            }
        }
        
        return ret;
    }
    
    public boolean checkRange(int x, int y) {
        if (x < 0 || x >= n || y < 0 || y >= m) {
            return false;
        }
        
        return true;
    }
    
    public void print(int[][] map) {
        for(int[] arr: map) {
            for(int k : arr) {
                System.out.print(k + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
