/**
 *
 */
package reega.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Marco
 *
 */
public final class FiscalCodeValidator {

    private static FiscalCodeValidator instance;
    private static final Map<Character, Integer> oddIntegersFromChars;
    private static final Map<Character, Integer> evenIntegersFromChars;
    private static Pattern fiscalCodePattern = Pattern.compile("([A-Z]){6}([0-9]){2}([A-Z]){1}([0-9]){2}([0-9A-Z]){4}");

    static {
        // Get all the possible letters and numbers for a fiscal code
        final char[] lettersAndNumbers = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        // Get all the values associated with each alphanumeric character (in order),
        // when the character of the fiscal code is in a odd index
        final int[] oddIntegerValues = { 1, 0, 5, 7, 9, 13, 15, 17, 19, 21, 1, 0, 5, 7, 9, 13, 15, 17, 19, 21, 2, 4, 18,
                20, 11, 3, 6, 8, 12, 14, 16, 10, 22, 25, 24, 23 };
        // Get all the values associated with each alphanumeric character (in order),
        // when the character of the fiscal code is in a even index
        final int[] eventIntegerValues = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
                14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25 };
        oddIntegersFromChars = new HashMap<>(lettersAndNumbers.length);
        evenIntegersFromChars = new HashMap<>(lettersAndNumbers.length);
        for (int i = 0; i < lettersAndNumbers.length; i++) {
            FiscalCodeValidator.oddIntegersFromChars.put(lettersAndNumbers[i], oddIntegerValues[i]);
            FiscalCodeValidator.evenIntegersFromChars.put(lettersAndNumbers[i], eventIntegerValues[i]);
        }
    }

    /**
     * Get the static instance of a {@link FiscalCodeValidator}
     *
     * @return the static instance of a {@link FiscalCodeValidator}
     */
    public synchronized static FiscalCodeValidator getInstance() {
        if (FiscalCodeValidator.instance == null) {
            FiscalCodeValidator.instance = new FiscalCodeValidator();
        }
        return FiscalCodeValidator.instance;
    }

    /**
     * Check if a fiscal code is valid
     *
     * @param fiscalCode fiscal code
     * @return true if it is valid, false otherwise
     */
    public boolean isFiscalCodeValid(final String fiscalCode) {
        return fiscalCode.length() == 16 && FiscalCodeValidator.fiscalCodePattern.matcher(fiscalCode).find()
                && this.isCDCCorrect(fiscalCode);
    }

    /**
     * Check if the CDC of the fiscal code is correct
     *
     * @param fiscalCode fiscal code to check
     * @return true if the CDC is correct, false otherwise
     */
    private boolean isCDCCorrect(final String fiscalCode) {
        final char lastDigit = fiscalCode.charAt(fiscalCode.length() - 1);

        int sum = 0;
        for (int i = 0; i < fiscalCode.length() - 1; i++) {
            if ((i + 1) % 2 == 0) {
                sum += FiscalCodeValidator.evenIntegersFromChars.get(fiscalCode.charAt(i));
            } else {
                sum += FiscalCodeValidator.oddIntegersFromChars.get(fiscalCode.charAt(i));
            }
        }
        final int remainder = sum % 26;
        return lastDigit == (remainder + 'A');
    }

}
