package com.js.authentication.repository;


import com.js.authentication.model.Application;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface ApplicationRepository extends JpaRepository<Application, String> {

    @Query("SELECT A FROM Application A WHERE A.access = :accessToken")
    public Application getApplicationByAccessToken(@Param("accessToken") String accessToken );

    @Query("SELECT A FROM Application A WHERE A.appName = :appName")
    public  Application getApplicationByName(@Param("appName") String appName );

    @Query("SELECT A FROM Application A WHERE A.access = :accessToken AND A.id = :id")
    public Application getApplicationByIDAndAccessToken(@Param("accessToken") String accessToken ,@Param("id")  String appId );     

    @Modifying
    @Query("DELETE FROM Application A WHERE A.access = :accessToken AND A.id = :id")
    public int deleteApplicationByAppidAndAccess(@Param("accessToken") String accessToken,
                                                         @Param("id")  String appId );    
    
    @Query(value = "SELECT A.* FROM application A INNER JOIN applicationadmin AD on A.id = AD.appid WHERE AD.userid = :id",nativeQuery = true)
    public  List<Application> getApplicationByUserId(@Param("id")  String id);
    
    @Query(value = "SELECT A.* FROM application A INNER JOIN applicationadmin AD on A.id = AD.appid WHERE AD.userid = :id and AD.appid= :appid",nativeQuery = true)
    public  Application getApplicationByUserIdAndApplication(@Param("id")  String id,@Param("appid")  String appid);
    
    @Query(value = "SELECT COUNT (A) FROM application A INNER JOIN applicationadmin AD on A.id = AD.appid WHERE AD.userid = :id",nativeQuery = true)
    public  int getCountByUserId(@Param("id")  String id);
}