package com.mycompany.login;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Login class handles user registration, input validation,
 * password complexity verification, South African international cell phone
 * number
 * verification, and user authentication for the console chat application.
 *
 * References:
 * 1. Oracle (2024) 'Class Pattern - Regular Expressions in Java SE 21', Oracle
 * Documentation.
 * Available at:
 * https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/regex/Pattern.html
 * (Accessed: 4 September 2026).
 * 2. Goyvaerts, J. and Levithan, S. (2012) 'Regular Expressions Cookbook', 2nd
 * edn. Sebastopol: O'Reilly Media.
 * 3. International Telecommunication Union (2020) 'National Numbering Plans:
 * South Africa (country code +27)',
 * ITU-T Recommendation E.164. Available at: https://www.itu.int/ (Accessed: 4
 * September 2026).
 */
public class Login {

   // Instance variables to store registered user details
   private String firstName;
   private String lastName;
   private String username;
   private String password;
   private String cellPhoneNumber;

   /**
    * Regular expression pattern for validating South African cell phone numbers.
    * Criteria: Must start with international country code (+27) followed by 9 to
    * 10 digits.
    * Example valid number: +27838968976
    *
    * Reference Attribution:
    * Regex Pattern design adapted according to ITU-T E.164 standard for South
    * African (+27) numbering format.
    */
   private static final String SA_PHONE_REGEX = "^\\+27\\d{9,10}$";
   private static final Pattern PHONE_PATTERN = Pattern.compile(SA_PHONE_REGEX);

   // Constant feedback messages
   public static final String MSG_USERNAME_SUCCESS = "Username successfully captured.";
   public static final String MSG_USERNAME_ERROR = "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";

   public static final String MSG_PASSWORD_SUCCESS = "Password successfully captured.";
   public static final String MSG_PASSWORD_ERROR = "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";

   public static final String MSG_PHONE_SUCCESS = "Cell number successfully captured.";
   public static final String MSG_PHONE_SUCCESS_ALT = "Cell phone number successfully added.";
   public static final String MSG_PHONE_ERROR = "Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.";
   public static final String MSG_PHONE_ERROR_ALT = "Cell phone number incorrectly formatted or does not contain international code.";

   public static final String MSG_REGISTRATION_SUCCESS = "The user has been registered successfully.";
   public static final String MSG_LOGIN_FAILED = "Username or password incorrect, please try again.";

   /**
    * Default constructor for Login class.
    */
   public Login() {
      this.firstName = "";
      this.lastName = "";
      this.username = "";
      this.password = "";
      this.cellPhoneNumber = "";
   }



   /**
    * Parameterized constructor to initialize user information.
    *
    * @param firstName       User's first name
    * @param lastName        User's last name
    * @param username        User's username
    * @param password        User's password
    * @param cellPhoneNumber User's cell phone number
    */
   public Login(String firstName, String lastName, String username, String password, String cellPhoneNumber) {
      this.firstName = firstName;
      this.lastName = lastName;
      this.username = username;
      this.password = password;
      this.cellPhoneNumber = cellPhoneNumber;
   }

   // --- Getters and Setters ---

   public String getFirstName() {
      return firstName;
   }

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   public String getLastName() {
      return lastName;
   }

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getCellPhoneNumber() {
      return cellPhoneNumber;
   }

   public void setCellPhoneNumber(String cellPhoneNumber) {
      this.cellPhoneNumber = cellPhoneNumber;
   }

