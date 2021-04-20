package reega.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FCValidatorTest {

    @Test
    public void checkFiscalCode() {
        Assertions.assertTrue(FiscalCodeValidator.isFiscalCodeValid("PLOMNL99C05I829K"));
        Assertions.assertTrue(FiscalCodeValidator.isFiscalCodeValid("VRDMRA00A41F205F"));
        Assertions.assertTrue(FiscalCodeValidator.isFiscalCodeValid("DCMLCU90E13D612V"));

        Assertions.assertFalse(FiscalCodeValidator.isFiscalCodeValid("XVRDMRA00A41F205F"));
        Assertions.assertFalse(FiscalCodeValidator.isFiscalCodeValid("5VRDMRA00A41F205F"));
        Assertions.assertFalse(FiscalCodeValidator.isFiscalCodeValid("VRDMRA00A41F205FX"));
        Assertions.assertFalse(FiscalCodeValidator.isFiscalCodeValid("VRDMRA00A41F205F$"));
        Assertions.assertFalse(FiscalCodeValidator.isFiscalCodeValid("&VRDMRA00A41F205F"));

        Assertions.assertFalse(FiscalCodeValidator.isFiscalCodeValid("VRDMRA0041F205F"));
        Assertions.assertFalse(FiscalCodeValidator.isFiscalCodeValid("VRDMRA00A4F205F"));
        Assertions.assertFalse(FiscalCodeValidator.isFiscalCodeValid("VRDMA00A41F205F"));
        Assertions.assertFalse(FiscalCodeValidator.isFiscalCodeValid("VRDMRA00A41F25F"));
        Assertions.assertFalse(FiscalCodeValidator.isFiscalCodeValid("VRDMRA00A41205F"));
        Assertions.assertFalse(FiscalCodeValidator.isFiscalCodeValid("VRDMRA00A41F205"));

        Assertions.assertFalse(FiscalCodeValidator.isFiscalCodeValid("VRDMRA00A41F205J"));
        Assertions.assertFalse(FiscalCodeValidator.isFiscalCodeValid("PLOMNL99C04I829K"));
        Assertions.assertFalse(FiscalCodeValidator.isFiscalCodeValid("DCMLCU90E14D612V"));
        Assertions.assertFalse(FiscalCodeValidator.isFiscalCodeValid("dcmlcu90e14d612v"));
    }
}
