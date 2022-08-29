package com.example.family.Interfaces;

import com.example.family.MainObjectsFamilyMemberDto.MemberDto;

public interface FullNameCreator {
    default String createFullName(MemberDto memberDto) {
        return  memberDto.getFamilyName()+" "+memberDto.getName();
    }

}
