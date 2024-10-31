package SOEN342_PROJECT.Services;

import SOEN342_PROJECT.PackageActorsAndObjects.Actor;

public class UserSession {
    private static String currentUserRole;
    private static Actor currentUser; // This can hold an instance of Admin, Instructor, or Client

    public static void setCurrentUserRole(String role, Actor user) {
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

