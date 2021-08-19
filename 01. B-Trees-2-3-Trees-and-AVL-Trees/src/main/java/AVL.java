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

        node = balance(node, item);

        updateHeight(node);

        return node;
    }

    private Node<T>  balance(Node<T> node, T item) {
        int balanceFactor = getBalanceFactor(node);

        if (balanceFactor > 1) {
            // ----- Left Right Case -----
            //   rotateLeft(8)    rotateRight(2)
            //       8    >>       8   >>       5
            //      / \   >>      / \  >>    /     \
            //     2   d  >>     5   d >>   2       8
            //    / \     >>    / \    >>  / \     / \
            //   a   5    >>   2   c   >> a   b   c   d
            //      / \   >>  / \      >>
            //     b   c  >> a   b     >>

            // ----- Left Left Case -----
            //       rotateRight(2)
            //       8    >>        5
            //      / \   >>     /     \
            //     5   d  >>    2       8
            //    / \     >>  / \      / \
            //   2   c    >> a   b    c   d
            //  / \       >>
            // a   b      >>
            if (item.compareTo(node.left.value) > 0)
                node.left = rotateLeft(node.left);
            node = rotateRight(node);
        }
        else if (balanceFactor < -1) {
            // ----- Right Left Case -----
            //   rotateRight(8)    rotateLeft(2)
            //   2      >>   2        >>        5
            //  / \     >>  / \       >>     /     \
            // a   8    >> a   5      >>    2       8
            //    / \   >>    / \     >>  / \      / \
            //   5   d  >>   b   8    >> a   b    c   d
            //  / \     >>      / \   >>
            // b   c    >>     c   d  >>

            // ----- Right Right Case -----
            //       rotateLeft(2)
            //   2        >>        5
            //  / \       >>     /     \
            // a   5      >>    2       8
            //    / \     >>  / \      / \
            //   b   8    >> a   b    c   d
            //      / \   >>
            //     c   d  >>
            if (item.compareTo(node.right.value) < 0)
                node.right = rotateRight(node.right);
            node = rotateLeft(node);
        }

        return node;
    }

    private int getBalanceFactor(Node<T> node) {
        return getHeight(node.left) - getHeight(node.right);
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

    private static <T extends Comparable<T>> int getHeight(Node<T> node) {
        return node == null ? 0 : node.height;
    }

    private Node<T> rotateRight (Node<T> node) {
        //     5   >>   2
        //    / \  >>  / \
        //   2   # >> #   5
        //  / \    >>    / \
        // #   3   >>   3   #

        Node<T> top = node.left;
        node.left = node.left.right;
        top.right = node;

        updateHeight(node);
        updateHeight(top);

        return top;
    }

    private Node<T> rotateLeft (Node<T> node) {
        //   2     >>     5
        //  / \    >>    / \
        // #   5   >>   2   #
        //    / \  >>  / \
        //   3   # >> #   3

        Node<T> top = node.right;
        node.right = node.right.left;
        top.left = node;

        updateHeight(node);
        updateHeight(top);

        return top;
    }

    // ------------------
    public String inOrderPrint() {
        StringBuilder output = new StringBuilder();
        if (this.root != null)
            inOrderPrint(output, "", this.root);
        return output.toString();
    }

    private void inOrderPrint(StringBuilder output, String indent, Node<T> current) {

        if (current.right != null)
            this.inOrderPrint(output, indent + "    ", current.right);

        output
                .append(indent)
                .append(current.value).append("-").append(current.height)
                .append(System.lineSeparator());

        if (current.left != null)
            this.inOrderPrint(output, indent + "    ", current.left);

    }
}
