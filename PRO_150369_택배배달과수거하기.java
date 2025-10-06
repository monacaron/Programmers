/*
 * 일렬로 나열된 n개의 집에 택배 배달
 * 물건은 모두 크기가 같은 상자에 담아 배달
 * 배달을 다니면서 빈 상자 수거
 * i번째 집은 창고에서 i만큼 떨어져 있고, i번째 집은 j번째 집과 j - 1만큼 떨어져 있음(i < j)
 * 트럭에는 상자를 최대 cap만큼 실을 수 있음
 * 트럭 하나로 모든 배달과 수거를 마치고 창고까지 돌아올 수 있는 최소 이동 거리 구하기
 
 * i번째 집을 갔다오면서 가장 먼 집부터 배달/수거
 * 출발할 때 상자의 개수는 중요 x ... 최대한 챙겨서 배달 ... 남은 건 안챙겼다 생각해도 됨 ... 즉 상관없음
 * 배달/수거의 총 양이 포인트
 */

class Solution {
    public long solution(int cap, int n, int[] deliveries, int[] pickups) {
        long answer = 0;
        
        int delCap = 0; // 해당 타임의 배달 개수
        int pickCap = 0; // 해당 타임의 수거 개수
        for (int i = n - 1; i >= 0; i--) { // 먼 집부터 배달/수거
            // i번째 집의 배달/수거 기록
            delCap += deliveries[i];
            pickCap += pickups[i];
            
            while (delCap > 0 || pickCap > 0) { // i번째 집을 들려야 하는 경우
                // 오고가며 cap만큼 배달/수거 진행
                // cap을 빼도 양수이면 i번째 집을 한 번 더 방문
                // 음수가 되면 그만큼 여유가 있어 앞에 존재하는 집의 배달/수거 처리 가능
                delCap -= cap;
                pickCap -= cap;
                
                answer += (i + 1) * 2; // i번째 집까지의 거리
            }
        }
        
        return answer;
    }
}
