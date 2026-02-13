package model.characters.special;

import model.characters.GameCharacter;
import model.characters.preset.LonnieLiebgott;
import model.classes.Classes;
import model.enemies.Enemy;
import model.items.Equipment;
import model.items.accessories.GrayRing;
import model.items.clothing.FullPlateArmor;
import model.items.clothing.LeatherArmor;
import model.items.clothing.ScaleArmor;
import model.items.weapons.*;
import model.races.Race;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.AvatarSprite;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;

public class AllyFromEnemyCharacter extends GameCharacter {
    private static final Sprite DEAD_SPRITE = new Sprite32x32("deadally", "enemies.png", 0xBF,
            MyColors.BLACK, MyColors.WHITE, MyColors.GRAY, MyColors.PEACH);
    private final Enemy enemy;

    public AllyFromEnemyCharacter(Enemy enemy) {
        super(enemy.getName(), "", Race.NORTHERN_HUMAN, Classes.CAP, new LonnieLiebgott(),
                makeEquipmentFrom(enemy));
        setLevel(1 + enemy.getMaxHP() / 4 + enemy.getDamage() / 3);
        this.enemy = enemy;
    }

    @Override
    public void drawAvatar(ScreenHandler screenHandler, int xpos, int ypos) {
        Sprite spr;
        if (!isDead()) {
            spr = enemy.getAvatar();
        } else {
            spr = DEAD_SPRITE;
        }
        screenHandler.register(spr.getName(), new Point(xpos, ypos), spr);
    }

    private static Equipment makeEquipmentFrom(Enemy enemy) {
        Equipment eq = new Equipment();
        if (enemy.getDamageReduction() > 2) {
            eq.setClothing(new FullPlateArmor()); // 7
        } else if (enemy.getDamageReduction() > 1) {
            eq.setClothing(new ScaleArmor()); // 5
        } else if (enemy.getDamageReduction() > 0) {
            eq.setClothing(new LeatherArmor()); // 3
        }
        if (enemy.getDamage() > 3) {
            eq.setWeapon(new Claymore());
        } else if (enemy.getDamage() > 2) {
            eq.setWeapon(new Broadsword());
        } else if (enemy.getDamage() > 1) {
            eq.setWeapon(new ShortSword());
        } else {
            eq.setWeapon(new Dirk());
        }
        return eq;
    }
}
