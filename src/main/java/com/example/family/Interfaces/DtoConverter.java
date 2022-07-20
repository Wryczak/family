package com.example.family.Interfaces;

import com.example.family.MainObjectsFamilyMemberDto.Member;
import com.example.family.MainObjectsFamilyMemberDto.MemberDto;
import org.modelmapper.ModelMapper;

public interface DtoConverter {

    default MemberDto convertToDto(Member member,ModelMapper modelMapper) {
        return modelMapper.map(member,MemberDto.class);
    }


    default Member convertToEntity(MemberDto memberDtoDTO,ModelMapper modelMapper){

        return modelMapper.map(memberDtoDTO, Member.class);
    }

}
