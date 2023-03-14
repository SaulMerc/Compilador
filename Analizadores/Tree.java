package Analizadores;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Creacion de nodos almacenados en una lista de arreglos
 */
class Node {
    int value;
    ArrayList<Node> children;
    
    public Node(int value) {
        this.value = value;
        children = new ArrayList<Node>();
    }
}

class Tree {
    Node root;
    
    public Tree() {
        root = null;
    }
    /**
     * Añadir un nuevo nodo
     * @param value
     * @param parentValue
     */
    public void addNode(int value, int parentValue) {
        Node newNode = new Node(value);
        
        if (root == null) {
            root = newNode;
        } else {
            Node parent = findNode(root, parentValue);
            
            if (parent == null) {
                System.out.println("Parent not found.");
            } else {
                parent.children.add(newNode);
            }
        }
    }
    /**
     * Metodo para encontrar el nodo
     * @param node
     * @param value
     * @return
     */
    private Node findNode(Node node, int value) {
        if (node.value == value) {
            return node;
        } else {
            for (Node child : node.children) {
                Node foundNode = findNode(child, value);
                
                if (foundNode != null) {
                    return foundNode;
                }
            }
            
            return null;
        }
    }
    /**
     * Metodo para imprimir el arbol en un preorden
     * osea recorrido hasta el mas profundo de cada recorrido
     * de raiz a hojas
     * @param node
     */
    public void preOrderTraversal(Node node) {
        if (node != null) {
            System.out.print(node.value + " ");
            
            for (Node child : node.children) {
                preOrderTraversal(child);
            }
        }
    }
    
    /**
     * Recorrido de hojas a raiz
     * @param node
     */
    public void postOrderTraversal(Node node) {
        if (node != null) {
            for (Node child : node.children) {
                postOrderTraversal(child);
            }
            
            System.out.print(node.value + " ");
        }
    }
    
    /**
     * Recorrido en orden de niveles
     */
    public void levelOrderTraversal() {
        Queue<Node> queue = new LinkedList<Node>();
        queue.add(root);
        
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            System.out.print(node.value + " ");
            
            for (Node child : node.children) {
                queue.add(child);
            }
        }
    }
}
