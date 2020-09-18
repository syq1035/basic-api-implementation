package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.Vote;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RsListApplicationTests {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;

    @Test
    void should_get_rs_list() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .userName("aaaa")
                .age(19)
                .email("female")
                .gender("a@thoughtworks.com")
                .phone("18888888888")
                .build();
        userRepository.save(userEntity);
        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName("哈哈哈哈哈")
                .keyword("娱乐")
                .user(userEntity)
                .build();
        rsEventRepository.save(rsEventEntity);
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].eventName", is("哈哈哈哈哈")))
                .andExpect(jsonPath("$[0].user.userName", is(userEntity.getUserName())));;
    }

    @Test
    void should_get_one_rs_event_when_index_out_of_bounds() throws Exception {
        mockMvc.perform(get("/rs/30"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid index")));
    }

    @Test
    void should_no_add_a_rs_event_when_name_empty() throws Exception {
        RsEvent rsEvent = new RsEvent(null, "异常");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid param")));
    }

    @Test
    void should_get_one_rs_event() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .userName("aaaa")
                .age(19)
                .email("female")
                .gender("a@thoughtworks.com")
                .phone("18888888888")
                .build();
        userRepository.save(userEntity);
        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName("哈哈哈哈哈")
                .keyword("娱乐")
                .user(userEntity)
                .build();
        rsEventRepository.save(rsEventEntity);
        mockMvc.perform(get("/rs/{id}", rsEventEntity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("哈哈哈哈哈")));
    }

    @Test
    void should_add_rs_event_when_user_exists() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .userName("addrsevent")
                .age(19)
                .email("female")
                .gender("a@thoughtworks.com")
                .phone("18888888888")
                .build();
        userRepository.save(userEntity);

        String json = "{\"eventName\":\"台风来了\",\"keyword\":\"天气\",\"userId\":" + userEntity.getId() + "}";
        mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());

        List<RsEventEntity> rsList = rsEventRepository.findAll();
        assertEquals(1, rsList.size());

    }

    @Test
    void should_edit_rs_event_by_id() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .userName("edit")
                .age(19)
                .email("female")
                .gender("a@thoughtworks.com")
                .phone("18888888888")
                .build();
        userRepository.save(userEntity);
        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName("睡觉了")
                .keyword("娱乐")
                .user(userEntity)
                .build();
        rsEventRepository.save(rsEventEntity);
        String json = "{\"eventName\":\"新的热搜事件名\",\"keyword\":\"新的关键字\",\"userId\":" + userEntity.getId() + "}";
        mockMvc.perform(put("/rs/{rsEventId}", rsEventEntity.getId()).content(json).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        RsEventEntity eventEntity = rsEventRepository.findById(rsEventEntity.getId()).get();
        assertEquals("新的热搜事件名", eventEntity.getEventName());
        assertEquals("新的关键字", eventEntity.getKeyword());

    }

    @Test
    void should_delete_rs_event() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .userName("shanchu")
                .age(19)
                .email("female")
                .gender("a@thoughtworks.com")
                .phone("18888888888")
                .build();
        userRepository.save(userEntity);
        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName("睡觉了")
                .keyword("娱乐")
                .user(userEntity)
                .build();
        rsEventRepository.save(rsEventEntity);
        mockMvc.perform(delete("/rs/{id}", rsEventEntity.getId()))
                .andExpect(status().isOk());
        List<RsEventEntity> rsEventEntityList = rsEventRepository.findAll();
        assertEquals(0, rsEventEntityList.size());
    }

    @Test
    void should_vote() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .userName("vv")
                .age(19)
                .email("female")
                .gender("a@thoughtworks.com")
                .phone("18888888888")
                .voteNum(10)
                .build();
        userRepository.save(userEntity);
        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName("投票")
                .keyword("娱乐")
                .user(userEntity)
                .build();
        rsEventRepository.save(rsEventEntity);
        Date voteTime = new Date();
        Vote vote = new Vote(userEntity.getId(), 4, voteTime);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(vote);
        mockMvc.perform(post("/rs/vote/{rsEventId}", rsEventEntity.getId()).content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        UserEntity userEntity1 = userRepository.findById(userEntity.getId()).get();
        assertEquals(6, userEntity1.getVoteNum());
    }

    @Test
    void should_not_vote_when_voteNum_not_enough() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .userName("vv")
                .age(19)
                .email("female")
                .gender("a@thoughtworks.com")
                .phone("18888888888")
                .voteNum(10)
                .build();
        userRepository.save(userEntity);
        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName("投票")
                .keyword("娱乐")
                .user(userEntity)
                .build();
        rsEventRepository.save(rsEventEntity);
        Date voteTime = new Date();
        Vote vote = new Vote(userEntity.getId(), 12, voteTime);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(vote);
        mockMvc.perform(post("/rs/vote/{rsEventId}", rsEventEntity.getId()).content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
