package DistCenter.Queue;

import java.util.LinkedList;

/**
 * The generic queue class
 */
public class GenericQueue<E> {
    private LinkedList<E> list = new LinkedList<>();

    /**
     * This method inserts an element at the back of the queue
     * @param e the element to be inserted at the back of the queue
     */
    public void enqueue(E e){
        list.addLast(e);
    }

    /**
     * This method removes an element at the beginning og the queue
     * @return the element that is removed
     */
    public E dequeue(){
        return list.removeFirst();
    }

    /**
     * This method returns the size of the queue
     * @return the size of the queue
     */
    public int getSize(){
        return list.size();
    }

    /**
     * This method determines the printing format of the queue
     */
    @Override
    public String toString(){
        return list.toString();
    }
}

