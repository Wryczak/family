package com.example.family.MainObjectsFamilyMemberDto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class MemberDto {

    private Long id;

    private String name;

    private String familyName;

    private Integer age;

//    private Member myfather;

    private Long fatherID;

    private Long matherID;

    private String mather;

    private String father;

    private String partner;

    private String fullName;

    private Gender gender;

    private Long partnerId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate diedOn;

    private Long tempId;

}