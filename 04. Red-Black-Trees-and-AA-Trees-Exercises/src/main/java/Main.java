
public class Main {
    public static void main(String[] args) {
        RedBlackTree<String, Integer> redBlackTree = new RedBlackTree<>();
        String[] input = { "S", "E", "A", "R", "C", "H", "E", "X", "A", "M", "P", "L", "E" };
        for (int i = 0; i < 4; i++) {
            redBlackTree.put(input[i], i);
            System.out.println(i + ":add -> key: " + input[i]);
//            System.out.println(redBlackTree.inOrderPrint(true));
        }
        for (int i = 4; i < input.length; i++) {
            redBlackTree.put(input[i], i);
            System.out.println(i + ":add -> key: " + input[i]);

            System.out.println(redBlackTree.inOrderPrint(true));
            System.out.println(redBlackTree.isBalanced());
        }
        System.out.println(redBlackTree.inOrderPrint(true));
        System.out.println(redBlackTree.isBalanced());
    }
}
