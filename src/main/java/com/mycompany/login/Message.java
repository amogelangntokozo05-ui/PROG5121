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

// Message class: represents a chat message and its validation logic.
public class Message {

    // Instance attributes.
    private String messageId;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;
    private String status; // Sent, Stored, Disregarded

    // Total messages successfully sent.
    private static int totalMessagesSent = 0;

    // Stored message list in memory.
    private static final List<Message> sentMessagesList = new ArrayList<>();

    // SA phone regex for recipient validation.
    private static final String SA_PHONE_REGEX = "^\\+27\\d{9,10}$";
    private static final Pattern PHONE_PATTERN = Pattern.compile(SA_PHONE_REGEX);

    // Feedback constants.
    public static final String MSG_PHONE_VALID = "Cell phone number successfully captured.";
    public static final String MSG_PHONE_INVALID = "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";

    public static final String MSG_LENGTH_VALID = "Message ready to send.";
    public static final String MSG_SENT_SUCCESS = "Message successfully sent.";
    public static final String MSG_DISREGARD = "Press 0 to delete the message.";
    public static final String MSG_STORED_SUCCESS = "Message successfully stored.";

    // Default constructor.
    public Message() {
        this.messageId = "";
        this.messageNumber = 0;
        this.recipient = "";
        this.messageText = "";
        this.messageHash = "";
        this.status = "";
    }

    // Constructor with message details.
    public Message(String messageId, int messageNumber, String recipient, String messageText) {
        this.messageId = messageId;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash(messageId, messageNumber, messageText);
        this.status = "Pending";
    }

    // Getters and setters.

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

    // Validation methods.

    // Validate message ID length.
    public boolean checkMessageID(String id) {
        return id != null && !id.trim().isEmpty() && id.trim().length() <= 10;
    }

    // Validate stored message ID.
    public boolean checkMessageID() {
        return checkMessageID(this.messageId);
    }

    // Check recipient cell format and return message result.
    public String checkRecipientCell(String recipientPhone) {
        if (recipientPhone != null && PHONE_PATTERN.matcher(recipientPhone.trim()).matches()) {
            return MSG_PHONE_VALID;
        }
        return MSG_PHONE_INVALID;
    }

    // Check stored recipient cell format.
    public String checkRecipientCell() {
        return checkRecipientCell(this.recipient);
    }

    // Check recipient validity.
    public boolean isRecipientCellValid(String recipientPhone) {
        return recipientPhone != null && PHONE_PATTERN.matcher(recipientPhone.trim()).matches();
    }

    // Validate message length up to 250 characters.
    public String validateMessageLength(String text) {
        if (text == null || text.length() <= 250) {
            return MSG_LENGTH_VALID;
        }
        int excess = text.length() - 250;
        return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
    }

    // Generate a unique 10-digit message ID.
    public static String generateMessageID() {
        SecureRandom random = new SecureRandom();
        long number = (long) (random.nextDouble() * 10_000_000_000L);
        return String.format("%010d", Math.abs(number));
    }

    // Message hash generation.

    // Create a message hash from the message ID, number, and text.
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
                firstWord = cleanWord(words[0]);
                lastWord = cleanWord(words[words.length - 1]);
            }
        }

        String combinedWords = (firstWord + lastWord).toUpperCase();
        return idPrefix + ":" + num + ":" + combinedWords;
    }

    // Create the message hash from instance fields.
    public String createMessageHash() {
        return createMessageHash(this.messageId, this.messageNumber, this.messageText);
    }

    // Remove punctuation from word edges.
    private static String cleanWord(String word) {
        if (word == null) return "";
        return word.replaceAll("^[^a-zA-Z0-9]+|[^a-zA-Z0-9]+$", "");
    }

    // Handle message sending actions.

    // Act on the chosen message action.
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

    // Handle string-based action choices.
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

    // Print full message details.
    public String printMessages() {
        return "Message ID: " + this.messageId + "\n" +
               "Message Hash: " + this.messageHash + "\n" +
               "Recipient: " + this.recipient + "\n" +
               "Message: " + this.messageText;
    }

    // Return current total sent messages.
    public int returnTotalMessagess() {
        return totalMessagesSent;
    }

    // Static accessor for total sent messages.
    public static int getTotalMessagesSent() {
        return totalMessagesSent;
    }

    // Reset counters for tests.
    public static void resetTotalMessagesSent() {
        totalMessagesSent = 0;
        sentMessagesList.clear();
    }

    // JSON storage.

    // Store a message in a JSON file.
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

    // Store to the default JSON file.
    public boolean storeMessage() {
        return storeMessage("messages.json");
    }
}
