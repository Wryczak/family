package com.example.family.services;

import com.example.family.Interfaces.Details;
import com.example.family.Interfaces.UsernameGetter;
import com.example.family.data.FamilyRepository;
import com.example.family.data.UserRepository;
import com.example.family.family.Family;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FamilyService implements UsernameGetter {

    private final FamilyRepository familyRepository;
    private final UserRepository userRepository;

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
}
