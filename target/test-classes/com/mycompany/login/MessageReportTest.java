package com.mycompany.login;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit Test Suite for Part 3: Message Reporting, Parallel Arrays, Search,
 * Longest Message, Delete by Hash, and JSON reading as specified on Page 19.
 *
 * References:
 * 1. JUnit 5 User Guide (2024). Available at: https://junit.org/junit5/docs/current/user-guide/ (Accessed: 4 September 2026).
 * 2. Hunt, A. and Thomas, D. (2015) 'Pragmatic Unit Testing in Java 8 with JUnit', Pragmatic Bookshelf.
 */
public class MessageReportTest {

    private MessageManager manager;

    @BeforeEach
    public void setUp() {
        manager = new MessageManager();
        // Populate standard assignment test data (Messages 1 to 5 from Page 18)
        manager.populateStandardTestData();
    }

    // =========================================================================
    // Test 1: Sent Messages array correctly populated (Page 19)
    // =========================================================================

    

    @Test
    @DisplayName("assertEquals: Sent Messages array correctly populated")
    public void testSentMessagesArrayCorrectlyPopulated() {
        String[] sentArray = manager.getSentMessagesArray();
        assertEquals(2, sentArray.length, "Should contain exactly 2 sent messages.");

        assertEquals("Did you get the cake?", sentArray[0]);
        assertEquals("It is dinner time !", sentArray[1]);
    }

    // =========================================================================
    // Test 2: Display the longest Message (Page 19)
    // =========================================================================

    @Test
    @DisplayName("assertEquals: Display the longest Message")
    public void testDisplayLongestMessage() {
        String expectedLongest = "Where are you? You are late! I have asked you to be on time.";
        String actualLongest = manager.displayLongestMessage();
        assertEquals(expectedLongest, actualLongest, "Longest message text must match Test Message 2.");
    }

    // =========================================================================
    // Test 3: Search for messageID (Page 19)
    // =========================================================================

    @Test
    @DisplayName("assertEquals: Search for messageID returns corresponding message")
    public void testSearchByMessageId() {
        String targetId = "0838884567";
        String expectedMessage = "It is dinner time !";
        String actualMessage = manager.searchByMessageId(targetId);
        assertEquals(expectedMessage, actualMessage, "Searching by message ID 0838884567 should return 'It is dinner time !'");
    }

    // =========================================================================
    // Test 4: Search all messages sent or stored regarding a particular recipient (Page 19)
    // =========================================================================

    @Test
    @DisplayName("assertEquals: Search all messages regarding a particular recipient")
    public void testSearchByRecipient() {
        String targetRecipient = "+27838884567";
        String expectedOutput = "\"Where are you? You are late! I have asked you to be on time.\" \"Ok, I am leaving without you.\"";
        String actualOutput = manager.searchByRecipient(targetRecipient);
        assertEquals(expectedOutput, actualOutput, "Recipient +27838884567 should return both messages 2 and 5.");
    }

    // =========================================================================
    // Test 5: Delete a message using a message hash (Page 19)
    // =========================================================================
