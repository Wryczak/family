package com.example.family.family;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class Details {
    private Long id;
    private String text;
    private String secondText;
    private String thirdText;
    private boolean status;
    private boolean secondStatus;
}
