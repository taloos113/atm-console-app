package atm.util;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class InputUtilTest {
    @Test
    void readIntReturnsValidInteger() {
        Scanner scanner = scannerWithInput("15\n");

        int value = InputUtil.readInt(scanner, "Number: ");

        assertEquals(15, value);
    }

    @Test
    void readIntRetriesUntilValidInteger() {
        Scanner scanner = scannerWithInput("abc\n25\n");

        int value = InputUtil.readInt(scanner, "Number: ");

        assertEquals(25, value);
    }

    @Test
    void readBigDecimalReturnsValidNumber() {
        Scanner scanner = scannerWithInput("10.50\n");

        BigDecimal amount = InputUtil.readBigDecimal(scanner, "Amount: ");

        assertEquals(new BigDecimal("10.50"), amount);
    }

    @Test
    void readBigDecimalRetriesUntilValidNumber() {
        Scanner scanner = scannerWithInput("abc\n10.50\n");

        BigDecimal amount = InputUtil.readBigDecimal(scanner, "Amount: ");

        assertEquals(new BigDecimal("10.50"), amount);
    }

    @Test
    void readNonEmptyStringReturnsTrimmedValue() {
        Scanner scanner = scannerWithInput(" Alice Johnson \n");

        String value = InputUtil.readNonEmptyString(scanner, "Name: ");

        assertEquals("Alice Johnson", value);
    }

    @Test
    void readNonEmptyStringRetriesUntilNonEmpty() {
        Scanner scanner = scannerWithInput("\n   \nBob Smith\n");

        String value = InputUtil.readNonEmptyString(scanner, "Name: ");

        assertEquals("Bob Smith", value);
    }

    @Test
    void readPinReturnsValidPin() {
        Scanner scanner = scannerWithInput("12345\n");

        String pin = InputUtil.readPin(scanner, "PIN: ");

        assertEquals("12345", pin);
    }

    @Test
    void readPinRetriesUntilValidPin() {
        Scanner scanner = scannerWithInput("12\nabcde\n12345\n");

        String pin = InputUtil.readPin(scanner, "PIN: ");

        assertEquals("12345", pin);
    }

    @Test
    void readStatusReturnsUpperCaseStatus() {
        Scanner scanner = scannerWithInput("active\n");

        String status = InputUtil.readStatus(scanner, "Status: ");

        assertEquals("ACTIVE", status);
    }

    @Test
    void readStatusRetriesUntilValidStatus() {
        Scanner scanner = scannerWithInput("wrong\nDisabled\n");

        String status = InputUtil.readStatus(scanner, "Status: ");

        assertEquals("DISABLED", status);
    }

    @Test
    void readRoleReturnsUpperCaseRole() {
        Scanner scanner = scannerWithInput("customer\n");

        String role = InputUtil.readRole(scanner, "Role: ");

        assertEquals("CUSTOMER", role);
    }

    @Test
    void readRoleRetriesUntilValidRole() {
        Scanner scanner = scannerWithInput("manager\nAdmin\n");

        String role = InputUtil.readRole(scanner, "Role: ");

        assertEquals("ADMIN", role);
    }

    private Scanner scannerWithInput(String input) {
        return new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
    }
}