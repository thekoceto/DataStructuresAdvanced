public class Main {
    public static void main(String[] args) {
        AVL<Integer> avl = new AVL<>();

        for (int i = 0; i < 31; i++)
            avl.insert(i);

        System.out.println(avl.inOrderPrint());
    }
}
