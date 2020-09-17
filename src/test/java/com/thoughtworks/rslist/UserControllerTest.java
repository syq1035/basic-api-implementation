package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;

    @Test
    void should_return_user_list() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void should_return_user_by_id() throws Exception {
        UserDto userDto = new UserDto("yanqin", 19, "female", "a@thoughtworks.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);
        mockMvc.perform(post("/user/register").content(userDtoJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName", is("yanqin")));
    }

    @Test
    void should_register_user() throws Exception {
        UserDto userDto = new UserDto("xiaowang", 19, "female", "a@thoughtworks.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);
        mockMvc.perform(post("/user/register").content(userDtoJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<UserEntity> users = userRepository.findAll();

        assertEquals(1, users.size());
        assertEquals("xiaowang", users.get(0).getUserName());
    }

    @Test
    void should_no_add_user_when_not_valid() throws Exception {
        UserDto userDto = new UserDto("", 19, "female", "a@thoughtworks.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);
        mockMvc.perform(post("/user/register").content(userDtoJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid param")));
    }

    @Test
    void name_should_not_empty() throws Exception {
        UserDto userDto = new UserDto("", 19, "female", "a@thoughtworks.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);
        mockMvc.perform(post("/user/register").content(userDtoJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void name_length_should_not_more_than_8() throws Exception {
        UserDto userDto = new UserDto("shenyanqin", 19, "female", "a@thoughtworks.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);
        mockMvc.perform(post("/user/register").content(userDtoJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void age_should_not_empty() throws Exception {
        UserDto userDto = new UserDto("yanqin", null, "female", "a@thoughtworks.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);
        mockMvc.perform(post("/user/register").content(userDtoJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void age_should_not_less_than_18() throws Exception {
        UserDto userDto = new UserDto("yanqin", 17, "female", "a@thoughtworks.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);
        mockMvc.perform(post("/user/register").content(userDtoJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void age_should_not_more_than_100() throws Exception {
        UserDto userDto = new UserDto("yanqin", 101, "female", "a@thoughtworks.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);
        mockMvc.perform(post("/user/register").content(userDtoJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void gender_should_not_empty() throws Exception {
        UserDto userDto = new UserDto("yanqin", 19, "", "a@thoughtworks.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);
        mockMvc.perform(post("/user/register").content(userDtoJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void email_should_valid() throws Exception {
        UserDto userDto = new UserDto("yanqin", 19, "female", "@thoughtworks.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);
        mockMvc.perform(post("/user/register").content(userDtoJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void phone_should_not_empty() throws Exception {
        UserDto userDto = new UserDto("yanqin", 19, "female", "a@thoughtworks.com", "");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);
        mockMvc.perform(post("/user/register").content(userDtoJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void phone_should_start_1() throws Exception {
        UserDto userDto = new UserDto("yanqin", 19, "female", "a@thoughtworks.com", "21234567890");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);
        mockMvc.perform(post("/user/register").content(userDtoJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void phone_length_should_is_11() throws Exception {
        UserDto userDto = new UserDto("yanqin", 19, "female", "a@thoughtworks.com", "11234567");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);
        mockMvc.perform(post("/user/register").content(userDtoJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
