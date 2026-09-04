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

    

    /**
     * Requirement 2.c: Search for a message ID and display corresponding recipient and message.
     *
     * @param targetId The message ID to search for
     * @return Matching message text or recipient & message description
     */
    public String searchByMessageId(String targetId) {
        if (targetId == null || targetId.trim().isEmpty()) {
            return "Message ID not found.";
        }

        for (int i = 0; i < messageIds.size(); i++) {
            if (messageIds.get(i).equalsIgnoreCase(targetId.trim())) {
                return allMessageObjects.get(i).getMessageText();
            }
        }
        return "Message ID not found.";
    }

    /**
     * Requirement 2.d: Search all messages sent or stored for a particular recipient.
     *
     * @param targetRecipient The recipient phone number to search for
     * @return Formatted string of all matching message contents
     */
    public String searchByRecipient(String targetRecipient) {
        if (targetRecipient == null || targetRecipient.trim().isEmpty()) {
            return "No messages found for recipient.";
        }

        List<String> matched = new ArrayList<>();
        for (int i = 0; i < recipients.size(); i++) {
            if (recipients.get(i).equalsIgnoreCase(targetRecipient.trim())) {
                String flag = flags.get(i);
                if ("Sent".equalsIgnoreCase(flag) || "Stored".equalsIgnoreCase(flag)) {
                    matched.add("\"" + allMessageObjects.get(i).getMessageText() + "\"");
                }
            }
        }

        if (matched.isEmpty()) {
            return "No messages found for recipient: " + targetRecipient;
        }

        return String.join(" ", matched);
    }

    /**
     * Requirement 2.e: Delete a message using the message hash.
     * Removes the message across all parallel arrays and lists.
     *
     * @param hash The Message Hash of the message to delete
     * @return Confirmation message: "Message: \"...\" successfully deleted."
     */
    public String deleteMessageByHash(String hash) {
        if (hash == null || hash.trim().isEmpty()) {
            return "Message hash not found.";
        }

        for (int i = 0; i < messageHashes.size(); i++) {
            if (messageHashes.get(i).equalsIgnoreCase(hash.trim())) {
                String msgText = allMessageObjects.get(i).getMessageText();
                String flag = flags.get(i);

                // Remove from specific category lists
                if ("Sent".equalsIgnoreCase(flag)) {
                    sentMessages.remove(msgText);
                } else if ("Disregard".equalsIgnoreCase(flag) || "Disregarded".equalsIgnoreCase(flag)) {
                    disregardedMessages.remove(msgText);
                } else if ("Stored".equalsIgnoreCase(flag)) {
                    storedMessages.remove(msgText);
                }

                // Remove from parallel arrays
                messageIds.remove(i);
                messageHashes.remove(i);
                recipients.remove(i);
                flags.remove(i);
                allMessageObjects.remove(i);

                return "Message: \"" + msgText + "\" successfully deleted.";
            }
        }

        return "Message with hash " + hash + " not found.";
    }

    

    /**
     * Requirement 2.f: Display a report that lists full details of all messages (or sent messages).
     * Includes: Message Hash, Recipient, Message.
     *
     * @return Formatted multi-line report string
     */
    public String displayReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("==================================================\n");
        sb.append("                 MESSAGE REPORT                   \n");
        sb.append("==================================================\n");

        if (allMessageObjects.isEmpty()) {
            sb.append("No messages to display.\n");
            return sb.toString();
        }

        for (int i = 0; i < allMessageObjects.size(); i++) {
            Message msg = allMessageObjects.get(i);
            sb.append("Message ID:   ").append(messageIds.get(i)).append("\n")
              .append("Message Hash: ").append(messageHashes.get(i)).append("\n")
              .append("Recipient:    ").append(recipients.get(i)).append("\n")
              .append("Message:      ").append(msg.getMessageText()).append("\n")
              .append("Status/Flag:  ").append(flags.get(i)).append("\n")
              .append("--------------------------------------------------\n");
        }
        return sb.toString();
    }

    // --- JSON Reading & Writing ---

    /**
     * Reads stored messages from a JSON file into the stored messages array.
     *
     * References:
     * Google Gson documentation for JSON deserialization into Java Collections:
     * https://github.com/google/gson
     *
     * @param filePath File path of the JSON file
     * @return Array of stored message texts loaded from JSON
     */
    public String[] readStoredMessagesFromJson(String filePath) {
        File file = new File(filePath);
        if (!file.exists() || file.length() == 0) {
            return new String[0];
        }

        Gson gson = new Gson();
        try (FileReader reader = new FileReader(file)) {
            Type listType = new TypeToken<ArrayList<Message>>() {}.getType();
            List<Message> loaded = gson.fromJson(reader, listType);

            if (loaded != null) {
                for (Message msg : loaded) {
                    addMessage(msg, "Stored");
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading JSON file: " + e.getMessage());
        }

        return getStoredMessagesArray();
    }

    /**
     * Saves all current stored messages into a JSON file.
     *
     * @param filePath File path
     * @return true if successful, false otherwise
     */
    public boolean saveStoredMessagesToJson(String filePath) {
        File file = new File(filePath);
        List<Message> toSave = new ArrayList<>();

        for (int i = 0; i < flags.size(); i++) {
            if ("Stored".equalsIgnoreCase(flags.get(i))) {
                toSave.add(allMessageObjects.get(i));
            }
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(toSave, writer);
            return true;
        } catch (IOException e) {
            System.err.println("Error saving stored messages to JSON: " + e.getMessage());
            return false;
        }
    }
}
