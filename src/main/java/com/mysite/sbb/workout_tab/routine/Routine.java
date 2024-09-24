package com.mysite.sbb.workout_tab.routine;

import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.workout_tab.workout.Workout;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;


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

    @NotNull
    @Column
    private String routine_name;

    @OneToMany(mappedBy = "routine"  , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Workout> workouts;

    public Routine(){}

    public Routine(SiteUser siteUser, String routine_name) {
        this.siteUser = siteUser;
        this.routine_name = routine_name;
    }
}
