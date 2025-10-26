/*
 * 1번 노드에 숫자 1, 2, 3 중 하나씩을 계속해서 떨어트려 리프 노드에 숫자를 쌓는 게임
 * 모든 간선은 부모 -> 자식인 단방향 간선
 * 모든 부모 노드는 자식 노드와 연결된 간선 중 하나를 길로 설정
 * 모든 부모 노드는 자신의 자식 노드 중 가장 번호가 작은 노드를 가리키는 간선을 초기 길로 설정
 * 1. 1번 노드(루트)에 숫자 1, 2, 3 중 하나를 떨어트림
 * 2. 숫자는 길인 간선을 따라 리프 노드까지 떨어짐
 * 3. 숫자가 리프 노드에 도착하면, 숫자는 "지나간" 각 노드는 "현재 길로 연결된 자식 노드 다음으로 번호가 큰 자식 노드"를 가리키는 간선을 새로운 길로 설정하고 기존의 길은 끊음 ~ 즉 숫자 이동 후 간선 정보 변경
 * 3-1. 만약 현재 연결된 길의 번호가 가장 크면, 가장 작은 노드로 변경
 * 3-2. 간선이 하나라면 계속 하나의 간선을 길로 설정
 * 4. 원하는 만큼 계속해서 숫자 떨어트리기
 * 게임 목표 : 각각의 리프 노드에 쌓인 숫자의 합을 target에서 가리키는 값과 같게 만들기
 * target대로 리프 노드에 쌓인 숫자의 합을 맞추기 위해 숫자를 떨어트리는 모든 경우 중 가장 적은 숫자를 사용하며,
 * 그 중 사전 순으로 가장 빠른 경우 구하기. 불가능한 경우 -1
 
 * 리프 노드에 카드 장 수(cnt) 저장
 * cnt <= target[i] <= cnt * 3 범위를 벗어나면 안됨
 * 자식 경로 = 부모 노드를 지나간 횟수 % 자식 개수
 */

import java.util.*;

class Node {
    int idx; // 노드 번호
    ArrayList<Node> children; // 자식 노드 리스트
    int childIdx; // 현재 연결된 자식의 인덱스
    int count; // 해당 노드를 지나간 횟수
    
    Node(int idx) {
        this.idx = idx;
        children = new ArrayList<>();
        childIdx = 0;
        this.count = 0;
    }
}

class Tree {
    Node root;
    ArrayList<Integer> visitList; // 리프 노드 방문 순서
    boolean[] isAble; // 리프 노드의 조건 만족 여부
    int[] target; // 매개 변수 입력 대신 저장
    int[] cntArr; // 몇 번 만에 target에 도달해야 하는지 적힌 배열
    
    Tree(int[] target) {
        root = new Node(1); // 루트는 항상 1
        visitList = new ArrayList<>();
        isAble = new boolean[target.length];
        this.target = target;
        cntArr = new int[target.length];
        
        for (int i = 0; i < target.length; i++) {
            if (target[i] == 0) {
                isAble[i] = true; // 부모 노드
            } else {
                isAble[i] = false; // 리프노드
            }
        }
    }

    // 부모를 찾아서 자식 추가하기
    public void addSearch(int parent, int child, Node node) {
        int idx = node.idx; // 현재 노드 번호
        
        if (idx == parent) { // 현재 노드가 부모 노드면
            node.children.add(new Node(child)); // 자식 리스트에 추가
            return;
        }
        
        if (node.children.size() != 0) { // 현재 노드에 자식이 있다면
            for (Node n : node.children) { //자식 노드를 재귀 탐색해서 부모 찾기
                addSearch(parent, child, n);
            }
            return;
        }
    }
    
    // 노드 추가
    public void addNode(int[] edge) {
        int parent = edge[0];
        int child = edge[1];
        
        addSearch(parent, child, root);
    }

    // 방문할 리프노드 구하기
    // 리프 노드에 몇 번 도착하는지, 어떤 순서로 도착하는지 알아내는 재귀함수
    public boolean leafSearch(Node node) {
        if (node.children.size() != 0) { // 리프 노드가 아닌 경우, 자식 노드 재귀 탐색
            Node c = node.children.get(node.childIdx); // 이번에 방문할 자식의 인덱스는 지나간 횟수 / 갈림길
            node.childIdx = (node.childIdx + 1) % node.children.size();
            return leafSearch(c);
        }
        
        // 리프 노드인 경우
        visitList.add(node.idx); // 방문 순서 기록
        node.count++; // 지나간 횟수 증가
        
        int curIdx = node.idx - 1; // 1번 노드부터 시작하니 배열 인덱스와 맞추기 위해 -1
        cntArr[curIdx]++; // 현재 리프 노드의 도착 횟수 증가

        if (node.count <= target[curIdx] && target[curIdx] <= node.count * 3) { // 1, 2, 3 중 하나가 도착함
            isAble[curIdx] = true; // 카드 채움 조건 만족
        } else if (isAble[curIdx]) { // 가장 적은 수를 사용해야 하므로 조건을 만족하면 더 추가 xxx
            return false; 
        }
        
        return true;
    }
    
    public boolean setLeafList() {
        boolean flag = true; // 진행 가능 여부
        
        while (flag) {
            if (!leafSearch(root)) { // 루트부터 경로따라 방문할 리프 노드 구하기
                return false;
            }
            
            boolean ansFlag = true; // 정답 여부
            for (boolean bln : isAble) { // 모두 조건 만족하는지 확인
                if (!bln) { // 아직 다 못 채움~
                    ansFlag = false;
                    break;
                }
            }
            
            if (ansFlag) { // 정답 나왔으면
                flag = false; // 진행 멈추기
            }
        }
        
        return true;
    }
    
    public int[] makeAnswer() {
        ArrayList<Integer> list = new ArrayList<>();
        
        for (int i : visitList) {
            int idx = i - 1;
            if (cntArr[idx] > 1) { // 이 노드에 더 와야하는 경우
                cntArr[idx]--; // 한 장 사용
                if (target[idx] <= cntArr[idx] * 3) { // 1을 넣어도 다음에 커버 가능
                    list.add(1);
                    target[idx] -= 1;
                } else {
                    int num = target[idx] - (cntArr[idx] * 3); // 이미 조건 달성 가능함을 알기에 조건에 부합한 숫자 구하기
                    list.add(num);
                    target[idx] -= num;
                }
            }
            else { // 이 노드에 마지막으로 온 경우... cntArr[idx] == 1
                list.add(target[idx]);
            }
        }
        
        int[] ans = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            ans[i] = list.get(i);
        }
        
        return ans;
    }
}

class Solution {
    public int[] solution(int[][] edges, int[] target) {
        int[] answer = {};
        
        // 가장 작은 숫자부터 경로가 결정되니 오름차순 정렬
        Arrays.sort(edges,
                   new Comparator<int[]>() { // 2차원 배열이라 Comporator 재정의
                       @Override
                       public int compare(int[] a, int[] b) {
                        return a[0] - b[0] == 0 ? a[1] - b[1] : a[0] - b[0]; // 부모 오름차순, 자식 오름차순
                       }
                   }
                   );
        
        Tree tree = new Tree(target);
        for (int[] edge : edges) {
            tree.addNode(edge);
        }
        
        Node node = tree.root;
        
        if (!tree.setLeafList()) return new int[]{-1}; // 리프 노드 조건을 만족할 때까지 탐색. 불만족 시 return {-1}
        
        return tree.makeAnswer(); // 조건을 만족하는 배열 return
    } 
}
