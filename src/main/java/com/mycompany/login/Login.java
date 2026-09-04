package com.mycompany.login;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Login: handles registration, validation, and authentication.
public class Login {

   // Instance variables to store registered user details
   private String firstName;
   private String lastName;
   private String username;
   private String password;
   private String cellPhoneNumber;

   // SA phone regex: +27 followed by 9-10 digits.
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

   // Default constructor for Login class.
   public Login() {
      this.firstName = "";
      this.lastName = "";
      this.username = "";
      this.password = "";
      this.cellPhoneNumber = "";
   }

   // Constructor to initialize user fields.
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

   // Validate username: contains '_' and <= 5 characters.
   public boolean checkUserName(String username) {
      if (username == null || username.trim().isEmpty()) {
         return false;
      }
      return username.contains("_") && username.length() <= 5;
   }

   // Validate the stored username.
   public boolean checkUserName() {
      return checkUserName(this.username);
   }

   // Validate password complexity: >=8 chars, uppercase, digit, special.
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

   // Validate the stored password complexity.
   public boolean checkPasswordComplexity() {
      return checkPasswordComplexity(this.password);
   }

   // Validate SA phone format: international +27 followed by 9-10 digits.
   public boolean checkCellPhoneNumber(String cellPhoneNumber) {
      if (cellPhoneNumber == null || cellPhoneNumber.trim().isEmpty()) {
         return false;
      }
      Matcher matcher = PHONE_PATTERN.matcher(cellPhoneNumber.trim());
      return matcher.matches();
   }

   // Validate the stored cell phone number format.
   public boolean checkCellPhoneNumber() {
      return checkCellPhoneNumber(this.cellPhoneNumber);
   }

   // Return username validation message.
   public String validateUsernameMessage(String username) {
      if (checkUserName(username)) {
         return MSG_USERNAME_SUCCESS;
      }
      return MSG_USERNAME_ERROR;
   }

   // Return password validation message.
   public String validatePasswordMessage(String password) {
      if (checkPasswordComplexity(password)) {
         return MSG_PASSWORD_SUCCESS;
      }
      return MSG_PASSWORD_ERROR;
   }

   // Return cell phone validation message.
   public String validateCellPhoneMessage(String cellNumber) {
      if (checkCellPhoneNumber(cellNumber)) {
         return MSG_PHONE_SUCCESS;
      }
      return MSG_PHONE_ERROR;
   }

   // Register user if all validations pass; return status message.
   public String registerUser(String firstName, String lastName, String username, String password,
         String cellPhoneNumber) {
      if (!checkUserName(username)) {
         return MSG_USERNAME_ERROR;
      }

      if (!checkPasswordComplexity(password)) {
         return MSG_PASSWORD_ERROR;
      }

      if (!checkCellPhoneNumber(cellPhoneNumber)) {
         return MSG_PHONE_ERROR;
      }

      // Store details once all validation checks succeed
      this.firstName = firstName;
      this.lastName = lastName;
      this.username = username;
      this.password = password;
      this.cellPhoneNumber = cellPhoneNumber;

      return MSG_USERNAME_SUCCESS + "\n" + MSG_PASSWORD_SUCCESS + "\n" + MSG_PHONE_SUCCESS;
   }

   // Register user using instance fields.
   public String registerUser() {
      return registerUser(this.firstName, this.lastName, this.username, this.password, this.cellPhoneNumber);
   }

   // --- Login & Authentication Methods ---

   // Verify entered credentials against stored user.
   public boolean loginUser(String enteredUsername, String enteredPassword) {
      if (enteredUsername == null || enteredPassword == null) {
         return false;
      }
      if (this.username == null || this.password == null || this.username.isEmpty() || this.password.isEmpty()) {
         return false;
      }
      return this.username.equals(enteredUsername) && this.password.equals(enteredPassword);
   }

   // Return greeting on successful login or failure message.
   public String returnLoginStatus(boolean loginSuccess) {
      if (loginSuccess) {
         return "Welcome " + this.firstName + ", " + this.lastName + " it is great to see you again.";
      }
      return MSG_LOGIN_FAILED;
   }

   // Authenticate entered credentials and return appropriate login message.
   public String returnLoginStatus(String enteredUsername, String enteredPassword) {
      boolean success = loginUser(enteredUsername, enteredPassword);
      return returnLoginStatus(success);
   }
}
