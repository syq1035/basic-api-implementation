package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.Vote;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class RsController {

    public final UserRepository userRepository;
    public final RsEventRepository rsEventRepository;
    public final VoteRepository voteRepository;

    public RsController(UserRepository userRepository, RsEventRepository rsEventRepository, VoteRepository voteRepository) {
        this.userRepository = userRepository;
        this.rsEventRepository = rsEventRepository;
        this.voteRepository = voteRepository;
    }


    @GetMapping("/rs/list")
    public ResponseEntity<List<RsEvent>> getAllRsEvent() {
        List<RsEventEntity> rsEventEntityList = rsEventRepository.findAll();
        List<RsEvent> rsEvents = rsEventEntityList.stream().map(rsEventEntity -> rsEventEntityToRsEvent(rsEventEntity)).collect(Collectors.toList());
        return ResponseEntity.ok(rsEvents);
    }

    @GetMapping("/rs/{id}")
    public ResponseEntity<RsEvent> getOneRsEvent(@PathVariable int id) {
        Optional<RsEventEntity> rsEventEntityOptional = rsEventRepository.findById(id);
        if(!rsEventEntityOptional.isPresent()) {
            throw new IndexOutOfBoundsException();
        }
        RsEventEntity rsEventEntity = rsEventEntityOptional.get();
        RsEvent rsEvent = rsEventEntityToRsEvent(rsEventEntity);
        return ResponseEntity.ok(rsEvent);
    }

    @PostMapping("/rs/event")
    public ResponseEntity addRsEvent(@Valid @RequestBody RsEvent rsEvent) throws JsonProcessingException {
        Optional<UserEntity> user = userRepository.findById(rsEvent.getUserId());
        if (!user.isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName(rsEvent.getEventName())
                .keyword(rsEvent.getKeyword())
                .user(user.get())
                .build();
        rsEventRepository.save(rsEventEntity);
        return ResponseEntity.created(null).build();
    }

    @PutMapping("/rs/{rsEventId}")
    public ResponseEntity editRsEvent(@PathVariable int rsEventId, @RequestBody RsEvent rsEvent) throws JsonProcessingException {
        RsEventEntity rsEventEntity = rsEventRepository.findById(rsEventId).get();
        if (rsEvent.getUserId() != rsEventEntity.getUser().getId()) {
            return ResponseEntity.badRequest().build();
        }
        if(rsEvent.getEventName() != null) {
            rsEventEntity.setEventName(rsEvent.getEventName());
        }
        if(rsEvent.getKeyword() != null) {
            rsEventEntity.setKeyword(rsEvent.getKeyword());
        }
        rsEventRepository.save(rsEventEntity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/rs/{id}")
    public ResponseEntity deleteRsEvent(@PathVariable int id) {
        rsEventRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/rs/vote/{rsEventId}")
    public ResponseEntity vote(@PathVariable int rsEventId, @RequestBody Vote vote) {
        UserEntity userEntity = userRepository.findById(vote.getUserId()).get();
        if(userEntity.getVoteNum() < vote.getVoteNum()) {
            return ResponseEntity.badRequest().build();
        }
        VoteEntity voteEntity = VoteEntity.builder()
                .rsEventId(rsEventId)
                .userId(vote.getUserId())
                .voteNum(vote.getVoteNum())
                .voteTime(vote.getVoteTime())
                .build();
        voteRepository.save(voteEntity);
        userEntity.setVoteNum(userEntity.getVoteNum() - vote.getVoteNum());
        userRepository.save(userEntity);
        return ResponseEntity.created(null).build();
    }

    private RsEvent rsEventEntityToRsEvent(RsEventEntity rsEventEntity) {
        RsEvent rsEvent = RsEvent.builder()
                .eventName(rsEventEntity.getEventName())
                .keyword(rsEventEntity.getKeyword())
                .user(UserDto.builder()
                        .userName(rsEventEntity.getUser().getUserName())
                        .age(rsEventEntity.getUser().getAge())
                        .gender(rsEventEntity.getUser().getGender())
                        .email(rsEventEntity.getUser().getEmail())
                        .phone(rsEventEntity.getUser().getPhone())
                        .build())
                .build();
        return rsEvent;
    }
}
