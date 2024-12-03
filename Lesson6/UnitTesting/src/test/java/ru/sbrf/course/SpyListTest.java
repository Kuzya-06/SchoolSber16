package ru.sbrf.course;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SpyListTest {

//    @Spy
//    private List<String> spyOnList = new ArrayList<String>(100);

    @Spy
    private List<String> spyOnList;

    @Spy
    private HashSet<String> spyOnSet;


    @Test
    public void testListVerify() {
        spyOnList.add("One");
        verify(spyOnList).add("One");
        verifyNoMoreInteractions(spyOnList);
    }

    @Test
    public void testListWhen() {
        when(spyOnList.contains("One")).thenReturn(false);

        spyOnList.add("One");

//        assertEquals("One", spyOnList.get(0));
        assertFalse(spyOnList.contains("One"));
    }

}
