package edu.cmu.cs.cs214.rec02;

/**
 * A linked-list implementation of the {@link IntQueue} interface.
 * This implementation has no bugs and is used as the reference for
 * specification (black-box) testing.
 *
 * @author Alex Lockwood
 */
public class LinkedIntQueue implements IntQueue {

    /** Node class for the linked list */
    private static class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head;  // front of queue (dequeue from here)
    private Node tail;  // back of queue  (enqueue here)
    private int size;

    /** Constructs an empty queue. */
    public LinkedIntQueue() {
        head = null;
        tail = null;
        size = 0;
    }

    /** {@inheritDoc} */
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    /** {@inheritDoc} */
    public Integer dequeue() {
        if (isEmpty()) {
            return null;
        }
        int value = head.data;
        head = head.next;
        if (head == null) {
            tail = null;
        }
        size--;
        return value;
    }

    /** {@inheritDoc} */
    public boolean enqueue(Integer value) {
        Node newNode = new Node(value);
        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
        return true;
    }

    /** {@inheritDoc} */
    public boolean isEmpty() {
        return size == 0;
    }

    /** {@inheritDoc} */
    public Integer peek() {
        if (isEmpty()) {
            return null;
        }
        return head.data;
    }

    /** {@inheritDoc} */
    public int size() {
        return size;
    }
}
