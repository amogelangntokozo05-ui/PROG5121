package com.mycompany.login;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login {

   // Stored user details.
   private String firstName;
   private String lastName;
   private String username;
   private String password;
   private String cellPhoneNumber;

   // SA phone rule.
   private static final String SA_PHONE_REGEX = "^\\+27\\d{9,10}$";
   private static final Pattern PHONE_PATTERN = Pattern.compile(SA_PHONE_REGEX);

   // Basic status messages.
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

   // Default constructor.
   public Login() {
      this.firstName = "";
      this.lastName = "";
      this.username = "";
      this.password = "";
      this.cellPhoneNumber = "";
   }

   // Main constructor.
   public Login(String firstName, String lastName, String username, String password, String cellPhoneNumber) {
      this.firstName = firstName;
      this.lastName = lastName;
      this.username = username;
      this.password = password;
      this.cellPhoneNumber = cellPhoneNumber;
   }

   // Basic getters and setters.
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

   // Username checks.
   public boolean checkUserName(String username) {
      if (username == null || username.trim().isEmpty()) {
         return false;
      }
      return username.contains("_") && username.length() <= 5;
   }

   public boolean checkUserName() {
      return checkUserName(this.username);
   }

   // Password checks.
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

   public boolean checkPasswordComplexity() {
      return checkPasswordComplexity(this.password);
   }

   // Phone checks.
   public boolean checkCellPhoneNumber(String cellPhoneNumber) {
      if (cellPhoneNumber == null || cellPhoneNumber.trim().isEmpty()) {
         return false;
      }
      Matcher matcher = PHONE_PATTERN.matcher(cellPhoneNumber.trim());
      return matcher.matches();
   }

   public boolean checkCellPhoneNumber() {
      return checkCellPhoneNumber(this.cellPhoneNumber);
   }

   // Username feedback.
   public String validateUsernameMessage(String username) {
      if (checkUserName(username)) {
         return MSG_USERNAME_SUCCESS;
      }
      return MSG_USERNAME_ERROR;
   }

   // Password feedback.
   public String validatePasswordMessage(String password) {
      if (checkPasswordComplexity(password)) {
         return MSG_PASSWORD_SUCCESS;
      }
      return MSG_PASSWORD_ERROR;
   }

   // Cell phone feedback.
   public String validateCellPhoneMessage(String cellNumber) {
      if (checkCellPhoneNumber(cellNumber)) {
         return MSG_PHONE_SUCCESS;
      }
      return MSG_PHONE_ERROR;
   }

   // User registration.
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

      // Save user values.
      this.firstName = firstName;
      this.lastName = lastName;
      this.username = username;
      this.password = password;
      this.cellPhoneNumber = cellPhoneNumber;

      return MSG_USERNAME_SUCCESS + "\n" + MSG_PASSWORD_SUCCESS + "\n" + MSG_PHONE_SUCCESS;
   }

   public String registerUser() {
      return registerUser(this.firstName, this.lastName, this.username, this.password, this.cellPhoneNumber);
   }

   // Login checks.
   public boolean loginUser(String enteredUsername, String enteredPassword) {
      if (enteredUsername == null || enteredPassword == null) {
         return false;
      }
      if (this.username == null || this.password == null || this.username.isEmpty() || this.password.isEmpty()) {
         return false;
      }
      return this.username.equals(enteredUsername) && this.password.equals(enteredPassword);
   }

   // Login status.
   public String returnLoginStatus(boolean loginSuccess) {
      if (loginSuccess) {
         return "Welcome " + this.firstName + ", " + this.lastName + " it is great to see you again.";
      }
      return MSG_LOGIN_FAILED;
   }

   public String returnLoginStatus(String enteredUsername, String enteredPassword) {
      boolean success = loginUser(enteredUsername, enteredPassword);
      return returnLoginStatus(success);
   }
}
