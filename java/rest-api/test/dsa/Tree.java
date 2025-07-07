package test.dsa;

import java.util.Collection;

public interface Tree<E> extends Collection<E> {
    boolean add(E element);
    boolean remove(Object element);
    boolean contains(Object element);

    int size();
    boolean isEmpty();
    void clear();

    E findMin();
    E findMax();

    // Tree traversal (optional)
    void inorderTraversal(java.util.function.Consumer<E> action);
    void preorderTraversal(java.util.function.Consumer<E> action);
    void postorderTraversal(java.util.function.Consumer<E> action);
}
