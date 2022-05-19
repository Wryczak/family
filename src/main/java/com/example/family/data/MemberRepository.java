package com.example.family.data;

import com.example.family.family.Member;
import com.example.family.security.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByUserid(Long id);
}
