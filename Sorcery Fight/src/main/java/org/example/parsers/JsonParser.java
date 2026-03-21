package org.example.parsers;

import org.example.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonParser implements MissionParser {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Mission parse(String filePath) throws IOException {
        Mission missionFromJson = mapper.readValue(new File(filePath), Mission.class);
        return convert(missionFromJson);
    }

    private Mission convert(Mission jsonMission) {
        Mission mission = new Mission();
        mission.setMissionId(jsonMission.getMissionId());
        mission.setDate(jsonMission.getDate());
        mission.setLocation(jsonMission.getLocation());
        mission.setOutcome(jsonMission.getOutcome());
        mission.setDamageCost(jsonMission.getDamageCost());

        Curse curse = new Curse();
        curse.setName(jsonMission.getCurse().getName());
        curse.setThreatLevel(jsonMission.getCurse().getThreatLevel());
        mission.setCurse(curse);

        List<Sorcerer> jsonSorcerers = jsonMission.getSorcerers();
        List<Sorcerer> convertedSorcerers = new ArrayList<>();

        for (Sorcerer jsonSorcerer : jsonSorcerers) {
            Sorcerer sorcerer = new Sorcerer();
            sorcerer.setName(jsonSorcerer.getName());
            sorcerer.setRank(jsonSorcerer.getRank());
            convertedSorcerers.add(sorcerer);
        }
        mission.setSorcerers(convertedSorcerers);

        List<Technique> jsonTechniques = jsonMission.getTechniques();
        List<Technique> convertedTechniques = new ArrayList<>();

        for (Technique jsonTechnique : jsonTechniques) {
            Technique technique = new Technique();
            technique.setName(jsonTechnique.getName());
            technique.setType(jsonTechnique.getType());
            technique.setOwner(jsonTechnique.getOwner());
            technique.setDamage(jsonTechnique.getDamage());
            convertedTechniques.add(technique);
        }
        mission.setTechniques(convertedTechniques);
        return mission;
    }
}