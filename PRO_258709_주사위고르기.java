/*
 * A와 B가 n개의 주사위를 가지고 승부
 * 주사위의 6개 면에 각각 하나의 수가 쓰여 있다. 각 면이 나올 확률은 동일하다.
 * 각 주사위는 1 ~ n의 번호를 가지고 있으며, 주사위에 쓰인 수의 구성은 모두 다르다.
 * A가 먼저 n/2개의 주사위를 가져가면 B가 남은 n/2개의 주사위를 가져간다.
 * 각각 가져간 주사위를 모두 굴린 뒤, 나온 수들을 모두 합해 점수를 계산한다.
 * 점수가 더 큰 쪽이 승리하며, 같다면 무승부이다.
 * 자신이 승리할 확률이 가장 높아지기 위해 A가 골라야 하는 주사위 번호를 오름차순으로 구하기
 * 승리 확률이 가장 높은 주사위 조합은 유일하다.
 
 * 1. A의 주사위 조합, B의 주사위 조합 구하기 ... n < 16이므로 비트 마스킹 사용해도 됨
 * 2. 각 조합에서 가능한 점수 구하기 -> 완전 탐색은 시간 초과 ... 이분 탐색 적용
 * 3. 확률 계산
 
 * 주사위 총 개수가 10개 이하이니 조합 사용 가능
 */

import java.util.*;

class Solution {
    static int n, ansCount;
    static List<Integer> aList, bList;
    static int[] answer;
    public int[] solution(int[][] dice) {
        n = dice.length;
        
        answer = new int[n / 2];
        diceComb(0, 0, new int[n], dice);
        
        for (int i = 0; i < n / 2; i++) {
            answer[i]++;
        }
        
        return answer;
    }
    
    public void getProb(int[] selected, int[][] dice) {
        int[] aSelected = new int[n / 2];
        int[] bSelected = new int[n / 2];
        
        int aIdx = 0, bIdx = 0;
        for (int i = 0; i < n; i++) {
            if (selected[i] == 1) {
                aSelected[aIdx++] = i;
            } else if (selected[i] == 0) {
                bSelected[bIdx++] = i;
            }
        }
        
        aList = new ArrayList<>();
        bList = new ArrayList<>();
        scoreComb(0, 0, aSelected, dice, aList);
        scoreComb(0, 0, bSelected, dice, bList);
        
        // 이분탐색을 위해 정렬
        Collections.sort(bList);
        
        // 이분탐색으로 이긴 횟수 구하기
        int win = 0;
        for (int a : aList) {
            int left = 0;
            int right = bList.size() - 1;
            
            while (left <= right) {
                int mid = (left + right) / 2;
                
                if (a <= bList.get(mid)) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
            
            win += left;
        }
        System.out.println();
        
        if (win > ansCount) {
            ansCount = win;
            answer = aSelected;
        }
        
    }
    
    public void scoreComb(int cnt, int sum, int[] selected, int[][] dice, List<Integer> list) {
        if (cnt == n / 2) {
            list.add(sum);
            return;
        }
        
        for(int i = 0; i < 6; i++) { // 6면 주사위
            int score = dice[selected[cnt]][i];
            scoreComb(cnt + 1, sum + score, selected, dice, list);
        }
        
    }
    
    public void diceComb(int index, int cnt, int[] selected, int[][] dice) {
        if (cnt == n / 2) { // 선택 완료
            // System.out.println("주사위 선택 완료 : " + Arrays.toString(selected));
            getProb(selected, dice); // 해당 주사위 조합의 승률 구하기
            return;
        }

        if (index == n) { // 선택 종료 ... 더 이상 고를 주사위 없음
            return;
        }
        

        for(int i = index; i < n; i++){
            selected[i] = 1;
            diceComb(i + 1, cnt + 1, selected, dice);
            selected[i] = 0;
        }
    }
}
