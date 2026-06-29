package com.tracker.entity;


import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessActivity {

    private Long id;

    private Long pid;

    private String processName;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Long durationSeconds;

    private String status;
}