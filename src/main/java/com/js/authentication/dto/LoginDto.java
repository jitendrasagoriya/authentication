package com.js.authentication.dto;

import com.js.authentication.enums.UserType;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class LoginDto {

    private String userName;
    private String password;
    private UserType userType;

}
