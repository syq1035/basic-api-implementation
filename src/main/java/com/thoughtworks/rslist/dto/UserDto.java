package com.thoughtworks.rslist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
public class UserDto {
    @NotEmpty
    @Size(max = 8)
    private String userName;
    @NotNull
    @Min(18)
    @Max(100)
    private Integer age;
    @NotEmpty
    private String gender;
    private String email;
    private String phone;

    public UserDto(String userName, Integer age, String gender, String email, String phone) {
        this.userName = userName;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
    }
}
