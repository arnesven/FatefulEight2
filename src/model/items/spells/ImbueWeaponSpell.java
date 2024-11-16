package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.items.Item;
import model.items.ItemDeck;
import model.items.imbuements.WeaponImbuement;
import model.items.weapons.NaturalWeapon;
import model.items.weapons.Weapon;
import model.states.GameState;
import util.MyLists;
import util.MyRandom;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;
import view.subviews.ArrowMenuSubView;

import java.util.ArrayList;
import java.util.List;

public class ImbueWeaponSpell extends ImmediateSpell {
    private static final Sprite SPRITE = new ItemSprite(4, 8, MyColors.BROWN, MyColors.PEACH, MyColors.BLACK);
    private Weapon weaponToImbue;

    public ImbueWeaponSpell() {
        super("Imbue Weapon", 44, COLORLESS, 11, 5);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new ImbueWeaponSpell();
    }

    @Override
    protected boolean preCast(Model model, GameState state, GameCharacter caster) {
        state.print(caster.getName() + " is preparing to imbue a weapon with magic. Do you want to imbue " +
                "an equipped weapon (Y) or a weapon from your inventory(N)? ");
        Weapon w = null;
        if (state.yesNoInput()) {
            w = getEquippedWeapon(model, state);
        } else {
            w = getInventoryWeapon(model, state);
        }
        if (w == null) {
            state.println("Spell cancelled.");
            return false;
        }
        if (w instanceof NaturalWeapon || w.isImbued()) {
            state.println(w.getName() + " cannot be imbued with magic.");
            return false;
        }
        this.weaponToImbue = w;
        state.print("Are you sure you want to permanently imbue the " + weaponToImbue.getName() + " with magic? (Y/N) ");
        if (!state.yesNoInput()) {
            return false;
        }
        return true;
    }

    @Override
    protected void applyAuxiliaryEffect(Model model, GameState state, GameCharacter caster) {
        WeaponImbuement imbuement = MyRandom.sample(ItemDeck.allImbuements());
        weaponToImbue.setImbuement(imbuement);
        state.println(weaponToImbue.getName() + " was imbued with magic!");
        caster.addToSP(-caster.getSP());

        state.partyMemberSay(caster, MyRandom.sample(List.of("That was taxing...", "I'm finished.",
                "Ugh... I think I need a nap.", "I feel completely wasted.")));
        state.println(caster.getName() + " is completely exhausted from casting the spell!");
    }

    private Weapon getInventoryWeapon(Model model, GameState state) {
        List<String> options = new ArrayList<>(MyLists.transform(model.getParty().getInventory().getWeapons(),
                Weapon::getName));
        options.add("Cancel");
        final String[] selected = {null};
        model.setSubView(new ArrowMenuSubView(model.getSubView(), options, 28, 24 - options.size()*2, ArrowMenuSubView.NORTH_WEST) {
            @Override
            protected void enterPressed(Model model, int cursorPos) {
                selected[0] = options.get(cursorPos);
                model.setSubView(getPrevious());
            }
        });
        for (Weapon weapon : model.getParty().getInventory().getWeapons()) {
            if (weapon.getName().equals(selected[0])) {
                return weapon;
            }
        }
        return null;
    }

    private Weapon getEquippedWeapon(Model model, GameState state) {
        if (model.getParty().size() == 1) {
            return model.getParty().getPartyMember(0).getEquipment().getWeapon();
        }
        state.println("Whose weapon do you want to imbue with magic?");
        GameCharacter gc = model.getParty().partyMemberInput(model, state, model.getParty().getPartyMember(0));
        return gc.getEquipment().getWeapon();
    }

    @Override
    public String getDescription() {
        return "Imbues a weapon with magic.";
    }
}
