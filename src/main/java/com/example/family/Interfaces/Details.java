package com.example.family.Interfaces;

import com.example.family.family.Member;
import lombok.Data;

import java.util.List;

@Data
public class Details {
    private Long id;
    private String text;
    private String secondText;
    private String thirdText;
    private boolean status;
    private boolean secondStatus;
    private List<Member> members;
}
