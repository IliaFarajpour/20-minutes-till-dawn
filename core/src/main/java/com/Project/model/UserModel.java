package com.Project.model;

import com.badlogic.gdx.Input;

public class UserModel {
    private String username;
    private String password;
    private String securityQuestion;
    private String securityAnswer;
    private String avatar;
    private int score;
    private boolean sfxEnabled = true;
    private float surviveTimeSeconds = 0f;
    private int kills = 0;
    private int keyUp = Input.Keys.W;
    private int keyDown = Input.Keys.S;
    private int keyLeft = Input.Keys.A;
    private int keyRight = Input.Keys.D;
    private int keyReload = Input.Keys.R;
    private boolean grayscale = false;
    private int keyAutoAim = Input.Keys.SPACE;

    private int mouseShootButton = Input.Buttons.LEFT;

    public UserModel(String username, String password, String securityQuestion, String securityAnswer, String avatar) {
        this.username = username;
        this.password = password;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
        this.avatar = avatar;
        this.score = 0;
    }

    public static boolean isWeakPassword(String password) {
        return password.length() < 8 ||
            !password.matches(".*[A-Z].*") ||
            !password.matches(".*\\d.*") ||
            !password.matches(".*[^a-zA-Z0-9].*");
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getSecurityQuestion() { return securityQuestion; }
    public String getSecurityAnswer() { return securityAnswer; }
    public String getAvatar() { return avatar; }

    public void setAvatar(String avatar) { this.avatar = avatar; }
    public void setPassword(String password) { this.password = password; }
    public void setUsername(String username) { this.username = username; }

    public void setScore(int score) { this.score = score; }

    public boolean isSfxEnabled() { return sfxEnabled; }
    public void setSfxEnabled(boolean sfxEnabled) { this.sfxEnabled = sfxEnabled; }

    public int getKeyUp() { return keyUp; }
    public void setKeyUp(int keyUp) { this.keyUp = keyUp; }

    public int getKeyDown() { return keyDown; }
    public void setKeyDown(int keyDown) { this.keyDown = keyDown; }

    public int getKeyLeft() { return keyLeft; }
    public void setKeyLeft(int keyLeft) { this.keyLeft = keyLeft; }

    public int getKeyRight() { return keyRight; }
    public void setKeyRight(int keyRight) { this.keyRight = keyRight; }

    public int getKeyReload() { return keyReload; }
    public void setKeyReload(int keyReload) { this.keyReload = keyReload; }


    public int getKeyAutoAim() { return keyAutoAim; }
    public void setKeyAutoAim(int keyAutoAim) { this.keyAutoAim = keyAutoAim; }

    public int getMouseShootButton() { return mouseShootButton; }
    public void setMouseShootButton(int mouseShootButton) { this.mouseShootButton = mouseShootButton; }

    public void updateSurviveTime(float delta) {
        surviveTimeSeconds += delta;
    }

    public float getSurviveTimeSeconds() {
        return surviveTimeSeconds;
    }
    public void setSurviveTimeSeconds(float surviveTimeSeconds) {
        this.surviveTimeSeconds = surviveTimeSeconds;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void addKill() {
        kills++;
    }

    public int getKills() {
        return kills;
    }

    public int getScore() {
        return (int)(surviveTimeSeconds * kills);
    }
    public boolean isGrayscale() {
        return grayscale;
    }

    public void setGrayscale(boolean grayscale) {
        this.grayscale = grayscale;
    }
}
