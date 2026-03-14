package org.example.printer;


import org.example.model.*;

public class MissionPrinter {

    public static void print(Mission mission) {
        if (mission == null) {
            System.out.println("Ошибка: миссия не загружена.");
            return;
        }

        System.out.println("МИССИЯ: " + mission.getMissionId());
        System.out.println("Дата: " + mission.getDate());
        System.out.println("Место: " + mission.getLocation());
        System.out.println("Статус: " + mission.getOutcome());
        System.out.println("Урон: " + mission.getDamageCost());


        System.out.println("\nПРОКЛЯТИЕ:");
        Curse curse = mission.getCurse();
        if (curse != null) {
            System.out.println("  Имя: " + curse.getName());
            System.out.println("  Уровень угрозы: " + curse.getThreatLevel());
        } else {
            System.out.println("  Нет данных о проклятии");
        }

        System.out.println("\nУЧАСТНИКИ:");
        if (mission.getSorcerers() != null && !mission.getSorcerers().isEmpty()) {
            for (Sorcerer s : mission.getSorcerers()) {
                System.out.println("  - " + s.getName() + " (" + s.getRank() + ")");
            }
        } else {
            System.out.println("  Нет участников");
        }

        System.out.println("\nПРИМЕНЁННЫЕ ТЕХНИКИ:");
        if (mission.getTechniques() != null && !mission.getTechniques().isEmpty()) {
            for (Technique t : mission.getTechniques()) {
                System.out.println("  * " + t.getName() + " [" + t.getType() + "] (владелец: " + t.getOwner() + ") урон: " + t.getDamage());
            }
        } else {
            System.out.println("  Нет техник");
        }

        System.out.println("========================================");
    }
}