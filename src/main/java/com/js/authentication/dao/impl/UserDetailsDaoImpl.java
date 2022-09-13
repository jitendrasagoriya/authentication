package com.js.authentication.dao.impl;

import com.js.authentication.dao.UserDetailsDao;
import com.js.authentication.model.UserDetails;
import com.js.authentication.repository.UserDetailsRepository;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsDaoImpl implements UserDetailsDao<UserDetailsRepository> {

    @Autowired
    private UserDetailsRepository repository;

    @Override
    public UserDetailsRepository getRepository() {
        return repository;
    }

    @Override
    public Optional<UserDetails> getUserDetails(String id) {
        return repository.findById(id);
    }

    @Override
    public Optional<UserDetails> save(UserDetails userDetails) {
        try {
            return Optional.of(repository.saveAndFlush(userDetails));
        }catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserDetails> getByExample(Example<UserDetails> example) {
        return repository.findOne(example);
    }
}
