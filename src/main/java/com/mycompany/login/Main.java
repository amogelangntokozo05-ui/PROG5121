package com.mycompany.login;

import java.util.Scanner;

/**
 * Main application class providing a pure console interface for both Part 1
 * (Registration and Login) and Part 2 (QuickChat Messaging system).
 *
 * References:
 * 1. Schildt, H. (2018) 'Java: The Complete Reference', 11th edn. New York: McGraw-Hill Education.
 * 2. Deitel, P. and Deitel, H. (2017) 'Java How to Program, Early Objects', 11th edn. Boston: Pearson.
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login userAccount = new Login();

        System.out.println("==================================================");
        System.out.println("          WELCOME TO QUICKCHAT APPLICATION        ");
        System.out.println("             Part 1: User Registration            ");
        System.out.println("==================================================");

        // --- Step 1: User Registration ---
        System.out.print("Enter your First Name: ");
        String firstName = scanner.nextLine().trim();

        System.out.print("Enter your Last Name: ");
        String lastName = scanner.nextLine().trim();

        // Username validation loop
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

        // Password complexity validation loop
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

        // Cell phone number validation loop
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

        

        // Complete registration
        userAccount.registerUser(firstName, lastName, username, password, cellPhoneNumber);
        System.out.println("\n>>> Registration completed successfully! <<<\n");

        // --- Step 2: User Login ---
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
            }
        }

        // --- Step 3: Part 2 QuickChat Feature Menu ---
        System.out.println("\n==================================================");
        System.out.println("              Welcome to QuickChat                ");
        System.out.println("==================================================");

        boolean running = true;
        while (running) {
            System.out.println("\nPlease choose an option from the menu:");
            System.out.println("Option 1) Send Messages");
            System.out.println("Option 2) Show recently sent messages");
            System.out.println("Option 3) Quit");
            System.out.print("Enter your choice (1-3): ");

            String choiceInput = scanner.nextLine().trim();

            switch (choiceInput) {
                case "1":
                    handleSendMessages(scanner);
                    break;

                case "2":
                    System.out.println("Coming Soon.");
                    break;

                case "3":
                    System.out.println("Exiting QuickChat. Thank you for using the application!");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option. Please choose 1, 2, or 3.");
                    break;
            }
        }

        scanner.close();
    }

    

    /**
     * Handles the message creation and sending process for the requested number of messages.
     * Uses a for loop to process each message as specified in Part 2.
     *
     * @param scanner The active Scanner object
     */
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

        // For loop to process the defined number of messages
        for (int i = 0; i < numMessages; i++) {
            System.out.println("\n--- Entering Message " + (i + 1) + " of " + numMessages + " ---");

            // Generate unique 10-digit message ID
            String messageId = Message.generateMessageID();
            int messageNumber = i;

            // Prompt and validate recipient cell number
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

            // Prompt and validate message text
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

            
            // Create Message object and compute hash
            Message message = new Message(messageId, messageNumber, recipient, messageText);

            // Action menu for sending, disregarding, or storing the message
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

            // Display full message details in specified order: Message ID, Message Hash, Recipient, Message
            System.out.println("\n--- Message Details ---");
            System.out.println(message.printMessages());
            System.out.println("-----------------------");
        }

        // Display accumulated total sent messages
        Message tracker = new Message();
        System.out.println("\n==================================================");
        System.out.println("Total messages sent so far: " + tracker.returnTotalMessagess());
        System.out.println("==================================================");
    }
}
