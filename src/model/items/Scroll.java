package model.items;

import model.Model;
import model.characters.GameCharacter;
import model.items.analysis.ItemAnalysis;
import model.items.analysis.ScrollAnalysis;
import model.items.spells.Spell;
import view.*;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scroll extends UsableItem {

    private static Map<MyColors, Sprite> spriteMap = makeSpriteMap();
    private final Spell spell;
    private final Sprite sprite;

    public Scroll(Spell innerSpell) {
        super("Scroll '" + makeAbbreviation(innerSpell.getName()) + "'", (int)(innerSpell.getCost() * 1.5));
        this.spell = innerSpell;
        spell.setCastFromScroll(true);
        this.sprite = spriteMap.get(innerSpell.getColor());
    }

    @Override
    public boolean isStackable() {
        return true;
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public void addYourself(Inventory inventory) {
        inventory.add(this);
    }

    @Override
    public String getShoppingDetails() {
        return ", a scroll of '" + spell.getName() + "'.";
    }

    @Override
    public Scroll copy() {
        return new Scroll(spell);
    }

    @Override
    public String getSound() {
        return "paper";
    }

    public String castFromMenu(Model model, GameCharacter gc) {
        String toReturn = spell.castFromMenu(model, gc);
        if (toReturn.contains("is casting")) {
            model.getParty().removeFromInventory(this);
        }
        return toReturn;
    }

    public Spell getSpell() {
        return spell;
    }

    private static Map<MyColors, Sprite> makeSpriteMap() {
        Map<MyColors, Sprite> map = new HashMap<>();
        map.put(MyColors.WHITE,  new ItemSprite(10, 5, MyColors.GRAY, MyColors.BEIGE));
        map.put(MyColors.GREEN,  new ItemSprite(11, 5, MyColors.GREEN, MyColors.BEIGE));
        map.put(MyColors.BLUE,  new ItemSprite(12, 5, MyColors.BLUE, MyColors.BEIGE));
        map.put(MyColors.RED,  new ItemSprite(13, 5, MyColors.RED, MyColors.BEIGE));
        map.put(MyColors.BLACK,  new ItemSprite(14, 5, MyColors.BLACK, MyColors.BEIGE));
        map.put(Spell.COLORLESS, new ItemSprite(15, 5, MyColors.BROWN, MyColors.BEIGE));
        return map;
    }

    @Override
    public String useYourself(Model model, GameCharacter gc) {
        return null;
    }

    @Override
    public boolean canBeUsedOn(Model model, GameCharacter target) {
        return false;
    }

    @Override
    public boolean isAnalyzable() {
        return true;
    }

    @Override
    public List<ItemAnalysis> getAnalyses(Model model) {
        return List.of(new ScrollAnalysis(this));
    }

    @Override
    public int getWeight() {
        return 0;
    }

    @Override
    public boolean isCraftable() {
        return false;
    }

    @Override
    public boolean isSellable() {
        return true;
    }
}
