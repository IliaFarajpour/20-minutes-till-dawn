package com.Project.utils;

import com.Project.model.Weapon;

public class WeaponFactory {
    public static Weapon createRevolver() {
        return new Weapon("Revolver", 6, 1f, 1, 20);
    }

    public static Weapon createShotgun() {
        return new Weapon("Shotgun", 8, 1f, 4, 10);
    }

    public static Weapon createDualSMGs() {
        return new Weapon("SMGs Dual", 24, 2f, 1, 8);
    }
}
