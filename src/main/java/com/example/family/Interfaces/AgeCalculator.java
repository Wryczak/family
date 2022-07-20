package com.example.family.Interfaces;

import java.time.LocalDate;
import java.time.Period;

public interface AgeCalculator {
   default Integer calculateAge(LocalDate birthday){
            LocalDate today = LocalDate.now();

            Period p = Period.between(birthday, today);
            return p.getYears();
        }

}
