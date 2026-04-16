package view.sprites;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.combat.loot.CombatLoot;
import model.enemies.Enemy;
import model.horses.Horse;
import model.horses.Pony;
import model.items.Equipment;
import model.items.weapons.Weapon;
import model.races.Race;
import view.MyColors;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public class RidingSprite extends LoopingSprite {
    private final Sprite avatar;
    private final boolean isPony;
    private boolean isShort = false;

    public RidingSprite(GameCharacter leader, Horse horse, int version) {
        super("riding", "riding.png", 0x10 * version + (horse instanceof Pony ? 4 : 0), 32, 48);
        setFrames(4);
        this.isPony = horse instanceof Pony;
        setColor1(MyColors.BLACK);
        setColor2(horse.getFaceColor());
        setColor3(horse.getAvatarColor());
        setColor4(horse.getPatchColor());
        if (leader.getRace().isShort()) {
            this.isShort = true;
        }
        this.avatar = leader.getAvatarSprite().getStance(Weapon.TWO_HANDED_STANCE);
    }

    public RidingSprite(GameCharacter leader, Horse horse) {
        this(leader, horse, 0);
    }

    public RidingSprite(Enemy enemy, Horse horse) {
        super("riding", "riding.png",  (horse instanceof Pony ? 4 : 0), 32, 48);
        setFrames(4);
        setColor1(MyColors.BLACK);
        this.isPony = horse instanceof Pony;
        setColor2(horse.getFaceColor());
        setColor3(horse.getAvatarColor());
        setColor4(horse.getPatchColor());
        this.isShort = false;
        this.avatar = enemy.getAvatar();
    }

    public RidingSprite(Horse horse) {
        this(new DummyEnemy(), horse);
    }

    @Override
    protected BufferedImage internalGetImage() throws IOException {
        BufferedImage img = super.internalGetImage();
        BufferedImage toReturn = new BufferedImage(getWidth(), getHeight(), img.getType());
        Graphics g = toReturn.getGraphics();
        if (avatar != null) {
            BufferedImage avatarImg = avatar.internalGetImage();
            int yOffset = 0;
            if (isShort) {
                yOffset = -2;
            }
            if (isPony) {
                yOffset += 2;
            }
            g.drawImage(avatarImg, 0, yOffset, avatarImg.getWidth(), avatarImg.getHeight(), null);
        }
        g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
        return toReturn;
    }

    private static class DummyEnemy extends Enemy {
        public DummyEnemy() {
            super('A', "Dummy");
        }

        @Override
        protected Sprite getSprite() {
            return null;
        }

        @Override
        public int getDamage() {
            return 0;
        }

        @Override
        public CombatLoot getLoot(Model model) {
            return null;
        }

        @Override
        public int getMaxHP() {
            return 0;
        }

        @Override
        public int getSpeed() {
            return 0;
        }

        @Override
        public String getDeathSound() {
            return "";
        }
    }
}
