package com.example.family.service;

import com.example.family.data.FamilyRepository;
import com.example.family.family.Family;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FamilyService {

    private final FamilyRepository familyRepository;

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

}
