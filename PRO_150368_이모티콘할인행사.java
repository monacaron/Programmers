/*
 * 목표 1. 가입자 최대 증가
 * 목표 2. 판매액 최대 증가
 * n명의 사용자들에게 이모티콘 m개를 할인하여 판매
 * 이모티콘마다 할인율은 다를 수 있으며, 10%, 20%, 30%, 40% 중 하나로 설정
 * 다음과 같은 기준을 따라 이모티콘 구매 or 플러스 서비스 가입
 * 1. 일정 비율 이상 할인하는 이모티콘 모두 구매
 * 2. 비용의 합이 일정 가격 이상이 되면 대신 플러스 서비스 가입
 * 행사 목적을 최대한으로 달성했을 때, 플러스 서비스 가입 수와 이모티콘 매출액 구하기
 
 * 1. 이모티콘별 할인율 설정 - 순열
 * 2. 해당 할인율에 따른 구매자별 비용의 합 - 구현
 * 3. 서비스 가입 수 및 매출액 갱신
 * 매번 비용의 합을 구할 때 계산하기 귀찮으니까 할인율별 이모티콘 구매 비용을 저장
 */

import java.util.*;

class Solution {
    static int ans1 = 0, ans2 = 0; // 가입 수, 매출액
    static int n, m; // 사용자 수, 이모티콘 수
    static int[] percents = new int[]{10, 20, 30, 40};
    static int[][] map; // 이모티콘별 할인 가격
    public int[] solution(int[][] users, int[] emoticons) {
        n = users.length;
        m = emoticons.length;
        map = new int[m][percents.length];

        // 1. 이모티콘별 할인 가격 저장
        calcSum(emoticons);

        // 2. 할인율 설정
        perm(0, new int[m], users, emoticons);
        
        return new int[]{ans1, ans2};
    }
    
    public void calc(int[] discount, int[][] users, int[] emoticons) {
        int[] sum = new int[n]; // 사용자별 구매 비용
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (percents[discount[j]] >= users[i][0]) {
                    sum[i] += map[j][discount[j]];
                }
            }
        }
        
        int plusService = 0;
        int sales = 0;
        
        for (int i = 0; i < n; i++) {
            if (sum[i] >= users[i][1]) {
                plusService++;
            } else {
                sales += sum[i];
            }
        }
        
        if (ans1 < plusService) {
            ans1 = plusService;
            ans2 = sales;
        } else if (ans1 == plusService) {
            ans2 = Math.max(ans2, sales);
        }
        
    }
    
    public void calcSum(int[] emoticons) {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < percents.length; j++) {
                map[i][j] = emoticons[i] / 100 * (100 - percents[j]);
            }
        }
    }
    
    public void perm(int index, int[] discount, int[][] users, int[] emoticons) {
        if (index == m) {
            // System.out.println(Arrays.toString(discount));
            // 3. 비용 계산
            calc(discount, users, emoticons);
            return;
        }
        
        for (int i = 0; i < percents.length; i++) {
            discount[index] = i;
            perm(index + 1, discount, users, emoticons);
        }
    }
}
