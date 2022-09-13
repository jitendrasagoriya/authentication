package com.js.authentication.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "USERDETAILS")
public class UserDetails  implements Serializable {

    @Id
    @Column(name = "id",unique = true,nullable = false,length = 150)
    private String id;

    @Column(name = "fullname" ,length = 50)
    private String fullName;

    @Column(name = "mobile" ,length = 10)
    private String mobile;

    @Column(name = "address" ,length = 150)
    private String address;

    @Column(name = "gender" ,length = 10)
    private String gender;

    @Column(name = "dob")
    private Date dob;

    @Column(name = "avatar" , length = 10)
    private String avatar;

    @Column(name = "isDetailsVisible" , columnDefinition = "boolean default false")
    private boolean isDetailsVisible;
}
