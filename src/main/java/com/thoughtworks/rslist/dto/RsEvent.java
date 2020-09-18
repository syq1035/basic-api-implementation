package com.thoughtworks.rslist.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RsEvent {
    @NotEmpty
    private String eventName;
    private String keyword;
    @Valid
    private int userId;

    private UserDto user;

    public RsEvent(String eventName, String keyword) {
        this.eventName = eventName;
        this.keyword = keyword;
    }

    public RsEvent(@NotEmpty String eventName, String keyword, @Valid int userId) {
        this.eventName = eventName;
        this.keyword = keyword;
        this.userId = userId;
    }

    @JsonIgnore
    public int getUserId() {
        return userId;
    }

    @JsonProperty
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @JsonProperty
    public UserDto getUser() {
        return user;
    }

    @JsonIgnore
    public void setUser(UserDto user) {
        this.user = user;
    }
}
