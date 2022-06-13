package com.example.family.Interfaces;

import java.time.LocalDate;
import java.time.Period;

public interface AgeCalculator {
   default Integer calculateAge(String birthday){
            LocalDate today = LocalDate.now();
            Period p = Period.between(LocalDate.parse(birthday), today);
            return p.getYears();
        }
}
