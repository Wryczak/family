package com.example.family.services;


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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService implements DtoConverter, AgeCalculator, MaturityChecker,UsernameGetter {

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

    public void updateMember(Long idToModify, MemberDto memberToUpdate) {

        Member member = memberRepository.getById(idToModify);
        member.setName(memberToUpdate.getName());
        member.setFamilyName(memberToUpdate.getFamilyName());
        member.setBirthday(memberToUpdate.getBirthday());
        memberRepository.save(member);
        idToModify = 0L;

        log.info("   --- Member updated!");

    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);

    }

    public void deleteAllMembersFromFamily(List<Member> members) {
        memberRepository.deleteAll(members);

    }

    public void deactivateMember(Long id) {
        memberRepository.deleteById(id);
    }

    public List<Member> getMembers() {
        Long myFamilyId = (userRepository.findByUsername(getUsername()).getMyFamilyNr());
        return familyRepository.findById(myFamilyId).get().getMembers();
    }

    public List<Long> getMembersIdList(String username) {
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

    public void getFamilyMemberDtoListAndAddToModel(Model model,String username){
        Details userDetails = new Details();
        model.addAttribute("userDetails", userDetails);
        model.addAttribute("memberDto", getMemberDTOList(username));
    }

    private List<MemberDto> getMemberDTOList(String username) {
        Long idToFind = userRepository.findByUsername(username).getMyFamilyNr();
        List<MemberDto> memberDtoList = Arrays.asList(modelMapper.
                map(familyRepository.findById(idToFind).get().getMembers(), MemberDto[].class));
        for (MemberDto member : memberDtoList) {
            member.setAge(calculateAge(member.getBirthday()));
        }
        return memberDtoList;
    }

    public void newMemberSaver(Member member, Family family) {
        member.setMature(checkMaturity(member));
        member.setUserid(userRepository.findByUsername(getUsername()).getId());
        member = memberRepository.saveAndFlush(member);
        family.addFamilyMember(member);
    }

    public void detailsSet(Details details, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        if (Objects.equals(username, "anonymousUser")) {
            details.setText("Zaloguj się:");
            details.setStatus(false);
            model.addAttribute("details", details);
            return;
        }
        details.setText(username);
        details.setStatus(true);
        model.addAttribute("details", details);
        if (userRepository.findByUsername(username).isDoIHaveFamily()) {
            details.setSecondStatus(true);
            model.addAttribute("status", details);
        }
    }

    public boolean isDoIHaveFamily() {
        return userRepository.findByUsername(getUsername()).isDoIHaveFamily();
    }

    public boolean isAnyMemberCreatedByUser() {
        return memberRepository.findByUserid(userRepository.findByUsername(getUsername()).getId()) == null;
    }
}
