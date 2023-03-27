package com.foxminded;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        LapTimeService timeCounter = new LapTimeService();
        System.out.print(timeCounter.analyzeRace("start.log", "end.log", "abbreviations.txt"));
    }
}
