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
