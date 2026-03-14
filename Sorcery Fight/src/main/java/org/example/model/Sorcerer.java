package org.example.model;

public class Sorcerer {
    private String name;
    private Rank rank;

    public Sorcerer() {}
    
    public String getName() { 
        return name; 
    }
    
    public void setName(String name) { 
        this.name = name; 
    }
    
    public Rank getRank() {
        return rank; 
    }
    
    public void setRank(Rank rank) {
        this.rank = rank; 
    }
}
