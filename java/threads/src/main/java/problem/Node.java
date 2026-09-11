package problem;

public class Node  {

    private int data;
    private Node next;
    public Node(int data , Node next){
        this.data=data;
        this.next=next;
    }

    public Node getNext(){
        return this.next;

    }

    public int getData(){
        return this.data;

    }
}
