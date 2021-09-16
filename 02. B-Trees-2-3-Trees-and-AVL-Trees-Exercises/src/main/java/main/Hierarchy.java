package main;

import java.util.*;
import java.util.stream.Collectors;

public class Hierarchy<T> implements IHierarchy<T> {

    private final Node<T> root;
    private final LinkedHashMap<T, Node<T>> mapNode;

    private static class Node<T>{
        private final T value;
        private Node<T> parent;
        private final List<Node<T>> children;

        public Node(T value) {
            this.value = value;
            this.parent = null;
            this.children = new ArrayList<>();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node<?> node = (Node<?>) o;
            return value.equals(node.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }


    public Hierarchy(T value) {
        this.root = new Node<>(value);
        this.mapNode = new LinkedHashMap<>();
        this.mapNode.put(value, this.root);
    }

    @Override
    public int getCount() {
        return this.mapNode.size();
    }

    @Override
    public void add(T element, T child) {
        Node<T> node = this.mapNode.get(element);

        if (node == null || this.mapNode.containsKey(child))
            throw new IllegalArgumentException();

        Node<T> toAdd = new Node<>(child);

        toAdd.parent = node;
        node.children.add(toAdd);
        this.mapNode.put(child, toAdd);
    }

    @Override
    public void remove(T element) {

        Node<T> node = this.mapNode.get(element);

        if (node == null )
            throw new IllegalArgumentException();

        if (node == this.root)
            throw new IllegalStateException();

        node.children.forEach(child -> child.parent = node.parent);
        node.parent.children.addAll(node.children);
        node.parent.children.remove(node);
        this.mapNode.remove(element);
    }

    @Override
    public Iterable<T> getChildren(T element) {
        Node<T> node = this.mapNode.get(element);

        if (node == null)
            throw new IllegalArgumentException();

        return node.children
                .stream()
                .map(e -> e.value)
                .collect(Collectors.toList());
    }

//    private Node<T> findNode(T element) {
//        return findNode(this.root, element);
//    }
//
//    private Node<T> findNode(Node<T> current, T element) {
//        if (current == null || current.value.equals(element))
//            return current;
//
//        current.children.forEach(child -> findNode(child, element));
//
//        return null;
//    }

    @Override
    public T getParent(T element) {
        Node<T> node = this.mapNode.get(element);

        if (node == null)
            throw new IllegalArgumentException();

        return node.parent == null ? null : node.parent.value;
    }

    @Override
    public boolean contains(T element) {
        return this.mapNode.get(element) != null;
    }

    @Override
    public Iterable<T> getCommonElements(IHierarchy<T> other) {
        List<T> result = new ArrayList<>();
        getCommonElements(this.root, other, result);
        return result;
    }

    private void getCommonElements(Node<T> current, IHierarchy<T> other, List<T> result) {
        if (current == null)
            return;

        if (other.contains(current.value))
            result.add(current.value);

        current.children.forEach(child -> getCommonElements(child, other, result));
    }

//    @Override
//    public Iterator<T> iterator() {
//        return this.mapNode.keySet().iterator();
//    }

    @Override
    public Iterator<T> iterator() {
        List<T> result = new ArrayList<>();

        ArrayDeque<Node<T>> queue = new ArrayDeque<>();
        queue.offer(this.root);

        while(!queue.isEmpty()) {
            Node<T> current = queue.poll();
            result.add(current.value);
            current.children.forEach(queue::offer);
        }

        return result.iterator();

//        List<T> result = new ArrayList<>();
//        getAllNodeValue(this.root, result);
//        return result.iterator();
    }

//    private void getAllNodeValue(Node<T> current, List<T> result) {
//        if (current == null)
//            return;
//        result.add(current.value);
//        current.children.forEach(child -> getAllNodeValue(child, result));
//    }
}
