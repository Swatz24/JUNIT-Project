package org.example;

import org.junit.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testing UserService Class")
public class UserServiceTest {
    @Mock
    private Map<String, User> userDatabase;

    @InjectMocks
    private UserService userService;


    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        System.out.println("Testing started.");
    }

    @AfterEach
    public void tearDownClass() {
        System.out.println("Compiling this method after testing with @AfterEach");
    }

    @AfterAll
    public static void tearDown() {
        System.out.println("Executing clean up operations after each test with @AfterAll");
    }

    // // Testing registerUser Method
    @Test
    public void registerUserPositive() {
        // Creating a new user for testing the registration process
        User user = new User("user1", "abcd1234", "user1@gmail.com");

        // Mocking the behaviour of the userDB, it checks the DB and returns false -> meaning the user doesn't exist in the DB.
        when(userDatabase.containsKey("user1")).thenReturn(false);

        // Calling the registerUser method using the userService object and passing the user as input.
        boolean result = userService.registerUser(user);
        assertTrue(result); // asserting the result. result will be true if the user is registered successfully.

        // verify() method is used to check if the put() method is called with the following arguments.
        verify(userDatabase).put("user1", user);
    }

    @Test
    public void registerUserNegative() {
        // Creating a new user for testing the registration process
        User user = new User("user1", "abcd1234", "user1@gmail.com");
        // Mocking the behaviour of the userDB, it checks the DB and returns true -> meaning the user already exist in the DB.
        when(userDatabase.containsKey("user1")).thenReturn(true);
        // So when calling the registerUser method with the "user" it should return false and we assertFalse the result.
        boolean result = userService.registerUser(user);
        assertFalse(result);
        // verify() method is used to check that the put() method is not called with any aruguments.
        verify(userDatabase, never()).put(anyString(), any(User.class));
    }

    @Test
    public void registerUserEdge() {
        // creating user with no username. Since the main UserService class accepts user without userName we are checking the edge case to see how it works.
        User user = new User("", "abcd1234", "user1@gmail.com");

        when(userDatabase.containsKey("")).thenReturn(false);
        boolean result = userService.registerUser(user);
        assertTrue(result);
        verify(userDatabase).put("", user);
    }

    @Test
    public void loginUserPositive() {
        // Creating a user.
        User user = new User("user1", "abcd1234", "user1@gmail.com");
        // set the behaviour of the DB mock to return the user when searching with "user1"
        when(userDatabase.get("user1")).thenReturn(user);
        // Check the username and password
        User loggedInUser = userService.loginUser("user1", "abcd1234");
        assertNotNull(loggedInUser); // should not be null, since the user is present in the DB
        assertEquals(user, loggedInUser); // should return the user after verifying the login credentials.
    }

    @Test
    public void loginUserNegative_UserNotFound() {
        // mock the behaviour to return null if the user is not found.
        when(userDatabase.get("unknownUser")).thenReturn(null);
        User loggedInUser = userService.loginUser("unknownUser", "pwd1234");
        assertNull(loggedInUser);
    }

    @Test
    public void loginUserEdge_WrongPassword() {
        // user is present in DB but the password given for login is wrong, so return Null
        User user = new User("user1", "abcd1234", "user1@gmail.com");
        when(userDatabase.get("user1")).thenReturn(user);
        User loggedInUser = userService.loginUser("user1", "1234abcd");
        // the method loginUser should return null when password is incorrect.
        assertNull(loggedInUser);
    }

    @Test
    public void updateProfilePositive() {
        // Creating new user which we can update later.
        User user = new User("user1", "abcd1234", "user1@gmail.com");
        // check if the new username is already taken, this mocks behaviour to show the username is not taken.
        when(userDatabase.containsKey("user2")).thenReturn(false);

        // updating the user1 with the new username, password and email. this should assert to True.
        boolean result = userService.updateUserProfile(user, "user2", "1234abcd", "user2@gmail.com");
        assertTrue(result);
        assertEquals("user2", user.getUsername()); // new username should be user2
        assertEquals("1234abcd", user.getPassword());
        assertEquals("user2@gmail.com", user.getEmail());
        // verifying if the put method is called to updated the new updated username.
        verify(userDatabase).put("user2", user);
    }

    @Test
    public void updateProfileNegative_UsernameExistsAlready() {
        User user1 = new User("user1", "abcd1234", "user1@gmail.com");
        // if the new username is already taken, then assert false.
        when(userDatabase.containsKey("user2")).thenReturn(true);
        boolean result = userService.updateUserProfile(user1, "user2", "1234", "user2@gmail.com");
        assertFalse(result);

        // when you check the details of the user , it should not reflect the changes that you tried to apply as the changes won't be applied.
        assertEquals("user1", user1.getUsername());
        assertEquals("abcd1234", user1.getPassword());
        assertEquals("user1@gmail.com", user1.getEmail());
        // check to see the put() is not called with any arguments.
        verify(userDatabase, never()).put(anyString(), any(User.class));
    }

    @Test
    public void updateProfileEdge() {
        User user1 = new User("user1", "abcd1234", "user1@gmail.com");
        // checking to see if any username that is already in DB, should return true.
        when(userDatabase.containsKey(anyString())).thenReturn(true);
        boolean result = userService.updateUserProfile(user1, "user2", "1234abcd", "user2@gmail.com");
        // new values should not be updated as the username already exists in DB
        assertFalse(result);
        assertEquals("user1", user1.getUsername());
        assertEquals("abcd1234", user1.getPassword());
        assertEquals("user1@gmail.com", user1.getEmail());
        // check to see the put() is not called with any arguments.
        verify(userDatabase, never()).put(anyString(), any(User.class));
    }
}
