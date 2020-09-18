package com.thoughtworks.rslist.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "rs")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RsEventEntity {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "name")
    private String eventName;
    private String keyword;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
