package model.items.weapons;

import model.Model;
import model.actions.CombatAction;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.combat.abilities.SpecialAbilityCombatAction;
import model.items.Item;
import model.items.Prevalence;
import model.items.SocketedItem;
import model.items.spells.AuxiliarySpell;
import model.items.spells.CombatSpell;
import model.items.spells.Spell;
import model.states.CombatEvent;
import util.MyLists;
import view.GameView;
import view.MyColors;
import view.help.HelpDialog;
import view.party.ConfigureSocketedItemMenu;
import view.party.SelectableListMenu;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

import java.util.ArrayList;
import java.util.List;

public class StaffOfDeimosItem extends StaffWeapon implements SocketedItem {

    private static final Sprite SPRITE = new TwoHandedItemSprite(10, 12, MyColors.DARK_RED, MyColors.TAN, MyColors.CYAN);

    private CombatSpell spell = null;

    public StaffOfDeimosItem() {
        super("Staff of Deimos", 3000, new int[]{6, 10});
    }

    @Override
    public boolean isSellable() {
        return false;
    }

    @Override
    public boolean isCraftable() {
        return false;
    }

    @Override
    public boolean supportsHigherTier() {
        return false;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new StaffOfDeimosItem();
    }

    @Override
    public int getNumberOfSockets() {
        return 1;
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.unique;
    }

    @Override
    public Item getInnerItem(int index) {
        return spell;
    }

    @Override
    public void setInnerItem(int index, Item it) {
        spell = (CombatSpell) it;
    }

    @Override
    public String getSocketLabels() {
        return "Spell   ";
    }

    @Override
    public List<Item> getItemsForSlot(Model model, int index) {
        List<Item> result = new ArrayList<>(
                MyLists.filter(model.getParty().getInventory().getSpells(),
                        (Spell sp) -> sp instanceof CombatSpell));
        result.addAll(MyLists.transform(MyLists.filter(model.getParty().getInventory().getSpells(),
                (Spell sp) -> sp instanceof AuxiliarySpell && ((AuxiliarySpell) sp).canBeCastInCombat()),
                (Spell sp) -> ((AuxiliarySpell)sp).getCombatSpell()));
        return result;
    }

    @Override
    public boolean hasDualUseInMenu() {
        return true;
    }

    @Override
    public String getDualUseLabel() {
        return "Configure";
    }

    @Override
    public SelectableListMenu getDualUseMenu(GameView innerView, int x, int y) {
        return new ConfigureSocketedItemMenu(innerView, this);
    }

    public static boolean canDoAbility(GameCharacter performer) {
        return performer.getEquipment().getWeapon().isOfType(StaffOfDeimosItem.class);
    }

    public static SpecialAbilityCombatAction makeCombatAbility(GameCharacter performer) {
        return new AutoCastAbility((StaffOfDeimosItem)performer.getEquipment().getWeapon());
    }

    private static class AutoCastAbility extends SpecialAbilityCombatAction {

        private final StaffOfDeimosItem staff;

        public AutoCastAbility(StaffOfDeimosItem weapon) {
            super("Auto-Cast", false, false);
            this.staff = weapon;
        }

        @Override
        public HelpDialog getHelpChapter(Model model) {
            return new HelpDialog(model.getView(), "Auto-Cast", "While wielding the Staff of Deimos you have the " +
                    "ability to automatically cast a spell which the staff has been configured with.");
        }

        @Override
        protected void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
            if (staff.spell.canBeCastOn(model, target)) {
                staff.spell.applyCombatEffect(model, combat, performer, target);
            } else {
                model.getLog().addAnimated("Nothing happened...\n");
            }
        }

        @Override
        public boolean possessesAbility(Model model, GameCharacter performer) {
            return meetsOtherRequirements(model, performer, null);
        }

        @Override
        protected boolean meetsOtherRequirements(Model model, GameCharacter performer, Combatant target) {
            return StaffOfDeimosItem.canDoAbility(performer);
        }
    }
}
