package model.items;

import model.items.weapons.BladedWeapon;
import model.mainstory.honorable.ShingenWeapon;
import model.states.swords.SamuraiSword;
import util.MyStrings;
import view.MyColors;
import view.sprites.Sprite;

public class SpecialEasternWeapon extends BladedWeapon {
    private final SamuraiSword inner;

    public SpecialEasternWeapon(SamuraiSword samuraiSword) {
        super(makeName(samuraiSword), samuraiSword.getCost(),
                samuraiSword.getInnerItem().getDamageTable(),
                samuraiSword.getInnerItem().isTwoHanded(), +1);
        this.inner = samuraiSword;
    }

    @Override
    public int getCriticalTarget() {
        return inner.getInnerItem().getCriticalTarget();
    }

    private static String makeName(SamuraiSword samuraiSword) {
        return MyStrings.capitalize(samuraiSword.getColorName().replace("dark ", "")) + (samuraiSword.hasInscription()
                ? " Inscribed " : " ") + samuraiSword.getTypeName();
    }

    @Override
    protected Sprite getSprite() {
        return inner.getInnerItem().getSprite();
    }

    @Override
    public boolean isCraftable() {
        return false;
    }

    @Override
    public Item copy() {
        throw new IllegalStateException("Special Eastern Weapon should not be copied!");
    }

    public boolean matchesWeaponType(ShingenWeapon shingenWeapon) {
        return inner.matchesWeaponType(shingenWeapon);
    }

    public boolean matchesColor(MyColors shingenColor) {
        return inner.getColorName().equals(SamuraiSword.colorToString(shingenColor));
    }

    public boolean hasInscription() {
        return inner.hasInscription();
    }
}
