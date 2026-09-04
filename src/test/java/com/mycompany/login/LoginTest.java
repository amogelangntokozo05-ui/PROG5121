package com.mycompany.login;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit Test Suite for Login class validating functionality according to
 * requirements detailed on pages 8-10 of the assignment specification.
 *
 * References:
 * 1. JUnit 5 User Guide (2024). Available at: https://junit.org/junit5/docs/current/user-guide/ (Accessed: 4 September 2026).
 * 2. Hunt, A. and Thomas, D. (2015) 'Pragmatic Unit Testing in Java 8 with JUnit', Pragmatic Bookshelf.
 */
public class LoginTest {

    private Login login;

    @BeforeEach
    public void setUp() {
        login = new Login();
        // Register default valid test user for authentication tests
        login.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
    }

    // =========================================================================
    // SECTION 1: assertEquals Tests (Pages 8-9)
    // =========================================================================

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