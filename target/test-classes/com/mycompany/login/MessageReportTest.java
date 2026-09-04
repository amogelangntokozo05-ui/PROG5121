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

    
    @Test
    @DisplayName("assertEquals: Delete a message using message hash")
    public void testDeleteMessageUsingHash() {
        String hashToDelete = "00:1:WHERETIME";
        String expectedResponse = "Message: \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.";
        String actualResponse = manager.deleteMessageByHash(hashToDelete);

        assertEquals(expectedResponse, actualResponse);

        // Verify it was removed from arrays
        String searchResult = manager.searchByMessageId("0022222222");
        assertEquals("Message ID not found.", searchResult, "Deleted message ID should no longer be found.");
    }

    // =========================================================================
    // Test 6: Display Report (Page 19)
    // =========================================================================

    @Test
    @DisplayName("assertEquals / assertTrue: Display report showing Message Hash, Recipient, Message")
    public void testDisplayReport() {
        String report = manager.displayReport();

        assertTrue(report.contains("MESSAGE REPORT"), "Report must include header.");
        assertTrue(report.contains("00:0:DIDCAKE"), "Report must contain Message Hash.");
        assertTrue(report.contains("+27834557896"), "Report must contain Recipient.");
        assertTrue(report.contains("Did you get the cake?"), "Report must contain Message payload.");
    }

    // =========================================================================
    // Test 7: Read JSON file into array (Page 26 Rubric)
    // =========================================================================

    

    @Test
    @DisplayName("assertTrue: Read JSON file into an array")
    public void testReadJsonIntoArray() {
        String tempJsonPath = "temp_report_test.json";
        File file = new File(tempJsonPath);

        // Save current stored messages to temp file
        manager.saveStoredMessagesToJson(tempJsonPath);
        assertTrue(file.exists() && file.length() > 0, "JSON file should exist and contain data.");

        // Create clean manager and load from JSON
        MessageManager freshManager = new MessageManager();
        String[] loadedArray = freshManager.readStoredMessagesFromJson(tempJsonPath);

        assertTrue(loadedArray.length > 0, "Loaded array should contain stored messages from JSON.");
        assertEquals("Where are you? You are late! I have asked you to be on time.", loadedArray[0]);

        // Cleanup
        file.delete();
    }
}
