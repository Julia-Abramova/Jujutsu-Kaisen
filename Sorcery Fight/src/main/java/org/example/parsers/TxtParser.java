package org.example.parsers;

import org.example.model.*;
import java.io.*;
import java.util.*;

public class TxtParser implements MissionParser {

    @Override
    public Mission parse(String file) throws IOException {
        Map<String, String> keyValues = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                String[] parts = line.split(":", 2);
                if (parts.length < 2) {
                    throw new IOException("Invalid line format: " + line);
                }
                String key = parts[0].trim();
                String value = parts[1].trim();
                keyValues.put(key, value);
            }
        }

        Mission mission = new Mission();
        mission.setMissionId(getRequired(keyValues, "missionId"));
        mission.setDate(getRequired(keyValues, "date"));
        mission.setLocation(getRequired(keyValues, "location"));
        mission.setOutcome(Outcome.valueOf(getRequired(keyValues, "outcome")));
        mission.setDamageCost(Long.parseLong(getRequired(keyValues, "damageCost")));

        Curse curse = new Curse();
        curse.setName(getRequired(keyValues, "curse.name"));
        curse.setThreatLevel(ThreatLevel.valueOf(getRequired(keyValues, "curse.threatLevel")));
        mission.setCurse(curse);

        Map<Integer, Sorcerer> sorcererMap = new TreeMap<>();
        for (Map.Entry<String, String> entry : keyValues.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith("sorcerer[")) {
                int idx = extractIndex(key);
                Sorcerer s = sorcererMap.computeIfAbsent(idx, k -> new Sorcerer());
                if (key.endsWith(".name")) {
                    s.setName(entry.getValue());
                } else if (key.endsWith(".rank")) {
                    s.setRank(Rank.valueOf(entry.getValue()));
                }
            }
        }
        mission.setSorcerers(new ArrayList<>(sorcererMap.values()));


        Map<Integer, Technique> techniqueMap = new TreeMap<>();
        for (Map.Entry<String, String> entry : keyValues.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith("technique[")) {
                int idx = extractIndex(key);
                Technique t = techniqueMap.computeIfAbsent(idx, k -> new Technique());
                if (key.endsWith(".name")) {
                    t.setName(entry.getValue());
                } else if (key.endsWith(".type")) {
                    t.setType(TechniqueType.valueOf(entry.getValue()));
                } else if (key.endsWith(".owner")) {
                    t.setOwner(entry.getValue());
                } else if (key.endsWith(".damage")) {
                    t.setDamage(Long.parseLong(entry.getValue()));
                }
            }
        }
        mission.setTechniques(new ArrayList<>(techniqueMap.values()));

        return mission;
    }

    private String getRequired(Map<String, String> map, String key) throws IOException {
        String value = map.get(key);
        if (value == null) {
            throw new IOException("Missing required key: " + key);
        }
        return value;
    }

    private int extractIndex(String key) {
        int start = key.indexOf('[') + 1;
        int end = key.indexOf(']', start);
        return Integer.parseInt(key.substring(start, end));
    }
}