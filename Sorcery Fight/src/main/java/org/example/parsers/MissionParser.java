package org.example.parsers;

import org.example.model.Mission;
import java.io.File;
import java.io.IOException;

public interface MissionParser {
    Mission parse(String file) throws IOException;
}