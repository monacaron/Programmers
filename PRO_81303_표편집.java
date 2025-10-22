/*
 * 명령어 기반 표의 행을 선택, 삭제, 복구하는 프로그램
 * 한 번에 한 행만 선택 가능 (0 ~ 마지막 행)
 * U X : 현재 선택된 행에서 X칸 위에 있는 행 선택
 * D X : 현재 선택된 행에서 X칸 아래에 있는 행 선택
 * C : 현재 선택된 행 삭제 후, 바로 아래 행 선택. 삭제된 행이 마지막 행인 경우 바로 윗 행 선택
 * Z : 가장 최근에 삭제된 행을 원래대로 복구. 현재 선택된 행은 바뀌지 않음
 * 모든 명령어를 수행한 후 표의 상태 출력
 * 존재(O), 삭제(X)
 
 * 최근 삭제 -> 스택 이용
 * 연결리스트 사용 !!!
 */

import java.util.*;

class Node {
    Node prev = null;
    Node post = null;
    boolean isDeleted;
}

class Solution {
    public String solution(int n, int k, String[] cmd) {        
        Node[] nodes = new Node[n];
        nodes[0] = new Node();
        for (int i = 1; i < n; i++) {
            nodes[i] = new Node();          
            nodes[i].prev = nodes[i - 1];
            nodes[i - 1].post = nodes[i];
        }
        
        Stack<Node> stack = new Stack<>(); // 삭제된 행의 정보
        Node now = nodes[k]; // 현재 선택된 행
        for (String command : cmd) {
            String[] input = command.split(" ");
            
            if (input[0].equals("U")) { // 윗 행 선택(U X) : X칸 윗 행 선택
                int x = stoi(input[1]);                
                
                for (int i = 0; i < x; i++) {
                    now = now.prev;
                }
            } else if (input[0].equals("D")) { // 아래 행 선택(D X) : X칸 아래 행 선택
                int x = stoi(input[1]);                
                
                for (int i = 0; i < x; i++) {
                    now = now.post;
                }
            } else if (input[0].equals("C")) { // 삭제(C) : 현재 행 삭제 후 아래 행 선택. 마지막 행인 경우 윗 행 선택
                // 1. 삭제
                stack.push(now);
                now.isDeleted = true;
                
                Node prev = now.prev;
                Node post = now.post;
                
                // 첫 행 삭제가 아닌 경우
                if (prev != null) {
                    prev.post = post;
                }
                
                // 마지막 행 삭제가 아닌 경우
                if (post != null) {
                    post.prev = prev;
                    now = post; // 아래 행 선택
                } else { // 마지막 행을 삭제한 경우 윗 행 선택
                    now = prev;
                }
      
            } else { // 복구(Z) : 최근에 삭제된 행 복구. 현재 행은 바뀌지 않음 
                Node resto = stack.pop();
                resto.isDeleted = false;
                
                Node prev = resto.prev;
                Node post = resto.post;
                
                // 첫 행 복구가 아닌 경우
                if (prev != null) {
                    prev.post = resto;
                } 
                
                // 마지막 행 복구가 아닌 경우
                if (post != null) {
                    post.prev = resto;
                } 
            }            
        }
        
        
        StringBuilder sb = new StringBuilder();
        for (Node node : nodes) {
            if (node.isDeleted) {
                sb.append("X");
            } else {
                sb.append("O");
            }
        }
        
        return sb.toString();
    }
    
    public int stoi(String str) {
        return Integer.parseInt(str);
    }
}
