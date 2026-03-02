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
 * Unit tests for IntQueue interface.
 */
public class IntQueueTest {

    private IntQueue mQueue;
    private List<Integer> testList;

    @Before
    public void setUp() {
        // LinkedIntQueue-г шалгах бол доорх мөрийг идэвхжүүлж, Array-г comment болгоно
        // mQueue = new LinkedIntQueue(); 
        
        mQueue = new LinkedIntQueue();
        testList = new ArrayList<>(List.of(1, 2, 3));
    }

    @Test
    public void testIsEmpty() {
        assertTrue(mQueue.isEmpty());
    }

    @Test
    public void testNotEmpty() {
        mQueue.enqueue(10);
        assertFalse(mQueue.isEmpty());
    }

    @Test
    public void testPeekEmptyQueue() {
        assertNull(mQueue.peek());
    }

    @Test
    public void testPeekNoEmptyQueue() {
        mQueue.enqueue(5);
        assertEquals(Integer.valueOf(5), mQueue.peek());
    }

    @Test
    public void testEnqueue() {
        for (int i = 0; i < testList.size(); i++) {
            mQueue.enqueue(testList.get(i));
            assertEquals(testList.get(0), mQueue.peek());
            assertEquals(i + 1, mQueue.size());
        }
    }

    @Test
    public void testDequeue() {
        mQueue.enqueue(1);
        mQueue.enqueue(2);
        assertEquals(Integer.valueOf(1), mQueue.dequeue());
        assertEquals(1, mQueue.size());
        assertEquals(Integer.valueOf(2), mQueue.dequeue());
        assertTrue(mQueue.isEmpty());
    }

    @Test
    public void testClear() {
        mQueue.enqueue(1);
        mQueue.enqueue(2);
        mQueue.clear();
        assertEquals(0, mQueue.size());
        assertTrue(mQueue.isEmpty());
    }

    @Test
    public void testEnsureCapacity() {
        // Initial size is 10, so let's push it beyond that
        for (int i = 0; i < 15; i++) {
            mQueue.enqueue(i);
        }
        assertEquals(15, mQueue.size());
        for (int i = 0; i < 15; i++) {
            assertEquals(Integer.valueOf(i), mQueue.dequeue());
        }
    }

    @Test
    public void testEnsureCapacityWithWrapAround() {
        // 1. Fill some elements
        for (int i = 0; i < 5; i++) mQueue.enqueue(i);
        // 2. Remove some to move 'head' away from 0
        for (int i = 0; i < 3; i++) mQueue.dequeue();
        // 3. Fill until it reaches initial capacity (10) to trigger wrap-around
        for (int i = 5; i < 13; i++) mQueue.enqueue(i);
        
        // At this point, ensureCapacity should have triggered correctly
        assertEquals(10, mQueue.size());
        assertEquals(Integer.valueOf(3), mQueue.peek());
    }
    @Test
    public void testDequeueEmptyQueue() {
    // 1. Дараалал хоосон байгаа эсэхийг шалгана
    assertTrue(mQueue.isEmpty());
    
    // 2. Хоосон дарааллаас dequeue хийхэд null ирэх ёстойг шалгана
    assertNull(mQueue.dequeue());
   }

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
                assertEquals(result, mQueue.dequeue());
            }
        }
    }
}