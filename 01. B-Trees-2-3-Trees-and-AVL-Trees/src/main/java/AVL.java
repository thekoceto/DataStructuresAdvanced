import java.util.Collection;
import java.util.function.Consumer;

public class AVL<T extends Comparable<T>> {

    private Node<T> root;

    public Node<T> getRoot() {
        return this.root;
    }

    public boolean contains(T item) {
        Node<T> node = this.search(this.root, item);
        return node != null;
    }

    public void insert(T item) {
        this.root = this.insert(this.root, item);
    }

    public void eachInOrder(Consumer<T> consumer) {
        this.eachInOrder(this.root, consumer);
    }

    private void eachInOrder(Node<T> node, Consumer<T> action) {
        if (node == null) {
            return;
        }

        this.eachInOrder(node.left, action);
        action.accept(node.value);
        this.eachInOrder(node.right, action);
    }

    private Node<T> insert(Node<T> node, T item) {
        if (node == null) {
            return new Node<>(item);
        }

        int cmp = item.compareTo(node.value);
        if (cmp < 0) {
            node.left = this.insert(node.left, item);
        } else if (cmp > 0) {
            node.right = this.insert(node.right, item);
        }




        updateHeight(node);
        return node;
    }

    private Node<T> search(Node<T> node, T item) {
        if (node == null) {
            return null;
        }

        int cmp = item.compareTo(node.value);
        if (cmp < 0) {
            return search(node.left, item);
        } else if (cmp > 0) {
            return search(node.right, item);
        }

        return node;
    }

    private void updateHeight(Node<T> node) {
        node.height =
                getHeight(node.left) > getHeight(node.right) ?
                getHeight(node.left) + 1 :
                getHeight(node.right) + 1;
    }

    private <T extends Comparable<T>> int getHeight(Node<T> node) {
        return node == null ? 0 : node.height;
    }

    private Node<T> rotateRight (Node<T> node) {
        //         [p(5)]     >>      [p(2)]
        //        /      \    >>     /     \
        //    [l(2)]   [r(#)] >> [l(#)]  [r(5)]
        //    /    \          >>         /    \
        // [l(#)] [r(3)]      >>      [l(3)] [r(#)]

        Node<T> parent = node.left;
        node.left = parent.right;
        parent.right = node;

        updateHeight(node);
        updateHeight(parent);

        return parent;
    }

    private Node<T> rotateLeft (Node<T> node) {
        //      [p(2)]        >>         [p(5)]
        //     /     \        >>        /      \
        // [l(#)]  [r(5)]     >>    [l(2)]   [r(#)]
        //         /    \     >>    /    \
        //      [l(3)] [r(#)] >> [l(#)] [r(3)]

        Node<T> parent = node.right;
        node.right = parent.left;
        parent.left = node;

        updateHeight(node);
        updateHeight(parent);

        return parent;
    }
}
