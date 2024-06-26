package com.hackathon.backend.utilities.user;

import java.util.concurrent.*;

public class PasswordChecker {

    public static boolean isStrongPassword(String password){
        if (password == null || password.length() < 8) {
            return false;
        }

        int numThreads = 4;
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        Callable<Boolean> isUppercase = () -> containsUppercase(password);
        Callable<Boolean> isLowercase = () -> containsLowercase(password);
        Callable<Boolean> isDigit = () -> containsDigit(password);
        Callable<Boolean> isSpecial = () -> containsSpecialCharacter(password);

        try{
            Future<Boolean> futureUppercase = executorService.submit(isUppercase);
            Future<Boolean> futureLowercase = executorService.submit(isLowercase);
            Future<Boolean> futureDigit = executorService.submit(isDigit);
            Future<Boolean> futureSpecial = executorService.submit(isSpecial);

            executorService.shutdown();

            return futureUppercase.get() && futureLowercase.get() && futureDigit.get() && futureSpecial.get();
        }catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
            executorService.shutdown();
            return false;
        }
    }

    private static boolean containsUppercase(String password) {
        return password.chars().anyMatch(Character::isUpperCase);
    }

    private static boolean containsLowercase(String password) {
        return password.chars().anyMatch(Character::isLowerCase);
    }

    private static boolean containsDigit(String password) {
        return password.chars().anyMatch(Character::isDigit);
    }

    private static boolean containsSpecialCharacter(String password) {
        return password.chars().anyMatch(c -> !Character.isLetterOrDigit(c));
    }
}
