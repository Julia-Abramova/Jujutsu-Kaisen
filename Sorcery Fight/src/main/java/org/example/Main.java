package org.example;

import org.example.factory.ParserFactory;
import org.example.model.*;
import org.example.parsers.MissionParser;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import org.example.printer.MissionPrinter;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter path to mission file: ");
        String path = scanner.nextLine();
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("File not found: " + path);
            return;
        }

        MissionParser parser = ParserFactory.getParser(file);
        Mission mission = parser.parse(path);
        MissionPrinter.print(mission);
    }
}