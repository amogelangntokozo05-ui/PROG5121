package com.mycompany.login;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Message class represents a message in the QuickChat application.
 * It manages message creation, unique ID tracking, hash generation, recipient validation,
 * status dispatching, and JSON storage.
 *
 * References:
 * 1. Oracle (2024) 'Class Pattern - Regular Expressions in Java SE 21', Oracle Documentation.
 *    Available at: https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/regex/Pattern.html
 *    (Accessed: 4 September 2026).
 * 2. Google (2024) 'Gson User Guide - JSON serialization and deserialization library for Java'.
 *    Available at: https://github.com/google/gson (Accessed: 4 September 2026).
 * 3. Schildt, H. (2018) 'Java: The Complete Reference', 11th edn. New York: McGraw-Hill Education.
 */
public class Message {

    // Instance attributes
    private String messageId;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;
    private String status; // "Sent", "Stored", "Disregarded"

    // Static counter for total messages successfully sent
    private static int totalMessagesSent = 0;

    // Static list to track all sent message details in memory
    private static final List<Message> sentMessagesList = new ArrayList<>();

    // Regular expression for validating recipient cell phone number (starts with +27 followed by 9-10 digits)
    private static final String SA_PHONE_REGEX = "^\\+27\\d{9,10}$";
    private static final Pattern PHONE_PATTERN = Pattern.compile(SA_PHONE_REGEX);

    // Feedback constants
    public static final String MSG_PHONE_VALID = "Cell phone number successfully captured.";
    public static final String MSG_PHONE_INVALID = "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";

    public static final String MSG_LENGTH_VALID = "Message ready to send.";
    public static final String MSG_SENT_SUCCESS = "Message successfully sent.";
    public static final String MSG_DISREGARD = "Press 0 to delete the message.";
    public static final String MSG_STORED_SUCCESS = "Message successfully stored.";

    /**
     * Default constructor for Message class.
     */
    public Message() {
        this.messageId = "";
        this.messageNumber = 0;
        this.recipient = "";
        this.messageText = "";
        this.messageHash = "";
        this.status = "";
    }

    /**
     * Parameterized constructor for Message class.
     *
     * @param messageId     10-digit tracking ID
     * @param messageNumber Sequence number of message
     * @param recipient     Recipient phone number
     * @param messageText   Message payload
     */
    public Message(String messageId, int messageNumber, String recipient, String messageText) {
        this.messageId = messageId;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash(messageId, messageNumber, messageText);
        this.status = "Pending";
    }

    // --- Getters and Setters ---

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
        if (this.messageText != null && !this.messageText.isEmpty()) {
            this.messageHash = createMessageHash(this.messageId, this.messageNumber, this.messageText);
        }
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public void setMessageNumber(int messageNumber) {
        this.messageNumber = messageNumber;
        if (this.messageId != null && this.messageText != null) {
            this.messageHash = createMessageHash(this.messageId, this.messageNumber, this.messageText);
        }
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
        if (this.messageId != null && !this.messageId.isEmpty()) {
            this.messageHash = createMessageHash(this.messageId, this.messageNumber, this.messageText);
        }
    }

    public String getMessageHash() {
        return messageHash;
    }

    public void setMessageHash(String messageHash) {
        this.messageHash = messageHash;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static List<Message> getSentMessagesList() {
        return sentMessagesList;
    }

    // --- Validation Methods ---

    /**
     * Ensures that the message ID is not more than ten characters in length.
     *
     * @param id The message ID to validate
     * @return true if ID is non-null and <= 10 characters, false otherwise
     */
    public boolean checkMessageID(String id) {
        return id != null && !id.trim().isEmpty() && id.trim().length() <= 10;
    }

    /**
     * Checks the currently stored message ID.
     *
     * @return true if ID is valid (<= 10 chars), false otherwise
     */
    public boolean checkMessageID() {
        return checkMessageID(this.messageId);
    }

    /**
     * Ensures that the recipient cell number is correctly formatted:
     * Starts with international code (+27) and has valid format.
     *
     * @param recipientPhone The phone number string to validate
     * @return Success message or descriptive error message
     */
    public String checkRecipientCell(String recipientPhone) {
        if (recipientPhone != null && PHONE_PATTERN.matcher(recipientPhone.trim()).matches()) {
            return MSG_PHONE_VALID;
        }
        return MSG_PHONE_INVALID;
    }

    /**
     * Checks the currently stored recipient phone number.
     *
     * @return Success message or descriptive error message
     */
    public String checkRecipientCell() {
        return checkRecipientCell(this.recipient);
    }

    /**
     * Boolean check for recipient cell number validity.
     *
     * @param recipientPhone The phone number to test
     * @return true if valid, false otherwise
     */
    public boolean isRecipientCellValid(String recipientPhone) {
        return recipientPhone != null && PHONE_PATTERN.matcher(recipientPhone.trim()).matches();
    }

    /**
     * Validates that the message text does not exceed 250 characters.
     *
     * @param text The message text to validate
     * @return "Message ready to send." if valid, or "Message exceeds 250 characters by X; please reduce the size."
     */
    public String validateMessageLength(String text) {
        if (text == null || text.length() <= 250) {
            return MSG_LENGTH_VALID;
        }
        int excess = text.length() - 250;
        return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
    }

    /**
     * Generates a 10-digit unique tracking Message ID.
     *
     * @return 10-digit numeric string
     */
    public static String generateMessageID() {
        SecureRandom random = new SecureRandom();
        long number = (long) (random.nextDouble() * 10_000_000_000L);
        return String.format("%010d", Math.abs(number));
    }

    // --- Message Hash Generation ---

    /**
     * Creates and returns the Message Hash from message ID, message number, and text.
     * Format: <First 2 numbers of Message ID>:<Message Number>:<FIRST WORD><LAST WORD>
     * Displayed in all uppercase (e.g. 00:0:HITONIGHT).
     *
     * References:
     * String manipulation methods referencing Oracle Java String API documentation.
     *
     * @param id   Message ID string
     * @param num  Message sequence number
     * @param text Message body
     * @return Formatted message hash in all caps
     */
    public String createMessageHash(String id, int num, String text) {
        String idPrefix = "00";
        if (id != null && id.length() >= 2) {
            idPrefix = id.substring(0, 2);
        } else if (id != null) {
            idPrefix = id;
        }

        String firstWord = "";
        String lastWord = "";

        if (text != null && !text.trim().isEmpty()) {
            String[] words = text.trim().split("\\s+");
            if (words.length > 0) {
                // Remove non-alphanumeric punctuation from boundaries
                firstWord = cleanWord(words[0]);
                lastWord = cleanWord(words[words.length - 1]);
            }
        }

        String combinedWords = (firstWord + lastWord).toUpperCase();
        return idPrefix + ":" + num + ":" + combinedWords;
    }

    /**
     * Creates the message hash using the instance attributes.
     *
     * @return Formatted message hash in all caps
     */
    public String createMessageHash() {
        return createMessageHash(this.messageId, this.messageNumber, this.messageText);
    }

    /**
     * Helper method to strip punctuation from word boundaries.
     *
     * @param word Raw word
     * @return Cleaned alphanumeric word
     */
    private static String cleanWord(String word) {
        if (word == null) return "";
        return word.replaceAll("^[^a-zA-Z0-9]+|[^a-zA-Z0-9]+$", "");
    }

    // --- Message Action & Sending ---

    /**
     * Handles message dispatching based on user's numeric choice:
     * 1: Send Message -> "Message successfully sent."
     * 2: Disregard Message -> "Press 0 to delete the message."
     * 3: Store Message -> "Message successfully stored."
     *
     * @param choice Numeric menu selection (1, 2, or 3)
     * @return Appropriate status feedback message
     */
    public String SentMessage(int choice) {
        switch (choice) {
            case 1:
                this.status = "Sent";
                totalMessagesSent++;
                sentMessagesList.add(this);
                return MSG_SENT_SUCCESS;
            case 2:
                this.status = "Disregarded";
                return MSG_DISREGARD;
            case 3:
                this.status = "Stored";
                storeMessage();
                return MSG_STORED_SUCCESS;
            default:
                return "Invalid choice.";
        }
    }

    /**
     * Overloaded SentMessage accepting string choice.
     *
     * @param choice String choice description or number
     * @return Feedback message
     */
    public String SentMessage(String choice) {
        if (choice == null) return "Invalid choice.";
        String trimmed = choice.trim().toLowerCase();
        if (trimmed.contains("send") || trimmed.equals("1")) {
            return SentMessage(1);
        } else if (trimmed.contains("disregard") || trimmed.contains("discard") || trimmed.contains("delete") || trimmed.equals("2")) {
            return SentMessage(2);
        } else if (trimmed.contains("store") || trimmed.equals("3")) {
            return SentMessage(3);
        }
        return "Invalid choice.";
    }

    /**
     * Returns formatted details of the message in the order specified on page 13:
     * Message ID, Message Hash, Recipient, Message.
     *
     * @return Formatted multi-line string of message details
     */
    public String printMessages() {
        return "Message ID: " + this.messageId + "\n" +
               "Message Hash: " + this.messageHash + "\n" +
               "Recipient: " + this.recipient + "\n" +
               "Message: " + this.messageText;
    }

    /**
     * Returns the total accumulated number of messages sent during the application lifecycle.
     *
     * @return Total number of sent messages
     */
    public int returnTotalMessagess() {
        return totalMessagesSent;
    }

    /**
     * Static accessor for total messages sent.
     *
     * @return Total number of sent messages
     */
    public static int getTotalMessagesSent() {
        return totalMessagesSent;
    }

    /**
     * Resets the total messages sent count (primarily for unit testing isolation).
     */
    public static void resetTotalMessagesSent() {
        totalMessagesSent = 0;
        sentMessagesList.clear();
    }

    // --- JSON Storage ---

    /**
     * Stores this message in a JSON file (messages.json).
     * Reads existing messages, appends current message, and writes back formatted JSON.
     *
     * References:
     * Google Gson documentation for JSON serialization and file persistence:
     * https://github.com/google/gson
     *
     * @param filePath File path to store the JSON data
     * @return true if stored successfully, false if an error occurred
     */
    public boolean storeMessage(String filePath) {
        File file = new File(filePath);
        List<Message> messageList = new ArrayList<>();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        if (file.exists() && file.length() > 0) {
            try (FileReader reader = new FileReader(file)) {
                Type listType = new TypeToken<ArrayList<Message>>() {}.getType();
                List<Message> existing = gson.fromJson(reader, listType);
                if (existing != null) {
                    messageList.addAll(existing);
                }
            } catch (IOException e) {
                System.err.println("Error reading existing JSON file: " + e.getMessage());
            }
        }

        messageList.add(this);

        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(messageList, writer);
            return true;
        } catch (IOException e) {
            System.err.println("Error writing to JSON file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Stores this message in the default JSON file ("messages.json").
     *
     * @return true if stored successfully, false otherwise
     */
    public boolean storeMessage() {
        return storeMessage("messages.json");
    }
}
