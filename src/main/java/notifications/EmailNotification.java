package notifications;

import pojo.Student;
import tracker.PlatformCourses;

public class EmailNotification implements NotificationService {

    @Override
    public void sendNotification(PlatformCourses completedCourse, Student student) {
        String emailTemplate = "To: %s\nRe: Your Learning Progress\nHello, %s! You have accomplished our %s course!\n";
        System.out.printf(emailTemplate, student.getEmail(), student.getFirstName() + " "
                + student.getLastName(), completedCourse.getCourseName());
    }
}
