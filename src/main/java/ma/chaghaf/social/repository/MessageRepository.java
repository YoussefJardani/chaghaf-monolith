package ma.chaghaf.social.repository;

import ma.chaghaf.social.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE " +
           "(m.senderId = :userId AND m.receiverId = :otherId) OR " +
           "(m.senderId = :otherId AND m.receiverId = :userId) " +
           "ORDER BY m.sentAt ASC")
    List<Message> findConversation(Long userId, Long otherId);
}
