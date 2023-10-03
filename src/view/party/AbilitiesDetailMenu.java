package view.party;

import model.Model;
import model.actions.AbilityCombatAction;
import model.characters.GameCharacter;
import model.characters.appearance.RandomAppearance;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.combat.CombatAction;
import model.enemies.Enemy;
import model.enemies.SkeletonEnemy;
import model.items.Equipment;
import model.items.accessories.SkullCap;
import model.items.clothing.LeatherArmor;
import model.items.weapons.Longsword;
import model.races.AllRaces;
import model.races.Race;
import view.MyColors;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class AbilitiesDetailMenu extends FixedPositionSelectableListMenu {
    private final List<String> abilities;

    public AbilitiesDetailMenu(Model model, PartyView partyView, GameCharacter gc, int x, int y) {
        super(partyView, 24, makeAbilities(model, gc).size()+2, x, y);
        this.abilities = makeAbilities(model, gc);
    }

    private static List<String> makeAbilities(Model model, GameCharacter gc) {
        List<String> result = new ArrayList<>();
        Enemy enemy = new SkeletonEnemy('A');
        for (CombatAction act : new AbilityCombatAction(gc, enemy).getInnerActions(model)) {
            result.add(act.getName());
        }
        GameCharacter other = makeDummyCharacter();
        for (CombatAction act : new AbilityCombatAction(gc, other).getInnerActions(model)) {
            if (!result.contains(act.getName())) {
                result.add(act.getName());
            }
        }
        for (CombatAction act : new AbilityCombatAction(gc, gc).getInnerActions(model)) {
            if (!result.contains(act.getName())) {
                result.add(act.getName());
            }
        }
        return result;
    }

    private static GameCharacter makeDummyCharacter() {
        return new GameCharacter("Dummy", "", Race.HALFLING, Classes.CAP, new RandomAppearance(Race.HALFLING),
                new CharacterClass[]{Classes.None, Classes.None, Classes.None, Classes.None},
                new Equipment());
    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        return List.of(new TextDecoration("Combat Abilities:", xStart+1, yStart+1, MyColors.WHITE, MyColors.BLUE, false));
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        List<ListContent> cont = new ArrayList<>();
        int count = 0;
        for (String str : abilities) {
            cont.add(new ListContent( xStart+1, yStart+2+count, str));
            count++;
        }
        return cont;
    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) {

    }
}
