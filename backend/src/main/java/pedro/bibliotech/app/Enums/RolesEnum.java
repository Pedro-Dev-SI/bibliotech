package pedro.bibliotech.app.Enums;

public enum RolesEnum {
    ROLE_USER("user"),
    ROLE_ADMIN("admin");

    private String role;

    RolesEnum(String role) {
        this.role = role;
    }
}
