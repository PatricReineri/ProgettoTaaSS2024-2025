package com.service.guestgameservice.dto;

public class TreeNodeDTO {
    private String splitFeatureQuestion;
    private TreeNodeDTO leftNode;
    private TreeNodeDTO rightNode;
    
    public TreeNodeDTO() {}
    
    public TreeNodeDTO(String splitFeatureQuestion) {
        this.splitFeatureQuestion = splitFeatureQuestion;
    }
    
    public TreeNodeDTO(String splitFeatureQuestion, TreeNodeDTO leftNode, TreeNodeDTO rightNode) {
        this.splitFeatureQuestion = splitFeatureQuestion;
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }

    public String getSplitFeatureQuestion() { return splitFeatureQuestion; }
    public void setSplitFeatureQuestion(String splitFeatureQuestion) { this.splitFeatureQuestion = splitFeatureQuestion; }

    public TreeNodeDTO getLeftNode() { return leftNode; }
    public void setLeftNode(TreeNodeDTO leftNode) { this.leftNode = leftNode; }

    public TreeNodeDTO getRightNode() { return rightNode; }
    public void setRightNode(TreeNodeDTO rightNode) { this.rightNode = rightNode; }
}
