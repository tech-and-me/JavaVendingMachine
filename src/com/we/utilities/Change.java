package com.we.utilities;

import java.util.HashMap;
import java.util.Map;

public class Change {
	public enum ChangeType {
        QUARTER(25),
        NICKEL(10),
        PENNY(1);

        private final int value;

        ChangeType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static Map<ChangeType, Integer> calculateChange(int amountInPennies) {
        Map<ChangeType, Integer> change = new HashMap<>();

        for (ChangeType denomination : ChangeType.values()) {
            int denominationValue = denomination.getValue();
            int count = amountInPennies / denominationValue;
            amountInPennies %= denominationValue; //remaining amount in pennies

            if (count > 0) {
                change.put(denomination, count);
            }
        }

        return change;
    }
}
