package edu.cmu.cs.cs214.rec02;

import java.util.Arrays;

/**
 * A resizable-array implementation of the {@link IntQueue} interface.
 *
 * BUGS FIXED:
 *  Bug 1 - isEmpty():      was (size >= 0) — always true. Fixed to (size == 0).
 *  Bug 2 - peek():         no null-check. Fixed to return null when empty.
 *  Bug 3 - ensureCapacity(): wrap-around copy index was newData[head - i] — off by one / wrong.
 *                            Fixed to newData[oldCapacity - head + i].
 *
 * @author Alex Lockwood
 * @author Ye Lu
 */
public class ArrayIntQueue implements IntQueue {

    private int[] elementData;
    private int head;
    private int size;

    private static final int INITIAL_SIZE = 10;

    public ArrayIntQueue() {
        elementData = new int[INITIAL_SIZE];
        head = 0;
        size = 0;
    }

    /** {@inheritDoc} */
    public void clear() {
        Arrays.fill(elementData, 0);
        size = 0;
        head = 0;
    }

    /** {@inheritDoc} */
    public Integer dequeue() {
        if (isEmpty()) {
            return null;
        }
        Integer value = elementData[head];
        head = (head + 1) % elementData.length;
        size--;
        return value;
    }

    /** {@inheritDoc} */
    public boolean enqueue(Integer value) {
        ensureCapacity();
        int tail = (head + size) % elementData.length;
        elementData[tail] = value;
        size++;
        return true;
    }

    /** {@inheritDoc} */
    public boolean isEmpty() {
        // BUG 1 FIX: was (size >= 0) which is always true
        return size == 0;
    }

    /** {@inheritDoc} */
    public Integer peek() {
        // BUG 2 FIX: return null when empty
        if (isEmpty()) {
            return null;
        }
        return elementData[head];
    }

    /** {@inheritDoc} */
    public int size() {
        return size;
    }

    private void ensureCapacity() {
        if (size == elementData.length) {
            int oldCapacity = elementData.length;
            int newCapacity = 2 * oldCapacity + 1;
            int[] newData = new int[newCapacity];
            // Copy elements from head to end of array
            for (int i = head; i < oldCapacity; i++) {
                newData[i - head] = elementData[i];
            }
            // BUG 3 FIX: was newData[head - i] — wrong index for wrap-around elements
            for (int i = 0; i < head; i++) {
                newData[oldCapacity - head + i] = elementData[i];
            }
            elementData = newData;
            head = 0;
        }
    }
}
