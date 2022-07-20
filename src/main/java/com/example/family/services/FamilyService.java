package com.example.family.services;

import com.example.family.MainObjectsFamilyMemberDto.Details;
import com.example.family.Interfaces.UsernameGetter;
import com.example.family.Repositories.FamilyRepository;
import com.example.family.Repositories.UserRepository;
import com.example.family.MainObjectsFamilyMemberDto.Family;
import com.example.family.MainObjectsFamilyMemberDto.Member;
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
    private final LinkedHashSet<Long> parentsIdListFatherLine = new LinkedHashSet<>();

    private final LinkedHashSet<Long> parentsIdListMatherLine = new LinkedHashSet<>();
    private final LinkedHashSet<Long> allChildrenList = new LinkedHashSet<>();

    public Family getFamily() {
        return familyRepository.getById(userRepository.findByUsername(getUsername()).getMyFamilyNr());
    }

    public Long getFamilyNumber() {
        return userRepository.findByUsername(getUsername()).getMyFamilyNr();
    }

    public Family getFamilyByGivenId(Long id) {
        return familyRepository.findById(id).orElseThrow();
    }

    public Family createFamily(Family family) {
        return familyRepository.save(family);

    }

    public void setFamilyDetails(Family family) {
        userRepository.findByUsername(getUsername()).setDoIHaveFamily(true);
        userRepository.findByUsername(getUsername()).setMyFamilyNr(family.getId());
    }

    public Long getUserFamilyNumber() {
        return userRepository.findByUsername(getUsername()).getMyFamilyNr();
    }

    public boolean isDoIHaveFamily() {
        return userRepository.findByUsername(getUsername()).isDoIHaveFamily();
    }

    public void repositoryDeleteSetter() {
        Long myFamilyId = getUserFamilyNumber();
        String username = getUsername();
        userRepository.findByUsername(username).setDoIHaveFamily(false);
        userRepository.findByUsername(username).setMyFamilyNr(0L);
        familyRepository.deleteById(myFamilyId);
    }

    public boolean isFamilyEmpty() {
        return familyRepository
                .findById(userRepository
                        .findByUsername(getUsername()).getMyFamilyNr()).isEmpty();
    }

    public Member getMemberByIdFromFamily(Long id) {
        List<Member> list = getFamily().getMembers();
        Map<Long, Member> collect = list.stream()
                .collect(Collectors.toMap(Member::getId, identity()));
        return collect.get(id);
    }

    public LinkedHashSet<Long> getParentTreeForFather(Long id) {

        Member member = getMemberByIdFromFamily(id);
        if (member.getFatherId() != null) {
            parentsIdListFatherLine.add(member.getFatherId());
        }
        if (member.getMatherId() != null) {
            parentsIdListFatherLine.add(member.getMatherId());
        }

        if (member.getFatherId() == null) {

            return parentsIdListFatherLine;
        }
        return getParentTreeForFather(member.getFatherId());
    }

    public LinkedHashSet<Long> getParentTreeForMather(Long id) {

        Member member = getMemberByIdFromFamily(id);
        if (member.getMatherId() != null) {
            parentsIdListMatherLine.add(member.getMatherId());
        }
        if (member.getFatherId() != null) {
            parentsIdListMatherLine.add(member.getFatherId());
        }

        if (member.getMatherId() == null) {

            return parentsIdListMatherLine;
        }
        return getParentTreeForMather(member.getMatherId());
    }


    public List<Long> getParentsForSinglePerson(Long id) {
        List<Long> parents = new ArrayList<>();
        Member member = getMemberByIdFromFamily(id);
        if (member.getFatherId() != null) {
            parents.add(member.getFatherId());
        }
            if (member.getMatherId() != null) {
                parents.add(member.getMatherId());
            }
        return parents;
    }

    public void createAllChildrenIdList(Long id) {
        List<Member> familyMembersList = getFamily().getMembers();
        List<Long> tempList = new ArrayList<>();
        for (Member member : familyMembersList) {
            if (member.getFatherId() != null && member.getFatherId().equals(id)) {
                allChildrenList.add(member.getId());
                tempList.add(member.getId());
            }
            if (member.getMatherId() != null && member.getMatherId().equals(id)) {
                allChildrenList.add(member.getId());
                tempList.add(member.getId());
            }
        }
        for (Long childrenId : tempList) {
            createAllChildrenIdList(childrenId);
        }
    }


    public List<Long> getChildrenForSinglePerson(Long id) {
        List<Member> list = getFamily().getMembers();
        List<Long>children=new ArrayList<>();
        for (Member member:list) {
            if (member.getFatherId()!=null){
            if (member.getFatherId().equals(id)){
                    children.add(member.getId());
                }
            }
            if (member.getMatherId()!=null) {
                if (member.getMatherId().equals(id)) {
                    children.add(member.getId());
                }
            }
        }
        return children;
    }

    public boolean isFamilyExists(Long id) {
        if (familyRepository.existsById(id)) {
            return true;
        }
        return false;
    }
    public LinkedHashSet<Long> getAllChildrenList(Long id){
        createAllChildrenIdList(id);

        return allChildrenList;

    }
    public LinkedHashSet getAllRelatives(Long id){
        findAllRelatives(id);
        LinkedHashSet<Long> allRelatives=new LinkedHashSet<>();
        allRelatives.addAll(allChildrenList);
        allRelatives.remove(id);
        allRelatives.addAll(parentsIdListFatherLine);
        allRelatives.addAll(parentsIdListMatherLine);
        return allRelatives;
    }

    private void findAllRelatives(Long id) {
        getParentTreeForFather(id);
        getParentTreeForMather(id);
        if (parentsIdListFatherLine.isEmpty()){
            System.out.println("Brak przodków");

            createAllChildrenIdList(id);
            if (allChildrenList.isEmpty()){
                System.out.println("Brak potomków");
            }else System.out.println("Znaleziono potomków! "+ allChildrenList);
            return;
        }
        createAllChildrenIdList(findRelativesByAncestorId());
    }

    private Long findRelativesByAncestorId(){
        System.out.println(parentsIdListFatherLine.stream().skip(parentsIdListFatherLine.size() - 1).findFirst().get());
        return parentsIdListFatherLine.stream().skip(parentsIdListFatherLine.size() - 1).findFirst().get();

    }
    public LinkedHashSet<Long> getAncestors(Long id){

        LinkedHashSet<Long> ancestors = new LinkedHashSet<>(getParentTreeForFather(id));
        ancestors.addAll( getParentTreeForMather(id));
        return ancestors;
    }
    public void clear(){
        parentsIdListFatherLine.clear();
        allChildrenList.clear();
        parentsIdListMatherLine.clear();
    }
}


