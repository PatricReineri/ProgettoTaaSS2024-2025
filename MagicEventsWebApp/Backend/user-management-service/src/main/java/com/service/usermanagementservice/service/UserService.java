package com.service.usermanagementservice.service;

import com.service.usermanagementservice.model.User;
import com.service.usermanagementservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserService {
    @Autowired
    UserRepository userRepository;

    public List<Long> getUserEmail(List<String> email) throws Exception {
        ArrayList<Long> usersEmail = new ArrayList<>();
        for (String s : email) {
            User user = userRepository.findByEmail(s);
            if (user != null) {
                usersEmail.add(user.getMagicEventTag());
            }else{
                throw new Exception("User not exist");
            }
        }
        return usersEmail;
    }
}