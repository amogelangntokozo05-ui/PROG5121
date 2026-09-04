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