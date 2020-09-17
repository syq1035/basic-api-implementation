package com.thoughtworks.rslist.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
@Data
public class RsEvent {
    @NotEmpty
    private String eventName;
    private String keyword;
    @Valid
    private int userId;

    public RsEvent() {}

    public RsEvent(String eventName, String keyword) {
        this.eventName = eventName;
        this.keyword = keyword;
    }

}
