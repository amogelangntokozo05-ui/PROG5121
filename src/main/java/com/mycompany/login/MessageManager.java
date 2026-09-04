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