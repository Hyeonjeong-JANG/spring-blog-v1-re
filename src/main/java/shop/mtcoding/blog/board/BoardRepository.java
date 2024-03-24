package shop.mtcoding.blog.board;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class BoardRepository {
    private final EntityManager em;

    public List<Board> findAll() {
        Query query = em.createNativeQuery("select * from board_tb order by id desc", Board.class);
        return query.getResultList();
    }

    public Board findById(int id) {
        Query query = em.createNativeQuery("select * from board_tb where id=?", Board.class);
        query.setParameter(1, id);
        Board board = (Board) query.getSingleResult();
        return board;
    }

    public BoardResponse.DetailDTO findByIdWithUser(int boardId) {
        Query query = em.createNativeQuery("""
                select b.id, b.title, b.content, b.user_id, u.username 
                from board_tb b
                inner join user_tb u 
                on b.user_id =u.id 
                where b.id=?
                """);
        query.setParameter(1, boardId);
        Object[] row = (Object[]) query.getSingleResult();

        Integer id = (Integer) row[0];
        String title = (String) row[1];
        String content = (String) row[2];
        Integer userId = (Integer) row[3];
        String username = (String) row[4];

        // 하나하나 담기
        BoardResponse.DetailDTO respDTO = new BoardResponse.DetailDTO();
        respDTO.setId(id);
        respDTO.setTitle(title);
        respDTO.setContent(content);
        respDTO.setUserId(userId);
        respDTO.setUsername(username);

//        // BoardReponse.DetailDTO에 @AllArgsConstructor 달아서 생성자로 담기 -> @AllArgsConstructor을 사용하는 경우 모든 필드를 인자로 받는 생성자가 자동으로 생성되므로, 만약 DTO에 추가적인 필드가 추가되거나 필드의 순서가 변경된다면 해당 생성자도 수정해야 한다. 이러한 점을 고려하여 사용해야 한다.
//        BoardResponse.DetailDTO respDTO = new BoardResponse.DetailDTO(
//                id, title, content, userId, username
//        );
        return respDTO;
    }

    @Transactional
    public void save(BoardRequest.SaveDTO reqDTO, Integer userId) {
        Query query = em.createNativeQuery("insert into board_tb(title, content, user_id, created_at) values (?,?,?,now())");
        query.setParameter(1, reqDTO.getTitle());
        query.setParameter(2, reqDTO.getContent());
        query.setParameter(3, userId);

        query.executeUpdate();
    }

    @Transactional
    public void deleteById(int id) {
        Query query = em.createNativeQuery("delete from board_tb where id = ?");
        query.setParameter(1, id);
        query.executeUpdate();
    }
}
