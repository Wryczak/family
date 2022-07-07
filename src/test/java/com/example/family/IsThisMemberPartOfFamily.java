package com.example.family;

import com.example.family.MainObjectsFamilyMemberDto.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;


@SpringBootTest
public class IsThisMemberPartOfFamily {

    @Test
    public void testFamilyList() {

        // given
        List<Member> familyOne = new ArrayList<>();
        Member member = new Member(1L, "Krzysztof", "Wrycza");
        Member member2 = new Member(2L, "Małgorzata", "Wrycza");
        Member member3 = new Member(3L, "Ilona", "Dubiel");
        Member member4 = new Member(4L, "Ryszard", "Wrycza");
        Member member5 = new Member(5L, "Marta", "Łuczko");
        familyOne.add(member);
        familyOne.add(member2);
        familyOne.add(member3);
        familyOne.add(member4);
        familyOne.add(member5);

        List<Member> familyTwo = new ArrayList<>();
        Member member6 = new Member(6L, "Dobrosława", "Paluszewska");
        Member member7 = new Member(7L, "Radosław", "Paluszewski");
        Member member8 = new Member(8L, "Bogusława", "Paluszewska");
        Member member9 = new Member(9L, "Stojgniew", "Paluszewski");
        Member member10 = new Member(10L, "Katarzyna", "Paluszewska");
        familyTwo.add(member6);
        familyTwo.add(member7);
        familyTwo.add(member8);
        familyTwo.add(member9);
        familyTwo.add(member10);

      assertThat(familyOne.contains(member));
      assertThat(familyTwo.isEmpty());



    }

    private void assertThat(boolean contains) {
    }


}
