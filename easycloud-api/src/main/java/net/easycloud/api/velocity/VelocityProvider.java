package net.easycloud.api.velocity;

import lombok.Getter;

import java.util.Random;

@Getter
public final class VelocityProvider {
    private final String privateKey;

    public VelocityProvider() {
        this.privateKey = generateKey();

    }

    private String generateKey() {
        String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerAlphabet = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";

        String alphaNumeric = upperAlphabet + lowerAlphabet + numbers;
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        int length = 10;
        for(int i = 0; i < length; i++) {
            int index = random.nextInt(alphaNumeric.length());
            char randomChar = alphaNumeric.charAt(index);

            sb.append(randomChar);
        }
        return sb.toString();
    }
}
