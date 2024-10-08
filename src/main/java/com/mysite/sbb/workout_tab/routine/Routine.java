package com.mysite.sbb.workout_tab.routine;

import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.workout_tab.workout.Workout;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;


@Data
@Entity
public class Routine {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @NotNull
  @JoinColumn(name = "user_id")
  private SiteUser siteUser;


  @Column
  private String date;

  @NotNull
  @Column
  private String routine_name;

  @OneToMany(mappedBy = "routine", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Workout> workouts;

  public Routine() {
  }

  public Routine(SiteUser siteUser, String date, String routine_name) {
    this.date = date;
    this.siteUser = siteUser;
    this.routine_name = routine_name;
  }
}
