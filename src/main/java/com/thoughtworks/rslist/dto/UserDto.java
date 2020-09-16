package com.thoughtworks.rslist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class UserDto {
    @NotEmpty
    private String username;
    private Integer age;
    private String gender;
    private String email;
    private String phone;

    public UserDto(String username, Integer age, String gender, String email, String phone) {
        this.username = username;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
    }
}
