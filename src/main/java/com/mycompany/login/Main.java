package com.mycompany.login;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login userAccount = new Login();

        System.out.println("==================================================");
        System.out.println("          WELCOME TO QUICKCHAT APPLICATION        ");
        System.out.println("             Part 1: User Registration            ");
        System.out.println("==================================================");

        // User registration.
        System.out.print("Enter your First Name: ");
        String firstName = scanner.nextLine().trim();

        System.out.print("Enter your Last Name: ");
        String lastName = scanner.nextLine().trim();

        // Username loop.
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

        // Password loop.
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

        // Cell number loop.
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

        // Finish registration.
        userAccount.registerUser(firstName, lastName, username, password, cellPhoneNumber);
        System.out.println("\n>>> Registration completed successfully! <<<\n");

        // User login.
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

        // QuickChat menu.
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

    // Send message flow.
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

        // Run message loop.
        for (int i = 0; i < numMessages; i++) {
            System.out.println("\n--- Entering Message " + (i + 1) + " of " + numMessages + " ---");

            // Create a message id.
            String messageId = Message.generateMessageID();
            int messageNumber = i;

            // Validate recipient.
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

            // Validate text.
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

            // Build message.
            Message message = new Message(messageId, messageNumber, recipient, messageText);

            // Message action menu.
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

            // Show message details.
            System.out.println("\n--- Message Details ---");
            System.out.println(message.printMessages());
            System.out.println("-----------------------");
        }

        // Show total count.
        Message tracker = new Message();
        System.out.println("\n==================================================");
        System.out.println("Total messages sent so far: " + tracker.returnTotalMessagess());
        System.out.println("==================================================");
    }
}
