package org.example.model;

public class Technique {
    private String name;
    private TechniqueType type;
    private String owner;
    private long damage;

    public Technique() {}

    public String getName() { 
        return name; 
    }
    
    public void setName(String name) { 
        this.name = name; 
    }
    
    public TechniqueType getType() {
        return type; 
    }
    
    public void setType(TechniqueType type) {
        this.type = type; 
    }
    
    public String getOwner() {
        return owner; 
    }
    
    public void setOwner(String owner) {
        this.owner = owner; 
    }
    
    public long getDamage() {
        return damage; 
    }
    
    public void setDamage(long damage) {
        this.damage = damage; 
    }
}
