package com.example.family.services;

import com.example.family.Interfaces.UsernameGetter;
import com.example.family.Repositories.FamilyRepository;
import com.example.family.Repositories.MemberRepository;
import com.example.family.Repositories.UserRepository;
import com.example.family.MainObjectsFamilyMemberDto.Family;
import com.example.family.MainObjectsFamilyMemberDto.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FamilyService implements UsernameGetter {

    private final FamilyRepository familyRepository;

    private  final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final LinkedHashSet<Long> parentsIdListFatherLine = new LinkedHashSet<>();

    private final LinkedHashSet<Long> parentsIdListMatherLine = new LinkedHashSet<>();
    private final LinkedHashSet<Long> allChildrenList = new LinkedHashSet<>();

    public Family getFamily() {
        return familyRepository.getById(userRepository.findByUsername(getUsername()).getMyFamilyNr());
    }

    public Family getFamilyByUserId(Long id) {
        return familyRepository.getById(userRepository.getById(memberRepository.getById(id).getUserId()).getMyFamilyNr());
    }

    public Long getFamilyNumber() {
        return userRepository.findByUsername(getUsername()).getMyFamilyNr();
    }

    public Family getFamilyByGivenId(Long id) {
        return familyRepository.getById(id);
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

    public LinkedHashSet<Long> getParentTreeForFather(Long id) {

        Member member = memberRepository.getById(id);
        if (member.getFather() != null) {
            parentsIdListFatherLine.add(member.getFather().getId());
        }
        if (member.getMother() != null) {
            parentsIdListFatherLine.add(member.getMother().getId());
        }

        if (member.getFather() == null) {

            return parentsIdListFatherLine;
        }
        return getParentTreeForFather(member.getFather().getId());
    }

    public LinkedHashSet<Long> getParentTreeForMother(Long id) {

        Member member = memberRepository.getById(id);
        if (member.getMother() != null) {
            parentsIdListMatherLine.add(member.getMother().getId());
        }
        if (member.getFather() != null) {
            parentsIdListMatherLine.add(member.getFather().getId());
        }

        if (member.getMother() == null) {

            return parentsIdListMatherLine;
        }
        return getParentTreeForMother(member.getMother().getId());
    }

    public List<Long> getParentsForSinglePerson(Long id) {
        List<Long> parents = new ArrayList<>();
        Member member = memberRepository.getById(id);
        if (member.getFather() != null) {
            parents.add(member.getFather().getId());
        }
            if (member.getMother() != null) {
                parents.add(member.getMother().getId());
            }
        return parents;
    }

    public void createAllChildrenIdList(Long id) {
        List<Member> familyMembersList = getFamilyByUserId(id).getMembers();
        List<Long> tempList = new ArrayList<>();
        for (Member member : familyMembersList) {
            if (member.getFather() != null && member.getFather().getId().equals(id)) {
                allChildrenList.add(member.getId());
                tempList.add(member.getId());
            }
            if (member.getMother() != null && member.getMother().getId().equals(id)) {
                allChildrenList.add(member.getId());
                tempList.add(member.getId());
            }
        }
        for (Long childrenId : tempList) {
            createAllChildrenIdList(childrenId);
        }
    }

    public List<Long> getChildrenForSinglePerson(Long id) {
        List<Member> list = getFamilyByUserId(id).getMembers();
        List<Long>children=new ArrayList<>();
        for (Member member:list) {
            if (member.getFather()!=null){
            if (member.getFather().getId().equals(id)){
                    children.add(member.getId());
                }
            }
            if (member.getMother()!=null) {
                if (member.getMother().getId().equals(id)) {
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
        getParentTreeForMother(id);
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
//        System.out.println(parentsIdListFatherLine.stream().skip(parentsIdListFatherLine.size() - 1).findFirst().get());
        return parentsIdListFatherLine.stream().skip(parentsIdListFatherLine.size() - 1).findFirst().get();

    }
    public LinkedHashSet<Long> getAncestors(Long id){

        LinkedHashSet<Long> ancestors = new LinkedHashSet<>(getParentTreeForFather(id));
        ancestors.addAll( getParentTreeForMother(id));
        return ancestors;
    }

    public void clear(){
        parentsIdListFatherLine.clear();
        allChildrenList.clear();
        parentsIdListMatherLine.clear();
    }
}


