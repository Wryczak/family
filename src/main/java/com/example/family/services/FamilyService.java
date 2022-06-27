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
import java.util.stream.Collectors;

import static java.util.function.Function.*;

@Service
@RequiredArgsConstructor
public class FamilyService implements UsernameGetter {

    private final FamilyRepository familyRepository;
    private final UserRepository userRepository;

    private final MemberRepository memberRepository;

    private final List<Long>membersIdList=new ArrayList<>();
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


    public List<Long> listOfParents(){
        return membersIdList;
    }

    public List<Long> getParentTree2(Long id) {
        if (id != 0) {
            Member member = getMemberByIdFromFamily(id);
           membersIdList.add(member.getId());
            if (member.getFatherId() == null) {
                return membersIdList;
            }
          return getParentTree2(member.getFatherId());
        }
        return null;
    }

    public Member getMemberByIdFromFamily(Long id) {
        List<Member> list = getFamily().getMembers();
        Map<Long, Member> collect = list.stream()
                .collect(Collectors.toMap(Member::getId, identity()));
        return collect.get(id);
    }
    public List<Long> childrenForEach(Long id){
        List<Member> list = getFamily().getMembers();
        List<Long> tempList = new ArrayList<>();
        System.out.println(getMemberByIdFromFamily(id));
        for (Member member : list) {
            if (member.getFatherId() != null && member.getFatherId().equals(id)) {
                tempList.add(member.getId());
            }
        }
        if (tempList.isEmpty()){
            System.out.println("Nie ma dzieci");
            System.out.println();
            System.out.println();
            System.out.println("-----------------");
            return null;
        }
        System.out.println(tempList);
        System.out.println();
        System.out.println();
        System.out.println("-----------------");
        return tempList;
    }

    public void listwithall(Long id){
       List<Long> list= getParentTree2(id);
        for (Long memberId: list){
            childrenForEach(memberId);
        }
    }
}


