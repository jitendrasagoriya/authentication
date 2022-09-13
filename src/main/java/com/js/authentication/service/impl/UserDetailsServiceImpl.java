package com.js.authentication.service.impl;

import com.js.authentication.dao.UserDetailsDao;
import com.js.authentication.model.UserDetails;
import com.js.authentication.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserDetailsDao userDetailsDao;


    @Override
    public Optional<UserDetails> getUserDetails(String id) {
        return userDetailsDao.getUserDetails(id);
    }

    @Override
    public Optional<UserDetails> save(UserDetails userDetails) {
        return userDetailsDao.save(userDetails);
    }

    @Override
    public Optional<UserDetails> getByExample(UserDetails userDetails) {
        Example<UserDetails> userDetailsExample = Example.of(userDetails);
        return userDetailsDao.getByExample(userDetailsExample);
    }
}
