package com.js.authentication.dao;

import com.js.authentication.model.UserDetails;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserDetailsDao<R> extends BaseSerivce<R> {

    Optional<UserDetails> getUserDetails(String id);

    Optional<UserDetails> save(UserDetails userDetails);

    Optional<UserDetails> getByExample(Example<UserDetails> example);


}
