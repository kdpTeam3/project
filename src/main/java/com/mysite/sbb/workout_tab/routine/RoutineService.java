package com.mysite.sbb.workout_tab.routine;

import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
import com.mysite.sbb.workout_tab.workout.Workout;
import com.mysite.sbb.workout_tab.workout.WorkoutRepository;
import com.mysite.sbb.workout_tab.workout.WorkoutUpdateDto;
import com.mysite.sbb.workout_tab.workoutSet.WorkoutSet;
import com.mysite.sbb.workout_tab.workoutSet.WorkoutSetRepository;
import com.mysite.sbb.workout_tab.workoutSet.WorkoutSetUpdateDto;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RoutineService {
    private final RoutineRepository routineRepository;
    private final UserService userService;
    private final WorkoutRepository workoutRepository;
    private final WorkoutSetRepository workoutSetRepository;

    public Routine save(Routine routine) {
        return routineRepository.save(routine);
    }
    public Optional<Routine> findById(Long id){
        return routineRepository.findById(id);
    }
//   public List<Routine> findByUser(/*String username*/Principal principal){
////        SiteUser user = userService.findByUsername(username); // 사용자를 찾음
////        return routineRepository.findRoutinesBySiteUserUsername(principal.getName()); // 해당 사용자의 루틴 목록 반환
//    }
    public void saveRoutine(RoutineUpdateDto routineUpdateDto){
        Routine routine = new Routine();
        routine.setRoutine_name(routineUpdateDto.getRoutine_name());

        routine = routineRepository.save(routine);

        for(WorkoutUpdateDto workoutUpdateDto : routineUpdateDto.getWorkouts()){
            Workout workout = new Workout();
            workout.setRoutine(routine);
            workout.setWorkout_name(workoutUpdateDto.getWorkout_name());

            // 운동 저장
            workout = workoutRepository.save(workout);

            for(WorkoutSetUpdateDto workoutSetUpdateDto : workoutUpdateDto.getWorkoutSet()){
                WorkoutSet workoutSet = new WorkoutSet();
                workoutSet.setWorkout(workout);
                workoutSet.setWeight(workoutSetUpdateDto.getWeight());
                workoutSet.setReps(workoutSetUpdateDto.getReps());

                // 세트 저장
                workoutSetRepository.save(workoutSet);
            }

        }
    }
}
