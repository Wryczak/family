package com.example.family.services;

import com.example.family.Interfaces.Details;
import com.example.family.Interfaces.UsernameGetter;
import com.example.family.data.FamilyRepository;
import com.example.family.data.MemberRepository;
import com.example.family.data.UserRepository;
import com.example.family.family.Family;
import com.example.family.family.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.function.Function.*;

@Service
@RequiredArgsConstructor
public class FamilyService implements UsernameGetter {

    private final FamilyRepository familyRepository;
    private final UserRepository userRepository;

    private final MemberRepository memberRepository;

    public List<Family> getFamilies() {
        return familyRepository.findAll();
    }

    public Family getFamily(Long id) {
        return familyRepository.findById(id).orElseThrow();
    }

    public Family createFamily(Family family) {
        return familyRepository.save(family);

    }

    public Family updateFamily(Family family) {
        return familyRepository.save(family);
    }

    public void deleteFamily(Long id) {
        familyRepository.deleteById(id);

    }

    public void deactivateFamily(Long id) {
        familyRepository.deleteById(id);
    }


    public void setFamilyDetails(Family family) {
        userRepository.findByUsername(getUsername()).setDoIHaveFamily(true);
        userRepository.findByUsername(getUsername()).setMyFamilyNr(family.getId());
    }

    public Long getUserFamilyNumber() {
        return userRepository.findByUsername(getUsername()).getMyFamilyNr();
    }

    public void getFamilyNumberAndAddToModel(Model model) {
        Details userDetails = new Details();
        userDetails.setId(getUserFamilyNumber());
        model.addAttribute("userDetails", userDetails);
    }

    public boolean isDoIHaveFamily() {
        return userRepository.findByUsername(getUsername()).isDoIHaveFamily();
    }

    public void detailsSetter(Model model, String username) {
        Details userDetails = new Details();
        model.addAttribute("userDetails", userDetails);
        Long idToFind = userRepository.findByUsername(username).getMyFamilyNr();
        model.addAttribute("member1", familyRepository.findById(idToFind).get().getMembers());
    }

    public void repositoryDeleteSetter(String username, Long myFamilyId) {
        userRepository.findByUsername(username).setDoIHaveFamily(false);
        userRepository.findByUsername(username).setMyFamilyNr(0L);
        familyRepository.deleteById(myFamilyId);
    }

    public boolean isFamilyEmpty() {
        return familyRepository
                .findById(userRepository
                        .findByUsername(getUsername()).getMyFamilyNr()).isEmpty();
    }

    public Family getFamily() {
        Family family;
        family = familyRepository.getById(userRepository.findByUsername(getUsername()).getMyFamilyNr());
        return family;
    }

    public Member getParentTree(Long id) {
        if (id != 0) {
            Member member = getMemberByIdFromFamily(id);
            System.out.println(member);
            System.out.println();
            if (member.getFatherId() == null) {
                return null;
            }
            getParentTree(member.getFatherId());
            return member;
        }
        return null;
    }

    public Long childrenListGetter(Long id) {
        if (id!=0) {

            if (convertListToMapByFatherId().containsKey(id)) {
                Member member = convertListToMapByFatherId().get(id);
                System.out.println(member);
                System.out.println();
                id = member.getId();
                return childrenListGetter(id);
            }
        }
            return 0L;
        }




    public Map<Long, Member> convertListToMap() {
        List<Member> list = getFamily().getMembers();
        return list.stream()
                .collect(Collectors.toMap(Member::getId, identity()));
    }

    public Map<Long, Member> convertListToMapByFatherId() {
        List<Member> list = getFamily().getMembers();
        List<Member> tempList = new ArrayList<>();
        for (Member member : list) {
            if (member.getFatherId() != null) {
                tempList.add(member);
            }
        }
        return tempList.stream()
                .collect(Collectors.toMap(Member::getFatherId, identity()));
    }

    public Member getMemberByIdFromFamily(Long id) {
        List<Member> list = getFamily().getMembers();
        Map<Long, Member> collect = list.stream()
                .collect(Collectors.toMap(Member::getId, identity()));
        return collect.get(id);

    }
}



