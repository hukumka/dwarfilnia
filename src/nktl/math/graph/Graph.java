package nktl.math.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class Graph<N> implements IGraph<Graph<N>.Node>{
    class Node{
        N data;
        HashSet<Node> neighbors;
    }

    HashSet<Node> nodes = new HashSet<>();

    @Override
    public void connect(Node a, Node b){
        a.neighbors.add(b);
        b.neighbors.add(a);
    }

    @Override
    public boolean isConnected(Node a, Node b){
        return a.neighbors.contains(b);
    }

    public Node newNode(N data){
        Node node = new Node();
        nodes.add(node);
        node.neighbors = new HashSet<>();
        node.data = data;
        return node;
    }

    @Override
    public Iterator<Node> neighbors(Node a){
        return a.neighbors.iterator();
    }

    // tests
    public static void main(String[] args) {
        Graph<Integer> g = new Graph<>();
        ArrayList<Graph<Integer>.Node> nodes = new ArrayList<>();
        for(int i=0; i<5; ++i){
            nodes.add(g.newNode(i));
        }
        g.connect(nodes.get(0), nodes.get(1));
        g.connect(nodes.get(0), nodes.get(2));
        g.connect(nodes.get(0), nodes.get(3));
        g.connect(nodes.get(0), nodes.get(4));
        g.connect(nodes.get(1), nodes.get(2));

        for(var iter=new WidthSearch<>(g, nodes.get(1)); iter.hasNext(); ){
            var value = iter.next();
            System.out.println(String.format("%s %d", value.node.toString(), value.depth));
        }
    }
}
