package com.digitalinnovation.parking.service;

import com.digitalinnovation.parking.exceptions.ParkingNotFoundException;
import com.digitalinnovation.parking.model.Parking;
import com.digitalinnovation.parking.repository.ParkingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ParkingService {

    ParkingRepository parkingRepository;
    private static Map<String, Parking> parkingMap = new HashMap();

    private static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    @Transactional
    public List<Parking> findAll() {
        return parkingMap.values().stream().collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Parking findById(String id) {
        Parking parking = parkingMap.get(id);
        if(parking == null) {
            throw new ParkingNotFoundException(id);
        }
        return parkingMap.get(id);
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Parking create(Parking parkingCreate) {
        String uuid = getUUID();
        parkingCreate.setId(uuid);
        parkingCreate.setEntryDate(LocalDateTime.now());
        parkingMap.put(uuid, parkingCreate);
        return parkingCreate;
    }

    @Transactional
    public void delete(String id) {
         findById(id);
        parkingMap.remove(id);
    }

    @Transactional
    public Parking update(String id, Parking parkingCreate) {
        Parking parking = findById(id);
        parking.setColor(parkingCreate.getColor());
        parkingMap.replace(id, parking);
        return parking;
    }

    public Parking checkOut(String id) {
        Parking parking = findById(id);
        parking.setExitDate(LocalDateTime.now());
        parking.setBill(ParkingChekOut.getBill(parking));
        parkingRepository.save(parking);
        return null;
    }
}
