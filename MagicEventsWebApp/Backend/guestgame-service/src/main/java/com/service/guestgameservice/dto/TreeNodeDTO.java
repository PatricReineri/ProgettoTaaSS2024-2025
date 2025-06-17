package com.service.guestgameservice.dto;

import lombok.Data;

@Data
public class TreeNodeDTO {
    private String splitFeatureQuestion;
    private TreeNodeDTO leftNode;
    private TreeNodeDTO rightNode;
    
    public TreeNodeDTO(String splitFeatureQuestion) {
        this.splitFeatureQuestion = splitFeatureQuestion;
    }
    
    public TreeNodeDTO(String splitFeatureQuestion, TreeNodeDTO leftNode, TreeNodeDTO rightNode) {
        this.splitFeatureQuestion = splitFeatureQuestion;
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }
}
