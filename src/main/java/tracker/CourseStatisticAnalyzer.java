package tracker;

import pojo.Course;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CourseStatisticAnalyzer {

    private List<Course> coursesToAnalyze;

    public CourseStatisticAnalyzer(List<Course> coursesToAnalyze) {
        this.coursesToAnalyze = coursesToAnalyze;
    }

    /**
     * The most popular has the biggest number of enrolled students
     * */
    public String getMostPopularCourse() {
        // in case of [0(Java) 0(DSA) 0(Database) 0 (Spring)] popular course should be "n/a"
        if (!areStudentsEnrolledInCourses()) {
            return "n/a";
        }
        StringBuilder mostPopularCourse = new StringBuilder();
        List<Course> sortedCourses = sortCoursesBySpecificComparator(
                Comparator.comparingInt(Course::getNumberOfEnrolledStudents));
        // in case of [0(Java) 1(DSA) 1(Database) 2(Spring)] popular course should be 2(Spring)
        mostPopularCourse.append(sortedCourses.get(sortedCourses.size() - 1));
        // in case of [0(Java) 0(DSA) 2(Database) 2 (Spring)] popular course should be "Database, Spring"
        // in case of [2(Java) 2(DSA) 2(Database) 2 (Spring)] popular course should be "Java, DSA, Database, Spring"
        for (int i = sortedCourses.size() - 2; i >= 0; i--) {
            if (sortedCourses.get(sortedCourses.size() - 1).getNumberOfEnrolledStudents()
                    == sortedCourses.get(i).getNumberOfEnrolledStudents()) {
                mostPopularCourse.insert(0, sortedCourses.get(i) + ", ");
            } else {
                break;
            }
        }
        return mostPopularCourse.toString();
    }

    /**
     * The least popular has the fewest number of enrolled students
     * */
    public String getLeastPopularCourse() {
        // in case of [0(Java) 0(DSA) 0(Database) 0 (Spring)] popular course should be "n/a"
        if (!areStudentsEnrolledInCourses()) {
            return "n/a";
        }
        StringBuilder leastPopularCourse = new StringBuilder();
        List<Course> sortedCourses = sortCoursesBySpecificComparator(
                Comparator.comparingInt(Course::getNumberOfEnrolledStudents));
        // in case of [0(Java) 1(DSA) 1(Database) 2(Spring)] the least popular course should be 0(Java)
        leastPopularCourse.append(sortedCourses.get(0));
        int temp = 1;
        for (int i = 1; i < sortedCourses.size(); i++) {
            if (sortedCourses.get(0).getNumberOfEnrolledStudents()
                    == sortedCourses.get(i).getNumberOfEnrolledStudents()) {
                // in case of [2(Java) 2(DSA) 2(Database) 2 (Spring)] the least popular course should be "n/a"
                temp++;
                if (temp == sortedCourses.size()) {
                    return "n/a";
                }
                // in case of [0(Java) 0(DSA) 2(Database) 2 (Spring)] popular course should be "Java, DSA"
                leastPopularCourse.insert(0, sortedCourses.get(i) + ", ");
            } else {
                break;
            }
        }
        return leastPopularCourse.toString();
    }

    /**
     * Higher student activity means a bigger number of completed tasks
     * */
    public String getHighestActivityCourse() {
        if (!areStudentsEnrolledInCourses()) {
            return "n/a";
        }
        StringBuilder highestActivityCourse = new StringBuilder();
        List<Course> sortedCourses = sortCoursesBySpecificComparator(
                Comparator.comparingInt(Course::getTotalNumberOfSubmissionOfAllStudents));
        // In case of [0(Java) 1(DSA) 1(Database) 2(Spring)] the highest activity course should be 2(Spring)
        highestActivityCourse.append(sortedCourses.get(sortedCourses.size() - 1));
        // In case of [2(Java) 2(DSA) 2(Database) 2 (Spring)] the highest activity course should be "Java, DSA, Database, Spring"?
        // In case of [0(Java) 0(DSA) 2(Database) 2 (Spring)] the highest activity course should be "Database, Spring"
        for (int i = sortedCourses.size() - 2; i >= 0; i--) {
            if (sortedCourses.get(sortedCourses.size() - 1).getTotalNumberOfSubmissionOfAllStudents()
                    == sortedCourses.get(i).getTotalNumberOfSubmissionOfAllStudents()) {
                highestActivityCourse.insert(0, sortedCourses.get(i) + ", ");
            } else {
                break;
            }
        }
        return highestActivityCourse.toString();
    }

    /**
     * Lowest student activity means the fewest number of completed tasks
     * */
    public String getLowestActivityCourse() {
        if (!areStudentsEnrolledInCourses()) {
            return "n/a";
        }
        StringBuilder lowestActivityCourse = new StringBuilder();
        List<Course> sortedCourses = sortCoursesBySpecificComparator(
                Comparator.comparingInt(Course::getTotalNumberOfSubmissionOfAllStudents));
        // In case of [0(Java) 1(DSA) 1(Database) 2(Spring)] the lowest activity course should be 0(Java)
        lowestActivityCourse.append(sortedCourses.get(0));
        int temp = 1;
        for (int i = 1; i < sortedCourses.size(); i++) {
            if (sortedCourses.get(0).getTotalNumberOfSubmissionOfAllStudents()
                    == sortedCourses.get(i).getTotalNumberOfSubmissionOfAllStudents()) {
                //  In case of [2(Java) 2(DSA) 2(Database) 2 (Spring)] the lowest activity course should be "n/a"
                temp++;
                if (temp == sortedCourses.size()) {
                    return "n/a";
                }
                // In case of [0(Java) 0(DSA) 2(Database) 2 (Spring)] the lowest activity course should "Java, DSA"
                lowestActivityCourse.insert(0, sortedCourses.get(i) + ", ");
            } else {
                break;
            }
        }
        return lowestActivityCourse.toString();
    }

    /**
     * The easiest course has the highest average grade per assignment;
     * */
    public String getEasiestCourse() {
        if (!areStudentsEnrolledInCourses()) {
            return "n/a";
        }
        StringBuilder easiestCourse = new StringBuilder();
        List<Course> sortedCourses = sortCoursesBySpecificComparator(
                Comparator.comparingDouble(Course::getAverageGradePerAssignment));
        // In case of [0.0(Java) 1.0(DSA) 1.0(Database) 2.0(Spring)] easiest course should be 2.0(Spring)
        easiestCourse.append(sortedCourses.get(sortedCourses.size() - 1));
        // In case of [2.0(Java) 2.0(DSA) 2.0(Database) 2.0 (Spring)] easiest course should be "Java, DSA, Database, Spring"
        // In case of [0(Java) 0(DSA) 2.0(Database) 2.0(Spring)] easiest course should be "Database, Spring"
        for (int i = sortedCourses.size() - 2; i >= 0; i--) {
            if (sortedCourses.get(sortedCourses.size() - 1).getAverageGradePerAssignment()
                    == sortedCourses.get(i).getAverageGradePerAssignment()) {
                easiestCourse.insert(0, sortedCourses.get(i) + ", ");
            } else {
                break;
            }
        }
        return easiestCourse.toString();
    }

    /**
     * The hardest course has the lowest average grade per assignment;
     * */
    public String getHardestCourse() {
        if (!areStudentsEnrolledInCourses()) {
            return "n/a";
        }
        StringBuilder hardestCourse = new StringBuilder();
        List<Course> sortedCourses = sortCoursesBySpecificComparator(
                Comparator.comparingDouble(Course::getAverageGradePerAssignment));
        // In case of [0.0(Java) 1.0(DSA) 1.0(Database) 2.0(Spring)] hardest course should be 0(Java)
        hardestCourse.append(sortedCourses.get(0));
        int temp = 1;
        for (int i = 1; i < sortedCourses.size(); i++) {
            if (sortedCourses.get(0).getAverageGradePerAssignment()
                    == sortedCourses.get(i).getAverageGradePerAssignment()) {
                //  In case of [2.0(Java) 2.0(DSA) 2.0(Database) 2.0 (Spring)] hardest course should be "n/a"
                temp++;
                if (temp == sortedCourses.size()) {
                    return "n/a";
                }
                // In case of [0.0(Java) 0.0(DSA) 2.0(Database) 2.0 (Spring)] hardest course should be "Java, DSA"
                hardestCourse.insert(0, sortedCourses.get(i) + ", ");
            } else {
                break;
            }
        }

        return hardestCourse.toString();
    }

    private boolean areStudentsEnrolledInCourses() {
        for (Course course: coursesToAnalyze) {
            if (course.getNumberOfEnrolledStudents() != 0) {
                return true;
            }
        }
        return false;
    }

    private List<Course> sortCoursesBySpecificComparator(Comparator<Course> comparator) {
        List<Course> courses = new ArrayList<>(coursesToAnalyze);
        courses.sort(comparator);
        return courses;
    }
}