package shop.mtcoding.blog.user;

import lombok.Data;

public class UserRequest {
    @Data
    public static class JoinDTO {
        private Integer id;
        private String username;
        private String password;
    }
}
