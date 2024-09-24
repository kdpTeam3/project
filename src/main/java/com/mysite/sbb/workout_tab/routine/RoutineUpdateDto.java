package com.mysite.sbb.workout_tab.routine;

import com.mysite.sbb.workout_tab.workout.WorkoutUpdateDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoutineUpdateDto {
    private Long id;
    private String routine_name;
    private List<WorkoutUpdateDto> workouts;

    public RoutineUpdateDto() {
    }

    public RoutineUpdateDto(Long id, String routine_name, List<WorkoutUpdateDto> workouts) {
        this.id = id;
        this.routine_name = routine_name;
        this.workouts = workouts;
    }

}
