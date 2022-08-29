package com.example.family.MainObjectsFamilyMemberDto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class MemberDto{

    private Long id;

    private String name;

    private String familyName;

    private Integer age;

    private String mother;

    private String father;

    private String partner;

    private Gender gender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate diedOn;

    private Long tempId;

    private Long tempId2;
}