package nktl.math.graph;

import java.util.Iterator;

public interface IGraph<T> {
    Iterator<T> neighbors(T node);
    void connect(T a, T b);
    boolean isConnected(T a, T b);
}
