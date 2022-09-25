package tracker;

import notifications.NotificationService;
import notifications.NotificationsFactory;
import pojo.Course;
import pojo.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class LearningPlatform {
    private static LearningPlatform learningPlatform;
    private NotificationService notificationService;
    private Map<String, Student> registeredOnPlatformStudents = new HashMap<>();
    private List<String> registeredOnPlatformStudentsEmails = new ArrayList<>();
    private Course javaCourse = new Course(PlatformCourses.JAVA);
    private Course databaseCourse = new Course(PlatformCourses.DATABASES);
    private Course dsaCourse = new Course(PlatformCourses.DSA);
    private Course springCourse = new Course(PlatformCourses.SPRING);
    private Map<PlatformCourses, List<Student>> courseToAlreadyNotifiedStudent = new HashMap<>();

    private static long idCounter = 10000L;
    public static final String NAME_REGEX = "[^-']([A-Za-z]*[-']?[A-Za-z])+";
    public static final String EMAIL_REGEX = "[\\w.]+@\\w+\\.\\w+";
    public static final String POINTS_REGEX = "\\w+\\s(\\d+\\s){3}\\d+";

    public static final int JAVA_COURSE_INDEX = 1;
    public static final int DSA_COURSE_INDEX = 2;
    public static final int DATABASE_COURSE_INDEX = 3;
    public static final int SPRING_COURSE_INDEX = 4;
    private static final int STUDENT_ID_INDEX = 0;

    private LearningPlatform() {
    }

    public static LearningPlatform getInstance() {
        if (Objects.isNull(learningPlatform)) {
            learningPlatform = new LearningPlatform();
        }
        return learningPlatform;
    }

    public boolean registerStudentToPlatform(String userInputStudentCredentials) {
        String[] userInputEntries = userInputStudentCredentials.split("\\s");
        if (validateStudentCredentials(userInputEntries)) {
            Student newStudent = setUpStudentData(userInputEntries);
            String newStudentId = createID();
            newStudent.setId(newStudentId);
            registeredOnPlatformStudents.put(newStudentId, newStudent);
            registeredOnPlatformStudentsEmails.add(newStudent.getEmail());
            System.out.println("The student has been added.");
            return true;
        }
        return false;
    }

    private String createID() {
        return String.valueOf(idCounter++);
    }

    private boolean validateStudentCredentials(String[] userInputEntries) {
        if (userInputEntries.length < 3) {
            System.out.println("Incorrect credentials.");
            return false;
        }

        if (!isNameValid(userInputEntries[0])) {
            System.out.println("Incorrect first name.");
            return false;
        }
        for (int i = 1; i < userInputEntries.length - 1; i++) {
            if (!isNameValid(userInputEntries[i])) {
                System.out.println("Incorrect last name.");
                return false;
            }
        }
        if (!isEmailValid(userInputEntries[userInputEntries.length - 1])) {
            System.out.println("Incorrect email.");
            return false;
        }

        if (registeredOnPlatformStudentsEmails.contains(userInputEntries[userInputEntries.length - 1])) {
            System.out.println("This email is already taken");
            return false;
        }
        return true;
    }

    private boolean isNameValid(String nameString) {
        return nameString.matches(NAME_REGEX);
    }

    private boolean isEmailValid(String emailString) {
        return emailString.matches(EMAIL_REGEX);
    }

    private Student setUpStudentData(String[] userInputEntries) {
        Student student = new Student();
        student.setFirstName(userInputEntries[0]);
        student.setEmail(userInputEntries[userInputEntries.length - 1]);
        // last name can consist of two or more words
        StringBuilder lastName = new StringBuilder();
        for (int i = 1; i < userInputEntries.length - 1; i++) {
            lastName.append(userInputEntries[i]);
        }
        student.setLastName(lastName.toString());
        return student;
    }

    public void addPoints(String pointsToAdd) {
        if (validatePointsInput(pointsToAdd)) {
            updateStudentPoints(pointsToAdd);
            System.out.println("Points updated.");
        }
    }

    private boolean validatePointsInput(String userInput) {

        if (!isPointsInputValid(userInput)) {
            System.out.println("Incorrect points format.");
            return false;
        }

        String studentId = userInput.split("\\s+")[0];
        Optional<Student> student = registeredOnPlatformStudents.values().stream().filter(registeredOnPlatformStudent
                -> registeredOnPlatformStudent.getId().matches(studentId)).findAny();
        if (student.isEmpty()) {
            System.out.printf("No student is found for id=%s\n", studentId);
            return false;
        }

        return true;
    }

    private static boolean isPointsInputValid(String userInputPointsString) {
        return userInputPointsString.matches(POINTS_REGEX);
    }


    private void updateStudentPoints(String userInput) {
        // 0(Java) 0(DSA) 0(Database) 0(Spring)
        String[] userInputPointsEntries = userInput.split("\\s+");
        String studentId = userInputPointsEntries[STUDENT_ID_INDEX];
        if (Integer.parseInt(userInputPointsEntries[JAVA_COURSE_INDEX]) != 0) {
            updateStudentPointsForCourse(javaCourse, studentId, Integer.parseInt(userInputPointsEntries[JAVA_COURSE_INDEX]));
        }
        if (Integer.parseInt(userInputPointsEntries[DSA_COURSE_INDEX]) != 0) {
            updateStudentPointsForCourse(dsaCourse, studentId, Integer.parseInt(userInputPointsEntries[DSA_COURSE_INDEX]));
        }
        if (Integer.parseInt(userInputPointsEntries[DATABASE_COURSE_INDEX]) != 0) {
            updateStudentPointsForCourse(databaseCourse, studentId, Integer.parseInt(userInputPointsEntries[DATABASE_COURSE_INDEX]));
        }
        if (Integer.parseInt(userInputPointsEntries[SPRING_COURSE_INDEX]) != 0) {
            updateStudentPointsForCourse(springCourse, studentId, Integer.parseInt(userInputPointsEntries[SPRING_COURSE_INDEX]));
        }
    }

    private void updateStudentPointsForCourse(Course course, String studentId, int pointsToAdd) {
        if (!course.isStudentExist(studentId)) {
            course.addStudentToTheCourse(studentId, registeredOnPlatformStudents.get(studentId));
        }
        course.addPointsToTheCourse(studentId, pointsToAdd);
    }


    public void printStudentPointsInAllPlatformCourses(String studentToFind) {
        String studentId = studentToFind.split("\\s+")[STUDENT_ID_INDEX];
        if (registeredOnPlatformStudents.containsKey(studentId)) {
            System.out.printf("%s points: Java=%d; DSA=%d; Databases=%d; Spring=%d", studentId,
                    javaCourse.getStudentPoints(studentId),
                    dsaCourse.getStudentPoints(studentId),
                    databaseCourse.getStudentPoints(studentId),
                    springCourse.getStudentPoints(studentId));
        } else {
            System.out.println(System.out.printf("No student is found for id=%s.", studentId));
        }
    }

    public void listStudentsRegisteredOnPlatform() {
        if (registeredOnPlatformStudents.isEmpty()) {
            System.out.println("No students found.");
        } else {
            System.out.println("Students: ");
            registeredOnPlatformStudents.keySet().forEach(System.out::println);
        }
    }

    public void retrieveCoursesStatistic() {
        CourseStatisticAnalyzer courseStatisticAnalyzer =
                new CourseStatisticAnalyzer(List.of(javaCourse, dsaCourse, databaseCourse, springCourse));
        System.out.printf("Most Popular: %s\n", courseStatisticAnalyzer.getMostPopularCourse());
        System.out.printf("Least Popular: %s\n", courseStatisticAnalyzer.getLeastPopularCourse());
        System.out.printf("Highest activity: %s\n", courseStatisticAnalyzer.getHighestActivityCourse());
        System.out.printf("Lowest Activity: %s\n", courseStatisticAnalyzer.getLowestActivityCourse());
        System.out.printf("Easiest course: %s\n", courseStatisticAnalyzer.getEasiestCourse());
        System.out.printf("Hardest course: %s\n", courseStatisticAnalyzer.getHardestCourse());
    }

    public boolean getTopLearnersForSpecificCourse(String userInput) {
        switch (userInput.toLowerCase()) {
            case "java":
                System.out.println("Java");
                javaCourse.getTopLearners();
                break;
            case "dsa":
                System.out.println("DSA");
                dsaCourse.getTopLearners();
                break;
            case "databases":
                System.out.println("Databases");
                databaseCourse.getTopLearners();
                break;
            case "spring":
                System.out.println("Spring");
                springCourse.getTopLearners();
                break;
            default:
                System.out.println("Unknown course.");
                return false;
        }
        return true;
    }


    public void notifyStudentByEmailIfCourseComplete() {
        notificationService = NotificationsFactory.createNotificationService("email");
        Set<Student> notifiedStudents = new HashSet<>();
        notifiedStudents.addAll(notifyStudentByEmailIfCourseComplete(javaCourse));
        notifiedStudents.addAll(notifyStudentByEmailIfCourseComplete(dsaCourse));
        notifiedStudents.addAll(notifyStudentByEmailIfCourseComplete(databaseCourse));
        notifiedStudents.addAll(notifyStudentByEmailIfCourseComplete(springCourse));
        System.out.printf("Total %d students have been notified.\n", notifiedStudents.size());
    }

    private List<Student> notifyStudentByEmailIfCourseComplete(Course course) {
        List<Student> studentsFinishedCourse = course.getStudentsFinishedCourse();
        List<Student> newNotifiedStudents = new ArrayList<>();
        for (Student studentFinishedCourse : studentsFinishedCourse) {
            if (courseToAlreadyNotifiedStudent.containsKey(course.getPlatformCourse())) {
                if (!courseToAlreadyNotifiedStudent.get(course.getPlatformCourse()).contains(studentFinishedCourse)) {
                    courseToAlreadyNotifiedStudent.get(course.getPlatformCourse()).add(studentFinishedCourse);
                    notificationService.sendNotification(course.getPlatformCourse(), studentFinishedCourse);
                    newNotifiedStudents.add(studentFinishedCourse);
                }
            } else {
                courseToAlreadyNotifiedStudent.put(course.getPlatformCourse(), new ArrayList<>(List.of(studentFinishedCourse)));
                notificationService.sendNotification(course.getPlatformCourse(), studentFinishedCourse);
                newNotifiedStudents.add(studentFinishedCourse);
            }
        }
        return newNotifiedStudents;
    }
}