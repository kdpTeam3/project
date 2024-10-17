package com.mysite.sbb.workout_tab.recordDate;

import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.workout_tab.routine.Routine;
import com.mysite.sbb.workout_tab.workout.Workout;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecordDateRepository extends JpaRepository<RecordDate, Long> {


//  @Query("select rd.date from RecordDate rd where rd.date =:date")
//  void selectDate(@Param("date") LocalDate date);
  Optional<RecordDate> findByDate(LocalDate date);

  @Modifying
  @Query("update RecordDate rd set rd.routine.routineNum = :routine where rd.date = :date ")
  void updateDate(@Param("routine") Long routine, @Param("date")  LocalDate date);
}