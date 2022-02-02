package io.github.dankoller.springaccountingservice.entity;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Optional;

public enum Role {

    ACCOUNTANT("ROLE_ACCOUNTANT"),
    ADMINISTRATOR("ROLE_ADMINISTRATOR"),
    AUDITOR("ROLE_AUDITOR"),
    USER("ROLE_USER"),
    ; // Role will be sorted by order of declaration

    private final String authority;
    private static final EnumSet<Role> enumSet = EnumSet.allOf(Role.class);

    Role(String authority) {
        this.authority = authority;
    }

    public static boolean contains(String authority) {
        return enumSet.stream().map(Role::getAuthority).anyMatch(authority::equals);
    }

    @JsonValue
    public String getAuthority() {
        return authority;
    }

    public static boolean isAdministrative(Role authority){
        return authority.equals(ADMINISTRATOR);
    }

    public static boolean isAdministrative(Collection<Role> authorities) {
        return authorities.stream().allMatch(role -> role.equals(ADMINISTRATOR));
    }

    public static boolean isBusiness(Role authority) {
        return enumSet.stream().filter(role -> role != ADMINISTRATOR)
                .anyMatch(role -> role.equals(authority));
    }

    public static boolean isBusiness(Collection<Role> authorities) {
        return enumSet.stream().filter(role -> role != ADMINISTRATOR)
                .allMatch(authorities::contains);
    }

    public static Optional<Role> fromString(String authority){
        return enumSet.stream().filter(role -> role.authority.equals(authority)).findFirst();
    }
}