package com.prodyna.voting.auth.user;

public enum Role {

    USER,
    ADMINISTRATOR;

    public static Role forRoleValue(String roleName) {
        for (Role role : Role.values()) {
            if (role.name().equalsIgnoreCase(roleName)) {
                return role;
            }
        }

        throw new IllegalArgumentException("No such role found.");
    }

    public static boolean isUserAdmin(User user) {
        return Role.ADMINISTRATOR == user.getRole();
    }
}
