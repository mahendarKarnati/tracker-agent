package com.tracker.entity;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Device {

    private Long id;

    private String macAddress;

    private String machineName;

    private String osName;
    private String lastIpAddress;
}