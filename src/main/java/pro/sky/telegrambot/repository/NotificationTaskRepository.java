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

    @Query("SELECT n FROM NotificationTask n WHERE n.DateTime >= :from AND n.DateTime <= :to")
    List<NotificationTask> getNotificationTasks(@Param ("from") LocalDateTime from, @Param ("to") LocalDateTime to);
}
