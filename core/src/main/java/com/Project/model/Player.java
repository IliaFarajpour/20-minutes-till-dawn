package com.Project.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import java.util.ArrayList;
import java.util.Collections;

public class Player {

    private Vector2 position = new Vector2(300, 300);
    private Vector2 velocity = new Vector2();

    private boolean reloading = false;
    private Array<Bullet> bullets = new Array<>();

    private Weapon weapon;
    private int ammo;
    private float surviveTimeSeconds = 0f;
    private int level = 1;
    private int xp = 0;
    private int xpToNextLevel = 60;
    private LevelUpListener levelUpListener;
    private HeroInfo heroInfo;

    private int hp;
    private boolean invincible = false;
    private float invincibleTimer = 0f;
    private static final float INVINCIBLE_DURATION = 1f;

    private boolean abilitySelectionActive = false;
    private AbilityType[] abilityOptions = new AbilityType[3];

    private boolean speedyActive = false;
    private float speedyTimer = 0f;
    private static final float SPEEDY_DURATION = 10f;

    // --- متغیرهای افکت ضربه ---
    private boolean damageEffectActive = false;
    private float damageEffectTimer = 0f;
    private static final float DAMAGE_EFFECT_DURATION = 0.5f;

    public Player(Weapon weapon, HeroInfo heroInfo) {
        this.weapon = weapon;
        this.ammo = weapon.getMaxAmmo();
        this.heroInfo = heroInfo;
        this.hp = heroInfo.getHp();
    }

    public void move(Vector2 direction, float deltaTime) {
        if (direction.len() > 0 && heroInfo != null) {
            float baseSpeed = heroInfo.getSpeed() * 50f;
            float speedMultiplier = speedyActive ? 2f : 1f;
            float speed = baseSpeed * speedMultiplier;
            velocity.set(direction).nor().scl(speed * deltaTime);
            position.add(velocity);
        } else {
            velocity.set(0, 0);
        }
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void shoot(Vector2 direction) {
        final float BULLET_SPEED = 600f;

        if (!reloading && ammo > 0) {
            if (weapon.getName().equals("Shotgun")) {
                int projectileCount = weapon.getProjectileCount();
                float spreadAngle = 30f;
                float angleStep = (projectileCount > 1) ? spreadAngle / (projectileCount - 1) : 0;
                float startAngle = -spreadAngle / 2f;

                for (int i = 0; i < projectileCount; i++) {
                    if (ammo <= 0) break;

                    float angle = startAngle + i * angleStep;
                    Vector2 bulletDirection = direction.cpy().nor().rotateDeg(angle).scl(BULLET_SPEED);

                    Bullet bullet = new Bullet(
                        new Vector2(position),
                        bulletDirection,
                        "bullet.png"
                    );
                    bullets.add(bullet);
                    ammo--;
                }
            } else {
                Vector2 bulletDirection = direction.cpy().nor().scl(BULLET_SPEED);
                Bullet bullet = new Bullet(
                    new Vector2(position),
                    bulletDirection,
                    "bullet.png"
                );
                bullets.add(bullet);
                ammo--;
            }
        }
    }

    public void refillBullets() {
        ammo = weapon.getMaxAmmo();
        System.out.println("Ammo refilled!");
    }

    public void update(float deltaTime) {
        updateSurviveTime(deltaTime);
        updateBullets(deltaTime);

        if (invincible) {
            invincibleTimer -= deltaTime;
            if (invincibleTimer <= 0) {
                invincible = false;
            }
        }

        if (speedyActive) {
            speedyTimer -= deltaTime;
            if (speedyTimer <= 0) {
                speedyActive = false;
                System.out.println("Speedy effect ended");
            }
        }

        if (damageEffectActive) {
            damageEffectTimer -= deltaTime;
            if (damageEffectTimer <= 0) {
                damageEffectActive = false;
            }
        }
    }

    public void updateBullets(float deltaTime) {
        for (Bullet bullet : bullets) {
            bullet.update(deltaTime);
        }

        Vector2 playerPos = this.position;
        float maxDistance = 1000f;

        for (int i = bullets.size - 1; i >= 0; i--) {
            Vector2 pos = bullets.get(i).getPosition();
            if (pos.dst(playerPos) > maxDistance) {
                bullets.get(i).dispose();
                bullets.removeIndex(i);
            }
        }
    }

    public void takeDamage(int damage, boolean isBulletHit) {
        if (!invincible) {
            hp -= damage;
            if (hp < 0) hp = 0;
            invincible = true;
            invincibleTimer = INVINCIBLE_DURATION;

            if(isBulletHit) {
                triggerDamageEffect();
            }
        }
    }

    public void triggerDamageEffect() {
        damageEffectActive = true;
        damageEffectTimer = DAMAGE_EFFECT_DURATION;
    }

    public boolean isDamageEffectActive() {
        return damageEffectActive;
    }

    public int getHp() {
        return hp;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public Vector2 getPosition() {
        return position;
    }

    public boolean isReloading() {
        return reloading;
    }

    public void setReloading(boolean reloading) {
        this.reloading = reloading;
    }

    public Array<Bullet> getBullets() {
        return bullets;
    }

    public int getAmmo() {
        return ammo;
    }

    public int getLevel() {
        return level;
    }

    public int getXp() {
        return xp;
    }

    public int getXpToNextLevel() {
        return xpToNextLevel;
    }

    public void addXP(int amount) {
        xp += amount;
        System.out.println("XP gained! Current XP: " + xp);
        checkLevelUp();
    }

    private void checkLevelUp() {
        while (xp >= xpToNextLevel) {
            xp -= xpToNextLevel;
            level++;
            xpToNextLevel = 20 * level;
            activateAbilitySelection();

            if (levelUpListener != null) {
                levelUpListener.onLevelUp();
            }
        }
    }

    public void activateAbilitySelection() {
        abilitySelectionActive = true;

        AbilityType[] allAbilities = AbilityType.values();
        java.util.List<AbilityType> list = new ArrayList<>();
        Collections.addAll(list, allAbilities);
        Collections.shuffle(list);

        for (int i = 0; i < 3; i++) {
            abilityOptions[i] = list.get(i);
        }
    }

    public boolean isAbilitySelectionActive() {
        return abilitySelectionActive;
    }

    public AbilityType[] getAbilityOptions() {
        return abilityOptions;
    }

    public void chooseAbility(int index) {
        if (!abilitySelectionActive) return;
        if (index < 0 || index >= abilityOptions.length) return;

        AbilityType chosen = abilityOptions[index];
        applyAbility(chosen);

        abilitySelectionActive = false;
    }

    private void applyAbility(AbilityType ability) {
        switch (ability) {
            case VITALITY:
                hp += 1;
                System.out.println("Ability Applied: VITALITY (HP +1)");
                break;
            case DAMAGER:
                weapon.setDamage(Math.round(weapon.getDamage() * 1.25f));
                System.out.println("Ability Applied: DAMAGER (25% Damage Increase)");
                break;
            case PROJECTILE:
                weapon.setProjectileCount(weapon.getProjectileCount() + 1);
                System.out.println("Ability Applied: PROJECTILE (+1 projectile)");
                break;
            case AMO_INCREASE:
                weapon.setMaxAmmo(weapon.getMaxAmmo() + 5);
                System.out.println("Ability Applied: AMO_INCREASE (+5 max ammo)");
                break;
            case SPEEDY:
                speedyActive = true;
                speedyTimer = SPEEDY_DURATION;
                System.out.println("Ability Applied: SPEEDY (Double speed for 10 seconds)");
                break;
        }
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void updateSurviveTime(float delta) {
        surviveTimeSeconds += delta;
    }


    public HeroInfo getHeroInfo() {
        return heroInfo;
    }
    public void setLevelUpListener(LevelUpListener listener) {
        this.levelUpListener = listener;
    }
}
