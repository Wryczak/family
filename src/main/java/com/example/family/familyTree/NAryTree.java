//package com.example.family.familyTree;
//
//import com.example.family.family.Member;
//import com.example.family.services.FamilyService;
//import lombok.Data;
//
//import java.util.ArrayList;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Queue;
//
//@Data
//public class NAryTree {
//    private final FamilyService familyService;
//
//    public static class TreeNode {
//        Long id;
//        List<TreeNode> children = new LinkedList<>();
//
//        TreeNode(Long data) {
//            id = data;
//        }
//
//        TreeNode(Long data, List<TreeNode> child) {
//            id = data;
//            children = child;
//        }
//    }
//
//    public void printNAryTree(TreeNode root) {
//        if (root == null) return;
//        Queue<TreeNode> queue = new LinkedList<>();
//        queue.offer(root);
//        while (!queue.isEmpty()) {
//            int len = queue.size();
//            for (int i = 0; i < len; i++) {
//                TreeNode node = queue.poll();
//                assert node != null;
//                System.out.print(node.id + " ");
//                for (TreeNode item : node.children) {
//                    queue.offer(item);
//                }
//            }
//            System.out.println();
//        }
//    }
//
////    public static void main(String[] args) {
////        // creating an exact replica of the above pictorial N-ary Tree
////        TreeNode root = new TreeNode(1L);
////        root.children.add(new TreeNode(2L));
////        root.children.add(new TreeNode(3L));
////        root.children.add(new TreeNode(4L));
////        root.children.get(0).children.add(new TreeNode(5L));
////        root.children.get(0).children.add(new TreeNode(6L));
////        root.children.get(0).children.add(new TreeNode(7L));
////        root.children.get(1).children.add(new TreeNode(8L));
////        root.children.get(2).children.add(new TreeNode(9L));
////        root.children.get(2).children.add(new TreeNode(10L));
////        root.children.get(2).children.add(new TreeNode(11L));
////        root.children.get(2).children.add(new TreeNode(11L));
////        root.children.get(2).children.get(3).children.add(new TreeNode(5L));
////        root.children.get(2).children.get(3).children.get(0).children.add(new TreeNode(577L));
////        printNAryTree(root);
////    }
//
//
////    TreeNode root = new TreeNode(id);
////    List<Long>children= childrenForEach(id);
////
////        for (Long childId:children) {
////        root.children.add(new TreeNode(childId));
//
////    }
//
//    public void MyTreeImplement(Long id) {
//        TreeNode root = new TreeNode(id);
//        List<Long> children = childrenForEach(id);
//
//        for (Long childId : children) {
//            root.children.add(new TreeNode(childId));
//
//        }
//        printNAryTree(root);
//    }
//
//    public List<Long> childrenForEach(Long id) {
//        List<Member> list = familyService.getFamily().getMembers();
//        List<Long> tempList = new ArrayList<>();
//        System.out.println(familyService.getMemberByIdFromFamily(id));
//        for (Member member : list) {
//            if (member.getFatherId() != null && member.getFatherId().equals(id)) {
//                tempList.add(member.getId());
//            }
//        }
//        if (tempList.isEmpty()) {
//            System.out.println("Nie ma dzieci");
//            return null;
//        }
//        return tempList;
//    }
//}
