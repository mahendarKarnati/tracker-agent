package com.tracker.entity;

import lombok.*;

import java.time.LocalDateTime;
@Data
@Builder
public class ActiveWindowActivity {


    private Long id;

    private String windowTitle;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Long durationSeconds;
    private String status;
}