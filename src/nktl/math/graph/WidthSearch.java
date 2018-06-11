package nktl.math.graph;


import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class WidthSearch<N> implements Iterator<WidthSearch<N>.Data>{
    LinkedList<Data> queue;
    HashSet<N> visited;
    IGraph<N> graph;

    class Data{
        N node;
        int depth;

        Data(N node, int depth){
            this.node = node;
            this.depth = depth;
        }

        public N getNode(){
            return this.node;
        }
        public int getDepth(){
            return depth;
        }
    }

    public WidthSearch(IGraph<N> g, N start){
        graph = g;
        queue = new LinkedList<>();
        visited = new HashSet<>();
        queue.push(new Data(start, 0));
    }

    @Override
    public boolean hasNext(){
        return !queue.isEmpty();
    }

    @Override
    public Data next(){
        Data next_ = queue.pollLast();
        visited.add(next_.node);
        for(var iter=graph.neighbors(next_.node); iter.hasNext(); ){
            var n = iter.next();
            if(!visited.contains(n)){
                queue.push(new Data(n, next_.depth + 1));
                visited.add(n);
            }
        }
        return next_;
    }
}
