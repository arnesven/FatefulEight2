package model.combat.conditions;

import model.classes.Skill;
import util.MyStrings;
import view.GameView;
import view.MyColors;
import view.VampireAbilityInfoDialog;
import view.help.HelpDialog;
import view.party.SelectableListMenu;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.io.Serializable;

public abstract class VampireAbility implements Serializable {
    private final Sprite32x32 sprite;
    public String name;
    public String description;

    public VampireAbility(String name, int spriteNum, String description) {
        this.name = name;
        this.sprite = new Sprite32x32("vampabi" + spriteNum, "quest.png", spriteNum,
                MyColors.BLACK, MyColors.RED, MyColors.BLACK, MyColors.CYAN);
        this.description = description;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public SelectableListMenu makeInfoDialog(GameView previous, boolean withSelect) {
        int WIDTH = 25;
        String[] parts = MyStrings.partitionWithLineBreaks(description, WIDTH-2);
        return new VampireAbilityInfoDialog(previous, WIDTH, parts, withSelect);
    }

    public int getBonusForSkill(Skill skill) {
        return 0;
    }

    public abstract HelpDialog makeHelpChapter(GameView view);
}
