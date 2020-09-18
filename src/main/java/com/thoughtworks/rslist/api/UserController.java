package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {

    private final UserRepository userRepository;
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/user/register")
    public ResponseEntity register(@Valid @RequestBody UserDto userDto) {
        UserEntity userEntity = UserEntity.builder()
                .userName(userDto.getUserName())
                .age(userDto.getAge())
                .email(userDto.getEmail())
                .gender(userDto.getGender())
                .phone(userDto.getPhone())
                .voteNum(userDto.getVoteNum())
                .build();
        userRepository.save(userEntity);
        return ResponseEntity.created(null).build();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity getUserById(@PathVariable int id) {
         UserEntity userEntity = userRepository.findById(id).get();
         return ResponseEntity.ok(UserEntity.builder()
                 .userName(userEntity.getUserName())
                 .age(userEntity.getAge())
                 .gender(userEntity.getGender())
                 .email(userEntity.getEmail())
                 .phone(userEntity.getPhone())
                 .build());
    }

    @DeleteMapping("/user/{id}")
    @Transactional
    public ResponseEntity deleteUserById(@PathVariable int id) {
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserEntity>> getAllUserList() {
        List<UserEntity> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }
}
