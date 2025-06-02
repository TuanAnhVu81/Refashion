package exe201.Refashion.repository;

import exe201.Refashion.entity.Messages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessagesRepository extends JpaRepository<Messages, String> {
    @Query("SELECT m FROM Messages m WHERE m.sender.id = :senderId AND m.receiver.id = :receiverId")
    List<Messages> findBySenderIdAndReceiverId(@Param("senderId") String senderId, @Param("receiverId") String receiverId);

    @Query("SELECT COUNT(m) FROM Messages m WHERE m.sender.id = :senderId AND m.receiver.id = :receiverId AND m.isFree = true")
    Long countFreeMessagesBySenderIdAndReceiverId(@Param("senderId") String senderId, @Param("receiverId") String receiverId);
}