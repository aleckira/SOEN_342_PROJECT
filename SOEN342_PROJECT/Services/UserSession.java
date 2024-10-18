package Services;

public class UserSession {
    private static String currentUserRole;
    private static Object currentUser; // This can hold an instance of Admin, Instructor, or Client

    public static void setCurrentUserRole(String role, Object user) {
        currentUserRole = role;
        currentUser = user;
    }

    public static String getCurrentUserRole() {
        return currentUserRole;
    }

    public static Object getCurrentUser() {
        return currentUser;
    }
}

