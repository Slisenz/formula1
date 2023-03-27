package com.foxminded;


public class Racer implements Comparable<Racer> {

    private Long lapTime;
    private String name;
    private String carModel;

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public long getLapTime() {
        return lapTime;
    }

    public void setLapTime(Long time) {
        this.lapTime = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Racer inputObject) {
        return Long.compare(this.getLapTime(), inputObject.getLapTime());
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
