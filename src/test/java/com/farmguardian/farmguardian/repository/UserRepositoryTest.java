package com.farmguardian.farmguardian.repository;

import com.farmguardian.farmguardian.domain.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    private final UserRepository userRepository;

    @Autowired
    public UserRepositoryTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    
    @Test
    public void test() throws Exception{
        /*
        userRepository.save(new User("asd","asdf"));
        List<User> all = userRepository.findAll();
        System.out.println("all.get(0) = " + all.get(0));
         */
    }
}