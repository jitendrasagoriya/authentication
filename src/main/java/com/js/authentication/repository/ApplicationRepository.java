package com.js.authentication.repository;


import com.js.authentication.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface ApplicationRepository extends JpaRepository<Application, String> {

    @Query("SELECT A FROM Application A WHERE A.access = :accessToken")
    public Application getApplicationByAccessToken(@Param("accessToken") String accessToken );

    @Query("SELECT A FROM Application A WHERE A.appName = :appName")
    public Application getApplicationByName(@Param("appName") String appName );

    @Query("SELECT A FROM Application A WHERE A.access = :accessToken AND A.id = :id")
    public Application getApplicationByIDAndAccessToken(@Param("accessToken") String accessToken ,@Param("id")  String appId );

}