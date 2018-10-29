package com.silviomoser.demo.security.password;

import com.google.common.base.Joiner;
import org.passay.*;

import java.util.Arrays;

import static com.silviomoser.demo.utils.StringUtils.isNotBlank;

public class PasswordUtils {

    public static boolean isValidPassword(String password, String password_confirmation) {
        return (isNotBlank(password) &&
                isNotBlank(password_confirmation)
                && password.equals(password_confirmation)
                && isValidPassword(password));
    }

    public static boolean isValidPassword(String password) {
        PasswordValidator validator = new PasswordValidator(Arrays.asList(
                new LengthRule(8, 16),

                // at least one upper-case character
                new CharacterRule(EnglishCharacterData.UpperCase, 1),

                // at least one lower-case character
                new CharacterRule(EnglishCharacterData.LowerCase, 1),

                // at least one digit character
                new CharacterRule(EnglishCharacterData.Digit, 1),

                // at least one symbol (special character)
                new CharacterRule(EnglishCharacterData.Special, 1),

                // no whitespace
                new WhitespaceRule()));

        RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }
        return false;
    }
}
