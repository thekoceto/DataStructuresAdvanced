import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;

public class RedBlackTree<Key extends Comparable<Key>, Value> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private Node root;     // root of the BST

    // BST helper node data type
    private class Node {
        private Key key;           // key
        private Value val;         // associated data
        private Node left, right;  // links to left and right subtrees
        private boolean color;     // color of parent link
        private int size;          // subtree count

        public Node(Key key, Value val, boolean color, int size) {
            this.key = key;
            this.val = val;
            this.color = color;
            this.size = size;
        }
    }

    public RedBlackTree() {
    }

    // is node x red; false if x is null ?
    private boolean isRed(Node current) {
        return current != null && current.color == RED;
    }

    // number of node in subtree rooted at x; 0 if x is null
    private int size(Node current) {
        return current == null ? 0 : current.size;
    }


    /**
     * Returns the number of key-value pairs in this symbol table.
     *
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return size(this.root);
    }

    /**
     * Is this symbol table empty?
     *
     * @return {@code true} if this symbol table is empty and {@code false} otherwise
     */
    public boolean isEmpty() {
        return size(this.root) == 0;
    }

    public Value get(Key key) {
        if (key == null)
            throw new IllegalArgumentException();
        return get(this.root, key);
    }

    // value associated with the given key in subtree rooted at x; null if no such key
    private Value get(Node current, Key key) {
        if(current != null) {
            if(current.key.compareTo(key) > 0 )
                return get(current.left, key);
            else if(current.key.compareTo(key) < 0)
                return get(current.right, key);
            else
                return current.val;
        }

        return null;
    }

    public boolean contains(Key key) {
        return this.get(key) != null;
    }

    public void put(Key key, Value val) {
        if (key == null)
            throw new IllegalArgumentException();
        this.root = this.put(this.root, key, val);
        this.root.color = BLACK;
    }

    // insert the key-value pair in the subtree rooted at h
    private Node put(Node current, Key key, Value val) {
        if (current == null)
            return new Node(key, val, RED,1);

        if (key.compareTo(current.key) < 0)
            current.left = put(current.left, key, val);
        else if (key.compareTo(current.key) > 0)
            current.right = put(current.right, key, val);
        else
            current.val = val;

        // rotation
        if (isRed(current.right) && !isRed(current.left))
            current = rotateLeft(current);
        else if (isRed(current.left) && isRed(current.left.left))
            current = rotateRight(current);
        else if (isRed(current.left) && isRed(current.right))
            flipColors(current);

        current.size = size(current.left) + size(current.right) + 1;

        return current;
    }

    public void deleteMin() {
        if (isEmpty())
            throw new NoSuchElementException();

        // if both children of root are black, set root to red
        if (!isRed(this.root.left) && !isRed(this.root.right))
            this.root.color = RED;

        this.root = deleteMin(this.root);
        if (!isEmpty()) this.root.color = BLACK;
    }

    // delete the key-value pair with the minimum key rooted at h
    private Node deleteMin(Node current) {
        if (current.left == null)
            return null;

        if (!isRed(current.left) && !isRed(current.left.left))
            current = moveRedLeft(current);

        current.left = deleteMin(current.left);
        return balance(current);    }

    public void deleteMax() {
        if (isEmpty())
            throw new NoSuchElementException();

        if (!isRed(this.root.left) && !isRed(this.root.right))
            this.root.color = RED;

        this.root = deleteMax(root);
        if (!isEmpty())
            this.root.color = BLACK;
    }

    // delete the key-value pair with the maximum key rooted at h
    private Node deleteMax(Node current) {
        if (isRed(current.left))
            current = rotateRight(current);

        if (current.right == null)
            return null;

        if (!isRed(current.right) && !isRed(current.right.left))
            current = moveRedRight(current);

        current.right = deleteMax(current.right);

        return balance(current);
    }

    public void delete(Key key) {
        if (key == null)
            throw new IllegalArgumentException();

        if (!contains(key)) return;

        if (!isRed(this.root.left) && !isRed(this.root.right))
            root.color = RED;

        this.root = delete(root, key);

        if (!isEmpty())
            this.root.color = BLACK;
    }

    // delete the key-value pair with the given key rooted at h
    private Node delete(Node current, Key key) {

        if (key.compareTo(current.key) < 0) {
            if (!isRed(current.left) && !isRed(current.left.left))
                current = moveRedLeft(current);
            current.left = delete(current.left, key);
        } else {
            if (isRed(current.left))
                current = rotateRight(current);
            if (key.compareTo(current.key) == 0 && (current.right == null))
                return null;
            if (!isRed(current.right) && !isRed(current.right.left))
                current = moveRedRight(current);
            if (key.compareTo(current.key) == 0) {
                Node x = min(current.right);
                current.key = x.key;
                current.val = x.val;
                current.right = deleteMin(current.right);
            } else
                current.right = delete(current.right, key);
        }
        return balance(current);
    }

    private Node rotateRight(Node current) {
        Node left = current.left;
        current.left = left.right;
        left.right = current;


        left.color = left.right.color;
        left.right.color = RED;

        left.size = current.size;
        current.size = size(current.left) + size(current.right) + 1;

        return left;
    }

    // make a right-leaning link lean to the left
    private Node rotateLeft(Node current) {
        Node right = current.right;
        current.right = right.left;
        right.left = current;

        right.color = right.left.color;
        right.left.color = RED;

        right.size = current.size;
        current.size = size(current.left) + size(current.right) + 1;

        return right;
    }

    // flip the colors of a node and its two children
    private void flipColors(Node current) {
        current.color = !current.color;
        current.left.color = !current.left.color;
        current.right.color = !current.right.color;
    }

    // Assuming that h is red and both h.left and h.left.left
    // are black, make h.left or one of its children red.
    private Node moveRedLeft(Node current) {
        flipColors(current);
        if (isRed(current.right.left)) {
            current.right = rotateRight(current.right);
            current = rotateLeft(current);
            flipColors(current);
        }
        return current;
    }

    // Assuming that h is red and both h.right and h.right.left
    // are black, make h.right or one of its children red.
    private Node moveRedRight(Node current) {
        flipColors(current);
        if (isRed(current.left.left)) {
            current = rotateRight(current);
            flipColors(current);
        }
        return current;
    }

    // restore red-black tree invariant
    private Node balance(Node current) {
        if (isRed(current.right))
            current = rotateLeft(current);
        if (isRed(current.left) && isRed(current.left.left))
            current = rotateRight(current);
        if (isRed(current.left) && isRed(current.right))
            flipColors(current);

        current.size = size(current.left) + size(current.right) + 1;
        return current;
    }

    public int height() {
        return height(this.root);
    }

    private int height(Node current) {
        if (current == null) return -1;
        return 1 + Math.max(height(current.left), height(current.right));
    }

    public Key min() {
        return min(this.root).key;
    }

    // the smallest key in subtree rooted at x; null if no such key
    private Node min(Node current) {
        if (current.left == null)
            return current;
        return min(current.left);
    }

    public Key max() {
        return max(this.root).key;
    }

    // the largest key in the subtree rooted at x; null if no such key
    private Node max(Node current) {
        if (current.right == null)
            return current;
        return max(current.right);
    }

    public Key floor(Key key) {
        return floor(this.root, null, key).key;
    }

    // the largest key in the subtree rooted at x less than or equal to the given key
    private Node floor(Node current, Node smaller, Key key) {
        if (current == null)
            return smaller;

        if (key.compareTo(current.key) < 0)
            return floor(current.left, smaller, key);

        return floor(current.right, current, key);
    }

    public Key ceiling(Key key) {
        return ceiling(this.root, null, key).key;
    }

    // the smallest key in the subtree rooted at x greater than or equal to the given key
    private Node ceiling(Node current, Node larger, Key key) {
        if (current == null)
            return larger;

        if (key.compareTo(current.key) > 0)
            return ceiling(current.right, larger, key);

        return ceiling(current.left, current, key);
    }

    public Key select(int rank) {
        if (rank < 0 || rank >= size())
            throw new IllegalArgumentException();

        return select(this.root, rank);
    }

    // Return key in BST rooted at x of given rank.
    // Precondition: rank is in legal range.
    private Key select(Node current, int rank) {
        if (current == null)
            return null;

        if (size(current.left) > rank)
            return select(current.left, rank);
        else if (size(current.left) < rank)
            return select(current.right, rank - size(current.left) - 1);
        else
            return current.key;
    }

    public int rank(Key key) {
        return rank(this.root, key);
    }

    // number of keys less than key in the subtree rooted at x
    private int rank( Node current, Key key) {
        return current == null ? 0 :
                (current.key.compareTo(key) < 0 ? 1 : 0)
                        + rank(current.left, key)
                        + rank(current.right, key);
    }

    public Iterable<Key> keys() {
        Deque<Key> queue = new ArrayDeque<>();
        keys(this.root, queue, min(), max());
        return queue;
    }

    public Iterable<Key> keys(Key lo, Key hi) {
        Deque<Key> queue = new ArrayDeque<>();
        keys(this.root, queue, lo, hi);
        return queue;
    }

    // add the keys between lo and hi in the subtree rooted at x
    // to the queue
    private void keys(Node current, Deque<Key> queue, Key lo, Key hi) {
        if (current != null){
            if (lo.compareTo(current.key) <= 0 && hi.compareTo(current.key) >= 0)
                queue.offer(current.key);
            if (lo.compareTo(current.key) < 0)
                keys(current.left, queue, lo, hi);
            if (hi.compareTo(current.key) > 0)
                keys(current.right, queue, lo, hi);
        }
    }

    public int size(Key lo, Key hi) {
        return 0;
    }

    private boolean check() {
        return false;
    }

    // does this binary tree satisfy symmetric order?
    // Note: this test also ensures that data structure is a binary tree since order is strict
    private boolean isBST() {
        return false;
    }

    // is the tree rooted at x a BST with all keys strictly between min and max
    // (if min or max is null, treat as empty constraint)
    private boolean isBST(Node x, Key min, Key max) {
        return false;
    }

    // are the size fields correct?
    private boolean isSizeConsistent() {
        return false;
    }

    private boolean isSizeConsistent(Node x) {
        return false;
    }

    // check that ranks are consistent
    private boolean isRankConsistent() {
        return false;
    }

    // Does the tree have no red right links, and at most one (left)
    // red links in a row on any path?
    private boolean isTwoThree() {
        return false;
    }

    private boolean isTwoThree(Node x) {
        return false;
    }

    // do all paths from root to leaf have same number of black edges?
    public boolean isBalanced() {
        return this.root == null || isBalanced(this.root, 0);
    }

    // does every path from the root to a leaf have the given number of black links?
    private boolean isBalanced(Node current, int black) {

        if (current.left != null)
            isBalanced(current.left, isRed(current.left) ? black : --black);
        if (current.right != null)
            isBalanced(current.right, isRed(current.right) ? black : ++black);

        return black == 0;
    }

    // ------------------
    public String inOrderPrint(boolean include) {
        StringBuilder output = new StringBuilder();
        if (this.root != null)
            inOrderPrint(output, "", this.root, include);
        return output.toString();
    }

    private void inOrderPrint(StringBuilder output, String indent, Node current, boolean include) {

        if (current.right != null)
            this.inOrderPrint(output, indent + "   ", current.right, include);

        output.append(indent).append(current.key);
        if (include)
            output.append(":").append(current.color ? "R" : "B");
        output.append(System.lineSeparator());

        if (current.left != null)
            this.inOrderPrint(output, indent + "   ", current.left, include);

    }
}