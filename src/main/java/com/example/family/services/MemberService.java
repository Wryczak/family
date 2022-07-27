package com.example.family.services;


import com.example.family.Interfaces.*;
import com.example.family.MainObjectsFamilyMemberDto.Gender;
import com.example.family.Repositories.FamilyRepository;
import com.example.family.Repositories.MemberRepository;
import com.example.family.MainObjectsFamilyMemberDto.Family;
import com.example.family.MainObjectsFamilyMemberDto.Member;
import com.example.family.MainObjectsFamilyMemberDto.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService implements DtoConverter, AgeCalculator, UsernameGetter {
    private final MemberRepository memberRepository;
    private final FamilyRepository familyRepository;
    private final FamilyService familyService;

    private final UserDetailsService userDetailsService;
    private final ModelMapper modelMapper;

    public Member getMember(Long id) {
        return memberRepository.findById(id).orElseThrow();
    }

    public boolean isMemberExist(Long id) {
        return memberRepository.existsById(id);
    }

    public List<Member> getMembers() {
        Long myFamilyId = (familyService.getFamilyNumber());
        return familyRepository.findById(myFamilyId).get().getMembers();
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public void deleteAllMembersFromFamily() {
        List<Member> members = getMembers();
        memberRepository.deleteAll(members);
    }

    public void createMember(Member member) {
        Family family = familyService.getFamily();
        member.setUserId(userDetailsService.getUserId());
        member = memberRepository.saveAndFlush(member);
        family.addFamilyMember(member);
    }

    public List<Long> getMembersIdList() {

        List<Member> members = getMembers();
        List<Long> allFamilyMembersId = new ArrayList<>();

        for (Member familyMember : members) {
            Long id = familyMember.getId();
            allFamilyMembersId.add(id);
        }
        return allFamilyMembersId;
    }

    public void updateMemberData(Long idToModify, MemberDto memberToUpdate) {

        Member member = memberRepository.getById(idToModify);
        member.setName(memberToUpdate.getName());
        member.setFamilyName(memberToUpdate.getFamilyName());
        member.setBirthday(memberToUpdate.getBirthday());
        memberRepository.save(member);

        log.info("   --- Member updated!");
    }


    public void getFamilyMembersDtoListAndAddToModel(Model model) {
        model.addAttribute("memberDto", getMembersDTOList());
    }

    public List<MemberDto> getMembersDTOList() {
        List<MemberDto> memberDtoList = Arrays.asList(modelMapper.
                map(familyService.getFamily().getMembers(), MemberDto[].class));
        for (MemberDto member : memberDtoList) {
            member.setAge(calculateAge(member.getBirthday()));
            relativesNameSetter(member);
        }
        return memberDtoList;
    }


    public List<MemberDto> createMembersDTOListForRelatives(List<Member> members) {
        List<MemberDto> memberDtoList = Arrays.asList(modelMapper.
                map(members, MemberDto[].class));
        for (MemberDto member : memberDtoList) {
            member.setAge(calculateAge(member.getBirthday()));
            relativesNameSetter(member);
        }
        return memberDtoList;
    }


    public void getRelativesListAndAddToModel(Model model, Long option, Long id) {

        List<Member> membersList = new ArrayList<>();
        if (option == 1) {
            List<Long> ancestors = new ArrayList<>(familyService.getAncestors(id));
            createRelativesListDependsOnTheOption(model, membersList, ancestors);

        }
        if (option == 2) {
            List<Long> parents = new ArrayList<>(familyService.getParentsForSinglePerson(id));
            createRelativesListDependsOnTheOption(model, membersList, parents);
        }

        if (option == 3) {
            List<Long> children = new ArrayList<>(familyService.getChildrenForSinglePerson(id));
            createRelativesListDependsOnTheOption(model, membersList, children);

        }
        if (option == 4) {
            List<Long> descendants = new ArrayList<>(familyService.getAllChildrenList(id));
            createRelativesListDependsOnTheOption(model, membersList, descendants);

        }
        if (option == 5) {
            List<Long> allRelatives = new ArrayList<>(familyService.getAllRelatives(id));
            createRelativesListDependsOnTheOption(model, membersList, allRelatives);
        }

    }

    private void createRelativesListDependsOnTheOption(Model model, List<Member> membersList, List<Long> relatives) {
        for (Long relativesId : relatives) {
            membersList.add(familyService.getMemberByIdFromFamily(relativesId));
        }
        model.addAttribute("memberDto", createMembersDTOListForRelatives(membersList));
    }

    public void createChildrenListForMemberById(Long id, Model model) {
        List<Long> children = new ArrayList<>(familyService.getChildrenForSinglePerson(id));
        List<Member> membersList = new ArrayList<>();
        for (Long relativesId : children) {
            membersList.add(familyService.getMemberByIdFromFamily(relativesId));

        }
        model.addAttribute("childrenDto", createMembersDTOListForRelatives(membersList));
    }

    private String setParentNameByParentId(Long id) {
        if (familyService.getMemberByIdFromFamily(id).getName() == null && familyService.getMemberByIdFromFamily(id).getFamilyName() == null) {
            return "Brak danych- członek rodziny usunięty.";
        }
        return familyService.getMemberByIdFromFamily(id).getName() + " " + familyService.getMemberByIdFromFamily(id).getFamilyName();

    }

    private void relativesNameSetter(MemberDto member) {
        if (member.getFatherID() != null) {
            member.setFather(setParentNameByParentId(member.getFatherID()));
        } else member.setFather("----");
        if (member.getMatherID() != null) {
            member.setMather(setParentNameByParentId(member.getMatherID()));
        } else member.setMather("----");
        if (member.getMatherID() != null) {
            member.setMather(setParentNameByParentId(member.getMatherID()));
        } else member.setMather("----");
        if (member.getPartnerId() != null) {
            member.setPartner(setParentNameByParentId(member.getPartnerId()));
        } else member.setPartner("----");
    }

    public void setGender(MemberDto member) {
            Long temp = member.getTempId();
            if (temp != 0) {
                member.setGender(Gender.M);
            } else member.setGender(Gender.F);
        }
    }
