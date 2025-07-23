package test.dsa;

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class AbstractTree<E> implements Tree<E> {
    protected int size = 0;

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() {
        size = 0;
        // concrete class should also nullify root
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            private final java.util.Stack<Node<E>> stack = new java.util.Stack<>();
            {
                pushLeft(getRoot());
            }

            private void pushLeft(Node<E> node) {
                while (node != null) {
                    stack.push(node);
                    node = node.left;
                }
            }

            @Override
            public boolean hasNext() {
                return !stack.isEmpty();
            }

            @Override
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                Node<E> node = stack.pop();
                pushLeft(node.right);
                return node.element;
            }
        };
    }

    protected abstract Node<E> getRoot();

    protected static class Node<E> {
        E element;
        Node<E> left, right;

        Node(E element) {
            this.element = element;
        }
    }
}
