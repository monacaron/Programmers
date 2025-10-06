/*
 * 1 ~ n 사이의 수가 적힌 카드가 하나씩 있는 카드 뭉치
 * coin개의 동전
 * 카드를 뽑는 순서가 정해져 있으며, 다음과 같이 게임을 진행한다.
 * 1. 처음 카드 뭉치에서 카드 n / 3 장을 뽑아 모두 가진다. (n은 6의 배수)
 * 2. 1라운드부터 시작되며, 각 라운드가 시작할 때 카드를 2장 뽑는다.
 * 2-1. 카드 뭉치에 남은 카드가 없다면 게임 종료
 * 2-2. 뽑은 카드는 한 장당 동전 하나를 소모해 가지거나, 소모하지 않고 버릴 수 있다.
 * 3. 카드에 적힌 수의 합이 n + 1이 되도록 카드 2장을 내고 다음 라운드로 진행 가능
 * 3-1. 만약 카드 두장을 낼 수 없다면 게임 종료
 * 도달 가능한 최대 라운드 수 구하기
 
 * 첫 패에 낼 수 있는 경우의 수 = 초기 라이프
 * 첫 패의 짝은 무조건 가짐 -> 코인 1개만 써서 라이프 중가 가능 ... 효율적
 * 첫 패의 짝이 아닌 경우 가져간다면 ? 이후 나오는 짝과 함께 제출 가능 ... 임시 라이프로 기록
 * 초기 라이프를 다 쓴 후, 임시 라이프 사용
 * 둘 다 불가능하면 종료
 */

import java.util.*;

class Solution {
    public int solution(int coin, int[] cards) {
        int answer = 0;
        
        int n = cards.length;
        boolean[] myCards = new boolean[n + 1]; // 첫 패
        boolean[] newCards = new boolean[n + 1]; // 동전으로 얻은 패
        
        int life = 0, tempLife = 0; // 초기 라이프, 임시 라이프
        for (int i = 0; i < n / 3; i++) {
            myCards[cards[i]] = true;
        }
        
        for (int i = 1; i <= n / 2; i++) {
            if (myCards[i] && myCards[n + 1 - i]) {
                life++;
            }
        }
        
        int round = 1; // 1라운드부터 시작 ... 카드 제출 못해도 최대 도달 1
        for (int i = n / 3; i < n; i = i + 2) {
            int first = cards[i];
            int second = cards[i + 1];
            
            // 1. 첫 패와 짝인 경우 무조건 구입
            if (myCards[n + 1 - first] && coin > 0) {
                myCards[first] = true;
                coin--;
                life++;
            }
            
            if (myCards[n + 1 - second] && coin > 0) {
                myCards[second] = true;
                coin--;
                life++;
            }
            
            // 2. 임시 패와 짝이 가능한 경우 구매는 하지 않고 임시 라이프 기록
            if (newCards[n + 1 - first]) {
                tempLife++;
            } else {
                newCards[first] = true;
            }
            
            if (newCards[n + 1 - second]) {
                tempLife++;
            } else {
                newCards[second] = true;
            }
            
            
            // 3. 라이프가 다 닳은 경우 임시 라이프 구매
            if (life == 0 && coin >= 2 && tempLife > 0) {
                tempLife--;
                coin -= 2;
                life++;
            }
            
            // 4. 모든 라이프를 소진한 경우 게임 종료                   
            if (life == 0) {
                break;
            }
            
            life--; // 카드 제출 가능
            round++;
        }
        
        return round;
    }
}
