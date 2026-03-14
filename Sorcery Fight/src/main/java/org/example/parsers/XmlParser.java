package org.example.parsers;

import org.example.model.*;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XmlParser implements MissionParser {

    @Override
    public Mission parse(String filePath) throws IOException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        try (FileInputStream fis = new FileInputStream(filePath)) {
            XMLEventReader reader = factory.createXMLEventReader(fis);
            return parseMission(reader);
        } catch (XMLStreamException e) {
            throw new IOException("XML parsing error", e);
        }
}

    private Mission parseMission(XMLEventReader reader) throws XMLStreamException {
        Mission mission = new Mission();
        Curse curse = null;
        List<Sorcerer> sorcerers = null;
        List<Technique> techniques = null;
        Sorcerer currentSorcerer = null;
        Technique currentTechnique = null;
        String currentElement = "";

        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();

            if (event.isStartElement()) {
                StartElement start = event.asStartElement();
                String localName = start.getName().getLocalPart();

                switch (localName) {
                    case "mission":
                        break;
                    case "missionId":
                        currentElement = "missionId";
                        break;
                    case "date":
                        currentElement = "date";
                        break;
                    case "location":
                        currentElement = "location";
                        break;
                    case "outcome":
                        currentElement = "outcome";
                        break;
                    case "damageCost":
                        currentElement = "damageCost";
                        break;
                    case "curse":
                        curse = new Curse();
                        break;
                    case "name":
                        if (curse != null) currentElement = "curse.name";
                        else if (currentSorcerer != null) currentElement = "sorcerer.name";
                        else if (currentTechnique != null) currentElement = "technique.name";
                        break;
                    case "threatLevel":
                        currentElement = "curse.threatLevel";
                        break;
                    case "sorcerers":
                        sorcerers = new ArrayList<>();
                        break;
                    case "sorcerer":
                        currentSorcerer = new Sorcerer();
                        break;
                    case "rank":
                        if (currentSorcerer != null) currentElement = "sorcerer.rank";
                        break;
                    case "techniques":
                        techniques = new ArrayList<>();
                        break;
                    case "technique":
                        currentTechnique = new Technique();
                        break;
                    case "type":
                        if (currentTechnique != null) currentElement = "technique.type";
                        break;
                    case "owner":
                        if (currentTechnique != null) currentElement = "technique.owner";
                        break;
                    case "damage":
                        if (currentTechnique != null) currentElement = "technique.damage";
                        break;
                }
            } else if (event.isCharacters()) {
                String data = event.asCharacters().getData().trim();
                if (data.isEmpty()) continue;

                switch (currentElement) {
                    case "missionId":
                        mission.setMissionId(data);
                        break;
                    case "date":
                        mission.setDate(data);
                        break;
                    case "location":
                        mission.setLocation(data);
                        break;
                    case "outcome":
                        mission.setOutcome(Outcome.valueOf(data));
                        break;
                    case "damageCost":
                        mission.setDamageCost(Long.parseLong(data));
                        break;
                    case "curse.name":
                        if (curse != null) curse.setName(data);
                        break;
                    case "curse.threatLevel":
                        if (curse != null) curse.setThreatLevel(ThreatLevel.valueOf(data));
                        break;
                    case "sorcerer.name":
                        if (currentSorcerer != null) currentSorcerer.setName(data);
                        break;
                    case "sorcerer.rank":
                        if (currentSorcerer != null) currentSorcerer.setRank(Rank.valueOf(data));
                        break;
                    case "technique.name":
                        if (currentTechnique != null) currentTechnique.setName(data);
                        break;
                    case "technique.type":
                        if (currentTechnique != null) currentTechnique.setType(TechniqueType.valueOf(data));
                        break;
                    case "technique.owner":
                        if (currentTechnique != null) currentTechnique.setOwner(data);
                        break;
                    case "technique.damage":
                        if (currentTechnique != null) currentTechnique.setDamage(Long.parseLong(data));
                        break;
                }
                currentElement = ""; 
            } else if (event.isEndElement()) {
                EndElement end = event.asEndElement();
                String localName = end.getName().getLocalPart();

                switch (localName) {
                    case "curse":
                        mission.setCurse(curse);
                        curse = null;
                        break;
                    case "sorcerer":
                        if (sorcerers != null && currentSorcerer != null) {
                            sorcerers.add(currentSorcerer);
                            currentSorcerer = null;
                        }
                        break;
                    case "sorcerers":
                        mission.setSorcerers(sorcerers);
                        break;
                    case "technique":
                        if (techniques != null && currentTechnique != null) {
                            techniques.add(currentTechnique);
                            currentTechnique = null;
                        }
                        break;
                    case "techniques":
                        mission.setTechniques(techniques);
                        break;
                }
            }
        }
        return mission;
    }
}
