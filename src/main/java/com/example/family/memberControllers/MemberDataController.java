package com.example.family.memberControllers;

import com.example.family.data.FamilyRepository;
import com.example.family.data.MemberRepository;
import com.example.family.data.UserRepository;
import com.example.family.family.Details;
import com.example.family.family.DetailsSet;
import com.example.family.family.Family;
import com.example.family.family.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Transactional
@Controller
@RequestMapping("/modify")
@SessionAttributes("family")
@Slf4j
public class MemberDataController implements DetailsSet {

    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;
    private final MemberRepository memberRepository;

    private Long idToRemove;

    @Autowired
    public MemberDataController(UserRepository userRepository, FamilyRepository familyRepository,
                                MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
        this.userRepository = userRepository;
        this.familyRepository = familyRepository;
    }

    @ModelAttribute(name = "family")
    public Family createFamily() {
        return new Family();
    }

    @ModelAttribute(name = "member")
    public Member createMember() {
        return new Member();
    }


    @GetMapping("memberUpdate")
    public String getMemberDataView(Model model,
                                    @CurrentSecurityContext(expression = "authentication?.name") String username) {


        String memberUpdateView = "memberUpdate";
        return getMenuDependsOnAuthentication(memberUpdateView, model, username);
    }

    @GetMapping
    public String getDeleteFamilyView(Model model,
                                      @CurrentSecurityContext(expression = "authentication?.name") String username) {

        String deleteFamilyView = "modify/removeFamily";
        return getMenuDependsOnAuthentication(deleteFamilyView, model, username);
    }

    @GetMapping("done")
    public String redirectIfFamilyIsDeleted(Model model,
                                            @CurrentSecurityContext(expression = "authentication?.name") String username) {

        String done = "modify/done";
        return getMenuDependsOnAuthentication(done, model, username);
    }

    @GetMapping("removeFamily")
    public String getHomeView(Model model, @CurrentSecurityContext(expression = "authentication?.name") String username) {

        Details userDetails = new Details();
        userDetails.setStatus(false);
        model.addAttribute("userDetails", userDetails);


        if (userRepository.findByUsername(username).isDoIHaveFamily()) {
            userDetails.setStatus(true);
            model.addAttribute("userDetails", userDetails);
            String deleteFamilyView = "modify/removeFamily";
            return getMenuDependsOnAuthentication(deleteFamilyView, model, username);

        }

        if (username.equals("anonymousUser")) {
            log.info("    --- No Family found");
            String noFamilyView = "redirect:/404";
            return getMenuDependsOnAuthentication(noFamilyView, model, username);
        }

        String indexView = "modify/removeFamily";
        return getMenuDependsOnAuthentication(indexView, model, username);
    }

    @PostMapping("removeFamily")
    public String DeleteFamilyFromDatabase(Model model,
                                           @CurrentSecurityContext(expression = "authentication?.name") String username) {

        Long myFamilyId = (userRepository.findByUsername(username).getMyFamilyNr());
        System.out.println(myFamilyId);
        System.out.println(username);
        userRepository.findByUsername(username).setDoIHaveFamily(false);
        userRepository.findByUsername(username).setMyFamilyNr(0L);
        List<Member> members = familyRepository.findById(myFamilyId).get().getMembers();
        memberRepository.deleteAll(members);
        familyRepository.deleteById(myFamilyId);

        log.info("    --- Family Deleted");

        String done = "redirect:/index";
        return getMenuDependsOnAuthentication(done, model, username);
    }

    @GetMapping("removeMember")
    public String getDeleteMemberView(Model model, @ModelAttribute Member member1,
                                      @CurrentSecurityContext(expression = "authentication?.name") String username) {
//        getMemberIdToRemove(model);
        Long idToFind = userRepository.findByUsername(username).getMyFamilyNr();
        model.addAttribute("families", familyRepository.findAllById(Collections.singleton(idToFind)));
        model.addAttribute("member1", familyRepository.findById(idToFind).get().getMembers());

        String deleteFamilyView = "modify/removeMember";
        return getMenuDependsOnAuthentication(deleteFamilyView, model, username);
    }


    @PostMapping("removeMember")
    public String createYourself(@Valid Member member, Model model,
                                 @CurrentSecurityContext(expression = "authentication?.name") String username) {

        if (member.getIdToRemove() == null) {
            String deleteFamilyView = "modify/removeMember";
            return getMenuDependsOnAuthentication(deleteFamilyView, model, username);
        }
        Long idToRemove = member.getIdToRemove();

        Long myFamilyId = (userRepository.findByUsername(username).getMyFamilyNr());


        List<Member> members = familyRepository.findById(myFamilyId).get().getMembers();
        List<Long> allFamilyMembersId = new ArrayList<>();
        for (Member familyMember : members) {
            Long id = familyMember.getId();
            allFamilyMembersId.add(id);
        }

        if (allFamilyMembersId.contains(idToRemove)) {
            memberRepository.deleteById(idToRemove);
            if (isFamilyEmpty(username)){
                userRepository.findByUsername(username).setDoIHaveFamily(false);
                userRepository.findByUsername(username).setMyFamilyNr(0L);
                familyRepository.deleteById(myFamilyId);

            }
                log.info("    --- Member deleted");
            return "redirect:/index";
        }

        log.info("    --- No member");
        String deleteFamilyView = "redirect:/modify/removeMember";
        return getMenuDependsOnAuthentication(deleteFamilyView, model, username);
    }

    @GetMapping("addMember")
    public String getAddMemberView(Model model,
                                   @CurrentSecurityContext(expression = "authentication?.name") String username) {

        String deleteFamilyView = "modify/removeFamily";
        return getMenuDependsOnAuthentication(deleteFamilyView, model, username);
    }

    @GetMapping("success")
    public String getSuccessView(Model model,
                                 @CurrentSecurityContext(expression = "authentication?.name") String username) {

        String deleteFamilyView = "modify/removeFamily";
        return getMenuDependsOnAuthentication(deleteFamilyView, model, username);
    }


    private boolean isFamilyEmpty(String username) {
        Long familyId = userRepository.findByUsername(username).getMyFamilyNr();
        Optional<Family> family=familyRepository.findById(familyId);
        if (family.get().getMembers().isEmpty()) {
            return true;
        }
        return false;
    }

//    private Details getMemberIdToRemove(Model model) {
//        Details memberId = new Details();
//        model.addAttribute("memberId", memberId);
//        return memberId;
//    }
}
