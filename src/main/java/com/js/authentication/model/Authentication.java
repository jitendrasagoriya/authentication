package com.js.authentication.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "AUTHENTICATION")
public class Authentication {

    @Id
    @Column(name = "USERNAME")
    private String userName;

    @Column(name = "PASSWORD")
    private String passward;

    @Column(name = "USERID")
    private String userId;

    @Column(name = "APPID")
    private String appId;

    @Column(name = "TOKEN")
    private String token;

    @Column(name = "EXPIREDAY" ,columnDefinition = "int default 1")
    private Integer expaireDay;

    @Column(name = "CREATIONDATE",columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp creationDate;

    @Column(name = "LASTLOGIN",columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp lastLogin;

    @Column(name = "ISLOGOUT" ,columnDefinition = "boolean default false")
    private Boolean isLogout;

    @Column(name = "ISACTIVE" ,columnDefinition = "boolean default true")
    private Boolean isActive;



    public Authentication(AuthenticationBuilder authenticationBuilder) {
        this.appId = authenticationBuilder.appId;
        this.creationDate = authenticationBuilder.creationDate;
        this.expaireDay = authenticationBuilder.expaireDay;
        this.isLogout = authenticationBuilder.isLogout;
        this.lastLogin = authenticationBuilder.lastLogin;
        this.passward = authenticationBuilder.passward;
        this.token = authenticationBuilder.token;
        this.userId = authenticationBuilder.userId;
        this.userName = authenticationBuilder.userName;
        this.isActive = authenticationBuilder.isActive;

    }

    public static class AuthenticationBuilder {

        private String userName;
        private String passward;
        private String userId;
        private String appId;
        private String token;
        private Integer expaireDay;
        private Timestamp creationDate;
        private Timestamp lastLogin;
        private Boolean isLogout;
        private Boolean isActive;

        public AuthenticationBuilder( Authentication authentication ) {
            this.userName = authentication.userName;
            this.passward = authentication.passward;
            this.userId = authentication.userId;
            this.appId = authentication.appId;
            this.token = authentication.token;
            this.expaireDay = authentication.expaireDay;
            this.creationDate = authentication.creationDate;
            this.lastLogin = authentication.lastLogin;
            this.isLogout = authentication.isLogout;
            this.isActive = authentication.isActive;
        }

        public AuthenticationBuilder (String userName, String passward) {
            this.userName = userName;
            this.passward = passward;
        }

        public AuthenticationBuilder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public AuthenticationBuilder passward(String passward) {
            this.passward = passward;
            return this;
        }

        public AuthenticationBuilder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public AuthenticationBuilder appId(String appId) {
            this.appId = appId;
            return this;
        }

        public AuthenticationBuilder token(String token) {
            this.token = token;
            return this;
        }

        public AuthenticationBuilder expireDay(Integer expireDay) {
            this.expaireDay = expireDay;
            return this;
        }

        public AuthenticationBuilder creationDate(Timestamp creationDate) {
            this.creationDate = creationDate;
            return this;
        }

        public AuthenticationBuilder lastLogin(Timestamp lastLogin) {
            this.lastLogin = lastLogin;
            return this;
        }

        public AuthenticationBuilder logout(Boolean logout) {
            this.isLogout = logout;
            return this;
        }

        public AuthenticationBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        //Return the finally consrcuted User object
        public Authentication build() {
            Authentication authentication =  new Authentication(this);
            validateUserObject(authentication);
            return authentication;
        }



        public String buildJson() throws JsonProcessingException {
            ObjectMapper objectMapper = new ObjectMapper();
            Authentication authentication =  new Authentication(this);
            validateUserObject(authentication);
            return objectMapper.writeValueAsString(authentication);
        }

        private void validateUserObject(Authentication authentication) {
            //Do some basic validations to check
            //if user object does not break any assumption of system
        }
    }

}
