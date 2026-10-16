import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class VendingMachineItemTest {

	@ParameterizedTest
	@CsvSource({ "'Chocolate bar', 1.75", "'Water', 0.0" })
	void constructorStoresNameAndPrice(String name, double price) {
		VendingMachineItem item = new VendingMachineItem(name, price);

		assertEquals(name, item.getName());
		assertEquals(price, item.getPrice(), 0.0);
	}

	@Test
	void constructorAllowsNullName() {
		VendingMachineItem item = new VendingMachineItem(null, 1.25);

		assertNull(item.getName());
		assertEquals(1.25, item.getPrice(), 0.0);
	}

	@Test
	void constructorRejectsNegativePrice() {
		VendingMachineException exception = assertThrows(
				VendingMachineException.class,
				() -> new VendingMachineItem("Chocolate bar", -0.01));

		assertEquals("Price cannot be less than zero", exception.getMessage());
	}

	@Test
	void gettersReturnStoredValuesWithoutChangingThem() {
		VendingMachineItem item = new VendingMachineItem("Juice", 2.25);

		assertEquals("Juice", item.getName());
		assertEquals(2.25, item.getPrice(), 0.0);
		assertEquals("Juice", item.getName());
		assertEquals(2.25, item.getPrice(), 0.0);
	}
}
