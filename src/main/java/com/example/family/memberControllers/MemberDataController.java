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
import java.util.*;

@Transactional
@Controller
@RequestMapping("/modify")
@SessionAttributes("family")
@Slf4j
public class MemberDataController implements DetailsSet {

    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;
    private final MemberRepository memberRepository;

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
                                    @CurrentSecurityContext(expression = "authentication?.name")
                                    String username, Details details) {
        detailsSet(userRepository, username, details, model);

        return "modify/memberUpdate";
    }

    @GetMapping
    public String getDeleteFamilyView(Model model,
                                      @CurrentSecurityContext(expression = "authentication?.name")
                                      String username, Details details) {
        detailsSet(userRepository, username, details, model);


        return "modify/removeFamily";
    }

    @GetMapping("done")
    public String redirectIfFamilyIsDeleted(Model model,
                                            @CurrentSecurityContext(expression = "authentication?.name")
                                            String username, Details details) {
        detailsSet(userRepository, username, details, model);


        return "modify/done";
    }

    @GetMapping("removeFamily")
    public String getHomeView(Model model, @CurrentSecurityContext(expression = "authentication?.name")
    String username, Details details) {
        detailsSet(userRepository, username, details, model);


        Details userDetails = new Details();
        userDetails.setStatus(false);
        model.addAttribute("userDetails", userDetails);


        if (userRepository.findByUsername(username).isDoIHaveFamily()) {
            userDetails.setStatus(true);
            model.addAttribute("userDetails", userDetails);
            return "modify/removeFamily";

        }

        if (username.equals("anonymousUser")) {
            log.info("    --- No Family found");
            return "redirect:/404";

        }

        return "modify/removeFamily";

    }

    @PostMapping("removeFamily")
    public String DeleteFamilyFromDatabase(Model model,
                                           @CurrentSecurityContext(expression = "authentication?.name")
                                           String username, Details details) {
        detailsSet(userRepository, username, details, model);

        Long myFamilyId = (userRepository.findByUsername(username).getMyFamilyNr());
        System.out.println(myFamilyId);
        System.out.println(username);
        userRepository.findByUsername(username).setDoIHaveFamily(false);
        userRepository.findByUsername(username).setMyFamilyNr(0L);
        List<Member> members = familyRepository.findById(myFamilyId).get().getMembers();
        memberRepository.deleteAll(members);
        familyRepository.deleteById(myFamilyId);

        log.info("    --- Family Deleted");


        return "index";
    }

    @GetMapping("removeMember")
    public String getDeleteMemberView(Model model, @ModelAttribute Member member1,
                                      @CurrentSecurityContext(expression = "authentication?.name")
                                      String username, Details details) {
        detailsSet(userRepository, username, details, model);

        Details userDetails = new Details();
        model.addAttribute("userDetails", userDetails);
        Long idToFind = userRepository.findByUsername(username).getMyFamilyNr();
        model.addAttribute("families", familyRepository.findAllById(Collections.singleton(idToFind)));
        model.addAttribute("member1", familyRepository.findById(idToFind).get().getMembers());

        Long myFamilyId = (userRepository.findByUsername(username).getMyFamilyNr());

        if (familyRepository.findById(myFamilyId).get().getMembers().isEmpty()){
            userRepository.findByUsername(username).setDoIHaveFamily(false);
            userRepository.findByUsername(username).setMyFamilyNr(0L);
            familyRepository.deleteById(myFamilyId);

        log.info("    --- Member deleted");
        return "redirect:/index";
    }

        return "modify/removeMember";
    }


    @PostMapping("removeMember")
    public String createYourself(Model model, Details userDetails,
                                 @CurrentSecurityContext(expression = "authentication?.name")
                                 String username, Details details) {
        detailsSet(userRepository, username, details, model);

        if (userDetails.getId() == null) {
            log.info("    ---Id is null");
            return "redirect:/modify/removeMember";
        }

        Long idToRemove = userDetails.getId();
        Long myFamilyId = (userRepository.findByUsername(username).getMyFamilyNr());

        List<Member> members = familyRepository.findById(myFamilyId).get().getMembers();
        List<Long> allFamilyMembersId = new ArrayList<>();
        for (Member familyMember : members) {
            Long id = familyMember.getId();
            allFamilyMembersId.add(id);
        }

        if (allFamilyMembersId.contains(idToRemove)) {
            memberRepository.deleteById(idToRemove);


            if (familyRepository.findById(myFamilyId).get().getMembers().isEmpty()){
                userRepository.findByUsername(username).setDoIHaveFamily(false);
                userRepository.findByUsername(username).setMyFamilyNr(0L);
                familyRepository.deleteById(myFamilyId);
            }

            log.info("    --- Member deleted");
            return "redirect:/modify/redirectControlPass";
        }


        log.info("    --- No member");
        return "redirect:/modify/removeMember";
    }

    @GetMapping("addMember")
    public String getAddMemberView(Model model,
                                   @CurrentSecurityContext(expression = "authentication?.name")
                                   String username, Details details) {
        detailsSet(userRepository, username, details, model);

        return "modify/addMember";
    }

    @GetMapping("redirectControlPass")
    public String RedirectDataChecker(Model model,
                                   @CurrentSecurityContext(expression = "authentication?.name")
                                   String username, Details details) {
        detailsSet(userRepository, username, details, model);
        Long myFamilyId = (userRepository.findByUsername(username).getMyFamilyNr());

        if (familyRepository.findById(myFamilyId).get().getMembers().isEmpty()){
            userRepository.findByUsername(username).setDoIHaveFamily(false);
            userRepository.findByUsername(username).setMyFamilyNr(0L);
            familyRepository.deleteById(myFamilyId);

            log.info("    --- Verify yours data");
            return "redirect:/index";
        }
        log.info("    --- Verify yours data");
        return "redirect:/index";
    }


    @GetMapping("success")
    public String getSuccessView(Model model,
                                 @CurrentSecurityContext(expression = "authentication?.name")
                                 String username, Details details) {
        detailsSet(userRepository, username, details, model);

        return "index";

    }

    @GetMapping("getMyFamilyAfterLog")
    public String viewFamilyByUser(Model model, @ModelAttribute Member member1, @CurrentSecurityContext(expression = "authentication?.name")
    String username, Details details) {
        detailsSet(userRepository, username, details, model);

        if (!userRepository.findByUsername(username).isDoIHaveFamily()) {
            return "modify/wellLog";
        }
        Details userDetails = new Details();
        model.addAttribute("userDetails", userDetails);
        Long idToFind = userRepository.findByUsername(username).getMyFamilyNr();
        model.addAttribute("families", familyRepository.findAllById(Collections.singleton(idToFind)));
        model.addAttribute("member1", familyRepository.findById(idToFind).get().getMembers());
        return "modify/getMyFamilyAfterLog";

    }

    @GetMapping("wellLog")
    public String viewFamilyByUser(Model model, @CurrentSecurityContext(expression = "authentication?.name")
    String username, Details details) {
        detailsSet(userRepository, username, details, model);
        return "modify/wellLog";

    }

}
