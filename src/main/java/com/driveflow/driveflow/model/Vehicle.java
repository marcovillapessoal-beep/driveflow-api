package com.driveflow.driveflow.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Entity
public class Vehicle {

    @Id
    private String plate;
    private String model;
    private String color;

    @Column(name = "vehicle_year")
    private int year;
    private long ipvaExpirationTimestamp;

    public Vehicle() {}

    public Vehicle(String plate, String model, String color, int year) {
        this.plate = plate;
        this.model = model;
        this.color = color;
        this.year = year;

        if (isIpvaRequired()) {
            this.ipvaExpirationTimestamp = Instant.now().getEpochSecond() + (30 * 86400L);
        } else {
            this.ipvaExpirationTimestamp = 0L;
        }
    }

    public String getPlate() { return plate; }
    public void setPlate(String plate) { this.plate = plate; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public long getIpvaExpirationTimestamp() { return ipvaExpirationTimestamp; }
    public void setIpvaExpirationTimestamp(long timestamp) { this.ipvaExpirationTimestamp = timestamp; }

    public boolean getIpvaRequired() {
        int currentYear = LocalDate.now().getYear();
        int vehicleAge = currentYear - this.year;

        return vehicleAge < 20;
    }

    public String getIpvaExpirationDate() {
        if (!getIpvaRequired() || this.ipvaExpirationTimestamp == 0L) {
            return "Exempt";
        }
        try {
            return Instant.ofEpochSecond(this.ipvaExpirationTimestamp).atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (Exception e) {
            return "Error";
        }
    }

    public String getIpvaStats() {
        if (getIpvaRequired()) {
            if (Instant.now().getEpochSecond() > ipvaExpirationTimestamp) {
                return "Pagar IPVA!";
            } else {
                return "Em dia";
            }
        } else {
            return "Veiculo não paga IPVA!";
        }
    }

    public boolean isIpvaRequired() {
        return getIpvaRequired();
    }

    public void renewIpva() {
        if (!getIpvaRequired()) {
            throw new IllegalStateException("This vehicle is exempt from IPVA.");
        }
        this.ipvaExpirationTimestamp = Instant.now().getEpochSecond() + (30 * 86400L);
    }
}
