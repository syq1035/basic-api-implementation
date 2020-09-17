package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    public List<UserDto> userList = initUserList();
    public List<UserDto> initUserList() {
        List<UserDto> userList = new ArrayList<>();
        userList.add(new UserDto("xiaowang", 19, "female", "a@thoughtworks.com", "18888888888"));
        userList.add(new UserDto("xiaoming", 22, "male", "a@thoughtworks.com", "18888888888"));
        return userList;
    }
    @PostMapping("/user/register")
    public ResponseEntity register(@Valid @RequestBody UserDto userDto) {
        userList.add(userDto);
        return ResponseEntity.created(null).header("index", String.valueOf(userList.size()-1)).build();
    }
    @GetMapping("users")
    public ResponseEntity<List<UserDto>> getAllUserList() {
        return ResponseEntity.ok(userList);
    }
}
