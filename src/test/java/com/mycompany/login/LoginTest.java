package com.mycompany.login;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

// LoginTest: unit tests for the Login class.
public class LoginTest {

    private Login login;

    @BeforeEach
    public void setUp() {
        login = new Login();
        // Register default valid test user for authentication tests
        login.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
    }


    @Test
    @DisplayName("assertEquals: Username is correctly formatted message")
    public void testUsernameCorrectlyFormattedMessage() {
        String testData = "kyl_1";
        String expectedMessage = "Username successfully captured.";
        String actualMessage = login.validateUsernameMessage(testData);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("assertEquals: Username is incorrectly formatted message")
    public void testUsernameIncorrectlyFormattedMessage() {
        String testData = "kyle!!!!!!!";
        String expectedMessage = "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        String actualMessage = login.validateUsernameMessage(testData);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("assertEquals: Password meets complexity requirements message")
    public void testPasswordMeetsComplexityMessage() {
        String testData = "Ch&&sec@ke99!";
        String expectedMessage = "Password successfully captured.";
        String actualMessage = login.validatePasswordMessage(testData);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("assertEquals: Password does not meet complexity requirements message")
    public void testPasswordDoesNotMeetComplexityMessage() {
        String testData = "password";
        String expectedMessage = "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        String actualMessage = login.validatePasswordMessage(testData);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("assertEquals: Cell phone is correctly formatted message")
    public void testCellPhoneCorrectlyFormattedMessage() {
        String testData = "+27838968976";
        String expectedMessage = "Cell number successfully captured.";
        String actualMessage = login.validateCellPhoneMessage(testData);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("assertEquals: Cell phone number is incorrectly formatted message")
    public void testCellPhoneIncorrectlyFormattedMessage() {
        String testData = "08966553";
        String expectedMessage = "Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.";
        String actualMessage = login.validateCellPhoneMessage(testData);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("assertEquals: Login return status when login is successful")
    public void testReturnLoginStatusSuccess() {
        String expected = "Welcome Kyle, Smith it is great to see you again.";
        String actual = login.returnLoginStatus(true);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("assertEquals: Login return status when login fails")
    public void testReturnLoginStatusFailed() {
        String expected = "Username or password incorrect, please try again.";
        String actual = login.returnLoginStatus(false);
        assertEquals(expected, actual);
    }

    // =========================================================================
    // SECTION 2: assertTrue / assertFalse Tests (Pages 9-10)
    // =========================================================================

    @Test
    @DisplayName("assertTrue: Login Successful")
    public void testLoginSuccessful() {
        boolean result = login.loginUser("kyl_1", "Ch&&sec@ke99!");
        assertTrue(result, "Login should return true for valid credentials.");
    }

    @Test
    @DisplayName("assertFalse: Login Failed")
    public void testLoginFailed() {
        boolean result = login.loginUser("wrongUser", "wrongPass");
        assertFalse(result, "Login should return false for invalid credentials.");
    }

    @Test
    @DisplayName("assertTrue: Username correctly formatted")
    public void testUsernameCorrectlyFormatted() {
        String testData = "kyl_1";
        boolean result = login.checkUserName(testData);
        assertTrue(result, "Username containing underscore and <= 5 characters should return true.");
    }

    @Test
    @DisplayName("assertFalse: Username incorrectly formatted")
    public void testUsernameIncorrectlyFormatted() {
        String testData = "kyle!!!!!!!";
        boolean result = login.checkUserName(testData);
        assertFalse(result, "Username without underscore or > 5 characters should return false.");
    }

    @Test
    @DisplayName("assertTrue: Password meets complexity requirements")
    public void testPasswordMeetsComplexity() {
        String testData = "Ch&&sec@ke99!";
        boolean result = login.checkPasswordComplexity(testData);
        assertTrue(result, "Password with >= 8 chars, uppercase, digit, and special char should return true.");
    }

    @Test
    @DisplayName("assertFalse: Password does not meet complexity requirements")
    public void testPasswordDoesNotMeetComplexity() {
        String testData = "password";
        boolean result = login.checkPasswordComplexity(testData);
        assertFalse(result, "Password missing uppercase, digit, or special character should return false.");
    }

    @Test
    @DisplayName("assertTrue: Cell phone number correctly formatted")
    public void testCellPhoneNumberCorrectlyFormatted() {
        String testData = "+27838968976";
        boolean result = login.checkCellPhoneNumber(testData);
        assertTrue(result, "SA cell number with +27 country code and 9-10 digits should return true.");
    }

    @Test
    @DisplayName("assertFalse: Cell phone number incorrectly formatted")
    public void testCellPhoneNumberIncorrectlyFormatted() {
        String testData = "08966553";
        boolean result = login.checkCellPhoneNumber(testData);
        assertFalse(result, "Phone number without international country code should return false.");
    }
}
