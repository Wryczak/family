package com.example.family;

import com.example.family.MainObjectsFamilyMemberDto.Member;
import com.example.family.MainObjectsFamilyMemberDto.MemberDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FamilyApplication {

    public static void main(String[] args) {
        SpringApplication.run(FamilyApplication.class, args);
    }

    @Bean
    public ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<Member, MemberDto>() {
            @Override
            public void configure() {
                map();
                map().setFamilyName(source.getFamilyName());
                map().setName(source.getName());
                map().setBirthday(source.getBirthday());
                map().setDiedOn(source.getDiedOn());
                map().setFather(source.getFather().getName());
                map().setMother(source.getMother().getName());
                map().setGender(source.getGender());
                map().setPartner(source.getPartner().getName());
            }
        });
        return modelMapper;
    }
}