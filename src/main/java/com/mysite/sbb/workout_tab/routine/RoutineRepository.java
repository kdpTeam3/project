package com.mysite.sbb.workout_tab.routine;

import com.mysite.sbb.user.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoutineRepository extends JpaRepository<Routine, Long> {
    List<Routine> findBySiteUser(SiteUser siteUser);
}
