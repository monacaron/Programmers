/*
 * 50 x 50 고정 크기의 표
 * 초기에 모든 셀은 비어 있음
 * 각 셀은 문자열 값을 가질 수 있고, 다른 셀과 병합될 수 있음
 * (r, c) : 위에서 r번째, 왼쪽에서 c번째 위치
 * 1. UPDATE r c value : (r, c) 셀 값을 value로 변경
 * 2. UPDATE value1 value2 : value1을 값으로 가진 모든 셀 값을 value2로 변경
 * 3. MERGE r1 c1 r2 c2 : (r1, c1) 셀과 (r2, c2) 병합
 * >> 동일 위치일 경우 무시
 * >> 두 셀 중 한 셀만 값을 가지고 있을 경우, 병합된 셀은 그 값을 가짐
 * >> 두 셀 모두 값을 가지고 있을 경우, 병합된 셀은 (r1, c1) 셀 값을 가짐
 * >> 이후 (r1, c1), (r2, c2) 중 어느 위치를 선택해도 병합된 셀로 접근
 * 4. UNMERGE r c : (r, c) 셀의 모든 병합 해제
 * >> 선택한 셀이 포함하고 있던 모든 셀은 초기 상태로 돌아감 ~ 빈 칸
 * >> 병합 해제 전 값을 가지고 있었을 경우 (r, c) 셀이 그 값을 가짐
 * 5. PRINT r c : (r, c) 셀의 값 출력. 비어있을 경우 "EMPTY" 출력
 * return PRINT 명령어에 대한 실행결과를 1차원 문자열 배열에 담아 return
 
 * 1. 명령어 읽기 : update rc, update value, merge, unmerge, print
 * 2. 기준 좌표와 연결된 좌표에 명령어 실팽
 
 * 연결 상태를 어떻게 표시 ? union - find로 표시
 */

import java.util.*;

class Solution {
    static final int n = 50;
    static String[][] map;
    static int[] parent;
    public String[] solution(String[] commands) {
        // 초기 세팅
        map = new String[n][n];
        parent = new int[n * n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(map[i], "EMPTY"); // 입력은 소문자로만 주어짐
            for (int j = 0; j < n; j++) {
                parent[i * 50 + j] = i * 50 + j;
            }
        }
             
        List<String> list = new ArrayList<>(); // 정답 리스트
        
        // 커맨드 시작        
        int r = 0, c = 0, r2 = 0, c2 = 0;
        for (int i = 0; i < commands.length; i++) {
            String[] command = commands[i].split(" ");
            
            if (command[0].equals("UPDATE")) {
                if (command.length == 4) { // 1. UPDATE r c v
                    r = Integer.parseInt(command[1]) - 1;
                    c = Integer.parseInt(command[2]) - 1;
                    updateRC(r, c, command[3]);
                } else { // 2. UPDATE v1 v2
                    updateValue(command[1], command[2]);
                }
            } else if(command[0].equals("MERGE")) { // 3. MERGE r c r2 c2
                r = Integer.parseInt(command[1]) - 1;
                c = Integer.parseInt(command[2]) - 1;
                r2= Integer.parseInt(command[3]) - 1;
                c2= Integer.parseInt(command[4]) - 1;
                
                merge(r, c, r2, c2);
            } else if(command[0].equals("UNMERGE")) { // 4. UNMERGE r c
                r = Integer.parseInt(command[1]) - 1;
                c = Integer.parseInt(command[2]) - 1;
                
                unmerge(r, c);
            } else { // 5. PRINT r c
                r = Integer.parseInt(command[1]) - 1;
                c = Integer.parseInt(command[2]) - 1;
                
                list.add(map[r][c]);
            }
        }
        
        return list.toArray(new String[0]);
        // return list.toArray(String[]::new);
    }
    
    public void unmerge(int r, int c) {
        int pn = parent[r * 50 + c]; // 연결된 셀을 구분하기 위한 부모 번호 
        String tmp = map[r][c]; // 최종 값
        
        for (int i = 0; i < n * n; i++) {
            if (parent[i] == pn) { // 연결된 상태
                int x = i / 50;
                int y = i % 50;
                
                parent[i] = i; // 연결관계 끊기
                map[x][y] = "EMPTY"; // 상태 초기화
            }
        }
        
        map[r][c] = tmp; // (r, c)는 값 유지
    }
    
    public void merge(int r, int c, int r2, int c2) {
        if (r == r2 && c == c2) { // 동일 좌표 무시
            return;
        }
        
        String value = "EMPTY"; // 최종 값
        
        // 1. 한 셀(r2, c2)만 값을 가진 경우
        if(map[r][c].equals("EMPTY") && !map[r2][c2].equals("EMPTY")) { // (r2, c2) 기준
            value = map[r2][c2];
        }
        // 2. 두 셀 모두 값을 가진 경우 + 한 셀(r, c)만 값을 가진 경우
        else {
            value = map[r][c]; // (r, c) 기준
        }


        // 연결 기록
        int root1 = find(r * 50 + c); // (r, c) 부모
        int root2 = find(r2 * 50 + c2); // (r2, c2) 부모
        union(root1, root2);
        int root = find(root1); // 최상위 부모
        
        for (int i = 0; i < n * n; i++) {
            if (parent[i] == root1 || parent[i] == root2) { // (r, c)와 연결된 셀, (r2, c2)와 연결된 셀 모두 값 변경
                parent[i] = root; // 병합
                
                int x = i / 50;
                int y = i % 50;
                
                map[x][y] = value;
            }
        }
    }
    
    public void updateValue(String v1, String v2) {
        // 이미 연결된 셀의 값은 동일하므로 전체 배열 탐색
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (map[i][j].equals(v1)) {
                    map[i][j] = v2;                
                }
            }
        }
    }
    
    public void updateRC(int r, int c, String value) {
        int pn = find(r * 50 + c); // 연결된 셀을 구분하기 위한 부모 번호 
        for (int i = 0; i < n * n; i++) {
            if (parent[i] == pn) { // 연결된 상태
                int x = i / 50;
                int y = i % 50;
                
                map[x][y] = value; // 연결된 모든 셀의 값을 변경
            }
        }
    }
    
    public int find(int x) {
        if (x != parent[x]) {
            return parent[x] = find(parent[x]);
        }
        
        return parent[x];
    }
    
    public void union(int x, int y) {
        x = find(x);
        y = find(y);
        
        if(x >= y) {
            parent[y] = x;
        } else {
            parent[x] = y;
        }
    }
    
    public void print(String[][] map) {
        for(int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.printf("%s ", map[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }
}
