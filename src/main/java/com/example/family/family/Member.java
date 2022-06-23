package com.example.family.family;

import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
@Data
@Entity
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)

public class Member{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private final Long id;

    @NotNull
    @Size(min = 3, message = "Imię musi mieć co najmniej 3 znaki!")
    private String name;

    @NotNull
    @Size(min = 2, message = "Nazwisko musi mieć co najmniej 2 znaki!")
    private String familyName;

    private Mature mature;

    @NotNull
    @Size(min = 2, message = "Podaj datę urodzin!")
    private String birthday;

    private Long userid;

    private Long matherId;

    private Long fatherId;

    public enum Mature {
        INFANT, CHILD, ADULT;
    }
}
