package exe201.Refashion.repository;

import exe201.Refashion.entity.Messages;
import exe201.Refashion.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessagesRepository extends JpaRepository<Messages, String> {
    @Query("SELECT m FROM Messages m WHERE m.sender.id = :senderId AND m.receiver.id = :receiverId")
    List<Messages> findBySenderIdAndReceiverId(@Param("senderId") String senderId, @Param("receiverId") String receiverId);

    @Query("SELECT COUNT(m) FROM Messages m WHERE m.sender.id = :senderId AND m.receiver.id = :receiverId AND m.isFree = true")
    Long countFreeMessagesBySenderIdAndReceiverId(@Param("senderId") String senderId, @Param("receiverId") String receiverId);

    @Query("SELECT COUNT(m) FROM Messages m WHERE m.sender.id = :senderId AND m.receiver.id = :receiverId AND m.isFree = true AND m.sentAt >= :fromTime")
    long countFreeMessagesSince(@Param("senderId") String senderId, @Param("receiverId") String receiverId, @Param("fromTime") LocalDateTime fromTime);

    @Query("SELECT m FROM Messages m WHERE (m.sender.id = :userId1 AND m.receiver.id = :userId2) " +
            "OR (m.sender.id = :userId2 AND m.receiver.id = :userId1) ORDER BY m.sentAt ASC")
    List<Messages> findBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderBySentAtAsc(
            @Param("userId1") String userId1,
            @Param("userId2") String userId2
    );

    @Modifying
    @Query("UPDATE Messages m SET m.isRead = true WHERE m.sender.id = :senderId AND m.receiver.id = :receiverId AND m.isRead = false")
    void markAsRead(@Param("senderId") String senderId, @Param("receiverId") String receiverId);

    @Query("SELECT DISTINCT CASE WHEN m.sender.id = :userId THEN m.receiver ELSE m.sender END FROM Messages m WHERE m.sender.id = :userId OR m.receiver.id = :userId")
    List<Users> findChatPartners(@Param("userId") String userId);

}