package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RsController {
    private List<RsEvent> rsList = initRsList();

    public List<RsEvent> initRsList() {
        List<RsEvent> rslist = new ArrayList<>();
        rslist.add(new RsEvent("第一条事件", "无分类"));
        rslist.add(new RsEvent("第二条事件", "无分类"));
        rslist.add(new RsEvent("第三条事件", "无分类"));
        return rslist;
    }

    @GetMapping("/rs/list")
    public List<RsEvent> getAllRsEvent(@RequestParam(required = false) Integer start,
                                       @RequestParam(required = false) Integer end) {
        if(start == null || end == null) {
            return rsList;
        }
        return rsList.subList(start - 1, end);
    }

    @GetMapping("/rs/{index}")
    public RsEvent getOneRsEvent(@PathVariable int index) {
        return rsList.get(index - 1);
    }

    @PostMapping("/rs/event")
    public void addRsEvent(@Valid @RequestBody RsEvent rsEvent) throws JsonProcessingException {
        UserController userController = new UserController();
        List<UserDto> userList = userController.userList;
        if(!userList.contains(rsEvent.getUser())) {
            userController.register(rsEvent.getUser());
        }
        rsList.add(rsEvent);
    }

    @PutMapping("/rs/{index}")
    public void editRsEvent(@PathVariable int index, @RequestBody RsEvent rsEvent) throws JsonProcessingException {
        RsEvent oldRsEvent = rsList.get(index-1);
        if(rsEvent.getEventName() != null) {
            oldRsEvent.setEventName(rsEvent.getEventName());
        }
        if(rsEvent.getKeyword() != null) {
            oldRsEvent.setKeyword(rsEvent.getKeyword());
        }
    }

    @DeleteMapping("/rs/{index}")
    public void deleteRsEvent(@PathVariable int index) {
        rsList.remove(index - 1);
    }
}
