package com.mysite.sbb.workout_tab;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
import com.mysite.sbb.workout_tab.routine.Routine;
import com.mysite.sbb.workout_tab.routine.RoutineRepository;
import com.mysite.sbb.workout_tab.routine.RoutineService;
import com.mysite.sbb.workout_tab.routine.RoutineUpdateDto;
import com.mysite.sbb.workout_tab.workout.Workout;
import com.mysite.sbb.workout_tab.workout.WorkoutService;
import com.mysite.sbb.workout_tab.workout.WorkoutUpdateDto;
import com.mysite.sbb.workout_tab.workoutSet.WorkoutSet;
import com.mysite.sbb.workout_tab.workoutSet.WorkoutSetService;
import com.mysite.sbb.workout_tab.workoutSet.WorkoutSetUpdateDto;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/workout")
@Controller
@RequiredArgsConstructor
public class WorkoutController {


  private final UserService userService;
  private final RoutineService routineService;
  private final WorkoutService workoutService;
  private final WorkoutSetService workoutSetService;
  private final RoutineRepository routineRepository;

//    public WorkoutController(UserService userService, RoutineService routineService, WorkoutService workoutService, WorkoutSetService workoutSetService) {
//        this.userService = userService;
//        this.routineService = routineService;
//        this.workoutService = workoutService;
//        this.workoutSetService = workoutSetService;
//    }

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
  public String makeRoutinePost(Model model, RoutineUpdateDto routineUpdateDto,
      Principal principal) {

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
      return "record_routine";
    }
  }


  // 나의 루틴
  @PreAuthorize("isAuthenticated()")
  @GetMapping("/my_routine")
  public String myRoutine(Model model, Principal principal) {
    // 로그인한 사용자에 대한 루틴 목록 가져오기
    List<Routine> routines = /*routineService.findByUser(principal.getName())*/routineRepository.findRoutinesBySiteUserUsername(
        principal.getName());
    model.addAttribute("routines", routines);
    return "my_routine";
  }

  // 루틴 삭제
  @PreAuthorize("isAuthenticated()")
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteRoutine(@PathVariable("id") Long id, Principal principal) {
    Optional<Routine> optionalRoutine = this.routineRepository.findById(id);

    if (optionalRoutine.isPresent()) {
      Routine routine = optionalRoutine.get();

      // 현재 로그인한 사용자가 루틴의 소유자인지 확인
      if (!routine.getSiteUser().getUsername().equals(principal.getName())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제할 권한이 없습니다");
      }

      this.routineRepository.delete(routine);
      return ResponseEntity.ok("루틴이 성공적으로 삭제되었습니다.");
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("루틴을 찾을 수 없습니다.");
    }
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/my_goal")
  public String myGoal(Model model, Principal principal) {
    List<Routine> routines = routineRepository.findRoutinesBySiteUserUsername(principal.getName());
    model.addAttribute("routines", routines);
    return "my_goal";
  }

  @GetMapping("/routines/{id}")
  public String getRoutineById(@PathVariable Long id, Model model) {
    Optional<Routine> routine = routineService.findById(id);
    model.addAttribute("routine", routine);
    return "my_goal";
  }

  @PostMapping("/api/saveRoutine")
  public ResponseEntity<Void> saveRoutine(@RequestBody RoutineUpdateDto routineUpdateDto) {
    routineService.saveRoutine(routineUpdateDto);
    return ResponseEntity.ok().build();
  }
}
