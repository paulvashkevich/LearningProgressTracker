package tracker;

public enum PlatformCourses {
    JAVA("Java", 600),
    DSA("DSA", 400),
    DATABASES("Databases", 480),
    SPRING("Spring", 550);

    private final String courseName;
    private final int totalNumberOfPointsToFinish;

    PlatformCourses(String courseName, int totalNumberOfPointsToFinish) {
        this.courseName = courseName;
        this.totalNumberOfPointsToFinish = totalNumberOfPointsToFinish;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getTotalNumberOfPointsToFinish() {
        return totalNumberOfPointsToFinish;
    }
}
