package com.ysq.theTourGuide.dto;


import com.ysq.theTourGuide.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {

    private String month;
    private List<Task> tasks;
}
