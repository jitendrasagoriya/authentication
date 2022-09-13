package com.js.authentication.service;

import com.js.authentication.model.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserDetailsService {

    Optional<UserDetails> getUserDetails(String id);

    Optional<UserDetails> save(UserDetails userDetails);

    Optional<UserDetails> getByExample(UserDetails userDetails);
}

