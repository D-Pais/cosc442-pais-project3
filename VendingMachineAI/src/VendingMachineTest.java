import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class VendingMachineTest {

	private static Stream<String> slotCodes() {
		return Stream.of(
				VendingMachine.A_CODE,
				VendingMachine.B_CODE,
				VendingMachine.C_CODE,
				VendingMachine.D_CODE);
	}

	@Test
	void startsWithZeroBalanceAndEmptySlots() {
		VendingMachine machine = new VendingMachine();

		assertEquals(0.0, machine.getBalance(), 0.0);
		slotCodes().forEach(code -> assertNull(machine.getItem(code)));
	}

	@ParameterizedTest
	@MethodSource("slotCodes")
	void addsGetsAndRemovesItemFromEachDocumentedSlot(String code) {
		VendingMachine machine = new VendingMachine();
		VendingMachineItem item = new VendingMachineItem("Snack", 1.5);

		machine.addItem(item, code);

		assertSame(item, machine.getItem(code));
		assertSame(item, machine.removeItem(code));
		assertNull(machine.getItem(code));
	}

	@ParameterizedTest
	@MethodSource("slotCodes")
	void addingToOccupiedSlotThrowsAndLeavesExistingItem(String code) {
		VendingMachine machine = new VendingMachine();
		VendingMachineItem existingItem = new VendingMachineItem("Snack", 1.5);
		machine.addItem(existingItem, code);

		VendingMachineException exception = assertThrows(
				VendingMachineException.class,
				() -> machine.addItem(new VendingMachineItem("Drink", 2.0), code));

		assertEquals("Slot " + code + " already occupied", exception.getMessage());
		assertSame(existingItem, machine.getItem(code));
	}

	@ParameterizedTest
	@ValueSource(strings = { "", "?" })
	void addGetAndRemoveRejectInvalidCodes(String code) {
		VendingMachine machine = new VendingMachine();

		VendingMachineException addException = assertThrows(
				VendingMachineException.class,
				() -> machine.addItem(new VendingMachineItem("Snack", 1.5), code));
		VendingMachineException getException = assertThrows(
				VendingMachineException.class,
				() -> machine.getItem(code));
		VendingMachineException removeException = assertThrows(
				VendingMachineException.class,
				() -> machine.removeItem(code));

		assertEquals("Invalid code for vending machine item", addException.getMessage());
		assertEquals("Invalid code for vending machine item", getException.getMessage());
		assertEquals("Invalid code for vending machine item", removeException.getMessage());
	}

	@ParameterizedTest
	@MethodSource("slotCodes")
	void removingAnEmptySlotThrows(String code) {
		VendingMachine machine = new VendingMachine();

		VendingMachineException exception = assertThrows(
				VendingMachineException.class,
				() -> machine.removeItem(code));

		assertEquals("Slot " + code + " is empty -- cannot remove item", exception.getMessage());
		assertNull(machine.getItem(code));
	}

	@Test
	void insertMoneyAddsToBalanceAndGetBalanceDoesNotChangeIt() {
		VendingMachine machine = new VendingMachine();

		machine.insertMoney(1.25);
		machine.insertMoney(2.5);

		assertEquals(3.75, machine.getBalance(), 0.0);
		assertEquals(3.75, machine.getBalance(), 0.0);
	}

	@Test
	void insertMoneyAcceptsZero() {
		VendingMachine machine = new VendingMachine();

		machine.insertMoney(0.0);

		assertEquals(0.0, machine.getBalance(), 0.0);
	}

	@Test
	void insertMoneyRejectsNegativeAmountWithoutChangingBalance() {
		VendingMachine machine = new VendingMachine();
		machine.insertMoney(2.0);

		VendingMachineException exception = assertThrows(
				VendingMachineException.class,
				() -> machine.insertMoney(-0.01));

		assertEquals("Invalid amount.  Amount must be >= 0", exception.getMessage());
		assertEquals(2.0, machine.getBalance(), 0.0);
	}

	@Test
	void purchaseWithEnoughMoneyRemovesItemAndSubtractsItsPrice() {
		VendingMachine machine = new VendingMachine();
		VendingMachineItem item = new VendingMachineItem("Snack", 2.5);
		machine.addItem(item, VendingMachine.A_CODE);
		machine.insertMoney(4.0);

		assertTrue(machine.makePurchase(VendingMachine.A_CODE));
		assertNull(machine.getItem(VendingMachine.A_CODE));
		assertEquals(1.5, machine.getBalance(), 0.0);
	}

	@Test
	void purchaseWithExactBalanceLeavesZeroBalance() {
		VendingMachine machine = new VendingMachine();
		machine.addItem(new VendingMachineItem("Snack", 2.5), VendingMachine.A_CODE);
		machine.insertMoney(2.5);

		assertTrue(machine.makePurchase(VendingMachine.A_CODE));
		assertEquals(0.0, machine.getBalance(), 0.0);
	}

	@Test
	void purchaseWithoutEnoughMoneyReturnsFalseAndPreservesItemAndBalance() {
		VendingMachine machine = new VendingMachine();
		VendingMachineItem item = new VendingMachineItem("Snack", 2.5);
		machine.addItem(item, VendingMachine.A_CODE);
		machine.insertMoney(2.0);

		assertFalse(machine.makePurchase(VendingMachine.A_CODE));
		assertSame(item, machine.getItem(VendingMachine.A_CODE));
		assertEquals(2.0, machine.getBalance(), 0.0);
	}

	@ParameterizedTest
	@MethodSource("slotCodes")
	void purchaseFromEmptySlotReturnsFalseAndPreservesBalance(String code) {
		VendingMachine machine = new VendingMachine();
		machine.insertMoney(2.0);

		assertFalse(machine.makePurchase(code));
		assertEquals(2.0, machine.getBalance(), 0.0);
	}

	@Test
	void returnChangeReturnsBalanceAndResetsItToZero() {
		VendingMachine machine = new VendingMachine();
		machine.insertMoney(3.75);

		assertEquals(3.75, machine.returnChange(), 0.0);
		assertEquals(0.0, machine.getBalance(), 0.0);
	}

	@Test
	void returnChangeWithNoBalanceReturnsZero() {
		VendingMachine machine = new VendingMachine();

		assertEquals(0.0, machine.returnChange(), 0.0);
		assertEquals(0.0, machine.getBalance(), 0.0);
	}
}
