package com.example.family.Interfaces;

import com.example.family.family.Member;
import com.example.family.family.MemberDto;
import org.modelmapper.ModelMapper;

public interface DtoConverter {

    default MemberDto convertToDto(Member member,ModelMapper modelMapper) {
        return modelMapper.map(member,MemberDto.class);
    }


    default Member convertToEntity(MemberDto memberDtoDTO,ModelMapper modelMapper){

        return modelMapper.map(memberDtoDTO, Member.class);
    }

}
