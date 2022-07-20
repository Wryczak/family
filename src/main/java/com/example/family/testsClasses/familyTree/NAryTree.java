package com.example.family.testsClasses.familyTree;

import com.example.family.services.FamilyService;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Data
public class NAryTree {
    private final FamilyService familyService;

    public static class TreeNode {
        Long id;
        List<TreeNode> children = new LinkedList<>();

        TreeNode(Long data) {
            id = data;
        }

        TreeNode(Long data, List<TreeNode> child) {
            id = data;
            children = child;
        }
    }

    public void printNAryTree(TreeNode root) {
        if (root == null) return;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int len = queue.size();
            for (int i = 0; i < len; i++) {
                TreeNode node = queue.poll();
                assert node != null;
                for (TreeNode item : node.children) {
                    queue.offer(item);
                }
            }
            System.out.println();
        }
    }

//    public void MyTreeImplement(Long id) {
//        TreeNode root = getTreeNodeRoot(id);
//        printNAryTree(root);
//    }

//    private TreeNode getTreeNodeRoot(Long id) {
//        TreeNode root = new TreeNode(id);
//        List<Long> children = familyService.getChildrenForEachGeneration(id);
//        for (Long childId : children) {
//            root.children.add(new TreeNode(childId));
//            }
//
//        return root;
//    }
//    private void getTreeNodeChildren(TreeNode root,Long id) {
//        TreeNode children = new TreeNode(id);
//        List<Long> childrenList = familyService.childrenForEach(id);
//
//        for (Long childId : childrenList) {
//            root.children.children.add(new TreeNode(childId));
//    }
}