package com.js.authentication.dto;

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

}
