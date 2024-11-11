package Services;


import PackageActorsAndObjects.Actor;

public class UserSession {
    private static String currentUserRole;
    private static Actor currentUser; // This can hold an instance of Admin, Instructor, Client or Guardian

    public static void setCurrentUserRole(String role, Actor user) {
        currentUserRole = role;
        currentUser = user;
    }

    public static String getCurrentUserRole() {
        return currentUserRole;
    }

    public static Actor getCurrentUser() {
        return currentUser;
    }

}

