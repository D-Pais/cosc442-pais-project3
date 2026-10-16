**Initial Coverage %**

| **Source File** | **Statement** | **Branch** |
| --------------- | ------------- | ---------- |
| VendingMachine.java | 43/43 (100%) | 20/20 (100%) |
| VendingMachineItem.java| 8/8 (100%)| 2/2 (100%) |

**No changes to reach 100%**

--

**Condition Analysis:**

Focusing on makePurchase(), the two factors in this compound conditional are the status of VendingMachineItem "item" (if it is null or not) and whether the vending machine's balance is greater than the item's price.

The instantiation of "item" is covered by testMakePurchaseEmpty (for if null) and testMakePurchase/testMakePurchasePoor (for if non-null), while whether the machine has sufficient balance is covered by testMakePurchase (for if balance >= price) and testMakePurchasePoor (for if balance > price).

--

**Test Sensitivity**

I changed line 184 (now 186 due to comments) in VendingMachine.java such that the price check went from "this.balance >= item.getPrice()" to "this.balance > item.getPrice()".

No tests came up as failures, showing that there is a gap in my testing.  To cover just this one injected fault, line 113 (now 114 due to comment) in VendingMachineTest.java, in the testMakePurchasePoor test, could be changed from "vendor.insertMoney(1.75)" to "vendor.insertMoney(1.50)".