package ru.sbrf.course;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.sbrf.course.ClientCategory.BASIC;
import static ru.sbrf.course.ClientCategory.PRIVILEGED;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {

    @Mock
    private DiscountRegistry mockedDiscountRegistry;

    private OrderController orderController;

    private static Client newPrivilegedClient() {
        Client client = new Client();
        client.setCategory(PRIVILEGED);

        return client;
    }

    private static Client newBasicClient() {
        Client client = new Client();
        client.setCategory(BASIC);

        return client;
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        // mockedDiscountRegistry = mock(DiscountRegistry.class);
        orderController = new OrderController(mockedDiscountRegistry);
    }

    @Test(expected = NullPointerException.class)
    public void testShouldThrowNpeIfItemIsNull() {
        orderController.getItemDiscountForClient(new Client(), null);
    }

    @Test
    //@Ignore
    public void testDiscountForClientMinimum15IfPrivileged() {
        Client client = newPrivilegedClient();
        Item item = new Item();

        int discount = orderController.getItemDiscountForClient(client, item);

        assertTrue("Discount for privileged clients should not be less than 15%", discount >= 15);
    }

    @Test
    public void testDiscountForClientEqItemDiscountIfNotPrivileged() {
        Client client = newBasicClient();
        Item item = new Item();
        item.setId(100);

        when(mockedDiscountRegistry.getDiscount(item)).thenReturn(5);

        int discount = orderController.getItemDiscountForClient(client, item);

        assertEquals("Discount for basic clients should equals item discount", 5, discount);
    }

    @Category(SlowTests.class)
    @Test
    public void testAlwaysCheckItemDiscount() {
//        String osName = System.getProperty("os.name");
//        assumeTrue(osName.contains("Windows"));

        Client client = newPrivilegedClient();
        Item item = new Item();

        orderController.getItemDiscountForClient(client, item);

        verify(mockedDiscountRegistry).getDiscount(item);
    }

}
