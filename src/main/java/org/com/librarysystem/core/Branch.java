package org.com.librarysystem.core;

import java.util.Map;

public class Branch {
    private String branchId;
    private String name;
    // Each branch manages its own physical inventory
    private Map<String, BookItem> branchInventory;

    // ... Constructors, Methods to add/remove BookItems ...

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, BookItem> getBranchInventory() {
        return branchInventory;
    }

    public void setBranchInventory(Map<String, BookItem> branchInventory) {
        this.branchInventory = branchInventory;
    }
}
