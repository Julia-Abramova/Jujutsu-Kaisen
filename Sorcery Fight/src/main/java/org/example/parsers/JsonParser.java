package org.example.parsers;

import org.example.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JsonParser implements MissionParser {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Mission parse(String filePath) throws IOException {
        Mission missionJson = mapper.readValue(new File(filePath), Mission.class);
        return convert(missionJson);
    }

    private Mission convert(Mission json) {
        Mission mission = new Mission();

        mission.setMissionId(json.getMissionId());
        mission.setDate(json.getDate());
        mission.setLocation(json.getLocation());
        mission.setOutcome(json.getOutcome());
        mission.setDamageCost(json.getDamageCost());

        Curse curse = new Curse();
        curse.setName(json.getCurse().getName());
        curse.setThreatLevel(json.getCurse().getThreatLevel());
        mission.setCurse(curse);

        List<Sorcerer> sorcerers = json.getSorcerers().stream()
                .map(s -> {
                    Sorcerer sorc = new Sorcerer();
                    sorc.setName(s.getName());
                    sorc.setRank(s.getRank());
                    return sorc;
                })
                .collect(Collectors.toList());
        mission.setSorcerers(sorcerers);

        List<Technique> techniques = json.getTechniques().stream()
                .map(t -> {
                    Technique tech = new Technique();
                    tech.setName(t.getName());
                    tech.setType(t.getType());
                    tech.setOwner(t.getOwner());
                    tech.setDamage(t.getDamage());
                    return tech;
                })
                .collect(Collectors.toList());
        mission.setTechniques(techniques);

        return mission;
    }
}