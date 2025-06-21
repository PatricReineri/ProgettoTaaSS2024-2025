package com.service.guestgameservice.dto;

public class DecisionTreeDTO {
    private TreeNodeDTO root;
    private double accuracy;
    private int totalInstances;
    
    public DecisionTreeDTO() {}
    
    public DecisionTreeDTO(TreeNodeDTO root, double accuracy, int totalInstances) {
        this.root = root;
        this.accuracy = accuracy;
        this.totalInstances = totalInstances;
    }

    public TreeNodeDTO getRoot() { return root; }
    public void setRoot(TreeNodeDTO root) { this.root = root; }

    public double getAccuracy() { return accuracy; }
    public void setAccuracy(double accuracy) { this.accuracy = accuracy; }

    public int getTotalInstances() { return totalInstances; }
    public void setTotalInstances(int totalInstances) { this.totalInstances = totalInstances; }
}
