package com.example.family.MainObjectsFamilyMemberDto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)

public class Member{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private final Long id;
    private String name;
    private String familyName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate diedOn;

    private Long userId;

    @ManyToOne(targetEntity = Member.class)
    private Member father;

    @ManyToOne(targetEntity = Member.class)
    private Member mother;

    @ManyToOne(targetEntity = Member.class)
    private Member partner;

    private Gender gender;
}
