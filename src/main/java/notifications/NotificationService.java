package notifications;

import pojo.Student;
import tracker.PlatformCourses;

public interface NotificationService {

    void sendNotification(PlatformCourses completedCourse, Student student);

}
