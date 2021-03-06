package com.example.family.memberControllers;

import com.example.family.Interfaces.Details;
import com.example.family.Interfaces.DetailsSet;
import com.example.family.Interfaces.MaturityChecker;
import com.example.family.data.FamilyRepository;
import com.example.family.data.MemberRepository;
import com.example.family.data.UserRepository;
import com.example.family.family.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.*;

@Transactional
@Controller
@RequestMapping("/modify")
@SessionAttributes("family")
@Slf4j
public class MemberDataController implements DetailsSet, MaturityChecker {

    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;
    private final MemberRepository memberRepository;
    private Long idToModify;

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
    public String getMemberUpdateView(Model model,
                                      @CurrentSecurityContext(expression = "authentication?.name")
                                      String username, Details details) {
        detailsSet(userRepository, username, details, model);
        if (familyRepository.findById(userRepository.findByUsername(username).getMyFamilyNr()).isEmpty()) {
            return "redirect:/index";
        }
        detailsSetter(model, username);

        return "modify/memberUpdate";
    }

    @PostMapping("memberUpdate")
    public String MemberUpdate(Model model, Details userDetails,
                               @CurrentSecurityContext(expression = "authentication?.name")
                               String username, Details details) {
        detailsSet(userRepository, username, details, model);

        if (userDetails.getId() == null) {
            log.info("    ---Id is null");
            return "redirect:/modify/memberUpdate";
        }

        Long memberIdToUpdate = userDetails.getId();
        idToModify = memberIdToUpdate;

        List<Long> allFamilyMembersId = getMembersIdList(username);

        if (allFamilyMembersId.contains(memberIdToUpdate)) {

            log.info("    --- Member found");
            return "redirect:/modify/updateData";
        }

        log.info("    --- No member");
        return "redirect:/modify/memberUpdate";
    }

    @GetMapping("addMember")
    public String getAddMemberView(Model model,
                                   @CurrentSecurityContext(expression = "authentication?.name")
                                   String username, Details details) {
        detailsSet(userRepository, username, details, model);

        return "modify/addMember";
    }

    @PostMapping("addMember")
    public String addNewMemberToFamily(Model model, @Valid Member member, Errors errors, Family family,
                                       @CurrentSecurityContext(expression = "authentication?.name")
                                       String username, Details details) {
        detailsSet(userRepository, username, details, model);

        if (errors.hasErrors()) {
            log.info("    --- Try again");
            return "modify/addMember";
        }
        family = familyRepository.getById(userRepository.findByUsername(username).getMyFamilyNr());

        log.info("    --- Creating new family member");
        Member saved = member;
        member.setMature(checkMaturity(saved));
        member.setUserid(userRepository.findByUsername(username).getId());
        saved = memberRepository.save(member);
        family.addFamilyMember(saved);

        return "redirect:/modify/getMyFamilyAfterLog";
    }

    @GetMapping("removeFamily")
    public String getHomeView(Model model, @CurrentSecurityContext(expression = "authentication?.name")
    String username, Details details) {
        detailsSet(userRepository, username, details, model);

        if (!userRepository.findByUsername(username).isDoIHaveFamily()){
            return "redirect:/index";
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
        List<Member> members = familyRepository.findById(myFamilyId).get().getMembers();
        memberRepository.deleteAll(members);

        repositoryDeleteSetter(username,myFamilyId);

        log.info("    --- Family Deleted");
        return "redirect:/index";
    }

    @GetMapping("removeMember")
    public String getDeleteMemberView(Model model, @ModelAttribute Member member1,
                                      @CurrentSecurityContext(expression = "authentication?.name")
                                      String username, Details details) {
        detailsSet(userRepository, username, details, model);

        detailsSetter(model, username);

        Long myFamilyId = (userRepository.findByUsername(username).getMyFamilyNr());

        if (familyRepository.findById(myFamilyId).get().getMembers().isEmpty()) {
         repositoryDeleteSetter(username,myFamilyId);

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
        List<Long> allFamilyMembersId = getMembersIdList(username);

        if (allFamilyMembersId.contains(idToRemove)) {
            memberRepository.deleteById(idToRemove);

            log.info("    --- Member deleted");
            return "redirect:/modify/redirectControlPass";
        }

        log.info("    --- No member");
        return "redirect:/modify/removeMember";
    }

    @GetMapping("redirectControlPass")
    public String RedirectDataChecker(Model model,
                                      @CurrentSecurityContext(expression = "authentication?.name")
                                      String username, Details details) {
        detailsSet(userRepository, username, details, model);
        Long myFamilyId = (userRepository.findByUsername(username).getMyFamilyNr());

        if (familyRepository.findById(myFamilyId).get().getMembers().isEmpty()) {
            repositoryDeleteSetter(username, myFamilyId);

            log.info("    --- Verify yours data");
            return "redirect:/index";
        }
        log.info("    --- Verify yours data");
        return "redirect:/index";

    }

    @GetMapping("getMyFamilyAfterLog")
    public String viewFamilyByUser(Model model, @ModelAttribute Member member1, @CurrentSecurityContext(expression = "authentication?.name")
    String username, Details details) {
        detailsSet(userRepository, username, details, model);

        if (!userRepository.findByUsername(username).isDoIHaveFamily()) {
            return "modify/wellLog";
        }
        detailsSetter(model, username);
        return "modify/getMyFamilyAfterLog";
    }

    @GetMapping("updateData")
    public String showUpdateForm(Model model, @CurrentSecurityContext(expression = "authentication?.name")
    String username, Details details) {
        detailsSet(userRepository, username, details, model);

        if (idToModify == null || idToModify == 0L) {
            return "redirect:/index";
        }
        Details userDetails = new Details();
        Member memberToUpdate = memberRepository.getById(idToModify);
        String memberData = memberToUpdate.getName() + "   " + memberToUpdate.getFamilyName() + " ID: " + idToModify;
        userDetails.setText(memberData);
        model.addAttribute("userDetails", userDetails);
        model.addAttribute("memberToUpdate", memberToUpdate);

        log.info("   --- Updating Member");
        return "modify/updateData";
    }

    @PostMapping("updateData")
    public String postUpdateForm(Model model, @Valid Member memberToUpdate, Errors errors, Details userDetails,
                                 @CurrentSecurityContext(expression = "authentication?.name") String username, Details details) {
        detailsSet(userRepository, username, details, model);
        if (errors.hasErrors()) {
            userDetails.setText("");
            model.addAttribute("userDetails", userDetails);
            log.info("    --- Try again");
            return "/modify/updateData";
        }

        Member member = memberRepository.getById(idToModify);
        member.setName(memberToUpdate.getName());
        member.setFamilyName(memberToUpdate.getFamilyName());
        memberRepository.save(member);
        idToModify = 0L;

        log.info("   --- Member updated!");
        return "redirect:/modify/getMyFamilyAfterLog";

    }

    @GetMapping("wellLog")
    public String viewFamilyByUser(Model model, @CurrentSecurityContext(expression = "authentication?.name")
    String username, Details details) {
        detailsSet(userRepository, username, details, model);
        return "modify/wellLog";

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

    private void detailsSetter(Model model, @CurrentSecurityContext(expression = "authentication?.name") String username) {
        Details userDetails = new Details();
        model.addAttribute("userDetails", userDetails);
        Long idToFind = userRepository.findByUsername(username).getMyFamilyNr();
        model.addAttribute("families", familyRepository.findAllById(Collections.singleton(idToFind)));
        model.addAttribute("member1", familyRepository.findById(idToFind).get().getMembers());
    }
    private void repositoryDeleteSetter(String username, Long myFamilyId) {
        userRepository.findByUsername(username).setDoIHaveFamily(false);
        userRepository.findByUsername(username).setMyFamilyNr(0L);
        familyRepository.deleteById(myFamilyId);
    }
}