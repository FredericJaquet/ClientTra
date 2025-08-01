package com.frederic.clienttra.validators;

/**
 * Utility class for validating International Bank Account Numbers (IBAN).
 */
public class IbanValidator {

    /**
     * Validates whether the given IBAN string is correctly formatted and valid according to the IBAN standard.
     *
     * <p>The validation process includes:
     * <ul>
     *   <li>Removing whitespace and converting the string to uppercase.</li>
     *   <li>Rearranging the IBAN by moving the first four characters to the end.</li>
     *   <li>Converting letters to numbers (A=10, B=11, ..., Z=35).</li>
     *   <li>Performing a modulo 97 operation on the resulting numeric string.</li>
     * </ul>
     *
     * <p>A valid IBAN must produce a modulo 97 result equal to 1.
     *
     * @param iban the IBAN string to validate
     * @return true if the IBAN is valid; false otherwise (null, too short, invalid characters, or checksum fail)
     */
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
