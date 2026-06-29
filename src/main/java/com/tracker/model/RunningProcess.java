package com.tracker.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RunningProcess {

    private Long pid;

    private String processName;
    
//    private String windowTitle;
}