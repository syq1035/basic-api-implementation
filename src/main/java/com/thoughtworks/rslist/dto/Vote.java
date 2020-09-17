package com.thoughtworks.rslist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vote {
    private int userId;
    private int rsEventId;
    private int voteNum;
    private Date voteTime;

    public Vote(int userId, int voteNum, Date voteTime) {
        this.userId = userId;
        this.voteNum = voteNum;
        this.voteTime = voteTime;
    }
}
