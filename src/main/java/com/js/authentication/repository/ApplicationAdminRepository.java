package com.js.authentication.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.js.authentication.model.ApplicationAdmin;

@Repository
public interface ApplicationAdminRepository extends JpaRepository<ApplicationAdmin, Long> {

	@Query("SELECT AA FROM ApplicationAdmin AA WHERE AA.userId = :userId")
	List<ApplicationAdmin> findByUserId(@Param(value = "userId") String userId);

	@Query("SELECT AA FROM ApplicationAdmin AA WHERE AA.appId = :appId")
	Optional<ApplicationAdmin> findByAppId(@Param(value = "appId") String appId);
	
	
	@Query("SELECT COUNT (AA.appId) FROM ApplicationAdmin AA WHERE AA.userId = :userId")
	int applicationCountPerUser(@Param(value = "userId") String userId);
	
	@Query(value = "SELECT count(au) FROM authentication au inner join (select ap.* from  application ap inner join applicationadmin aa on ap.id=aa.appid  where aa.userid = :userId) app on au.appid = app.id",nativeQuery = true)
	int authenticationCountPerAdmin(@Param(value = "userId") String userId);
	
	@Query("SELECT COUNT( DISTINCT AA.userId ) FROM ApplicationAdmin AA")
	int totalUsers();
	
	@Query("SELECT COUNT( DISTINCT AA.appId ) FROM ApplicationAdmin AA")
	int totalApplication();
	


	
}
