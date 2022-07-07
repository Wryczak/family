//package com.example.family.services;
//
//import com.example.family.Repositories.MemberRepository;
//import com.example.family.MainObjectsFamilyMemberDto.Family;
//import com.example.family.MainObjectsFamilyMemberDto.Member;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Slf4j
//@Service
//public class ValidatorService {
//    private  final MemberRepository memberRepository;
//
//
//    public ValidatorService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }
//
//    public  String validateFamily(Family family) {
//        String memberValidateRedirect;
//        memberValidateRedirect = checkFamilyMembersByMaturity(family, family.getNrOfInfants(), Member.Mature.INFANT);
//        if (memberValidateRedirect != null) {
//            return memberValidateRedirect;
//        }
//
//        memberValidateRedirect = checkFamilyMembersByMaturity(family, family.getNrOfChildren(), Member.Mature.CHILD);
//        if (memberValidateRedirect != null) {
//            return memberValidateRedirect;
//        }
//
//        memberValidateRedirect = checkFamilyMembersByMaturity(family, family.getNrOfAdults(), Member.Mature.ADULT);
//        if (memberValidateRedirect != null) {
//            return memberValidateRedirect;
//        }
//        return null;
//    }
//
//    private  String checkFamilyMembersByMaturity(Family family, int nrOfMember, Member.Mature mature) {
//        if (nrOfMember > checkMature(mature, family)) {
//            log.info("    --- addAnother " + mature);
//            return "family";
//
//        }
//        if (checkMature(mature, family) > nrOfMember) {
//            System.out.println("I`m removing last " + mature);
//            Long id = getMemberIdFromSublist(mature, family);
//            family.deleteFamilyMember(memberRepository.getById(id));
//            memberRepository.deleteById(id);
//
//            log.info("    --- ToMany " + mature + " --deleting the last one");
//            return "redirect:removing";
//        }
//        return null;
//    }
//
//    private  int checkMature(Member.Mature mature, Family family) {
//        int nrOfMembers = 0;
//        List<Member> members = family.getMembers();
//        for (Member member : members) {
//            if (member.getMature() == mature) {
//                nrOfMembers++;
//            }
//        }
//        return nrOfMembers;
//    }
//
//    private Long getMemberIdFromSublist(Member.Mature mature, Family family) {
//        List<Member> members = family.getMembers();
//        List<Member> tempMember = new ArrayList<>();
//        for (Member member : members) {
//            if (member.getMature() == mature) {
//                tempMember.add(member);
//            }
//        }
//        if (tempMember.isEmpty()) {
//            return null;
//        }
//        return tempMember.get(tempMember.size() - 1).getId();
//    }
//}
