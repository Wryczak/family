package com.example.family.Repositories;

import com.example.family.MainObjectsFamilyMemberDto.Family;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FamilyRepository extends JpaRepository<Family, Long> {

}
