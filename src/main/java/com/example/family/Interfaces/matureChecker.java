package com.example.family.Interfaces;
import com.example.family.family.Family;
import com.example.family.family.Member;
import org.springframework.stereotype.Component;
@Component
public interface matureChecker {
    int checkMature(Member.Mature mature, Family family);
}
