package ru.edu;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.*;

public class FlyAverage {
    private String filePath;
    private String fileName;
    private List<String> listDeparture = new ArrayList<>();
    private List<String> listArrival = new ArrayList<>();
    private List<Long> listMilliseconds = new ArrayList<>();
    private List<String> listTimeFly = new ArrayList<>();
    private List<String> timeBetween = new ArrayList<>();
    private List<String> timeAverage = new ArrayList<>();
    long averageFlyBetweenCity;
    long percentFlight;


    public FlyAverage(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public List<String> getListDeparture() {
        return listDeparture;
    }

    public List<String> getListArrival() {
        return listArrival;
    }

    // get the file name from the path
    public String getFileName(String filePath) {
        String path = filePath;
        String[] parts = path.split("/");
        fileName = parts[parts.length - 1];
        return fileName;
    }

    public List<Long> getListMilliseconds() {
        return listMilliseconds;
    }

    // get time from json
    public void getTimeFromJson() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

            JSONArray lang= (JSONArray) jsonObject.get("tickets");

            Iterator iterator = lang.iterator();

            while (iterator.hasNext()) {
                JSONObject innerObj = (JSONObject) iterator.next();
                listDeparture.add((String) innerObj.get("departure_time"));
                listArrival.add((String) innerObj.get("arrival_time"));
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Файл не найден");
        } catch (IllegalArgumentException ex) {
            System.out.println("Введено отрицательное значение");
        } catch (Exception ex) {
            System.out.println("Неверно введены данные");
        }
    }


    // getting milliseconds
    public void calculateFlightTime() throws ParseException {

        for (int i=0; i <= listDeparture.size()-1; i++) {
            // convert String to Time
            DateFormat format = new SimpleDateFormat("HH:mm");
            Time timeValueDeparture = new java.sql.Time(format.parse(listDeparture.get(i)).getTime());
            Time timeValueArrival = new java.sql.Time(format.parse(listArrival.get(i)).getTime());

            long milliseconds = timeValueArrival.getTime() - timeValueDeparture.getTime();
            listMilliseconds.add(milliseconds);
            long minutes = milliseconds / (60 * 1000) % 60;
            long hours = milliseconds / (60 * 60 * 1000);
            System.out.println("Время полета " + hours + " часов " + minutes + " минут");
            listTimeFly.add(hours + " часов " + minutes + " минут");
        }
    }


    // calculation average flight
    public void getAverageFlight() {

        long temp = 0L;
        for (int i=0; i<=listMilliseconds.size()-1; i++) {
            temp += listMilliseconds.get(i);
        }
        averageFlyBetweenCity = temp / listMilliseconds.size();

        long minutes = averageFlyBetweenCity / (60 * 1000) % 60;
        long hours = averageFlyBetweenCity / (60 * 60 * 1000);
        System.out.println("Среднее время полета между городами Владивосток " +
                "и Тель-Авив составляет: " + hours + ":" + minutes);

        timeBetween.add("Среднее время полета между городами Владивосток " +
                "и Тель-Авив составляет: " + hours + ":" + minutes);

        double percentsTimeFly = 0.9;
        percentFlight = Math.round((double) averageFlyBetweenCity * percentsTimeFly);

        long secondsPercent = (percentFlight / 1000) % 60;
        long minutesPercent = percentFlight / (60 * 1000) % 60;
        long hoursPercent = percentFlight / (60 * 60 * 1000);
        System.out.println("90% времени полета между городами " +
                "Владивосток и Тель-Авив составляет: " + hoursPercent + ":" + minutesPercent + ":" + secondsPercent);

        timeAverage.add("90% времени полета между городами " +
                "Владивосток и Тель-Авив составляет: " + hoursPercent + ":" + minutesPercent + ":" + secondsPercent);

    }


    public void saveInFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {

            writer.write(String.valueOf("Вылеты в: " + listDeparture));
            writer.newLine();
            writer.write(String.valueOf("Прилеты в: " + listArrival));
            writer.newLine();
            writer.write(String.valueOf("Время полетов: " + listTimeFly));
            writer.newLine();
            writer.write(String.valueOf(timeBetween));
            writer.newLine();
            writer.write(String.valueOf(timeAverage));
            System.out.println("------------------------");

            System.out.println("Выведенная в консоль информация записана в файл output.txt");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
