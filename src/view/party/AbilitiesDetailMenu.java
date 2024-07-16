package view.party;

import model.Model;
import model.actions.AbilityCombatAction;
import model.characters.GameCharacter;
import model.characters.appearance.RandomAppearance;
import model.classes.Classes;
import model.actions.CombatAction;
import model.actions.PassiveCombatAction;
import model.enemies.Enemy;
import model.enemies.SkeletonEnemy;
import model.items.Equipment;
import model.races.Race;
import view.MyColors;
import view.help.SpellMasteryHelpChapter;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbilitiesDetailMenu extends FixedPositionSelectableListMenu {
    private final List<String> abilities;
    private static Map<String, CombatAction> abilityDictionary;

    public AbilitiesDetailMenu(Model model, PartyView partyView, GameCharacter gc, int x, int y) {
        super(partyView, findWidthOfDialog(makeAbilities(model, gc))+1,
                makeAbilities(model, gc).size()+3, x, y);
        this.abilities = makeAbilities(model, gc);
    }

    private static List<String> makeAbilities(Model model, GameCharacter gc) {
        List<String> result = new ArrayList<>();
        Enemy enemy = new SkeletonEnemy('A');
        abilityDictionary = new HashMap<>();
        addAbilityEntries(model, new AbilityCombatAction(gc, enemy), result);
        GameCharacter other = makeDummyCharacter();
        addAbilityEntries(model, new AbilityCombatAction(gc, other), result);
        addAbilityEntries(model, new AbilityCombatAction(gc, gc), result);
        addPassiveAbilities(gc, result);
        result.addAll(gc.getMasteries().getAbilityList());
        return result;
    }

    private static void addPassiveAbilities(GameCharacter gc, List<String> result) {
        for (PassiveCombatAction passive : AbilityCombatAction.getPassiveCombatActions(gc)) {
            result.add(passive.getName());
            abilityDictionary.put(passive.getName(), passive);
        }
    }

    private static void addAbilityEntries(Model model, AbilityCombatAction abilityCombatAction, List<String> result) {
        for (CombatAction act : abilityCombatAction.getInnerActions(model)) {
            if (!result.contains(act.getName())) {
                result.add(act.getName());
                if (!abilityDictionary.containsKey(act.getName())) {
                    abilityDictionary.put(act.getName(), act);
                }
            }
        }
    }

    private static GameCharacter makeDummyCharacter() {
        return new GameCharacter("Dummy", "", Race.HALFLING, Classes.CAP, new RandomAppearance(Race.HALFLING),
                Classes.NO_OTHER_CLASSES,
                new Equipment());
    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        List<DrawableObject> result = new ArrayList<>();
        result.add(new TextDecoration("Combat Abilities:", xStart+1, yStart+1, MyColors.WHITE, MyColors.BLUE, false));
        if (abilities.size() > abilityDictionary.size()) {
            result.add(new TextDecoration("Spell Masteries:", xStart+1, yStart+2+abilityDictionary.size(), MyColors.WHITE, MyColors.BLUE, false));
        }
        return result;
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        List<ListContent> cont = new ArrayList<>();
        int count = 0;
        boolean skipped = false;
        for (String str : abilities) {
            if (!abilityDictionary.containsKey(str) && !skipped) {
                skipped = true;
                count++;
            }
            cont.add(new SelectableListContent(xStart + 1, yStart + 2 + count, str) {
                @Override
                public void performAction(Model model, int x, int y) {
                    if (abilityDictionary.containsKey(str)) {
                        setInnerMenu(abilityDictionary.get(str).getHelpChapter(model), model);
                    } else {
                        setInnerMenu(new SpellMasteryHelpChapter(model.getView(), str), model);
                    }
                }

                @Override
                public boolean isEnabled(Model model) {
                    return true;
                }
            });
            count++;
        }
        return cont;
    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) {

    }

    public int getNoOfAbilities() {
        return abilities.size();
    }

    private static int findWidthOfDialog(List<String> makeAbilities) {
        int longest = 0;
        for (String str : makeAbilities) {
            if (str.length() >  longest) {
                longest = str.length();
            }
        }
        return Math.max(longest, 20);
    }
}
