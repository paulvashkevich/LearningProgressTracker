package pojo;

import tracker.PlatformCourses;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class Course {

    private final PlatformCourses platformCourse;
    private Map<String, Student> idToStudent = new HashMap<>();
    private Map<String, Integer> idToEarnedPoints = new HashMap<>();
    private Map<String, Integer> idToNumberOfSubmissions = new HashMap<>();
    private static final int DEFAULT_POINTS_FOR_STUDENT = 0;
    private static final int DEFAULT_SUBMISSIONS_FOR_STUDENT = 0;
    protected static final int BIG_DECIMAL_SCALE = 1;


    public Course(PlatformCourses platformCourse) {
        this.platformCourse = platformCourse;
    }

    public int getEarnedCoursePoints(String studentId) {
        return idToEarnedPoints.get(studentId);
    }

    public void addPointsToTheCourse(String studentId, int pointsToAdd) {
        int numberOfSubmissions = idToNumberOfSubmissions.get(studentId);
        int actualPoints = idToEarnedPoints.get(studentId);
        idToEarnedPoints.replace(studentId, actualPoints + pointsToAdd);
        idToNumberOfSubmissions.replace(studentId, numberOfSubmissions + 1);
    }

    protected boolean isCourseFinished(String studentId) {
        return getEarnedCoursePoints(studentId) >= platformCourse.getTotalNumberOfPointsToFinish();
    }

    public void addStudentToTheCourse(String studentId, Student student) {
        idToStudent.put(studentId, student);
        idToEarnedPoints.put(studentId, DEFAULT_POINTS_FOR_STUDENT);
        idToNumberOfSubmissions.put(studentId, DEFAULT_SUBMISSIONS_FOR_STUDENT);
    }

    public Student getStudentByStudentId(String studentId) {
        return idToStudent.get(studentId);
    }

    public int getStudentPoints(String studentId) {
        return idToEarnedPoints.getOrDefault(studentId, DEFAULT_POINTS_FOR_STUDENT);
    }

    public int getNumberOfSubmissionsForTheStudent(String studentId) {
        return idToNumberOfSubmissions.getOrDefault(studentId, DEFAULT_SUBMISSIONS_FOR_STUDENT);
    }

    public int getTotalNumberOfSubmissionOfAllStudents() {
        int totalNumberOfSubmissions = 0;
        for (Map.Entry<String, Integer> studentIdToNumberOfSubmissions : idToNumberOfSubmissions.entrySet()) {
            totalNumberOfSubmissions += studentIdToNumberOfSubmissions.getValue();
        }
        return totalNumberOfSubmissions;
    }

    public int getTotalNumberOfEarnedPointsOfAllStudents() {
        int totalNumberOfEarnedPoints = 0;
        for (Map.Entry<String, Integer> studentIdToEarnedPoints : idToEarnedPoints.entrySet()) {
            totalNumberOfEarnedPoints += studentIdToEarnedPoints.getValue();
        }
        return totalNumberOfEarnedPoints;
    }

    public int getNumberOfEnrolledStudents() {
        return idToStudent.size();
    }

    public boolean isStudentExist(String studentId) {
        return idToStudent.containsKey(studentId);
    }

    public void listStudentsId() {
        if (idToStudent.isEmpty()) {
            System.out.println("No students found.");
        } else {
            System.out.println("Students: ");
            idToStudent.forEach((studentId, student) -> System.out.println(studentId));
        }
    }

    public double getAverageGradePerAssignment() {
        return (double) getTotalNumberOfEarnedPointsOfAllStudents() / getTotalNumberOfSubmissionOfAllStudents();
    }

    private double calculateCourseCompletionForStudent(String studentId) {
        if (isCourseFinished(studentId)) {
            return 100.0;
        }
        double courseCompletionPercentage = (getEarnedCoursePoints(studentId) * 100.0)
                / platformCourse.getTotalNumberOfPointsToFinish();
        BigDecimal bigDecimalCourseCompletionPercentage = BigDecimal.valueOf(courseCompletionPercentage);
        bigDecimalCourseCompletionPercentage = bigDecimalCourseCompletionPercentage.setScale(BIG_DECIMAL_SCALE, RoundingMode.HALF_UP);
        return bigDecimalCourseCompletionPercentage.doubleValue();
    }

    public void getTopLearners() {
        LinkedHashMap<String, Integer> sortedIdToEarnedPoints = idToEarnedPoints.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .collect(Collectors.toMap(Map.Entry<String, Integer>::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        System.out.format("%-6s%-10s%-9s\n", "id", "points", "completed");
        sortedIdToEarnedPoints.forEach((id, earnedPoints) -> System.out.printf("%-6s%-10d%.1f%%\n", id, earnedPoints
                , calculateCourseCompletionForStudent(id)));
    }

    public List<Student> getStudentsFinishedCourse() {
        List<Student> studentsFinishedCourse = new ArrayList<>();
        for (Map.Entry<String, Student> studentEntry: idToStudent.entrySet()) {
            if (isCourseFinished(studentEntry.getKey())) {
                studentsFinishedCourse.add(studentEntry.getValue());
            }
        }
        return studentsFinishedCourse;
    }

    @Override
    public String toString() {
        return platformCourse.getCourseName();
    }

    public PlatformCourses getPlatformCourse() {
        return platformCourse;
    }
}
