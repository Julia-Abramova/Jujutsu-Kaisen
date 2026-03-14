package org.example.model;

public class Curse {
    public String name;
    public ThreatLevel threatLevel;

    public Curse() {}

    public String getName() {
        return name; 
    }
    public void setName(String name) {
        this.name = name;
    }
    public ThreatLevel getThreatLevel() { 
        return threatLevel; 
    }
    public void setThreatLevel(ThreatLevel threatLevel) {
        this.threatLevel = threatLevel; 
    }
}