package notifications;

public class NotificationsFactory {

    public static NotificationService createNotificationService(String notificationType) {
        NotificationService notificationService = null;

        if (notificationType.equals("email")) {
            notificationService = new EmailNotification();
        }
        return notificationService;
    }
}
