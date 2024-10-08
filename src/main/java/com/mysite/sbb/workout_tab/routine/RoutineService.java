package com.mysite.sbb.workout_tab.routine;

import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
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
}
