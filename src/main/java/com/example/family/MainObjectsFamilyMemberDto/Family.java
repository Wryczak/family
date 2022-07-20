package com.example.family.MainObjectsFamilyMemberDto;

import com.example.family.security.User;
import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Family {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private User user;

    @NotNull
    @Size(min = 2, message = "Nazwisko musi mieć co najmniej 2 znaki!.")
    private String familyName;
    @ManyToMany(targetEntity = Member.class)
    private List<Member> members = new ArrayList<>();

    public void addFamilyMember(Member member) {
        this.members.add(member);
    }

    public void deleteFamilyMember(Member member) {
        this.members.remove(member);
    }

}
