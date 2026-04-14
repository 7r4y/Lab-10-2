package edu.cmu.cs.cs214.rec02;

import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.*;

/**
 * Unit tests for IntQueue implementations.
 *
 * Part 1: Specification (black-box) tests using LinkedIntQueue
 * Part 2: Structural (white-box) tests using ArrayIntQueue
 *         Covers 100% line coverage for ArrayIntQueue.
 *
 * BUGS FOUND IN ArrayIntQueue:
 *  Bug 1 - isEmpty():      returns (size >= 0) — always true. Fix: (size == 0)
 *  Bug 2 - peek():         no null-check on empty queue. Fix: return null when empty.
 *  Bug 3 - ensureCapacity(): wrap-around copy uses newData[head - i] — wrong index.
 *                            Fix: newData[oldCapacity - head + i] = elementData[i]
 */
public class IntQueueTest {

    private IntQueue mQueue;
    private List<Integer> testList;

    @Before
    public void setUp() {
        // Switch between implementations here:
        // mQueue = new LinkedIntQueue();
        mQueue = new ArrayIntQueue();

        testList = new ArrayList<>(List.of(1, 2, 3));
    }

    // -----------------------------------------------------------------------
    // isEmpty / size
    // -----------------------------------------------------------------------

    @Test
    public void testIsEmpty() {
        assertTrue("New queue should be empty", mQueue.isEmpty());
    }

    @Test
    public void testNotEmpty() {
        mQueue.enqueue(42);
        assertFalse("Queue with one element should not be empty", mQueue.isEmpty());
    }

    @Test
    public void testSizeAfterEnqueue() {
        assertEquals(0, mQueue.size());
        mQueue.enqueue(1);
        assertEquals(1, mQueue.size());
        mQueue.enqueue(2);
        assertEquals(2, mQueue.size());
    }

    @Test
    public void testSizeAfterDequeue() {
        mQueue.enqueue(1);
        mQueue.enqueue(2);
        mQueue.dequeue();
        assertEquals(1, mQueue.size());
    }

    // -----------------------------------------------------------------------
    // peek
    // -----------------------------------------------------------------------

    @Test
    public void testPeekEmptyQueue() {
        assertNull("Peek on empty queue should return null", mQueue.peek());
    }

    @Test
    public void testPeekNoEmptyQueue() {
        mQueue.enqueue(7);
        mQueue.enqueue(8);
        assertEquals(Integer.valueOf(7), mQueue.peek());
        // Peek must NOT remove the element
        assertEquals(Integer.valueOf(7), mQueue.peek());
        assertEquals(2, mQueue.size());
    }

    // -----------------------------------------------------------------------
    // enqueue
    // -----------------------------------------------------------------------

    @Test
    public void testEnqueue() {
        for (int i = 0; i < testList.size(); i++) {
            mQueue.enqueue(testList.get(i));
            assertEquals(testList.get(0), mQueue.peek());
            assertEquals(i + 1, mQueue.size());
        }
    }

    @Test
    public void testEnqueueReturnValue() {
        assertTrue("enqueue should return true", mQueue.enqueue(99));
    }

    // -----------------------------------------------------------------------
    // dequeue
    // -----------------------------------------------------------------------

    @Test
    public void testDequeue() {
        mQueue.enqueue(10);
        mQueue.enqueue(20);
        mQueue.enqueue(30);
        assertEquals(Integer.valueOf(10), mQueue.dequeue());
        assertEquals(Integer.valueOf(20), mQueue.dequeue());
        assertEquals(Integer.valueOf(30), mQueue.dequeue());
        assertTrue(mQueue.isEmpty());
    }

    @Test
    public void testDequeueEmptyQueue() {
        assertNull("Dequeue on empty queue should return null", mQueue.dequeue());
    }

    @Test
    public void testDequeueBecomesEmpty() {
        mQueue.enqueue(5);
        mQueue.dequeue();
        assertTrue("Queue should be empty after removing last element", mQueue.isEmpty());
    }

    // -----------------------------------------------------------------------
    // clear
    // -----------------------------------------------------------------------

    @Test
    public void testClear() {
        mQueue.enqueue(1);
        mQueue.enqueue(2);
        mQueue.clear();
        assertTrue("Queue should be empty after clear", mQueue.isEmpty());
        assertEquals(0, mQueue.size());
    }

    @Test
    public void testClearThenEnqueue() {
        mQueue.enqueue(1);
        mQueue.clear();
        mQueue.enqueue(99);
        assertEquals(Integer.valueOf(99), mQueue.peek());
        assertEquals(1, mQueue.size());
    }

    // -----------------------------------------------------------------------
    // FIFO ordering
    // -----------------------------------------------------------------------

    @Test
    public void testFifoOrder() {
        int[] values = {3, 1, 4, 1, 5, 9, 2, 6};
        for (int v : values) mQueue.enqueue(v);
        for (int v : values) {
            assertEquals(Integer.valueOf(v), mQueue.dequeue());
        }
    }

    // -----------------------------------------------------------------------
    // ensureCapacity — triggers array resize (structural test for ArrayIntQueue)
    // Enqueue more than INITIAL_SIZE (10) elements to force resize.
    // Also tests wrap-around copy by dequeuing some elements first so head != 0.
    // -----------------------------------------------------------------------

    @Test
    public void testResizeFromZeroHead() {
        // Fill beyond initial capacity starting from head=0
        for (int i = 0; i < 15; i++) {
            mQueue.enqueue(i);
        }
        assertEquals(15, mQueue.size());
        for (int i = 0; i < 15; i++) {
            assertEquals(Integer.valueOf(i), mQueue.dequeue());
        }
    }

    @Test
    public void testResizeWithWraparound() {
        // Advance head by dequeuing a few elements, then fill past capacity
        // so that the wrap-around copy path in ensureCapacity is executed.
        for (int i = 0; i < 5; i++) mQueue.enqueue(i);      // enqueue 0-4
        for (int i = 0; i < 5; i++) mQueue.dequeue();       // dequeue all → head=5
        for (int i = 0; i < 11; i++) mQueue.enqueue(i * 10); // 0,10,...,100 — forces resize

        assertEquals(11, mQueue.size());
        for (int i = 0; i < 11; i++) {
            assertEquals(Integer.valueOf(i * 10), mQueue.dequeue());
        }
    }

    // -----------------------------------------------------------------------
    // File-based content test (provided example, kept as-is)
    // -----------------------------------------------------------------------

    @Test
    public void testContent() throws IOException {
        InputStream in = new FileInputStream("src/test/resources/data.txt");
        try (Scanner scanner = new Scanner(in)) {
            scanner.useDelimiter("\\s*fish\\s*");

            List<Integer> correctResult = new ArrayList<>();
            while (scanner.hasNextInt()) {
                int input = scanner.nextInt();
                correctResult.add(input);
                mQueue.enqueue(input);
            }

            for (Integer result : correctResult) {
                assertEquals(mQueue.dequeue(), result);
            }
        }
    }
}
