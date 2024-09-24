package com.mysite.sbb.workout_tab;

import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
import com.mysite.sbb.workout_tab.routine.Routine;
import com.mysite.sbb.workout_tab.routine.RoutineRepository;
import com.mysite.sbb.workout_tab.routine.RoutineService;
import com.mysite.sbb.workout_tab.routine.RoutineUpdateDto;
import com.mysite.sbb.workout_tab.workout.Workout;
import com.mysite.sbb.workout_tab.workout.WorkoutRepository;
import com.mysite.sbb.workout_tab.workout.WorkoutService;
import com.mysite.sbb.workout_tab.workout.WorkoutUpdateDto;
import com.mysite.sbb.workout_tab.workoutSet.WorkoutSet;
import com.mysite.sbb.workout_tab.workoutSet.WorkoutSetService;
import com.mysite.sbb.workout_tab.workoutSet.WorkoutSetUpdateDto;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequestMapping("/workout")
@Controller
public class WorkoutController {


    private final UserService userService;
    private final RoutineService routineService;
    private final WorkoutService workoutService;
    private final WorkoutSetService workoutSetService;

    public WorkoutController(UserService userService, RoutineService routineService, WorkoutService workoutService, WorkoutSetService workoutSetService) {
        this.userService = userService;
        this.routineService = routineService;
        this.workoutService = workoutService;
        this.workoutSetService = workoutSetService;
    }

    // 운동 가이드
    @GetMapping("/guide")
    public String Guide() {
        return "workout_guide";
    }

    @GetMapping("/guide/chest")
    public String Chest() {
        return "workout_content/chest";
    }

    @GetMapping("/guide/triceps")
    public String Triceps() {
        return "workout_content/triceps";
    }

    @GetMapping("/guide/back")
    public String Back() {
        return "workout_content/back";
    }

    @GetMapping("/guide/biceps")
    public String Biceps() {
        return "workout_content/biceps";
    }

    @GetMapping("/guide/leg")
    public String Leg() {
        return "workout_content/leg";
    }

    @GetMapping("/guide/shoulder")
    public String Shoulder() {
        return "workout_content/shoulder";
    }

    @GetMapping("/guide/abs")
    public String Abs() {
        return "workout_content/abs";
    }

    // 루틴 만들기
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/make_routine")
    public String Make_routine(Model model) {
        model.addAttribute("routineUpdateDto", new RoutineUpdateDto());
        return "make_routine";
    }

    // 루틴 데이터 처리
    @PostMapping("/make_routine")
    public String makeRoutinePost(Model model, RoutineUpdateDto routineUpdateDto, Principal principal) {

        try {
            // 로그인한 사용자 가져오기
            SiteUser siteUser = userService.findByUsername(principal.getName());

            // 루틴 생성
            Routine routine = new Routine();
            routine.setSiteUser(siteUser);
            routine.setRoutine_name(routineUpdateDto.getRoutine_name());

            // 루틴 저장
            routineService.save(routine);

            // 운동과 세트 저장
            for (WorkoutUpdateDto workoutUpdateDto : routineUpdateDto.getWorkouts()) {
                Workout workout = new Workout();
                workout.setRoutine(routine);
                workout.setWorkout_name(workoutUpdateDto.getWorkout_name());

                // 세트 저장
                for (WorkoutSetUpdateDto setUpdateDto : workoutUpdateDto.getWorkoutSet()) {
                    WorkoutSet workoutSet = new WorkoutSet();
                    workoutSet.setWorkout(workout);
                    workoutSet.setWeight(setUpdateDto.getWeight());
                    workoutSet.setReps(setUpdateDto.getReps());

                    // 세트 저장
                    workout.getWorkoutSet().add(workoutSet);
                }

                // 운동 저장
                workoutService.save(workout);
            }

            model.addAttribute("message", "루틴이 저장되었습니다");
            return "redirect:my_routine"; // 리다이렉션
        } catch (Exception e) {
            model.addAttribute("message", "루틴 저장 중 오류가 발생했습니다" + e.getMessage());
            return "make_routine";
        }
    }


    // 나의 루틴
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my_routine")
    public String myRoutine(Model model, Principal principal) {
        // 로그인한 사용자에 대한 루틴 목록 가져오기
        List<Routine> routines = routineService.findByUser(principal.getName());
        model.addAttribute("routines",routines);
        return "my_routine";
    }


}
