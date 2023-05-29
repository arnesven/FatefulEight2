package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Classes;
import model.classes.Skill;
import model.items.Item;
import model.items.spells.Spell;
import model.states.DailyEventState;
import model.states.ShopState;
import util.MyPair;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class MageEvent extends DailyEventState {
    private final boolean withIntro;

    public MageEvent(Model model, boolean withIntro) {
        super(model);
        this.withIntro = withIntro;
    }

    public MageEvent(Model model) {
        this(model, true);
    }

    @Override
    protected void doEvent(Model model) {
        if (withIntro) {
            println("The party encounters a mage who seems eager to discuss the more academic aspects of magic.");
            showRandomPortrait(model, Classes.MAGE, "Mage");
            portraitSay("So friend, please tell me, which is your favorite spell?");
            if (model.getParty().getInventory().getSpells().isEmpty() ||
                    MyRandom.rollD10() > model.getParty().getInventory().getSpells().size() + 5) {
                MyPair<Boolean, GameCharacter> result = model.getParty().doSoloSkillCheckWithPerformer(model, this, Skill.MagicAny, 6);
                if (result.first) {
                    Spell wanted = model.getItemDeck().getRandomSpell();
                    model.getParty().partyMemberSay(model, result.second,
                            List.of("I've always been greatly intrigued by " + wanted.getName() + "."));
                    portraitSay("Yes of course, that's a fascinating spell!");
                    model.getParty().partyMemberSay(model, result.second,
                            List.of("But alas, I have not had the opportunity to learn it..."));
                    portraitSay("You know... I happen to have a copy of the spell book here. Please, take it!");
                    model.getParty().partyMemberSay(model, result.second,
                            List.of("Oh, I couldn't!", "Do you really mean it?", "Are you sure?"));
                    portraitSay("I insist! Anything to help a fellow scholar!");
                    model.getParty().getInventory().add(wanted);
                    println("You gained a copy of " + wanted.getName() + ".");
                } else {
                    model.getParty().partyMemberSay(model, model.getParty().getLeader(),
                            List.of("Uhm... 'Abracadabra'?", "Uhm... 'Hocus Pocus'?", "Uhm... 'Wingardium Leviosa'?"));
                    portraitSay("Hmph. Well we can't be all magical prodigies after all...");
                }
            } else {
                leaderSay("Uhm... " + model.getParty().getInventory().getSpells().get(0).getName() + "?");
                portraitSay("Yes, that's an excellent one!");
            }
        }
        portraitSay("Say, I happen to have a few spell books here that I could be convinced of " +
                "parting with for a few gold coins. Are you interested?");
        waitForReturn();
        List<Item> spellsbooks = new ArrayList<>();
        for (int i = 0; i < MyRandom.randInt(3, 5); ++i) {
            spellsbooks.add(model.getItemDeck().getRandomSpell());
        }
        int[] prices = new int[spellsbooks.size()];
        for (int i = 0; i < prices.length; ++i) {
            prices[i] = Math.max(1, spellsbooks.get(i).getCost() + MyRandom.randInt(-6, 6));
        }
        ShopState shop = new ShopState(model, "Mage", spellsbooks, prices);
        shop.run(model);
        println("You part ways with the mage.");
    }
}
