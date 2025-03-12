package backend.csquiz.controller;

import backend.csquiz.entity.User;
import backend.csquiz.service.UserService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    // 랭킹 조회(점수 순 정렬)
    @GetMapping("/ranking")
    public List<User> getRanking(){
        return userService.getRanking();
    }
}
