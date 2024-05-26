package org.SI.lab2_121002;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SILab2Test {

    @ParameterizedTest
    @MethodSource("provideTestCases")
    void testCheckCartEveryBranch(List<Item> allItems, int payment, boolean expectedResult, String expectedExceptionMessage) {
        if (expectedExceptionMessage == null) {
            boolean result = SILab2.checkCart(allItems, payment);
            assertEquals(expectedResult, result);
        } else {
            RuntimeException exception = assertThrows(RuntimeException.class, () -> SILab2.checkCart(allItems, payment));
            assertEquals(expectedExceptionMessage, exception.getMessage());
        }
    }

    private static Stream<Arguments> provideTestCases() {
        return Stream.of(
            // Test Case 1: allItems is null
            Arguments.arguments(null, 0, false, "allItems list can't be null!"),

            // Test Case 2: allItems is empty
            Arguments.arguments(new ArrayList<>(), 0, true, null),

            // Test Case 3: Item name is null or empty
            Arguments.arguments(List.of(new Item("", "123", 100, 0.2f)), 80, true, null),

            // Test Case 4: Item barcode is null
            Arguments.arguments(List.of(new Item("Item", null, 100, 0.2f)), 80, false, "No barcode!"),

            // Test Case 5: Item barcode contains invalid characters
            Arguments.arguments(List.of(new Item("Item", "123a", 100, 0.2f)), 80, false, "Invalid character in item barcode!"),

            // Test Case 6: Item with discount
            Arguments.arguments(List.of(new Item("Item", "123", 100, 0.2f)), 80, true, null),

            // Test Case 7: Item without discount
            Arguments.arguments(List.of(new Item("Item", "123", 100, 0.0f)), 100, true, null),

            // Test Case 8: Item with price > 300, discount > 0, and barcode starting with '0'
            Arguments.arguments(List.of(new Item("Item", "0123", 400, 0.2f)), 290, true, null),

            // Test Case 9: Payment is sufficient
            Arguments.arguments(List.of(new Item("Item1", "123", 100, 0.0f), new Item("Item2", "456", 200, 0.1f)), 270, true, null),

            // Test Case 10: Payment is insufficient
            Arguments.arguments(List.of(new Item("Item1", "123", 100, 0.0f), new Item("Item2", "456", 200, 0.0f)), 250, false, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideMultipleConditionTestCases")
    void testMultipleConditionCriteria(List<Item> allItems, int payment, boolean expectedResult) {
        boolean result = SILab2.checkCart(allItems, payment);
        assertEquals(expectedResult, result);
    }

    private static Stream<Arguments> provideMultipleConditionTestCases() {
        return Stream.of(
            // Test Case 1: All conditions are true
            Arguments.arguments(List.of(new Item("Item", "0123", 400, 0.2f)), 290, true),

            // Test Case 2: All conditions are false
            Arguments.arguments(List.of(new Item("Item", "1234", 200, 0.0f)), 200, true),

            // Test Case 3: First condition is true, others are false
            Arguments.arguments(List.of(new Item("Item", "1234", 400, 0.0f)), 400, true),

            // Test Case 4: Second condition is true, others are false
            Arguments.arguments(List.of(new Item("Item", "1234", 200, 0.2f)), 160, true),

            // Test Case 5: Third condition is true, others are false
            Arguments.arguments(List.of(new Item("Item", "0123", 200, 0.0f)), 200, true),

            // Test Case 6: First and second conditions are true, third is false
            Arguments.arguments(List.of(new Item("Item", "1234", 400, 0.2f)), 320, true),

            // Test Case 7: First and third conditions are true, second is false
            Arguments.arguments(List.of(new Item("Item", "0123", 400, 0.0f)), 400, true),

            // Test Case 8: Second and third conditions are true, first is false
            Arguments.arguments(List.of(new Item("Item", "0123", 200, 0.2f)), 160, true)
        );
    }
}
