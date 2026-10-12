package com.driveflow.driveflow.controller;

import com.driveflow.driveflow.model.Vehicle;
import com.driveflow.driveflow.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/vehicles")
public class VehicleController {

    @Autowired
    private VehicleRepository repository;

    @GetMapping
    public List<Vehicle> listAll() {
        return repository.findAll();
    }

    @PostMapping
    public Vehicle create(@RequestBody Vehicle vehicle) {
        Vehicle newVehicle = new Vehicle(vehicle.getPlate(), vehicle.getModel(), vehicle.getColor(), vehicle.getYear());
        return repository.save(newVehicle);
    }

    @PostMapping("/{plate}/renew")
    public String renewIpva(@PathVariable String plate) {
        Vehicle vehicle = repository.findById(plate)
                .orElseThrow(() -> new IllegalArgumentException("Veículo não encontrado!"));

        try {
            vehicle.renewIpva();
            repository.save(vehicle);
            return "Vehicle tax (IPVA) paid for the vehicle with license plate " + plate;
        } catch (IllegalStateException e) {
            return "Error " + e.getMessage();
        }
    }
}
