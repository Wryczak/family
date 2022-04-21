package com.example.family.family;
import org.springframework.stereotype.Component;
@Component
public interface matureChecker {
    int checkMature(Member.Mature mature, Family family);
}
