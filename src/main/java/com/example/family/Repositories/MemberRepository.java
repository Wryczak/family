package com.example.family.Repositories;

import com.example.family.MainObjectsFamilyMemberDto.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByUserid(Long id);
}
