package com.example.family.MainObjectsFamilyMemberDto;

import lombok.Data;

@Data
public class Details {
    private Long id;
    private Long secondId;
    private Long idToFind;
    private String text;
    private String secondText;
    private String thirdText;
    private boolean status;
    private boolean secondStatus;
}
