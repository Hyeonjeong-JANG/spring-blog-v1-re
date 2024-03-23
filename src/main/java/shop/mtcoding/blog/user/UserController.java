package shop.mtcoding.blog.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class UserController {
    private final UserRepository userRepository;
    private final HttpSession session;

    @GetMapping("/join-form")
    public String joinForm() {
        return "user/join-form";
    }

    @PostMapping("/join")
    public String join(UserRequest.JoinDTO reqDTO) {
        // 1. 유효성 검사
        if (reqDTO.getUsername().length()<3){
            return "error/400";
        }

        // 2. 동일 username 체크


        // 3. Model 필요(위임, DB 연결)
        // select * from user_tb where username = ? and password = ?;
        try {
            userRepository.save(reqDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/loginForm";
    }

    @GetMapping("/login-form")
    public String loginForm() {
        return "user/login-form";
    }

    @PostMapping("/login")
    public String login(UserRequest.LoginDTO reqDTO, HttpServletRequest request) {

        // 1. 유효성 검사
        if (reqDTO.getUsername().length() < 3) {
            return "error/400";
        }

        // 3. Model 필요(위임, DB 연결)
        User user = userRepository.findByUsernameAndPassword(reqDTO);

        if (user == null) {
            return "error/401";
        } else {
            session.setAttribute("sessionUser", user);
        }
        return "redirect:/";
    }

    @GetMapping("/user/update-form")
    public String updateForm() {
        return "user/update-form";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }
}
