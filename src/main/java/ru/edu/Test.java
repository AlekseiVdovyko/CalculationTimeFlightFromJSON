package ru.edu;

import java.io.IOException;
import java.text.ParseException;

public class Test {
    public static void main(String[] args) throws IOException, ParseException {
        FlyAverage flyAverage = new FlyAverage("tickets.json");
        flyAverage.getTimeFromJson();
        flyAverage.calculateFlightTime();
        flyAverage.getAverageFlight();
        flyAverage.saveInFile();
    }
}