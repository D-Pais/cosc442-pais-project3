**Prompt Given To VSCode Copilot:**
"Using included code comments, develop JUnit Jupiter 5 test classes for VendingMachine.java and VendingMachineItem.java that perform as sufficiently as possible for full coverage.  DO NOT alter or modify the original project code by any amount, only create test files for a human to later run.  You are NOT diagnosing faults, and SHOULD NOT write hardcoded test cases.  Write test cases based on the code comments indicating the code's correct functionality, and not based around faults you may or may not find in the code itself."

--

**Generated Tests Eval:**
Do they compile and use JUnit 5 correctly?

- The generateed tests do compile and include correct usage of JUnit methods.

Do they generally follow Arrange → Act → Assert?

- Yes: within each test Copilot instantiated a new VendingMachine and any items needed, then used them and asserted their status.

Do they contain meaningful assertions/oracles rather than merely executing methods?

- Each test has a purpose and executes on that purpose.

Do they include validation, exception/invalid, and boundary cases where appropriate?

- They include various such cases, including ones I would deem redundant.

Do they contain unnecessary duplication that could reasonably be handled with parameterized tests?

- They include many parameterized tests, but still include unnecessary duplication in terms of not utilizing set-up and tear-down methods.

Do any tests assume behavior that is not supported by the Javadocs/specification?

- Some do test for behaviour in situations not explicitly outlined in the original files, such as ensuring VendingMachineItems accept null names.

--

**Test Comparison:**

|Test Suite|Approx. # Tests|Statement/Line Coverage|Branch Coverage|Notable Strength/Weakness|
|----------|---------------|-----------------------|---------------|-------------------------|
|Manual suite from Task 1|15 Tests, 2 Parameterized|100%|100%|In light of seeing the AI tests: assuming values remain the same after retrieving them with getters once|
|Initial AI-generated suite|18 Tests, 6 Parameterized|100%|100%|Lack of set-up & tear-down usage, but double-checks almost every action.|
|AI suite after coverage-guided prompt|N/A|N/A|N/A|N/A|

--

**Reflection:**
What was the overall quality of the generated tests? What did the AI do particularly well or poorly?

- Tests successfully ran and were sufficient to spot the original 2 faults in the VendingMachine code ("i <= NUM_SLOTS" and "amount < 1")

How did the AI-generated test suite compare with your manually designed suite from Project 2/Task 1 in terms of coverage and test quality?

- Both achieve 100% coverage of the code, but the Copilot Tests check a wide variety of other mostly redundant combinations of actions (such as testing that adding an item, calling the getter for that item, then removing that item returns the original item, instead of just adding and removing the item).

Did coverage-guided prompting improve the generated suite? Give at least one concrete example.

- The first prompt I gave generated tests that already hit 100% coverage, and so no additional guidance was needed.

Did the AI generate any incorrect, redundant, weak, or misleading assertions? How did you recognize them?

- It did generate many redundant assertions, but no assertions were very weak or at all misleading.  I mostly recognized them by comparing tests to each other and seeing that some assertions in later tests were already incidentally covered by earlier tests.

What does this exercise suggest about the strengths and limitations of using AI to generate software tests?

- It suggests that AI is good at writing individual tests, including parameterized tests, but perhaps suites of tests are not as intuitive to it as to humans.  It is more thorough, but in areas that don't necessarily need such thorough testing efforts.  Overall, it will tend to create more tests and write more lines of code per test.