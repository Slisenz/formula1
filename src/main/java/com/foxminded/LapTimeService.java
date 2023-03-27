package com.foxminded;

import java.io.*;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class LapTimeService {

    private static final int ABBREVIATIONS_LENGTH = 3;
    private static final int SIZE_OF_WINNERS_TABLE = 15;
    private static final String FORMULA_FOR_SIZE_LINE = "%-27s";
    private static final String INPUT_DATE_FORMAT = "yyyy-MM-dd_HH:mm:ss.SSS";
    private static final String DESERVE_DATE_FORMAT = "mm:ss.SSS";
    private static final String NEW_LINE_SYMBOL = "\n";
    private static final String EXISTING_DELIMITER = "_";
    private static final String DESIRED_DELIMITER = "|";


    public String analyzeRace(String startLogFileName, String endLogFileName, String abbreviationsFileName) throws IOException {
        return distributeParameter(parseByName(startLogFileName), parseByName(endLogFileName), parseByName(abbreviationsFileName));
    }
    public String analyzeRace(File start, File end, File abbreviations) {
        return distributeParameter(readFile(start), readFile(end), readFile(abbreviations));
    }

    private List<String> parseByName(String fileName) throws IOException{
        if (fileName == null) {
            throw new IllegalArgumentException();
        }
        URL resourceUrl = getClass().getClassLoader().getResource(fileName);
        if (resourceUrl == null) {
            throw new FileNotFoundException(fileName + " file not found in resource folder");
        }
        return readFile(new File (resourceUrl.getFile()));
    }

    private List<String> readFile(File file) {
        List<String> result;
        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            result = bufferedReader.lines().collect(Collectors.toList());
        } catch (IOException exception) {
            throw new IllegalArgumentException(" Reading (" + file.getName() + ") failed!");
        }
        return result;
    }

    private String distributeParameter(List<String> startTime, List<String> endTime, List<String> abbreviations) {
        List<Racer> racersList = new ArrayList<>();
        Collections.sort(startTime);
        Collections.sort(endTime);
        Collections.sort(abbreviations);
        for (int i = 0; i < startTime.size(); i++) {
            Racer racer = new Racer();
            String nameAndModelCar = abbreviations.get(i).substring(ABBREVIATIONS_LENGTH + EXISTING_DELIMITER.length());
            racer.setName(nameAndModelCar.substring(0, nameAndModelCar.indexOf(EXISTING_DELIMITER)));
            racer.setLapTime(countLapTime(startTime.get(i), endTime.get(i)));
            racer.setCarModel(nameAndModelCar.substring(nameAndModelCar.indexOf(EXISTING_DELIMITER) + EXISTING_DELIMITER.length()));
            racersList.add(racer);
        }
        Collections.sort(racersList);
        return createTable(racersList).toString();
    }

    private StringBuilder createTable(List<Racer> racersList) {
        SimpleDateFormat outputFormat = new SimpleDateFormat(DESERVE_DATE_FORMAT);
        StringBuilder result = new StringBuilder();
        for (int counter = 0; counter < racersList.size(); counter++) {
            String pilotName = String.format(FORMULA_FOR_SIZE_LINE, counter + 1 + DESIRED_DELIMITER + racersList.get(counter).getName());
            String pilotCar = String.format(FORMULA_FOR_SIZE_LINE, DESIRED_DELIMITER + racersList.get(counter).getCarModel());
            if (counter == SIZE_OF_WINNERS_TABLE) {
                result.append(Stream.generate(() -> String.valueOf('-')).limit(result.indexOf(NEW_LINE_SYMBOL)).collect(Collectors.joining())).append(NEW_LINE_SYMBOL);
            }
            result.append(pilotName).append(pilotCar).append(DESIRED_DELIMITER).append(outputFormat.format(racersList.get(counter).getLapTime())).append(NEW_LINE_SYMBOL);
        }
        return result;
    }

    private long countLapTime(String startInput, String endInput) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(INPUT_DATE_FORMAT);
        Date startLapTime;
        Date endLapTime;
        try {
            startLapTime = inputFormat.parse(startInput.substring(ABBREVIATIONS_LENGTH));
            endLapTime = inputFormat.parse(endInput.substring(ABBREVIATIONS_LENGTH));
        } catch (ParseException exception) {
            throw new IllegalArgumentException(" wrong date format " + exception.getLocalizedMessage());
        }
        return endLapTime.getTime() - startLapTime.getTime();
    }

}
