package com.js.authentication.repository;

import com.js.authentication.model.Authentication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface AuthenticationRepository extends JpaRepository<Authentication,String> {

    @Query("SELECT A FROM Authentication A WHERE A.token = :accessToken")
    public Authentication getActive(@Param("accessToken") String accessToken );

    @Query("SELECT A FROM Authentication A WHERE A.userName = :email AND A.passward = :password")
    public Authentication getApplication(@Param("email") String email, @Param("password") String password );

    @Query("SELECT A FROM Authentication A WHERE A.appId = :appId")
    public List<Authentication> getAuthenticationByAppId(@Param("appId")  String appId);

    @Query("SELECT A FROM Authentication A WHERE A.appId = :appId")
    public Page<Authentication> getAuthenticationByAppId(@Param("appId")  String appId, Pageable pageable);

    @Modifying(flushAutomatically = true,clearAutomatically = true)
    @Query("UPDATE Authentication A SET A.lastLogin = CURRENT_TIMESTAMP WHERE A.token = :accessToken")
    public int updateLoginTimestamp(@Param("accessToken") String accessToken);

    @Modifying(flushAutomatically = true,clearAutomatically = true)
    @Query("UPDATE Authentication A SET A.isLogout = true WHERE A.token = :accessToken")
    public int logout(@Param("accessToken") String accessToken);
}
