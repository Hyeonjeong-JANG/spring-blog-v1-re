package shop.mtcoding.blog.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    private EntityManager em;

    public UserRepository(EntityManager em) {
        this.em = em;
    }

    @Transactional
    public void save(UserRequest.JoinDTO reqDTO) {
        System.out.println("UserRepository의 save의 메서드 호출!!!");
        Query query = em.createNativeQuery("insert into user_tb(username, password, email, created_at) values (?,?,?,now())");
        query.setParameter(1, reqDTO.getUsername());
        query.setParameter(2, reqDTO.getPassword());
        query.setParameter(3, reqDTO.getEmail());
        query.executeUpdate();
    }

    public User findByUsername(String username) {
        Query query = em.createNativeQuery("select * from user_tb where username=?", User.class);
        query.setParameter(1, username);

        User user = null;
        try {
            user = (User) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
        return user;
    }

    public User findByUsernameAndPassword(UserRequest.LoginDTO reqDTO) {
        Query query = em.createNativeQuery("select * from user_tb where username=? and password=?", User.class);
        query.setParameter(1, reqDTO.getUsername());
        query.setParameter(2, reqDTO.getPassword());

        User user = (User) query.getSingleResult();
        return user;
    }

}
