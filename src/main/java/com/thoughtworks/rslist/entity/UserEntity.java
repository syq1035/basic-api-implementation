package com.thoughtworks.rslist.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "name")
    private String userName;
    private Integer age;
    private String gender;
    private String email;
    private String phone;
}
