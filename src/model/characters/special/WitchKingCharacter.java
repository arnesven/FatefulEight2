package model.characters.special;

import model.Model;
import model.characters.GameCharacter;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.combat.conditions.Condition;
import model.combat.conditions.VampirismCondition;
import model.races.Race;
import model.races.WitchKingRace;
import util.MyLists;

import java.util.List;

import static model.classes.Classes.None;

public class WitchKingCharacter extends GameCharacter {
    public WitchKingCharacter() {
        super("Witch King", "", Race.WITCH_KING, Classes.WITCH_KING,
                new WitchKingAppearance(), new CharacterClass[]{Classes.WITCH_KING, None, None, None});
    }

    public static GameCharacter getFromParty(Model model) {
        List<GameCharacter> candidates = MyLists.filter(model.getParty().getPartyMembers(), (GameCharacter gc) -> gc.getRace().id() == Race.WITCH_KING.id());
        if (candidates.isEmpty()) {
            return null;
        }
        return candidates.get(0);
    }

   public static boolean isInParty(Model model) {
        return getFromParty(model) != null;
   }

    @Override
    protected boolean hasConditionImmunity(Condition cond) {
        return cond instanceof VampirismCondition;
    }
}
