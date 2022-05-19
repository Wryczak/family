//package com.example.family.web;
//
//import com.example.family.data.FamilyRepository;
//import com.example.family.data.MemberRepository;
//import com.example.family.data.UserRepository;
//import com.example.family.family.DetailsSet;
//import com.example.family.family.Family;
//import com.example.family.family.Member;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.annotation.CurrentSecurityContext;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import java.util.Collections;
//
//@Controller
//@RequestMapping("/familyDetails")
//@Slf4j
//public class TestController implements DetailsSet {
//
//    private final UserRepository userRepository;
//    private final FamilyRepository familyRepository;
//    private final MemberRepository memberRepository;
//
//    private Long idToRemove;
//
//    @Autowired
//    public TestController(UserRepository userRepository, FamilyRepository familyRepository,
//                                MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//        this.userRepository = userRepository;
//        this.familyRepository = familyRepository;
//    }
//
//    @ModelAttribute(name = "family")
//    public Family createFamily() {
//        return new Family();
//    }
//
//    @ModelAttribute(name = "member")
//    public Member createMember() {
//        return new Member();
//    }
//
//
//    @GetMapping("familyDetails")
//    public String getDeleteMemberView(){
//        return "familyDetails";
//    }
//}
