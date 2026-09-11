package problem;

import java.util.LinkedList;

public class Example {


    public static void main(String[] args) {
        Node n1 = new Node(5, null);
        Node n2 = new Node(10, n1);
        Node n3 = new Node(15, n2);
        Node n4 = new Node(20, n3);
        Node n5 = new Node(25, n4);
        Node counter = n5;
        do {
            //System.out.println(counter.getData());
            counter = counter.getNext();
        } while (counter.getNext() != null);
        System.out.println(counter.getData());
        System.out.println(getMid(n5));

    }

    public static Node getMid(Node head){
        int len = 0;
        Node counter = head;
        if(head == null) {
            return null;
        }
        while(counter.getNext()!= null) {
            len ++;
            counter= counter.getNext();
        }
        len+=1;
        System.out.println(len );
        counter = head;
        //int mid = Math.len/2;
        for (int i =0;i<len;i++){
            counter= counter.getNext();
        }
        return counter;
    }
}
