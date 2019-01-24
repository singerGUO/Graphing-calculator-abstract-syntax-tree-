
package datastructures;

import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertTrue;

/**
 * This file should contain any tests that check and make sure your
 * delete method is efficient.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDeleteStress extends TestDoubleLinkedList {
    //    @Test(timeout=SECOND)
//    public void testExample() {
//        // Feel free to modify or delete this dummy test.
//        assertTrue(true);
//        assertEquals(3, 3);
//    }
    @Test(timeout = 15 * SECOND)
    public void testDeleteAtEndIsEfficient() {
        IList<Integer> list = new DoubleLinkedList<>();
        int cap = 5000000;
        //0->1....->4999999

        for (int i = 0; i < cap; i++) {
            list.add(i);
        }

        for (int i = cap; i > 0; i--) {
            list.delete(i - 1);
            assertEquals(i - 1, list.size());
        }

    }

    @Test(timeout = 15 * SECOND)
    public void testDeleteNearEndIsEfficient() {
        IList<Integer> list = new DoubleLinkedList<>();


        int cap = 5000000;

        for (int i = 1; i <= cap; i++) {
            list.add(i);
        }
        //In this case, I delete the second to the last node each iteration.
        for (int i = cap - 1; i >= 1; i--) {
            int value = list.delete(i - 1);
            assertEquals(i, value);
        }
        // we can't delete(-1);
        assertEquals(1, list.size());
    }


    @Test(timeout = 15 * SECOND)
    public void testDeleteAtFrontIsEfficient() {
        IList<Integer> list = new DoubleLinkedList<>();


        int cap = 5000000;

        for (int i = 0; i < cap; i++) {
            list.add(i);
        }

        for (int i = 0; i < cap; i++) {
            int value = list.delete(0);
            assertEquals(i, value);
        }

        assertEquals(0, list.size());
    }





}
