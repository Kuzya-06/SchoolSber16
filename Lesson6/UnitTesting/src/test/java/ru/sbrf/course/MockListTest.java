package ru.sbrf.course;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MockListTest {

    @Mock
    private List<String> mockedList;

    @Mock
    private List<String> mockedList1;

    @Mock
    private List<String> mockedList2;

    @Mock
    private List<String> mockedList3;

    @Test
    public void testList() {
        mockedList1.add("Called first");
        mockedList2.add("Called second");

        InOrder inOrder = inOrder(mockedList1, mockedList2);

        inOrder.verify(mockedList1).add("Called first");
        inOrder.verify(mockedList2).add("Called second");
    }

    @Test
    public void testVerifyList() {
        // Using mock object
        mockedList.add("One");
        mockedList.add("Two");
        mockedList.add("Two");

        // Verification
        verify(mockedList).add("One");
        verify(mockedList, times(2)).add("Two");
        verify(mockedList, never()).add("Three");
        verify(mockedList, atLeast(3)).add(anyString());
    }

    @Test
    public void testWhenReturnList() {
        when(mockedList.get(anyInt())).thenReturn("Four");
        when(mockedList.get(5)).thenReturn("Five");
        doReturn("Six").when(mockedList).get(100);

        assertEquals("Four", mockedList.get(500));
        assertEquals("Five", mockedList.get(5));
        assertEquals("Six", mockedList.get(100));
    }

    @Test(expected = NullPointerException.class)
    public void testShouldThrowNullPointerException() {
        doThrow(NullPointerException.class).when(mockedList).clear();
        mockedList.clear();
    }

    @Test(expected = IllegalStateException.class)
    public void testShouldThrowIllegalStateException() {
        when(mockedList.get(anyInt())).thenThrow(new IllegalStateException());
        mockedList.get(0);
    }

    @After
    public void after() {
        // verifyZeroInteractions(mockedList);
//        verifyZeroInteractions(mockedList3);
        verifyNoInteractions(mockedList3);
    }

}
