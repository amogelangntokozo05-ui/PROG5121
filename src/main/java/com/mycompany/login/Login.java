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

   // --- Validation Methods ---

   /**
    * Checks whether the given username meets requirements:
    * 1. Must contain an underscore (_).
    * 2. Must be no more than 5 characters in length.
    *
    * @param username The username to validate
    * @return true if valid, false otherwise
    */
   public boolean checkUserName(String username) {
      if (username == null || username.trim().isEmpty()) {
         return false;
      }
      return username.contains("_") && username.length() <= 5;
   }

   /**
    * Checks the currently stored username.
    *
    * @return true if valid, false otherwise
    */
   public boolean checkUserName() {
      return checkUserName(this.username);
   }

   /**
    * Checks whether the given password meets complexity rules:
    * 1. At least 8 characters long.
    * 2. Contains at least one capital (uppercase) letter.
    * 3. Contains at least one number (digit).
    * 4. Contains at least one special character.
    *
    * @param password The password to validate
    * @return true if complex enough, false otherwise
    */
   public boolean checkPasswordComplexity(String password) {
      if (password == null || password.length() < 8) {
         return false;
      }

      boolean hasCapital = false;
      boolean hasNumber = false;
      boolean hasSpecial = false;

      for (int i = 0; i < password.length(); i++) {
         char ch = password.charAt(i);
         if (Character.isUpperCase(ch)) {
            hasCapital = true;
         } else if (Character.isDigit(ch)) {
            hasNumber = true;
         } else if (!Character.isLetterOrDigit(ch) && !Character.isWhitespace(ch)) {
            hasSpecial = true;
         }
      }

      return hasCapital && hasNumber && hasSpecial;
   }


   /**
    * Checks the currently stored password.
    *
    * @return true if complex enough, false otherwise
    */
   public boolean checkPasswordComplexity() {
      return checkPasswordComplexity(this.password);
   }

   /**
    * Checks whether the cell phone number is correctly formatted using regex:
    * Contains the international country code (+27) followed by the phone number
    * (not more than 10 digits long).
    *
    * References:
    * Regex validation referencing Oracle Java Pattern Documentation & ITU E.164
    * international numbering standards.
    *
    * @param cellPhoneNumber The cell phone number string to validate
    * @return true if matches valid international SA phone format, false otherwise
    */
   public boolean checkCellPhoneNumber(String cellPhoneNumber) {
      if (cellPhoneNumber == null || cellPhoneNumber.trim().isEmpty()) {
         return false;
      }
      Matcher matcher = PHONE_PATTERN.matcher(cellPhoneNumber.trim());
      return matcher.matches();
   }

   /**
    * Checks the currently stored cell phone number.
    *
    * @return true if matches valid international SA phone format, false otherwise
    */
   public boolean checkCellPhoneNumber() {
      return checkCellPhoneNumber(this.cellPhoneNumber);
   }

   /**
    * Returns the validation message for a username.
    *
    * @param username The username to evaluate
    * @return Success message or descriptive error message
    */
   public String validateUsernameMessage(String username) {
      if (checkUserName(username)) {
         return MSG_USERNAME_SUCCESS;
      }
      return MSG_USERNAME_ERROR;
   }
