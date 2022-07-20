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

//import java.util.LinkedList;
//import java.util.List;
//import java.util.Queue;
//
//public class NAryTree {
//    public static class TreeNode{
//        int val;
//        List<TreeNode> children = new LinkedList<>();
//
//        TreeNode(int data){
//            val = data;
//        }
//
//        TreeNode(int data,List<TreeNode> child){
//            val = data;
//            children = child;
//        }
//    }
//
//    private static void printNAryTree(TreeNode root){
//        if(root == null) return;
//        Queue<TreeNode> queue = new LinkedList<>();
//        queue.offer(root);
//        while(!queue.isEmpty()) {
//            int len = queue.size();
//            for(int i=0;i<len;i++) {
//                TreeNode node = queue.poll();
//                assert node != null;
//                System.out.print(node.val + " ");
//                for (TreeNode item : node.children) {
//                    queue.offer(item);
//                }
//            }
//            System.out.println();
//        }
//    }
//
//    public static void main(String[] args) {
//        TreeNode root = new TreeNode(1);
//        root.children.add(new TreeNode(2));
//        root.children.add(new TreeNode(3));
//        root.children.add(new TreeNode(4));
//        root.children.get(0).children.add(new TreeNode(5));
//        root.children.get(0).children.add(new TreeNode(6));
//        root.children.get(0).children.add(new TreeNode(7));
//        root.children.get(1).children.add(new TreeNode(8));
//        root.children.get(2).children.add(new TreeNode(9));
//        root.children.get(2).children.add(new TreeNode(10));
//        root.children.get(2).children.add(new TreeNode(11));
//        printNAryTree(root);
//    }
//}


//public List<Long> listOfParents(){
//        return membersIdList;
//        }
//
//public List<Long> getParentTree2(Long id) {
//        if (id != 0) {
//        Member member = getMemberByIdFromFamily(id);
//        membersIdList.add(member.getId());
//        if (member.getFatherId() == null) {
//        return membersIdList;
//        }
//        return getParentTree2(member.getFatherId());
//        }
//        return null;
//        }

//public List<Long> childrenForEach(Long id){
//        List<Member> list = getFamily().getMembers();
//        List<Long> tempList = new ArrayList<>();
//        System.out.println(getMemberByIdFromFamily(id));
//        for (Member member : list) {
//        if (member.getFatherId() != null && member.getFatherId().equals(id)) {
//        tempList.add(member.getId());
//        }
//        }
//        if (tempList.isEmpty()){
//        System.out.println("Nie ma dzieci");
//        System.out.println();
//        System.out.println();
//        System.out.println("-----------------");
//        return null;
//        }
//        for (Long childrenId:tempList){
//        childrenForEach(childrenId);
//
//        }
//        System.out.println(tempList);
//        System.out.println();
//        System.out.println();
//        System.out.println("-----------------");
//        return tempList;
//        }
//
//public void listwithall(Long id){
//        List<Long> list= getParentTree2(id);
//        for (Long memberId: list){
//        childrenForEach(memberId);
//        }
//        }
//        }


//    public  void getChildrenForEachGeneration(Long id) {
//        List<Long> tempList = getAllChildrenIdList(id);
//        for (Long childrenId : tempList) {
//            getChildrenForEachGeneration(childrenId);
//        }
////        if (tempList.isEmpty()){
////            System.out.println( "id "+id +"  nie ma dzieci");
////        }else
////        System.out.println( "id "+id +"  jest ojcem: "+"  "+tempList);
//        if (tempList.isEmpty()) {
//            return;
//        }
//    }

//        List<Long> ancestors = new ArrayList<>(familyService.getParentTree(idToFind));
//        ancestors.stream().distinct();
//        Details rel=new Details();
//        String rela=(String.valueOf(ancestors));
//        System.out.println(rela);
//        String rela2="Ojciec: "+rela.replaceAll(",","--->");
//        rela2=rela2.replaceAll("\\[","").replaceAll("]","");
//        rel.setText(rela2);
//        System.out.println(rela2);
//        model.addAttribute("rel",rel);
//
//        System.out.println(ancestors);


