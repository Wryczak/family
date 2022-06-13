package com.example.family.service;


import com.example.family.Interfaces.*;
import com.example.family.data.FamilyRepository;
import com.example.family.data.MemberRepository;
import com.example.family.data.UserRepository;
import com.example.family.family.Family;
import com.example.family.family.Member;
import com.example.family.family.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService implements DtoConverter, AgeCalculator , Modify , MaturityChecker {

    private final MemberRepository memberRepository;

    private final FamilyRepository familyRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    public List<Member> getFamilies() {
        return memberRepository.findAll();
    }

    public Member getMember(Long id) {
        return memberRepository.findById(id).orElseThrow();
    }

    public Member createMember(Member member) {
        return memberRepository.save(member);

    }

    public String updateMember(Details userDetails, Long idToModify, Errors errors, Model model, MemberDto memberToUpdate) {

        if (idToModify==0L){
            return "/modify/memberUpdate";
        }
        if (errors.hasErrors()) {
            userDetails.setText("");
            model.addAttribute("userDetails", userDetails);
            log.info("    --- Try again");
            return "/modify/updateData";
        }


        Member member = memberRepository.getById(idToModify);
        member.setName(memberToUpdate.getName());
        member.setFamilyName(memberToUpdate.getFamilyName());
        member.setBirthday(memberToUpdate.getBirthday());
        memberRepository.save(member);
        idToModify = 0L;

        log.info("   --- Member updated!");
        return "redirect:/modify/getMyFamilyAfterLog";
    }


    public void deleteMember(Long id) {
        memberRepository.deleteById(id);

    }

    public void deactivateMember(Long id) {
        memberRepository.deleteById(id);
    }

    public List<Member> getMembers() {
        return null;
    }

    private List<Long> getMembersIdList(String username) {
        Long myFamilyId = (userRepository.findByUsername(username).getMyFamilyNr());

        List<Member> members = familyRepository.findById(myFamilyId).get().getMembers();
        List<Long> allFamilyMembersId = new ArrayList<>();
        for (
                Member familyMember : members) {
            Long id = familyMember.getId();
            allFamilyMembersId.add(id);
        }
        return allFamilyMembersId;
    }

    public void detailsSetter(Model model, String username) {

        if (!userRepository.findByUsername(username).isDoIHaveFamily()) {
            return;
        }
        Details userDetails = new Details();
        model.addAttribute("userDetails", userDetails);
        model.addAttribute("memberDto", getMemberDTOList(username));
    }

    private List<MemberDto> getMemberDTOList( String username) {
        Long idToFind = userRepository.findByUsername(username).getMyFamilyNr();
        List<MemberDto> memberDtoList = Arrays.asList(modelMapper.
                map(familyRepository.findById(idToFind).get().getMembers(), MemberDto[].class));
        for (MemberDto member : memberDtoList) {
            member.setAge(calculateAge(member.getBirthday()));
        }
            return memberDtoList;
    }

    public void newMemberSaver(Member member, Family family, String username) {
        member.setMature(checkMaturity(member));
        member.setUserid(userRepository.findByUsername(username).getId());
        member = memberRepository.saveAndFlush(member);
        family.addFamilyMember(member);
    }


    @Override
    public int modifier(String birthday) {
        return 0;
    }
}
