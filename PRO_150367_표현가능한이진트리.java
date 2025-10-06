/*
 * 이진트리를 수로 표현하는 것
 * 1. 이진수를 저장할 빈 문자열 생성
 * 2. 주어진 이진트리에 더미 노드를 추가하여 포화 이진트리로 만들기. 루트 노드는 그대로 유지
 * 3. 포화 이진트리의 노드들을 가장 왼쪽 노드부터 가장 오른쪽 노드까지, 왼쪽에 있는 순서대로 살피기. 노드의 높이는 살펴보는 순서에 영향을 끼치지 않음
 * 4. 살펴본 노드가 더미 노드라면, 문자열 뒤에 0 추가. 더미 노드가 아니라면, 문자열 뒤에 1 추가
 * 5. 문자열에 저장된 이진수를 십진수로 변환
 
 * 1. 더미 노드 추가
 * 2. 왼쪽 > 오른쪽 순서로 살펴보기. 높이는 상관x
 * 3. 생성한 문자열(이진수)를 십진수로 변환
 
 * 초기 풀이
 * 리프 노드만 더미 노드 가능이라고 생각 -> 부모 노드에 해당 하는 자리(짝수 번호 노드)는 무조건 1이라고 생각
 * but 더미 부모 + 더미 자식인 경우 존재
 
 * 해설 참고
 * 부모 노드는 무조건 중간 ~ 가운데를 중심으로 좌우 나누기
 * 1. 초기 이진수를 기록할 수 있는 포화 이진 트리
 * 2. 부모 노드인 경우 1이여야 함
 * 즉, 0인데 자식을 갖고있으면 안됨
 */

import java.util.*;

class Solution {
    static boolean flag;
    public int[] solution(long[] numbers) {
        int n = numbers.length;
        int[] answer = new int[n];
        
        for (int i = 0; i < n; i++) {
            String origin = Long.toBinaryString(numbers[i]);
            
            int cnt = nodeCount(origin.length());
            
            String sf = "%" + cnt + "s";
            origin = String.format(sf, origin).replace(' ', '0');
            
            flag = true;
            root(origin, 0, origin.length() - 1);
            
            if (flag) {
                answer[i] = 1;
            }
        }
        
        return answer;
    }
    
    public void checkNode(String str, int start, int end) {
        for (int i = start; i <= end; i++) {
            if (str.charAt(i) == '1') {
                flag = false;
                return;
            }
        }
    }
    
    public void root(String str, int start, int end) {
        if (!flag) {
            return;
        }
        
        if (start >= end) {
            return;
        }
        
        int mid = (start + end) / 2;
        // 더미 노드가 부모인 경우 양 옆에 하위에 자식 존재 xxx
        if (str.charAt(mid) == '0') {
            checkNode(str, start, mid - 1); // 왼쪽
            checkNode(str, mid + 1, end); // 오른쪽
        } 
        
        // 일반 노드가 부모인 경우 하위 노드 검사
        else {
            root(str, start, mid - 1);
            root(str, mid + 1, end);
        }

    }
    
    public int nodeCount(int count) {
        int result = 1;
        int plus = 1;
        
        while (result < count) {
            plus *= 2;
            result += plus;
        }
        
        return result;
    }
}
