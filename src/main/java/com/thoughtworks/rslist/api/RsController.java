package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.exceptions.CommentError;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class RsController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;

    private List<RsEvent> rsList = initRsList();

    public List<RsEvent> initRsList() {
        List<RsEvent> rslist = new ArrayList<>();
        rslist.add(new RsEvent("第一条事件", "无分类"));
        rslist.add(new RsEvent("第二条事件", "无分类"));
        rslist.add(new RsEvent("第三条事件", "无分类"));
        return rslist;
    }

    @GetMapping("/rs/list")
    public ResponseEntity<List<RsEvent>> getAllRsEvent(@RequestParam(required = false) Integer start,
                                                      @RequestParam(required = false) Integer end) {
        if(start == null || end == null) {
            return ResponseEntity.ok(rsList);
        }
        if(start > 0 && end < rsList.size() && start < end) {
            return ResponseEntity.ok(rsList.subList(start - 1, end));
        }
        throw new IndexOutOfBoundsException("invalid request param");
    }

    @GetMapping("/rs/{index}")
    public ResponseEntity<RsEvent> getOneRsEvent(@PathVariable int index) {
        if(index > rsList.size()) {
            throw new IndexOutOfBoundsException();
        }
        return ResponseEntity.ok(rsList.get(index - 1));
    }

    @PostMapping("/rs/event")
    public ResponseEntity addRsEvent(@Valid @RequestBody RsEvent rsEvent) throws JsonProcessingException {
        if (!userRepository.existsById(rsEvent.getUserId())) {
            return ResponseEntity.badRequest().build();
        }
        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName(rsEvent.getEventName())
                .keyword(rsEvent.getKeyword())
                .userId(rsEvent.getUserId())
                .build();
        rsEventRepository.save(rsEventEntity);
        return ResponseEntity.created(null).build();
    }

    @PutMapping("/rs/{rsEventId}")
    public ResponseEntity editRsEvent(@PathVariable int rsEventId, @RequestBody RsEvent rsEvent) throws JsonProcessingException {
        RsEventEntity rsEventEntity = rsEventRepository.findById(rsEventId).get();
        if (rsEvent.getUserId() != rsEventEntity.getUserId()) {
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

    @DeleteMapping("/rs/{index}")
    public ResponseEntity deleteRsEvent(@PathVariable int index) {
        rsList.remove(index - 1);
        return ResponseEntity.ok().build();
    }
}
