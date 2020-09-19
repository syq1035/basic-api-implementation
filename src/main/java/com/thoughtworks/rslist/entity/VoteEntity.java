package com.thoughtworks.rslist.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "vote")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoteEntity {
    @Id
    @GeneratedValue
    private Integer id;
    private int userId;
    private int rsEventId;
    private int voteNum;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime voteTime;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    public LocalDateTime getVoteTime() {
        return voteTime;
    }
}
