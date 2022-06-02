package matrixbackend.helper;

import matrixbackend.SideMatrix;
import matrixbackend.entity.Matrix;

import matrixbackend.repository.MatrixRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class BinaryTree {

    public Node root;

    @Autowired
    MatrixRepository matrixRepository;


    public BinaryTree() {
    }
// Do not touch
    public LinkedList traverseLevelOrder() {
        LinkedList<Matrix> linkedList = new LinkedList();
        if (root == null) {
            return linkedList;
        }
        Queue<Node> nodes = new LinkedList<>();
        nodes.add(root);
        while (!nodes.isEmpty()) {
            try {
                Node node = nodes.remove();
                linkedList.add(node.parent);
                if (node.left != null) {
                    node.left.parent.setParentMatrix(node.parent);
                    node.left.parent.setSideMatrix(SideMatrix.LEFT);
                    nodes.add(node.left);
                }
                if (node.right != null) {
                    node.right.parent.setParentMatrix(node.parent);
                    node.right.parent.setSideMatrix(SideMatrix.RIGHT);
                    nodes.add(node.right);
                }
            } catch (NullPointerException e) {
            }
        }
        return linkedList;
    }


    public LinkedList traverseLevelOrderToList() {
        LinkedList<Matrix> linkedList = new LinkedList();
        if (root == null) {
            return linkedList;
        }
        Queue<Node> nodes = new LinkedList<>();
        nodes.add(root);

        while (!nodes.isEmpty()) {
            try {
                Node node = nodes.remove();
                linkedList.add(node.parent);
                if (node.left != null) {
                    nodes.add(node.left);
                }
                if (node.right != null) {
                    nodes.add(node.right);
                }


            } catch (NullPointerException e) {
            }
        }

        return linkedList;
    }



    public Node insertLevelOrderMatrix(List<Matrix> matrices, Node root, int i) {
        // Base case for recursion
        if (i < matrices.size()) {
            Node temp = new Node(matrices.get(i));
            root = temp;

            // insert left child
            root.left = insertLevelOrderMatrix(matrices, root.left, 2 * i + 1);
            // insert right child
            root.right = insertLevelOrderMatrix(matrices, root.right, 2 * i + 2);
        }
        return root;
    }


    public Node addRecursive(Node current, Matrix value) {

        if (current == null) {
            return new Node(value);
        }
            if (current.left == null &&
                    value.getParentMatrix().getId() == current.parent.getId() &&
                    value.getSideMatrix().value== SideMatrix.LEFT.value
                    ) {
//            System.out.println(value.getId());
                current.left = addRecursive(current.left, value);
            }

            else if (current.right == null &&
                    value.getParentMatrix().getId() == current.parent.getId() &&
                    value.getSideMatrix().value==SideMatrix.RIGHT.value
                    ) {
//            System.out.println(value.getId());
                current.right = addRecursive(current.right, value);
            }
        else {
            addRecursive(current.left,value);

            addRecursive(current.right,value);
        }
        return current;
    }
}
