package reega.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FCValidatorTest {
    private static final FiscalCodeValidator validator = new FiscalCodeValidator();

    @Test
    public void checkFiscalCode() {
        assertTrue(validator.isFiscalCodeValid("PLOMNL99C05I829K"));
        assertTrue(validator.isFiscalCodeValid("VRDMRA00A41F205F"));
        assertTrue(validator.isFiscalCodeValid("DCMLCU90E13D612V"));


        assertFalse(validator.isFiscalCodeValid("XVRDMRA00A41F205F"));
        assertFalse(validator.isFiscalCodeValid("5VRDMRA00A41F205F"));
        assertFalse(validator.isFiscalCodeValid("VRDMRA00A41F205FX"));
        assertFalse(validator.isFiscalCodeValid("VRDMRA00A41F205F$"));
        assertFalse(validator.isFiscalCodeValid("&VRDMRA00A41F205F"));


        assertFalse(validator.isFiscalCodeValid("VRDMRA0041F205F"));
        assertFalse(validator.isFiscalCodeValid("VRDMRA00A4F205F"));
        assertFalse(validator.isFiscalCodeValid("VRDMA00A41F205F"));
        assertFalse(validator.isFiscalCodeValid("VRDMRA00A41F25F"));
        assertFalse(validator.isFiscalCodeValid("VRDMRA00A41205F"));
        assertFalse(validator.isFiscalCodeValid("VRDMRA00A41F205"));

        assertFalse(validator.isFiscalCodeValid("VRDMRA00A41F205J"));
        assertFalse(validator.isFiscalCodeValid("PLOMNL99C04I829K"));
        assertFalse(validator.isFiscalCodeValid("DCMLCU90E14D612V"));
        assertFalse(validator.isFiscalCodeValid("dcmlcu90e14d612v"));
    }
}
