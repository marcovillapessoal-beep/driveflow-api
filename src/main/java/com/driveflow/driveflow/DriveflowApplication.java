package com.driveflow.driveflow;

import com.driveflow.driveflow.model.Vehicle;
import com.driveflow.driveflow.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class DriveflowApplication {

    @Autowired
    private VehicleRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(DriveflowApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onStartDatas() {
        Vehicle vehicleClient = new Vehicle("EAF-218-FGA", "Gol Quadrado (2025)", "Branca", 2025);
        vehicleClient.setIpvaExpirationTimestamp(100000L);

        repository.save(vehicleClient);
        repository.save(new Vehicle("EAF-218-FGB", "Gol Quadrado (2006)", "Preta", 2006));
        repository.save(new Vehicle("EAF-218-FGC", "Gol Quadrado (1998)", "Vermelha", 1998));

        System.out.println("IPVA testing system successfully launched.");
    }
}
