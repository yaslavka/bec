package matrixbackend.helper;


import matrixbackend.entity.Matrix;
import org.springframework.stereotype.Component;

@Component
public class Node {
    public Matrix parent;
    public Node left, right;

    public Node(Matrix item)
    {
        parent = item;
        left = null;
        right = null;
    }

    public Node() {
    }
}