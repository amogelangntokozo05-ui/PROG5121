package com.mycompany.login;

import java.util.Scanner;

// Main app: user registration, login, and QuickChat menu flow.
public class Main {

    private static final MessageManager messageManager = new MessageManager();
    private static String loggedInUserName = "User";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login userAccount = new Login();

        System.out.println("==================================================");
        System.out.println("          WELCOME TO QUICKCHAT APPLICATION        ");
        System.out.println("             Part 1: User Registration            ");
        System.out.println("==================================================");

        // Registration flow.
        System.out.print("Enter your First Name: ");
        String firstName = scanner.nextLine().trim();

        System.out.print("Enter your Last Name: ");
        String lastName = scanner.nextLine().trim();

        // Username validation loop.
        String username;
        while (true) {
            System.out.print("Enter Username (must contain '_' and <= 5 characters): ");
            username = scanner.nextLine().trim();
            if (userAccount.checkUserName(username)) {
                System.out.println(Login.MSG_USERNAME_SUCCESS);
                break;
            } else {
                System.out.println(Login.MSG_USERNAME_ERROR);
            }
        }

        // Password validation loop.
        String password;
        while (true) {
            System.out.print("Enter Password (min 8 chars, uppercase, digit, special char): ");
            password = scanner.nextLine().trim();
            if (userAccount.checkPasswordComplexity(password)) {
                System.out.println(Login.MSG_PASSWORD_SUCCESS);
                break;
            } else {
                System.out.println(Login.MSG_PASSWORD_ERROR);
            }
        }

        // Cell number validation loop.
        String cellPhoneNumber;
        while (true) {
            System.out.print("Enter SA Cell Phone Number (e.g., +27838968976): ");
            cellPhoneNumber = scanner.nextLine().trim();
            if (userAccount.checkCellPhoneNumber(cellPhoneNumber)) {
                System.out.println(Login.MSG_PHONE_SUCCESS);
                break;
            } else {
                System.out.println(Login.MSG_PHONE_ERROR);
            }
        }

        // Complete registration.
        userAccount.registerUser(firstName, lastName, username, password, cellPhoneNumber);
        System.out.println("\n>>> Registration completed successfully! <<<\n");

        // Login flow.
        System.out.println("==================================================");
        System.out.println("                   USER LOGIN                     ");
        System.out.println("==================================================");

        boolean loggedIn = false;
        while (!loggedIn) {
            System.out.print("Enter Username: ");
            String loginUsername = scanner.nextLine().trim();

            System.out.print("Enter Password: ");
            String loginPassword = scanner.nextLine().trim();

            loggedIn = userAccount.loginUser(loginUsername, loginPassword);
            String statusMessage = userAccount.returnLoginStatus(loggedIn);
            System.out.println(statusMessage);

            if (!loggedIn) {
                System.out.println("Please check your credentials and try again.\n");
            } else {
                loggedInUserName = firstName + " " + lastName;
            }
        }

        // Main menu flow.
        System.out.println("\n==================================================");
        System.out.println("              Welcome to QuickChat                ");
        System.out.println("==================================================");

        boolean running = true;
        while (running) {
            System.out.println("\nPlease choose an option from the main menu:");
            System.out.println("Option 1) Send Messages");
            System.out.println("Option 2) Show recently sent messages");
            System.out.println("Option 3) Stored Messages & Reports");
            System.out.println("Option 4) Quit");
            System.out.print("Enter your choice (1-4): ");

            String choiceInput = scanner.nextLine().trim();

            switch (choiceInput) {
                case "1":
                    handleSendMessages(scanner);
                    break;

                case "2":
                    System.out.println("Coming Soon.");
                    break;

                case "3":
                    handleStoredMessagesMenu(scanner);
                    break;

                case "4":
                    System.out.println("Exiting QuickChat. Thank you for using the application!");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option. Please choose 1, 2, 3, or 4.");
                    break;
            }
        }

        scanner.close();
    }

    // Send message workflow.
    private static void handleSendMessages(Scanner scanner) {
        int numMessages = 0;
        while (true) {
            System.out.print("\nHow many messages would you like to enter? ");
            try {
                numMessages = Integer.parseInt(scanner.nextLine().trim());
                if (numMessages > 0) {
                    break;
                } else {
                    System.out.println("Please enter a positive integer greater than 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a valid integer.");
            }
        }

        // Process each entered message.
        for (int i = 0; i < numMessages; i++) {
            System.out.println("\n--- Entering Message " + (i + 1) + " of " + numMessages + " ---");

            String messageId = Message.generateMessageID();
            int messageNumber = i;

            String recipient;
            Message tempValidator = new Message();
            while (true) {
                System.out.print("Enter recipient cell number (with international code e.g. +27718693002): ");
                recipient = scanner.nextLine().trim();
                String validationResponse = tempValidator.checkRecipientCell(recipient);
                System.out.println(validationResponse);
                if (tempValidator.isRecipientCellValid(recipient)) {
                    break;
                }
            }

            String messageText;
            while (true) {
                System.out.print("Enter message (max 250 characters): ");
                messageText = scanner.nextLine().trim();
                String lengthValidation = tempValidator.validateMessageLength(messageText);
                System.out.println(lengthValidation);
                if (messageText.length() <= 250) {
                    break;
                }
            }

            Message message = new Message(messageId, messageNumber, recipient, messageText);

            System.out.println("\nChoose action for this message:");
            System.out.println("1) Send Message");
            System.out.println("2) Disregard Message");
            System.out.println("3) Store Message to send later");
            System.out.print("Select choice (1-3): ");

            int actionChoice = 1;
            try {
                actionChoice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                actionChoice = 1;
            }

            String actionFeedback = message.SentMessage(actionChoice);
            System.out.println("\n" + actionFeedback);

            // Track message status.
            String flag = "Sent";
            if (actionChoice == 2) flag = "Disregard";
            else if (actionChoice == 3) flag = "Stored";
            messageManager.addMessage(message, flag);

            // Display message details.
            System.out.println("\n--- Message Details ---");
            System.out.println(message.printMessages());
            System.out.println("-----------------------");
        }

        // Display total messages sent.
        Message tracker = new Message();
        System.out.println("\n==================================================");
        System.out.println("Total messages sent so far: " + tracker.returnTotalMessagess());
        System.out.println("==================================================");
    }

    // Stored message and report menu.
    private static void handleStoredMessagesMenu(Scanner scanner) {
        boolean inSubMenu = true;
        while (inSubMenu) {
            System.out.println("\n--- Stored Messages & Reports Menu ---");
            System.out.println("a) Display sender and recipient of all stored messages");
            System.out.println("b) Display the longest stored message");
            System.out.println("c) Search for a message ID");
            System.out.println("d) Search all messages for a particular recipient");
            System.out.println("e) Delete a message using message hash");
            System.out.println("f) Display full message report");
            System.out.println("g) Load stored messages from JSON file");
            System.out.println("h) Populate standard test dataset (from specification)");
            System.out.println("i) Back to main menu");
            System.out.print("Choose an option (a-i): ");

            String subChoice = scanner.nextLine().trim().toLowerCase();

            switch (subChoice) {
                case "a":
                    System.out.println(messageManager.displaySenderAndRecipientOfStoredMessages(loggedInUserName));
                    break;

                case "b":
                    System.out.println("\nLongest message:");
                    System.out.println("\"" + messageManager.displayLongestMessage() + "\"");
                    break;

                case "c":
                    System.out.print("Enter message ID: ");
                    String id = scanner.nextLine().trim();
                    System.out.println(messageManager.searchByMessageId(id));
                    break;

                case "d":
                    System.out.print("Enter recipient number: ");
                    String recipient = scanner.nextLine().trim();
                    System.out.println(messageManager.searchByRecipient(recipient));
                    break;

                case "e":
                    System.out.print("Enter message hash: ");
                    String hash = scanner.nextLine().trim();
                    System.out.println(messageManager.deleteMessageByHash(hash));
                    break;

                case "f":
                    System.out.println(messageManager.displayReport());
                    break;

                case "g":
                    System.out.print("Enter JSON file path: ");
                    String jsonPath = scanner.nextLine().trim();
                    System.out.println(messageManager.readStoredMessagesFromJson(jsonPath));
                    break;

                case "h":
                    messageManager.populateStandardTestData();
                    System.out.println("Standard test dataset loaded.");
                    break;

                case "i":
                    inSubMenu = false;
                    break;

                default:
                    System.out.println("Invalid option. Please choose a valid menu option.");
                    break;
            }
        }
    }
}
