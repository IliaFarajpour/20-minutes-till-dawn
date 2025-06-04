package com.Project.model;

public class Weapon {
    private final String name;
    private int maxAmmo;
    private final float reloadTimeSeconds;
    private int projectileCount;
    private int damage;

    public Weapon(String name, int maxAmmo, float reloadTimeSeconds, int projectileCount, int damage) {
        this.name = name;
        this.maxAmmo = maxAmmo;
        this.reloadTimeSeconds = reloadTimeSeconds;
        this.projectileCount = projectileCount;
        this.damage = damage;
    }

    public String getName() {
        return name;
    }

    public int getMaxAmmo() {
        return maxAmmo;
    }

    public void setMaxAmmo(int maxAmmo) {
        this.maxAmmo = maxAmmo;
    }

    public float getReloadTimeSeconds() {
        return reloadTimeSeconds;
    }

    public int getProjectileCount() {
        return projectileCount;
    }

    public void setProjectileCount(int projectileCount) {
        this.projectileCount = projectileCount;
    }

    public int getDamage() {
        return damage;
    }
    public void setDamage(int damage) {
        this.damage = damage;
    }

    @Override
    public String toString() {
        return name;
    }
}
