package com.mycompany.login;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * MessageManager manages parallel arrays and collections for QuickChat messages:
 * - Sent Messages
 * - Disregarded Messages
 * - Stored Messages (loaded from JSON)
 * - Message Hashes
 * - Message IDs
 * - Recipients
 *
 * It provides searching, longest message calculation, hash-based deletion,
 * JSON reading/writing, and report generation.
 *
 * References:
 * 1. Oracle (2024) 'Arrays in Java', Oracle Java SE Documentation.
 *    Available at: https://docs.oracle.com/javase/tutorial/java/nutsandbolts/arrays.html (Accessed: 4 September 2026).
 * 2. Google (2024) 'Gson User Guide - Reading and Writing JSON Arrays'.
 *    Available at: https://github.com/google/gson (Accessed: 4 September 2026).
 * 3. Schildt, H. (2018) 'Java: The Complete Reference', 11th edn. New York: McGraw-Hill Education.
 */
public class MessageManager {

    // Parallel collections representing the required arrays
    private final List<String> sentMessages = new ArrayList<>();
    private final List<String> disregardedMessages = new ArrayList<>();
    private final List<String> storedMessages = new ArrayList<>();
    private final List<String> messageHashes = new ArrayList<>();
    private final List<String> messageIds = new ArrayList<>();
    private final List<String> recipients = new ArrayList<>();
    private final List<String> flags = new ArrayList<>();

    // Full Message objects for detailed reporting and JSON persistence
    private final List<Message> allMessageObjects = new ArrayList<>();

    /**
     * Default constructor.
     */
    public MessageManager() {
    }

    // --- Array Getters ---

    public String[] getSentMessagesArray() {
        return sentMessages.toArray(new String[0]);
    }

    public String[] getDisregardedMessagesArray() {
        return disregardedMessages.toArray(new String[0]);
    }

    public String[] getStoredMessagesArray() {
        return storedMessages.toArray(new String[0]);
    }

    public String[] getMessageHashesArray() {
        return messageHashes.toArray(new String[0]);
    }

    public String[] getMessageIdsArray() {
        return messageIds.toArray(new String[0]);
    }

    public String[] getRecipientsArray() {
        return recipients.toArray(new String[0]);
    }

    public List<Message> getAllMessageObjects() {
        return allMessageObjects;
    }

    

    // --- Message Addition & Population ---

    /**
     * Adds a message to the manager and populates the parallel arrays based on its flag.
     *
     * @param msg The Message object
     * @param flag "Sent", "Stored", or "Disregard"
     */
    public void addMessage(Message msg, String flag) {
        if (msg == null) return;

        allMessageObjects.add(msg);
        messageIds.add(msg.getMessageId());
        messageHashes.add(msg.getMessageHash());
        recipients.add(msg.getRecipient());
        flags.add(flag);

        if ("Sent".equalsIgnoreCase(flag)) {
            sentMessages.add(msg.getMessageText());
        } else if ("Disregard".equalsIgnoreCase(flag) || "Disregarded".equalsIgnoreCase(flag)) {
            disregardedMessages.add(msg.getMessageText());
        } else if ("Stored".equalsIgnoreCase(flag)) {
            storedMessages.add(msg.getMessageText());
        }
    }

    /**
     * Convenience method to populate test data as specified on Page 18 of the assignment.
     */
    public void populateStandardTestData() {
        clearAll();

        // Test Data Message 1: Recipient +27834557896, Message: "Did you get the cake?", Flag: Sent
        Message m1 = new Message("0011111111", 0, "+27834557896", "Did you get the cake?");
        m1.setMessageHash("00:0:DIDCAKE");
        addMessage(m1, "Sent");

        // Test Data Message 2: Recipient +27838884567, Message: "Where are you? You are late! I have asked you to be on time.", Flag: Stored
        Message m2 = new Message("0022222222", 1, "+27838884567", "Where are you? You are late! I have asked you to be on time.");
        m2.setMessageHash("00:1:WHERETIME");
        addMessage(m2, "Stored");

        // Test Data Message 3: Recipient +27834484567, Message: "Yohoooo, I am at your gate.", Flag: Disregard
        Message m3 = new Message("0033333333", 2, "+27834484567", "Yohoooo, I am at your gate.");
        m3.setMessageHash("00:2:YOHOOOOGATE");
        addMessage(m3, "Disregard");

        // Test Data Message 4: Developer / ID: 0838884567, Message: "It is dinner time !", Flag: Sent
        Message m4 = new Message("0838884567", 3, "0838884567", "It is dinner time !");
        m4.setMessageHash("08:3:ITTIME");
        addMessage(m4, "Sent");

        // Test Data Message 5: Recipient +27838884567, Message: "Ok, I am leaving without you.", Flag: Stored
        Message m5 = new Message("0055555555", 4, "+27838884567", "Ok, I am leaving without you.");
        m5.setMessageHash("00:4:OKYOU");
        addMessage(m5, "Stored");
    }

    
    /**
     * Clears all parallel arrays and collections.
     */
    public void clearAll() {
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageHashes.clear();
        messageIds.clear();
        recipients.clear();
        flags.clear();
        allMessageObjects.clear();
    }

    // --- Part 3 Requirements ---

    /**
     * Requirement 2.a: Display sender and recipient of all stored messages.
     *
     * @param defaultSender The name of the sender (e.g. current logged in user)
     * @return Formatted string displaying sender and recipient for all stored messages
     */
    public String displaySenderAndRecipientOfStoredMessages(String defaultSender) {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Stored Messages: Senders and Recipients ---\n");
        boolean found = false;
        for (int i = 0; i < flags.size(); i++) {
            if ("Stored".equalsIgnoreCase(flags.get(i))) {
                sb.append("Sender: ").append(defaultSender)
                  .append(" | Recipient: ").append(recipients.get(i))
                  .append(" | Message: \"").append(allMessageObjects.get(i).getMessageText()).append("\"\n");
                found = true;
            }
        }
        if (!found) {
            sb.append("No stored messages found.\n");
        }
        return sb.toString();
    }

    /**
     * Requirement 2.b: Display the longest stored message (or longest message across dataset).
     *
     * @return The text of the longest message
     */
    public String displayLongestMessage() {
        if (allMessageObjects.isEmpty()) {
            return "No messages available.";
        }

        String longest = "";
        for (Message msg : allMessageObjects) {
            if (msg.getMessageText() != null && msg.getMessageText().length() > longest.length()) {
                longest = msg.getMessageText();
            }
        }
        return longest;
    }