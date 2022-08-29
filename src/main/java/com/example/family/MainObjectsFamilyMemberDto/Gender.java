package com.example.family.MainObjectsFamilyMemberDto;
import lombok.Getter;

public enum Gender {
    F("female"),
    M("male");

@Getter
    final String label;

    Gender(String label) {
        this.label = label;
    }
}
