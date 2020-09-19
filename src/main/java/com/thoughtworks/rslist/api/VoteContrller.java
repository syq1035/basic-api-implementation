package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.Vote;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class VoteContrller {

    private final VoteRepository voteRepository;

    public VoteContrller(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @GetMapping("/votes")
    public ResponseEntity getVotesByTimeRange(@RequestParam String startTime, @RequestParam String endTime) throws ParseException {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startT = LocalDateTime.parse(startTime, df);
        LocalDateTime endT = LocalDateTime.parse(endTime, df);
        List<VoteEntity> voteEntityList = voteRepository.findAllByVoteTimeBetween(startT, endT);
        List<Vote> votes = voteEntityList.stream().map(voteEntity ->
                Vote.builder()
                        .rsEventId(voteEntity.getRsEventId())
                        .userId(voteEntity.getUserId())
                        .voteNum(voteEntity.getVoteNum())
                        .voteTime(voteEntity.getVoteTime())
                        .build()
        ).collect(Collectors.toList());
        return ResponseEntity.ok(votes);
    }

}
