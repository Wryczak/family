//package com.example.family.family;
//
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import static java.util.function.Function.identity;
//
//public class Brudnopiszmetodami {
//
//    public Map<Long, Member> convertListToMap() {
//        List<Member> list = getFamily().getMembers();
//        return list.stream()
//                .collect(Collectors.toMap(Member::getId, identity()));
//    }
//}

//
//public Long childrenListGetter(Long id) {
//        if (id != 0) {
//
//        if (convertListToMapByFatherId().containsKey(id)) {
//        Member member = convertListToMapByFatherId().get(id);
//        System.out.println(member);
//        System.out.println();
//        id = member.getId();
//        return childrenListGetter(id);
//        }
//        }
//        return 0L;
//        }
//
//public Map<Long, Member> convertListToMapByFatherId() {
//        List<Member> list = getFamily().getMembers();
//        List<Member> tempList = new ArrayList<>();
//        for (Member member : list) {
//        if (member.getFatherId() != null) {
//        tempList.add(member);
//        }
//        }
//        return tempList.stream()
//        .collect(Collectors.toMap(Member::getFatherId, identity()));
//        }

//public void getParentTree(Long id) {
//        if (id != 0) {
//        Member member = getMemberByIdFromFamily(id);
//        if (member.getFatherId() == null) {
//        return;
//        }
//        System.out.println("---------------------");
//        System.out.println();
//        System.out.print(member.getName()+ " "+ member.getFamilyName()+ " "+" ID "+ member.getId() +" ");
//        getAllChildren(id);
//        System.out.print("Ojciec: ");
//        getParentTree(member.getFatherId());
//        }
//        return;
//        }

//    public List<Member> getAllChildren(Long id) {
//        List<Long> list = getParentTree2(id);
//        List<Member> tempList = new ArrayList<>();
//        for (Member member : list) {
//            if (member.getFatherId() != null && member.getFatherId().equals(id)) {
//                tempList.add(member);
//            }
//        }
//            if (tempList.isEmpty()){
//                System.out.println("Nie ma dzieci");
//                return null;
//            }
//            System.out.print("Dzieci: ");
//        System.out.println(tempList);
//        return tempList;
//    }


//public List<Member> getAllChildren2(Long id) {
//        List<Member> list = getFamily().getMembers();
//        List<Member> tempList = new ArrayList<>();
//        Map<Long,List<Member>> tester=new HashMap<>();
//        for (Member member : list) {
//        if (member.getFatherId() != null && member.getFatherId().equals(id)) {
//        tempList.add(member);
//
//        }
//        }
//        if (tempList.isEmpty()){
//        System.out.println("Nie ma dzieci");
//        return null;
//        }
//        tester.put(id,tempList);
//        System.out.println("Dzieci:");
//        System.out.println(tester);
//        return tempList;
//        }