package com.example.driver.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phoneNumber;

    @Embedded
    private DriverAddress driverAddress;

    @Embedded
    private Vehicle vehicle;

    private Driver(String name, String password, String phoneNumber, DriverAddress driverAddress, Vehicle vehicle) {
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.driverAddress = driverAddress;
        this.vehicle = vehicle;
    }
}

