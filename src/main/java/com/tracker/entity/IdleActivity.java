package com.tracker.entity;



import java.time.LocalDateTime;

import lombok.Data;

@Data
public class IdleActivity {

    private Long id;

    private LocalDateTime idleStart;

    private LocalDateTime idleEnd;
    private String status;
    private Long idleSeconds;
}