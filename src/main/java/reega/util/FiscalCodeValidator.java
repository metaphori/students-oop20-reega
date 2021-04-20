/**
 *
 */
package reega.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public final class FiscalCodeValidator {

    private static final int ALPHABET_LETTERS_COUNT = 26;
    private static final Map<Character, Integer> ODD_INTEGERS_FROM_CHARS;
    private static final Map<Character, Integer> EVEN_INTEGERS_FROM_CHARS;
    private static Pattern fiscalCodePattern = Pattern.compile("([A-Z]){6}([0-9]){2}([A-Z])([0-9]){2}([0-9A-Z]){4}");

    static {
        // Get all the possible letters and numbers for a fiscal code
        final char[] lettersAndNumbers = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

        // Get all the values associated with each alphanumeric character (in order),
        // when the character of the fiscal code is in a odd index
        final int[] oddIntegerValues = { 1, 0, 5, 7, 9, 13, 15, 17, 19, 21, 1, 0, 5, 7, 9, 13, 15, 17, 19, 21, 2, 4, 18,
                20, 11, 3, 6, 8, 12, 14, 16, 10, 22, 25, 24, 23 };
        // Get all the values associated with each alphanumeric character (in order),
        // when the character of the fiscal code is in a even index
        final int[] evenIntegerValues = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
                14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25 };
        ODD_INTEGERS_FROM_CHARS = new HashMap<>(lettersAndNumbers.length);
        EVEN_INTEGERS_FROM_CHARS = new HashMap<>(lettersAndNumbers.length);
        for (int i = 0; i < lettersAndNumbers.length; i++) {
            FiscalCodeValidator.ODD_INTEGERS_FROM_CHARS.put(lettersAndNumbers[i], oddIntegerValues[i]);
            FiscalCodeValidator.EVEN_INTEGERS_FROM_CHARS.put(lettersAndNumbers[i], evenIntegerValues[i]);
        }
    }

    private FiscalCodeValidator() {
    }

    /**
     * Check if a fiscal code is valid.
     *
     * @param fiscalCode fiscal code
     * @return true if it is valid, false otherwise
     */
    public static boolean isFiscalCodeValid(final String fiscalCode) {
        return fiscalCode.length() == 16 && FiscalCodeValidator.fiscalCodePattern.matcher(fiscalCode).find()
                && FiscalCodeValidator.isCDCCorrect(fiscalCode);
    }

    /**
     * Check if the CDC of the fiscal code is correct.
     *
     * @param fiscalCode fiscal code to check
     * @return true if the CDC is correct, false otherwise
     */
    private static boolean isCDCCorrect(final String fiscalCode) {
        final char lastDigit = fiscalCode.charAt(fiscalCode.length() - 1);

        int sum = 0;
        for (int i = 0; i < fiscalCode.length() - 1; i++) {
            if ((i + 1) % 2 == 0) {
                sum += FiscalCodeValidator.EVEN_INTEGERS_FROM_CHARS.get(fiscalCode.charAt(i));
            } else {
                sum += FiscalCodeValidator.ODD_INTEGERS_FROM_CHARS.get(fiscalCode.charAt(i));
            }
        }
        final int remainder = sum % FiscalCodeValidator.ALPHABET_LETTERS_COUNT;
        return lastDigit == remainder + 'A';
    }

}
