package com.example.family.Interfaces;

import com.example.family.MainObjectsFamilyMemberDto.Member;
import com.example.family.MainObjectsFamilyMemberDto.MemberDto;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;

public interface DtoConverter {

    default MemberDto convertToDto(Member member,ModelMapper modelMapper) {
        return modelMapper.map(member,MemberDto.class);
    }

    default List<MemberDto> convertToDtoList(List<Member>members, ModelMapper modelMapper) {

            List<MemberDto> memberDtoList = Arrays.asList(modelMapper.
                    map(members, MemberDto[].class));
            return memberDtoList;
        }

    default Member convertToEntity(MemberDto memberDtoDTO,ModelMapper modelMapper){

        return modelMapper.map(memberDtoDTO, Member.class);
    }

}
