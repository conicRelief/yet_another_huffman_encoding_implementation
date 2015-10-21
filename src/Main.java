import sun.jvm.hotspot.memory.Dictionary;

import java.util.*;

/**
 * Created by otto on 10/20/15.
 */
public class Main {

//      Huffman Enconding is not treated well in this code. While compression is reached compressing our 200 bit ASCII
//      encoded example into a shorter 78 bit encoding, adequate
//      datastructures have not been put into place to make this code scale.
    public static void main(String[] args)
    {
//          String That we will be encoding.
        String textToBeCompressed = "Eerie eyes seen near lake";

//          I store word frequencies in their very own hashmap.
        Map<Character, Integer> frequency = new HashMap<Character, Integer>();
        for(int i = 0; i < textToBeCompressed.length(); i ++)
        {
            Character temp = new Character(textToBeCompressed.toCharArray()[i]);
            if(frequency.get(temp) != null)
            {
                frequency.put(temp, frequency.get(temp) + 1);
            }
            else
            {
                frequency.put(temp, 1);
            }
        }
//          Im prioritizing trees based on their frequencies and merging them.
        PriorityQueue<Tree> treePriorityQueue = new PriorityQueue<Tree>();
        System.out.println("__Frequencies__");
        for(Character i: frequency.keySet())
        {
            System.out.println(i + " :: " + frequency.get(i));
            Tree<Character> subTree = new Tree<Character>(i);
            subTree.frequency = frequency.get(i);
            treePriorityQueue.add(subTree);
        }

        while(treePriorityQueue.size()>1)
        {
            Tree<Character> mergeTree = new Tree<Character>('~');
            Tree<Character> rightTree = treePriorityQueue.poll();
            Tree<Character> leftTree  = treePriorityQueue.poll();
            mergeTree.frequency = rightTree.frequency + leftTree.frequency;
            mergeTree.root.righty = rightTree.root;
            mergeTree.root.lefty  = leftTree.root;
            treePriorityQueue.add(mergeTree);
        }

        System.out.println("\n __Computed Encodings__");

        Tree<Character> huffmanTree = treePriorityQueue.poll();
        Map<Character,String> map = createEncodingMap(huffmanTree);

        int sum = 0;
        String encodedString = "";
        for(int i = 0; i < textToBeCompressed.length(); i++)
        {
            sum += map.get(textToBeCompressed.toCharArray()[i]).length();
            encodedString += map.get(textToBeCompressed.toCharArray()[i]);
        }
        System.out.println("\n__Normal Ascii Encoding__\n" + textToBeCompressed.length()*8 + " bits\n");
        System.out.println("__Huffman Encoding__\n"+ sum+ " bits");
        System.out.println("\n__Encoded Sentence__\n" + encodedString);

    }

    public static Map<Character, String> createEncodingMap(Tree<Character> huffmanTree)
    {
//    This Guy return a hasmap that maps characters to their respective codes.
        Map<Character,String> encodingMap = new HashMap<Character, String>();
        Tree.Node n = huffmanTree.root;
        printEncoding(n,"",encodingMap);
        return  encodingMap;
    }

    public static void printEncoding(Tree.Node n, String string, Map<Character,String> map)
    {
        if(n.righty == null && n.lefty == null)
        {
            map.put((Character) n.data, string);
            System.out.println(n.data + "::" + string);
        }
        else
        {
            if(n.lefty != null){
                printEncoding(n.lefty, string + "0",map);
            }
            if(n.righty!= null){
                printEncoding(n.righty, string + "1",map);
            }
        }
    }

    public static class Tree<T> implements Comparable<Tree> {
        private Node<T> root;
        public int frequency = -1;
        public Tree(T rootData) {
            root = new Node<T>();
            root.data = rootData;
            root.righty = null;
            root.lefty = null;
        }
        @Override
        public int compareTo(Tree t) {
            return this.frequency > t.frequency ? 1 : -1;
        }
        public class Node<T> {
            private T data;
            private Node<T> parent;
            private Node<T> lefty;
            private Node<T> righty;;
        }
    }
}
