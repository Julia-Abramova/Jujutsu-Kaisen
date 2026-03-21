package org.example.parsers;

import org.example.model.*;
import java.io.*;
import java.util.*;

public class TxtParser implements MissionParser {

    @Override
    public Mission parse(String filePath) throws IOException {
        Map<String, String> keyValueMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                String[] parts = line.split(":", 2);
                if (parts.length < 2) {
                    throw new IOException("Invalid line format: " + line);
                }
                String key = parts[0].trim();
                String value = parts[1].trim();
                keyValueMap.put(key, value);
            }
        }

        Mission mission = new Mission();
        mission.setMissionId(getRequired(keyValueMap, "missionId"));
        mission.setDate(getRequired(keyValueMap, "date"));
        mission.setLocation(getRequired(keyValueMap, "location"));
        mission.setOutcome(Outcome.valueOf(getRequired(keyValueMap, "outcome")));
        mission.setDamageCost(Long.parseLong(getRequired(keyValueMap, "damageCost")));

        Curse curse = new Curse();
        curse.setName(getRequired(keyValueMap, "curse.name"));
        curse.setThreatLevel(ThreatLevel.valueOf(getRequired(keyValueMap, "curse.threatLevel")));
        mission.setCurse(curse);

        Map<Integer, Sorcerer> sorcererMap = new TreeMap<>();
        for (Map.Entry<String, String> entry : keyValueMap.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith("sorcerer[")) {
                int index = extractIndex(key);
                Sorcerer sorcerer = sorcererMap.get(index);
                if (sorcerer == null) {
                    sorcerer = new Sorcerer();
                    sorcererMap.put(index, sorcerer);
                }
                if (key.endsWith(".name")) {
                    sorcerer.setName(entry.getValue());
                } else if (key.endsWith(".rank")) {
                    sorcerer.setRank(Rank.valueOf(entry.getValue()));
                }
            }
        }
        mission.setSorcerers(new ArrayList<>(sorcererMap.values()));

        Map<Integer, Technique> techniqueMap = new TreeMap<>();
        for (Map.Entry<String, String> entry : keyValueMap.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith("technique[")) {
                int index = extractIndex(key);
                Technique technique = techniqueMap.get(index);
                if (technique == null) {
                    technique = new Technique();
                    techniqueMap.put(index, technique);
                }
                if (key.endsWith(".name")) {
                    technique.setName(entry.getValue());
                } else if (key.endsWith(".type")) {
                    technique.setType(TechniqueType.valueOf(entry.getValue()));
                } else if (key.endsWith(".owner")) {
                    technique.setOwner(entry.getValue());
                } else if (key.endsWith(".damage")) {
                    technique.setDamage(Long.parseLong(entry.getValue()));
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