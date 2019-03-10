package datastructures;

import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
//import misc.exceptions.EmptyContainerException;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

//import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * This class should contain all the tests you implement to verify that
 * your 'delete' method behaves as specified.
 *
 * This test _extends_ your TestDoubleLinkedList class. This means that when
 * you run this test, not only will your tests run, all of the ones in
 * TestDoubleLinkedList will also run.
 *
 * This also means that you can use any helper methods defined within
 * TestDoubleLinkedList here. In particular, you may find using the
 * 'assertListMatches' and 'makeBasicList' helper methods to be useful.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDeleteFunctionality extends TestDoubleLinkedList {


    @Test(timeout = SECOND)
    public void basicTestDelete() {
        IList<String> list = this.makeBasicList();
        list.add("d");
        list.add("e");


        list.delete(2);
        this.assertListMatches(new String[]{"a", "b", "d", "e"}, list);

        list.delete(1);
        this.assertListMatches(new String[]{"a", "d", "e"}, list);

        assertEquals("d", list.delete(1));
    }


    @Test(timeout=SECOND)
    public void testDeleteMany() {
        IList<Integer> list = new DoubleLinkedList<>();
        int cap = 1000;

        for (int i = 1; i <= cap; i++) {
            list.add(i);
        }


        for (int i = cap - 1; i >= 0; i--) {
            int value = list.delete(i);
            assertEquals(i + 1, value);

        }

        assertEquals(0, list.size());
    }


    @Test(timeout = SECOND)
    public void testDeleteOnEmptyListThrowsException() {
        IList<String> list = this.makeBasicList();
        list.delete(0);
        list.delete(0);
        list.delete(0);

        try {
            list.delete(0);
            // We didn't throw an exception? Fail now.
            fail("IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // Do nothing: this is ok
        }
    }


    @Test(timeout = SECOND)
    public void testDeleteOutOfBoundsThrowsException() {
        IList<String> list = this.makeBasicList();

        try {
            list.delete(-1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // Do nothing: this is ok
        }

        try {
            list.delete(4);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // Do nothing: this is ok
        }
    }


}