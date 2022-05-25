package com.example.family.family;

public interface MaturityChecker {
  default Member.Mature checkMaturity(Member member) {
        int age = member.getAge();
        if (age >= 0 && age < 4) {
            return Member.Mature.INFANT;
        } else if (age >= 4 && age <= 16) {
            return Member.Mature.CHILD;
        } else
            return Member.Mature.ADULT;
    }
}
