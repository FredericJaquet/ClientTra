package com.frederic.clienttra.validators;

public class IbanValidator {

    // Main method to check whether the given IBAN string is valid.
    public static boolean isValidIban(String iban) {
        // First, check if the input is null or too short to be a valid IBAN.
        if (iban == null || iban.length() < 4) return false;

        // Remove all whitespace and convert to uppercase.
        String trimmed = iban.replaceAll("\\s+", "").toUpperCase();

        // Move the first four characters (country code and check digits) to the end.
        String rearranged = trimmed.substring(4) + trimmed.substring(0, 4);

        StringBuilder numericIban = new StringBuilder();
        // Convert letters to numbers (A=10, B=11, ..., Z=35) and keep digits as-is.
        for (char ch : rearranged.toCharArray()) {
            if (Character.isDigit(ch)) {
                numericIban.append(ch);
            } else if (Character.isLetter(ch)) {
                numericIban.append((int) ch - 55); // ASCII 'A' is 65 => 65 - 55 = 10
            } else {
                // If there's an invalid character, return false.
                return false;
            }
        }

        try {
            // IBANs can be very long, so instead of parsing the whole string to a number,
            // compute the modulo 97 progressively, digit by digit.
            String bigNumber = numericIban.toString();
            int mod = 0;
            for (int i = 0; i < bigNumber.length(); i++) {
                int digit = Character.getNumericValue(bigNumber.charAt(i));
                mod = (mod * 10 + digit) % 97;
            }

            // A valid IBAN must have a modulo 97 of exactly 1.
            return mod == 1;
        } catch (NumberFormatException e) {
            // Catch any unexpected parsing errors and treat the IBAN as invalid.
            return false;
        }
    }
}
