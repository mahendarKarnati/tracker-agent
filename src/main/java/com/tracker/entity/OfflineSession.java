package com.tracker.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfflineSession {

    private Long deviceId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String status;
}