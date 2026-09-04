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

// MessageManager: handles stored messages, search, reports, and JSON operations.
public class MessageManager {

    // Parallel collections for message data.
    private final List<String> sentMessages = new ArrayList<>();
    private final List<String> disregardedMessages = new ArrayList<>();
    private final List<String> storedMessages = new ArrayList<>();
    private final List<String> messageHashes = new ArrayList<>();
    private final List<String> messageIds = new ArrayList<>();
    private final List<String> recipients = new ArrayList<>();
    private final List<String> flags = new ArrayList<>();

    // Message objects for reporting and persistence.
    private final List<Message> allMessageObjects = new ArrayList<>();

    // Default constructor.
    public MessageManager() {
    }

    // Array getters.

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

    // Add a message to the manager.
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

    // Populate standard test data.
    public void populateStandardTestData() {
        clearAll();

        Message m1 = new Message("0011111111", 0, "+27834557896", "Did you get the cake?");
        m1.setMessageHash("00:0:DIDCAKE");
        addMessage(m1, "Sent");

        Message m2 = new Message("0022222222", 1, "+27838884567", "Where are you? You are late! I have asked you to be on time.");
        m2.setMessageHash("00:1:WHERETIME");
        addMessage(m2, "Stored");

        Message m3 = new Message("0033333333", 2, "+27834484567", "Yohoooo, I am at your gate.");
        m3.setMessageHash("00:2:YOHOOOOGATE");
        addMessage(m3, "Disregard");

        Message m4 = new Message("0838884567", 3, "0838884567", "It is dinner time !");
        m4.setMessageHash("08:3:ITTIME");
        addMessage(m4, "Sent");

        Message m5 = new Message("0055555555", 4, "+27838884567", "Ok, I am leaving without you.");
        m5.setMessageHash("00:4:OKYOU");
        addMessage(m5, "Stored");
    }

    // Clear all stored message data.
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

    // Requirement 2.a: show sender and recipient of stored messages.
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

    // Requirement 2.b: display the longest message.
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

    // Requirement 2.c: search a message by ID.
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

    // Requirement 2.d: search messages by recipient.
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

    // Requirement 2.e: delete a message by hash.
    public String deleteMessageByHash(String hash) {
        if (hash == null || hash.trim().isEmpty()) {
            return "Message hash not found.";
        }

        for (int i = 0; i < messageHashes.size(); i++) {
            if (messageHashes.get(i).equalsIgnoreCase(hash.trim())) {
                String msgText = allMessageObjects.get(i).getMessageText();
                String flag = flags.get(i);

                if ("Sent".equalsIgnoreCase(flag)) {
                    sentMessages.remove(msgText);
                } else if ("Disregard".equalsIgnoreCase(flag) || "Disregarded".equalsIgnoreCase(flag)) {
                    disregardedMessages.remove(msgText);
                } else if ("Stored".equalsIgnoreCase(flag)) {
                    storedMessages.remove(msgText);
                }

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

    // Requirement 2.f: display the message report.
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

    // Read stored messages from JSON.
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

    // Save stored messages to JSON.
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
