package io.github.dankoller.springaccountingservice.misc;

import java.util.List;

public class PasswordBlacklist {

    // Breached password list
    public static final List<String> blacklist = List.of(
            "PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch", "PasswordForApril",
            "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
            "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember"
    );
}
