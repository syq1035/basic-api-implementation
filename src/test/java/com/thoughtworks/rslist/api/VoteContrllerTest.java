package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class VoteContrllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    VoteRepository voteRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;

    @Test
    void should_vote_list_by_time_range() throws Exception {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime time = LocalDateTime.now();

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
        VoteEntity voteEntity  = VoteEntity.builder()
                .rsEventId(rsEventEntity.getId())
                .userId(userEntity.getId())
                .voteNum(4)
                .voteTime(time.plusDays(3))
                .build();
        voteRepository.save(voteEntity);
        VoteEntity voteEntity1  = VoteEntity.builder()
                .rsEventId(rsEventEntity.getId())
                .userId(userEntity.getId())
                .voteNum(6)
                .voteTime(time.plusDays(1))
                .build();
        voteRepository.save(voteEntity1);

        mockMvc.perform(get("/votes").param("startTime", df.format(time.plusDays(-2))).param("endTime", df.format(time.plusDays(2))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].rsEventId", is(voteEntity1.getRsEventId())))
                .andExpect(jsonPath("$[0].voteNum", is(voteEntity1.getVoteNum())));
    }

}
