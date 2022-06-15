package com.example.family;

import com.example.family.family.Member;
import com.example.family.family.MemberDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class FamilyApplication {

    public static void main(String[] args) {
        SpringApplication.run(FamilyApplication.class, args);
    }

    @Bean
    public ModelMapper getModelMapper(){
        ModelMapper modelMapper =new ModelMapper();
        modelMapper.addMappings(new PropertyMap<Member, MemberDto>() {
            @Override
            public void configure(){
                map();
                map().setFamilyName(source.getFamilyName());
                map().setName(source.getName());
                map().setBirthday(source.getBirthday());
                map().setMature(source.getMature());
                map().setIdtest(source.getId());

            }
        });
        return modelMapper;
    }

}