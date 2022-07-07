package com.example.family.MainObjectsFamilyMemberDto;

import com.sun.istack.NotNull;
import lombok.Data;
import javax.validation.constraints.Size;

@Data
public class MemberDto {

    private Long id;

    @NotNull
    @Size(min = 3, message = "Imię musi mieć co najmniej 3 znaki!")
    private String name;
    @NotNull
    @Size(min = 3, message = "Nazwisko musi mieć co najmniej 3 znaki!")
    private String familyName;

    private Integer age;

    private Long fatherID;

    private Long matherID;

    @NotNull
    @Size(min = 3, message = "Podaj datę urodzin!")
    private String birthday;

    private Long tempId;

}