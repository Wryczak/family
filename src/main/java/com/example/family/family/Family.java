package com.example.family.family;

import com.example.family.security.User;
import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "family")
public class Family implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User user;

    @NotNull
    @Size(min = 2, message = "Nazwisko musi mieć co najmniej 2 znaki!.")
    private String familyName;

    private int nrOfInfants;

    private int nrOfChildren;

    private int nrOfAdults;


    @ManyToMany(targetEntity = Member.class)
    @Size(min = 1, message = "Rodzina musi mieć co najmniej jednego członka.")
    private List<Member> members = new ArrayList<>();


    public void addFamilyMember(Member member) {
        this.members.add(member);
    }

    public int getInfants() {
        int infantsNo=0;
        for (Member member : members) {
            if (member.getMature() == Member.Mature.INFANT) {
                infantsNo++;
            }
        }
        return infantsNo;
    }

    public int getChildren() {
        int childrenNo=0;
        for (Member member : members) {
            if (member.getMature() == Member.Mature.CHILD) {
                childrenNo++;

            }
        }
        return childrenNo;
    }

    public int getAdults() {
        int adultsNo=0;
        for (Member member : members) {
            if (member.getMature() == Member.Mature.ADULT) {
                adultsNo++;
            }
        }
        return adultsNo;
    }
}
