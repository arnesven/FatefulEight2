package model.items.clothing;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public abstract class RobesClothing extends Clothing {
    private final ItemSprite sprite;
    private final Skill magicSkill;

    public RobesClothing(String name, Skill skill, int spriteNum, MyColors color2, MyColors color3, MyColors color4) {
        super(name, 16, 1, false);
        this.sprite = new ItemSprite(spriteNum, 2, color2, color3, color4);
        this.magicSkill = skill;
    }

    public RobesClothing(String name, Skill skill, int spriteNum, MyColors color2, MyColors color3) {
        this(name, skill, spriteNum, color2, color3, MyColors.CYAN);
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(magicSkill, 1));
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public int getWeight() {
        return 1500;
    }
}
