package repositories;

import models.Branch;

import java.util.HashMap;
import java.util.Map;

public class BranchRepository {
    private final Map<String, Branch> branchMap;


    public BranchRepository() {
        this.branchMap = new HashMap<>();
    }

    public Branch getBranchById(String id){
        return this.branchMap.get(id);
    }

    public Branch addBranch(Branch branch){
        this.branchMap.put(branch.getId(), branch);
        return branch;
    }
}
