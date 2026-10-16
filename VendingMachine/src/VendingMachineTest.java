import static org.junit.Assert.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class VendingMachineTest {

    VendingMachine vendor;
    VendingMachineItem item1;
    VendingMachineItem item2;
    VendingMachineItem item3;
    VendingMachineItem item4;
    VendingMachineItem item5;
    VendingMachineItem item6;
    VendingMachineItem item7;
    VendingMachineItem item8;
    VendingMachineItem item9;
    VendingMachineItem item0;

    @BeforeEach
    void setUp() {
        vendor = new VendingMachine();
        item1 = new VendingMachineItem("Cola", 2.50);
        item2 = new VendingMachineItem("Water", 1.00);
        item3 = new VendingMachineItem("Tea", 1.50);
        item4 = new VendingMachineItem("Scone", 2.00);
    }

    @AfterEach
    void tearDown() {
        vendor = null;
        item1 = null;
        item2 = null;
        item3 = null;
        item4 = null;
        item5 = null;
        item6 = null;
        item7 = null;
        item8 = null;
        item9 = null;
        item0 = null;
    }

    @Test
    void testVendingConstructor() {
        assert(vendor != null);
        assert(vendor.getBalance() == 0.0);
        assert(vendor.getItem("A") == null);
        assert(vendor.getItem("B") == null);
        assert(vendor.getItem("C") == null);
        assert(vendor.getItem("D") == null);
    }

    @Test
    void testAddItem() {
        vendor.addItem(item1, "A");
        assert(vendor.getItem("A") == item1);
        vendor.addItem(item2, "B");
        assert(vendor.getItem("B") == item2);
        vendor.addItem(item3, "C");
        assert(vendor.getItem("C") == item3);
        vendor.addItem(item4, "D");
        assert(vendor.getItem("D") == item4);
        assertThrows(VendingMachineException.class, () -> vendor.addItem(item4, "A"));
    }

    @Test
    void testGetBalance() {
        assertEquals(0.0, vendor.getBalance(), 0.001);
        vendor.insertMoney(5.23);
        assertEquals(5.23, vendor.getBalance(), 0.001);
        vendor.returnChange();
        assertEquals(0.0, vendor.getBalance(), 0.001);
    }

    @Test
    void testGetItem() {
        vendor.addItem(item1, "A");
        VendingMachineItem itemGet = vendor.getItem("A");
        assert(itemGet.getName() == item1.getName());
        assertEquals(itemGet.getPrice(), item1.getPrice(), 0.001);
        assert(vendor.getItem("B") == null);
        assertThrows(VendingMachineException.class, () -> vendor.getItem("Spud"));
    }

    @Test
    void testInsertMoney() {
        vendor.insertMoney(5.23);
        assertEquals(5.23, vendor.getBalance(), 0.001);
        vendor.insertMoney(0.00);
        assertEquals(5.23, vendor.getBalance(), 0.001);
        assertThrows(VendingMachineException.class, () -> vendor.insertMoney(-1.00));
    }

    @Test
    void testMakePurchase() {
        vendor.addItem(item1, "A");
        vendor.addItem(item2, "B");
        vendor.insertMoney(10.00);
        assert(vendor.makePurchase("A"));
        assert(vendor.makePurchase("B"));
    }

    @Test
    void testMakePurchasePoor() {
        vendor.addItem(item3, "C");
        vendor.addItem(item4, "D");
        assert(!vendor.makePurchase("C"));
        assert(!vendor.makePurchase("D"));
        vendor.insertMoney(1.75);
        assert(vendor.makePurchase("C"));
        assert(!vendor.makePurchase("D"));
    }

    @Test
    void testMakePurchaseEmpty() {
        assert(!vendor.makePurchase("A"));
        assert(!vendor.makePurchase("B"));
    }

    @Test
    void testRemoveItem() {
        vendor.addItem(item1, "A");
        assert(vendor.getItem("A") == item1);
        VendingMachineItem itemBack = vendor.removeItem("A");
        assert(itemBack.getName() == item1.getName());
        assertEquals(itemBack.getPrice(), item1.getPrice(), 0.001);
        assert(vendor.getItem("A") == null);
        assertThrows(VendingMachineException.class, () -> vendor.removeItem("A"));
        assertThrows(VendingMachineException.class, () -> vendor.removeItem("B"));
        assertThrows(VendingMachineException.class, () -> vendor.removeItem("C"));
        assertThrows(VendingMachineException.class, () -> vendor.removeItem("D"));
    }

    @Test
    void testReturnChange() {
        vendor.insertMoney(1.23);
        assertEquals(1.23, vendor.returnChange(), 0.001);
        assertEquals(0.0, vendor.getBalance(), 0.001);
        vendor.returnChange();
    }

    @Test
    void testItemConstructor() {
        item5 = new VendingMachineItem("Taffy", 0.75);
        assert(item5 != null);
        item6 = new VendingMachineItem("Juicebox", 1.75);
        assert(item6 != null);
    }

    @Test
    void testItemConstructorNegative() {
        assertThrows(VendingMachineException.class, () -> new VendingMachineItem("Nuts", -2.25));
        assertThrows(VendingMachineException.class, () -> new VendingMachineItem("Antimatter", -1.00));
    }

    @Test
    void testItemConstructorZero() {
        item9 = new VendingMachineItem("Spud", -0.00);
        assert(item9 != null);
        item0 = new VendingMachineItem("NoTea", 0.00);
        assert(item0 != null);
    }

    @ParameterizedTest
    @CsvSource({"Cola, 2.50", "Water, 1.00", "Tea, 1.50", "Scone, 2.00", "Taffy, 0.75", "Juicebox, 1.75"})
    void testGetName(String name, double price) {
        item0 = new VendingMachineItem(name, price);
        assert(item0.getName() == name);
    }

    @ParameterizedTest
    @CsvSource({"Cola, 2.50", "Water, 1.00", "Tea, 1.50", "Scone, 2.00", "Taffy, 0.75", "Juicebox, 1.75"})
    void testGetPrice(String name, double price) {
        item0 = new VendingMachineItem(name, price);
        assertEquals(price, item0.getPrice(), 0.001);
    }

}
