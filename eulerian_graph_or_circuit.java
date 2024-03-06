import algorithms.*;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import java.util.ArrayDeque;
import java.util.Deque;

class Main {
    public static void main(String[] args) {
        // Uncomment this line if you want to read from a file
        In.open("public/test2.in");
        Out.compareTo("public/test2.out");

        int t = In.readInt();
        for (int i = 0; i < t; i++) {
            testCase();
        }
        
        // Uncomment this line if you want to read from a file
        // In.close();
    }

    public static void testCase() {
        // Input using In.java class
        int n = In.readInt();
        
        In.readLine();
        
        Map<String, Set<Node>> adjacencyPrefix = new HashMap<>();
        Map<String, Set<Node>> adjacencySuffix = new HashMap<>();
        
        Set<Node> nodes = new HashSet<>();
        
        Node firstNode = null;
        
        for(int i = 0; i < n; i++) {
          String curr = In.readLine();
          
          Node node = new Node(curr);
          
          if(firstNode == null) {
            firstNode = node;
          }
      
          if(adjacencyPrefix.containsKey(node.getPrefix())) {
            adjacencyPrefix.get(node.getPrefix()).add(node);
          } else {
            Set<Node> temp = new HashSet<>();
            temp.add(node);
            
            adjacencyPrefix.put(node.getPrefix(), temp);
          }
          
          if(adjacencySuffix.containsKey(node.getSuffix())) {
            adjacencySuffix.get(node.getSuffix()).add(node);
          } else {
            Set<Node> temp = new HashSet<>();
            temp.add(node);
            
            adjacencySuffix.put(node.getSuffix(), temp);
          }
        }
        
        Set<Node> visited = new HashSet<>();
        
        dfs(firstNode, adjacencyPrefix, adjacencySuffix, visited);
        
        if(visited.size() != n) {
          Out.println("no");
          return;
        }
        
        Iterator<Node> it = visited.iterator();
        
        boolean foundSource = false;
        boolean foundSink = false;
        boolean isCycle = true;
        
        while(it.hasNext()) {
          Node node = it.next();
          
          int inDegree = 0;
          
          if(adjacencySuffix.containsKey(node.getPrefix())) {
            inDegree = adjacencySuffix.get(node.getPrefix()).size();
          }
          
          int outDegree = 0;
          
          if(adjacencyPrefix.containsKey(node.getSuffix())) {
            outDegree = adjacencyPrefix.get(node.getSuffix()).size();
          }
          
          if(inDegree != outDegree) {
            if(inDegree + 1 == outDegree) {
              isCycle = false;
              if(foundSource) {
                Out.println("no");
                return;
              } else {
                foundSource = true;
              }
            } else if (outDegree + 1 == inDegree) {
              isCycle = false;
              if(foundSink) {
                Out.println("no");
                return;
              } else {
                foundSink = true;
              }
            } 
          }
        } 
        
        if(isCycle) {
          it = visited.iterator();
          
          while(it.hasNext()) {
            Node next = it.next();
            
            if(adjacencyPrefix.get(next.getSuffix()).size() != adjacencySuffix.get(next.getPrefix()).size()) {
              Out.println("no");
              return;
            }
          }
        }

        Out.println("yes");
    }
    
    public static void dfs(Node start, Map<String, Set<Node>> adjacencyPrefix, Map<String, Set<Node>> adjacencySuffix, Set<Node> visited) {
        Deque<Node> stack = new ArrayDeque<>();
        stack.push(start);
        
        while (!stack.isEmpty()) {
            Node curr = stack.pop();
            visited.add(curr);
            
            String prefix = curr.getPrefix();
            String suffix = curr.getSuffix();
            
            if (adjacencyPrefix.containsKey(suffix)) {
                for (Node node : adjacencyPrefix.get(suffix)) {
                    if (!visited.contains(node)) {
                      stack.push(node);
                    }
                }
            }
            
            if (adjacencySuffix.containsKey(prefix)) {
                for (Node node : adjacencySuffix.get(prefix)) {
                    if (!visited.contains(node)) {
                      stack.push(node);
                    }
                }
            }
        }
    }
}

class Node {
  String key;
  Set<Node> to;
  
  public Node(String key) {
    this.to = new HashSet<>();
    this.key = key;
  }
  
  public String getKey() {
    return key;
  }
  
  public String getPrefix() {
    return key.substring(0, 2);
  }
  
  public String getSuffix() {
    return key.substring(1, 3);
  }
  
  public void addNeighbor(Node node) {
    to.add(node);
  }
  
  public boolean equals(Object o) {
    if(!(o instanceof Node)) return false;
    
    Node other = (Node) o;
    
    return this.key.equals(other.key) && this.to.equals(other.to);
  }
}