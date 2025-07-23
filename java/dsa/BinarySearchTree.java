package test.dsa;

import java.util.Comparator;
import java.util.Collection;

public class BinarySearchTree<E> extends AbstractTree<E> {
    private Node<E> root = null;
    private Comparator<? super E> comparator;

    public BinarySearchTree() {}

    public BinarySearchTree(Comparator<? super E> comparator) {
        this.comparator = comparator;
    }

    @Override
    protected Node<E> getRoot() {
        return root;
    }

    @Override
    public boolean add(E element) {
        if (element == null) throw new NullPointerException();
        if (root == null) {
            root = new Node<>(element);
            size++;
            return true;
        }
        Node<E> current = root;
        Node<E> parent = null;
        int cmp = 0;

        while (current != null) {
            cmp = compare(element, current.element);
            parent = current;
            current = (cmp < 0) ? current.left : current.right;
        }

        if (cmp < 0) parent.left = new Node<>(element);
        else if (cmp > 0) parent.right = new Node<>(element);
        else return false; // duplicate

        size++;
        return true;
    }

    @Override
    public boolean contains(Object obj) {
        E element = (E) obj;
        Node<E> current = root;
        while (current != null) {
            int cmp = compare(element, current.element);
            if (cmp == 0) return true;
            current = (cmp < 0) ? current.left : current.right;
        }
        return false;
    }

    @Override
    public E findMin() {
        if (isEmpty()) return null;
        Node<E> current = root;
        while (current.left != null) current = current.left;
        return current.element;
    }

    @Override
    public E findMax() {
        if (isEmpty()) return null;
        Node<E> current = root;
        while (current.right != null) current = current.right;
        return current.element;
    }

    private int compare(E e1, E e2) {
        return comparator != null
            ? comparator.compare(e1, e2)
            : ((Comparable<E>) e1).compareTo(e2);
    }

    @Override
    public void inorderTraversal(java.util.function.Consumer<E> action) {
        inorder(root, action);
    }
    
    @Override
    public void postorderTraversal(java.util.function.Consumer<E> action) {
        
    }
    
    @Override
    public void preorderTraversal(java.util.function.Consumer<E> action) {
        
    }
    @Override
    public boolean remove(Object element){
        return false;
    }
    
    @Override
    public boolean retainAll(Collection<?> element){
        return false;
    }
    
    @Override
    public boolean removeAll(Collection<?> element){
        return false;
    }
    
    @Override
    public boolean containsAll(Collection<?> element){
        return false;
    }
    
    @Override
    public Object[] toArray( ){
        return null;
    }
    
    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }
    
     
    @Override
    public boolean addAll(Collection<? extends E> element){
        return false;
    }
    
    private void inorder(Node<E> node, java.util.function.Consumer<E> action) {
        if (node == null) return;
        inorder(node.left, action);
        action.accept(node.element);
        inorder(node.right, action);
    }

    // Optional: implement remove() and other traversal methods
}
