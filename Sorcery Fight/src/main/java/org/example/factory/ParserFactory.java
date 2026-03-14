package org.example.factory;

import org.example.parsers.*;
import java.io.File;

public class ParserFactory {
    public static MissionParser getParser(File file) throws IllegalArgumentException {
        String fileName = file.getName().toLowerCase();
        if (fileName.endsWith(".json")) {
            return new JsonParser();
        } else if (fileName.endsWith(".xml")) {
            return new XmlParser();
        } else if (fileName.endsWith(".txt")) {
            return new TxtParser();
        } else {
            throw new IllegalArgumentException("Unsupported file format: " + file.getName());
        }
    }
}
