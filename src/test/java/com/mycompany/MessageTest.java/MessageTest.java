package com.mycompany.login;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit Test Suite for Message class verifying requirements from Pages 14-16
 * of the assignment specification.
 *
 * References:
 * 1. JUnit 5 User Guide (2024). Available at: https://junit.org/junit5/docs/current/user-guide/
 *    (Accessed: 4 September 2026).
 * 2. Hunt, A. and Thomas, D. (2015) 'Pragmatic Unit Testing in Java 8 with JUnit', Pragmatic Bookshelf.
 */
public class MessageTest {

    private Message message;

    @BeforeEach
    public void setUp() {
        Message.resetTotalMessagesSent();
        message = new Message();
    }

    // =========================================================================
    // SECTION 1: Message Length Tests (Page 15)
    // =========================================================================

    @Test
    @DisplayName("assertEquals: Message length within 250 characters succeeds")
    public void testMessageLengthSuccess() {
        String testMessage = "Hi Mike, can you join us for dinner tonight?";
        String expected = "Message ready to send.";
        String actual = message.validateMessageLength(testMessage);
        assertEquals(expected, actual, "Short message should return 'Message ready to send.'");
    }

    @Test
    @DisplayName("assertEquals: Message exceeding 250 characters returns descriptive failure")
    public void testMessageLengthExceeded() {
        // Create a 260-character message (exceeds by 10)
        StringBuilder longMsg = new StringBuilder();
        for (int i = 0; i < 260; i++) {
            longMsg.append("a");
        }
        String testMessage = longMsg.toString();
        String expected = "Message exceeds 250 characters by 10; please reduce the size.";
        String actual = message.validateMessageLength(testMessage);
        assertEquals(expected, actual, "Overly long message should report exact excess characters.");
    }

    // =========================================================================
    // SECTION 2: Recipient Cell Number Tests (Page 15)
    // =========================================================================

    @Test
    @DisplayName("assertEquals: Correctly formatted recipient cell phone number")
    public void testRecipientCellCorrect() {
        String validNumber = "+27718693002";
        String expected = "Cell phone number successfully captured.";
        String actual = message.checkRecipientCell(validNumber);
        assertEquals(expected, actual);
        assertTrue(message.isRecipientCellValid(validNumber));
    }

    @Test
    @DisplayName("assertEquals: Incorrectly formatted recipient cell phone number")
    public void testRecipientCellIncorrect() {
        String invalidNumber = "08575975889";
        String expected = "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        String actual = message.checkRecipientCell(invalidNumber);
        assertEquals(expected, actual);
        assertFalse(message.isRecipientCellValid(invalidNumber));
    }

    // =========================================================================
    // SECTION 3: Message Hash Generation Tests (Page 15)
    // =========================================================================

    @Test
    @DisplayName("assertEquals: Message Hash for Test Case 1")
    public void testMessageHashTestCase1() {
        String messageId = "0012345678";
        int messageNumber = 0;
        String messageText = "Hi Mike, can you join us for dinner tonight?";

        String expectedHash = "00:0:HITONIGHT";
        String actualHash = message.createMessageHash(messageId, messageNumber, messageText);
        assertEquals(expectedHash, actualHash, "Hash should be '00:0:HITONIGHT'");
    }

    @Test
    @DisplayName("assertEquals: Message Hash loop verification for multiple test messages")
    public void testMessageHashesInLoop() {
        String[][] testCases = {
            {"0011223344", "0", "Hi Mike, can you join us for dinner tonight?", "00:0:HITONIGHT"},
            {"0855667788", "1", "Hi Keegan, did you receive the payment?", "08:1:HIPAYMENT"},
            {"2712345678", "2", "Hello world, welcome everyone", "27:2:HELLOEVERYONE"},
            {"9900000000", "3", "Quick update regarding project deadline", "99:3:QUICKDEADLINE"}
        };

        for (String[] testCase : testCases) {
            String id = testCase[0];
            int num = Integer.parseInt(testCase[1]);
            String text = testCase[2];
            String expected = testCase[3];

            String actual = message.createMessageHash(id, num, text);
            assertEquals(expected, actual, "Hash calculation failed for text: " + text);
        }
    }

    // =========================================================================
    // SECTION 4: Message ID Verification (Page 16)
    // =========================================================================

    @Test
    @DisplayName("assertEquals & assertTrue: Check Message ID is not more than 10 characters")
    public void testCheckMessageID() {
        String generatedId = Message.generateMessageID();
        assertTrue(message.checkMessageID(generatedId), "Generated 10-digit ID should be valid.");
        assertEquals(10, generatedId.length(), "Generated ID length must be exactly 10 characters.");

        assertFalse(message.checkMessageID("123456789012345"), "ID exceeding 10 characters should fail.");
        assertFalse(message.checkMessageID(""), "Empty ID should fail.");
    }

    // =========================================================================
    // SECTION 5: SentMessage Action Option Tests (Page 16)
    // =========================================================================

    @Test
    @DisplayName("assertEquals: SentMessage Option 1 - Send Message")
    public void testSentMessageSend() {
        String response = message.SentMessage(1);
        assertEquals("Message successfully sent.", response);
        assertEquals("Sent", message.getStatus());
    }

    @Test
    @DisplayName("assertEquals: SentMessage Option 2 - Disregard Message")
    public void testSentMessageDisregard() {
        String response = message.SentMessage(2);
        assertEquals("Press 0 to delete the message.", response);
        assertEquals("Disregarded", message.getStatus());
    }

    @Test
    @DisplayName("assertEquals: SentMessage Option 3 - Store Message")
    public void testSentMessageStore() {
        String response = message.SentMessage(3);
        assertEquals("Message successfully stored.", response);
        assertEquals("Stored", message.getStatus());
    }

    // =========================================================================
    // SECTION 6: Total Messages Count & Print Tests (Page 13-14)
    // =========================================================================

    @Test
    @DisplayName("assertEquals: Total sent messages accumulated count")
    public void testReturnTotalMessages() {
        assertEquals(0, message.returnTotalMessagess(), "Initial sent count should be 0.");

        Message m1 = new Message("0012345678", 0, "+27718693002", "Message 1");
        m1.SentMessage(1); // Sent

        Message m2 = new Message("0012345679", 1, "+27718693002", "Message 2");
        m2.SentMessage(2); // Disregarded

        Message m3 = new Message("0012345680", 2, "+27718693002", "Message 3");
        m3.SentMessage(1); // Sent

        assertEquals(2, message.returnTotalMessagess(), "Total sent count should equal 2.");
    }

    @Test
    @DisplayName("assertEquals: printMessages formatted details")
    public void testPrintMessages() {
        Message testMsg = new Message("0012345678", 0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        String details = testMsg.printMessages();

        assertTrue(details.contains("Message ID: 0012345678"));
        assertTrue(details.contains("Message Hash: 00:0:HITONIGHT"));
        assertTrue(details.contains("Recipient: +27718693002"));
        assertTrue(details.contains("Message: Hi Mike, can you join us for dinner tonight?"));
    }