package com.service.guestgameservice.dto;
import lombok.Data;

@Data
public class DecisionTreeDTO {
    private TreeNodeDTO root;
    private double accuracy;
    private int totalInstances;
    
    public DecisionTreeDTO(TreeNodeDTO root, double accuracy, int totalInstances) {
        this.root = root;
        this.accuracy = accuracy;
        this.totalInstances = totalInstances;
    }
}
