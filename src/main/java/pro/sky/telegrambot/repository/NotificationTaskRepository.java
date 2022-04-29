package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.model.NotificationTask;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationTaskRepository extends JpaRepository<NotificationTask, Long> {

    @Query("SELECT n FROM NotificationTask n WHERE n.dateTime >= :from AND n.dateTime <= :to")
    List<NotificationTask> getNotificationTasks(@Param ("from") LocalDateTime from, @Param ("to") LocalDateTime to);
}
