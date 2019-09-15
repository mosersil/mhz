package com.silviomoser.mhz.security.utils;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordGenerator;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.silviomoser.mhz.utils.StringUtils.isNotBlank;

public class PasswordUtils {

    public static String generateToken(int length, boolean specialCharacters) {
        PasswordGenerator passwordGenerator = new PasswordGenerator();
        List<CharacterRule> ruleList = new ArrayList<>();
        ruleList.add(new CharacterRule(EnglishCharacterData.UpperCase, 1));
        ruleList.add(new CharacterRule(EnglishCharacterData.LowerCase, 1));
        ruleList.add(new CharacterRule(EnglishCharacterData.Digit, 1));
        if (specialCharacters) {
            ruleList.add(new CharacterRule(EnglishCharacterData.Special, 1));
        }
        return passwordGenerator.generatePassword(length,
                ruleList
        );

    }

    /**
     * Ensure that chosen password matches with the confirmation and the password fulfills the complexity requirements
     *
     * @param password
     * @param password_confirmation
     * @return true if everything is OK
     */
    public static boolean isValidPassword(String password, String password_confirmation) {
        return (isNotBlank(password) &&
                isNotBlank(password_confirmation)
                && password.equals(password_confirmation)
                && isValidPassword(password));
    }

    /**
     * Ensures the chosen password is compliant with the password policy
     *
     * @param password
     * @return
     */
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

    public static String hashPassword(String input) {
        final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(input);
    }

    public static boolean matches(CharSequence rawPassword, String encodedPassword) {
        final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

}
