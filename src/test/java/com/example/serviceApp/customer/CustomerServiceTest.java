package com.example.serviceApp.customer;

import com.example.serviceApp.security.User.Role;
import com.example.serviceApp.security.User.User;
import com.example.serviceApp.security.User.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void deleteById_existingId_userDeleted() {
        // Create a user
        User user = new User("A","B",null, Role.USER,"test@email");


        // Save the user to the in-memory database
        entityManager.persist(user);
        entityManager.flush();
        entityManager.clear();  // Clear the context to simulate a new transaction

        // Delete the user by ID
        userRepository.deleteById(user.getId());

        // Check if the user does not exist
        assertFalse(userRepository.existsById(user.getId()));
    }

}