package model.states.events;

import model.GameStatistics;
import model.Model;
import model.Party;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.FacialExpression;
import model.characters.appearance.WeepingAmount;
import model.classes.*;
import model.classes.normal.NobleClass;
import model.items.Item;
import model.items.ItemDeck;
import model.items.Prevalence;
import model.items.potions.HealthPotion;
import model.items.potions.Potion;
import model.items.potions.RejuvenationPotion;
import model.items.potions.StaminaPotion;
import model.items.weapons.*;
import model.races.Race;
import model.states.DailyEventState;
import util.MyLists;
import view.MyColors;
import view.StatisticsView;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public class WeepingWomanEvent extends DailyEventState {
    private List<GameCharacter> usedCharacters = new ArrayList<>();

    public WeepingWomanEvent(Model model) {
        super(model);
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Meet upset woman",
                "There's a woman in town who lost her husband some time ago. They say she's inconsolable");
    }

    @Override
    protected void doEvent(Model model) {
        println("You encounter a woman on the street. She is weeping and sobbing loudly.");
        AdvancedAppearance woman = PortraitSubView.makeRandomPortrait(new NoClass(MyColors.LIGHT_BLUE), Race.randomRace(), true);
        showExplicitPortrait(model, woman, "Woman");

        getPortraitSubView().setFacialExpression(FacialExpression.afraid);
        getPortraitSubView().forceEyesClosed(true);
        getPortraitSubView().setWeeping(WeepingAmount.aLot);
        leaderSay("Uhm... Can " + iOrWe() + " help you ma'am?", FacialExpression.questioning);
        model.getLog().waitForAnimationToFinish();
        portraitSay("Boo-hoo... uh... uh... *sob* *sob*");
        if (model.getParty().size() > 1) {
            GameCharacter other = model.getParty().getRandomPartyMember(model.getParty().getLeader());
            partyMemberSay(other, "This woman has clearly been through some ordeal.");
            if (hasGoodPersonality(other)) {
                partyMemberSay(other, "We should try to console her!");
            } else if (hasMeanPersonality(other)) {
                partyMemberSay(other, "This is none of our business. We should move on.", FacialExpression.disappointed);
            }
        }
        print("Do you try to console the woman? (Y/N) ");
        if (!yesNoInput()) {
            leaderSay("Best let her be.");
            println("You walk away from the woman.");
            return;
        }
        print("Who should attempt to comfort the woman? ");
        GameCharacter performer = model.getParty().partyMemberInput(model, this, model.getParty().getPartyMember(0));
        SkillCheckResult result = doPersuadeSkillCheck(model, performer);
        if (result.isSuccessful()) {
            if (hasGoodPersonality(performer)) {
                partyMemberSay(performer, "Please let us help you. There must be something we can do for you. Why don't you " +
                        "tell us what's wrong. It's alright, you can trust " + meOrUs() + ".", FacialExpression.relief);
                println("The woman appears to calm down somewhat.");
            } else if (hasMeanPersonality(performer) || performer.hasPersonality(PersonalityTrait.aggressive)) {
                partyMemberSay(performer, "Put a cork in it woman. Your wailing is bringing everybody down!", FacialExpression.angry);
                println("Despite " + performer.getFirstName() + "'s brusqueness, the woman actually appears to calm down somewhat.");
            } else {
                partyMemberSay(performer, "There there. Won't you tell us what's wrong?");
                println("The woman appears to calm down somewhat.");
            }
            womanIsCalmer(model, woman);
        } else {
            if (hasMeanPersonality(performer) || performer.hasPersonality(PersonalityTrait.aggressive)) {
                partyMemberSay(performer, "Oh, put a sock in it woman! No need to be wailing like a lunatic!", FacialExpression.angry);
            } else if (performer.hasPersonality(PersonalityTrait.jovial) || performer.hasPersonality(PersonalityTrait.naive)) {
                partyMemberSay(performer, "Please stop crying. After all, it's a very nice day.", FacialExpression.relief);
            } else {
                println(performer.getName() + " tries " + hisOrHer(performer.getGender()) + " best to console the woman, but it seems to have little effect.");
            }
            womanIsHysterical(model, woman);
        }
    }

    private SkillCheckResult doPersuadeSkillCheck(Model model, GameCharacter performer) {
        int bonus = hasGoodPersonality(performer) ? 2 : (hasMeanPersonality(performer) ? -2 : 0);
        println(performer.getName() + " attempts to console the woman.");
        SkillCheckResult result = SkillChecks.doSkillCheckWithReRoll(model, this, performer, Skill.Persuade, 7, 20, bonus);
        GameStatistics.incrementSoloSkillChecks(1);
        if (!hasGoodPersonality(performer) && !usedCharacters.contains(performer)) {
            usedCharacters.add(performer);
        }
        return result;
    }

    private void womanIsCalmer(Model model, AdvancedAppearance woman) {
        println("Though a little calmer, the woman continues walking down the street, still weeping a little.");
        getPortraitSubView().forceEyesClosed(false);
        getPortraitSubView().setFacialExpression(FacialExpression.sad);
        getPortraitSubView().setWeeping(WeepingAmount.aLittle);
        portraitSay("That's good of you to try to comfort me, but there really isn't anything you can do.");
        print("Do you insist on trying to help to woman? (Y/N) ");
        if (!yesNoInput()) {
            leaderSay("If you say so. Good bye then.");
            portraitSay("*Sob* goodbye *sob*");
            return;
        }

        GameCharacter performer = getPartyMemberToComfort(model);
        SkillCheckResult result = doPersuadeSkillCheck(model, performer);
        if (result.isSuccessful()) {
            if (hasGoodPersonality(performer)) {
                partyMemberSay(performer, "It breaks my heart so see somebody so crestfallen. " +
                        "Please, there must be some way we can console you!", FacialExpression.sad);
                println(performer.getFirstName() + "'s kind words seem to get through to the woman, and she stops crying.");
            } else if (hasMeanPersonality(performer)) {
                partyMemberSay(performer, "Lighten up, get ahold of yourself!", FacialExpression.disappointed);
                println(performer.getFirstName() + "'s curt words seem to snap the woman out of her mood, and surprisingly, she stops crying.");
            } else {
                partyMemberSay(performer, "There must be something we can do. Won't you tell us what is the matter?");
                println("The woman stops crying.");
            }
            womanInvitesPartyIntoHouse(model, woman);
        } else {
            if (hasMeanPersonality(performer)) {
                partyMemberSay(performer, "Your tears are smearing your make-up. You're a disgrace.", FacialExpression.angry);
                println("The woman wails loudly.");
                if (model.getParty().getLeader() != performer) {
                    leaderSay(performer.getFirstName() + "! Really?", FacialExpression.disappointed);
                }
            } else if (performer.hasPersonality(PersonalityTrait.jovial) || performer.hasPersonality(PersonalityTrait.naive)) {
                partyMemberSay(performer, "Should I get a mop, or are you almost done?", FacialExpression.wicked);
                println("The woman wails loudly.");
                if (model.getParty().getLeader() != performer) {
                    leaderSay("Come on " + performer.getFirstName() + "...", FacialExpression.disappointed);
                }
            } else {
                println(performer.getFirstName() + " tries to distract the woman with small talk, but only manages to make things worse.");
            }
            womanIsHysterical(model, woman);
        }
    }

    private GameCharacter getPartyMemberToComfort(Model model) {
        print("Who should attempt to console the woman? ");
        GameCharacter performer;
        do {
            performer = model.getParty().partyMemberInput(model, this, model.getParty().getPartyMember(0));
            if (!usedCharacters.contains(performer) || model.getParty().size() == usedCharacters.size()) {
                break;
            }
            leaderSay("Uhm, maybe we should let somebody else try to talk to this woman?");
        } while (true);
        return performer;
    }

    private void womanInvitesPartyIntoHouse(Model model, AdvancedAppearance woman) {
        getPortraitSubView().setFacialExpression(FacialExpression.sad);
        getPortraitSubView().setWeeping(WeepingAmount.singleTear);
        println("The woman now stops in front of a house.");
        portraitSay("It's... it's just my grief. I lost my husband some time ago.");
        leaderSay("That's terrible. " + imOrWere() + " so sorry!");
        portraitSay("You're so nice. Won't you come in for a cup of tea?");
        print("Do you accept the invitation? (Y/N) ");
        if (!yesNoInput()) {
            leaderSay("Unfortunately, we have to be on our way. I'm glad you're feeling a little better.");
            portraitSay("That's alright. I appreciate you trying to cheer me up.");
            getPortraitSubView().setFacialExpression(FacialExpression.relief);
            portraitSay("Farewell.");
            leaderSay("Good bye.");
            return;
        }

        println("You step into the woman's home. It's obvious she has been living there alone for some time.");
        portraitSay("Please sit down. I'll get the tea.");
        println("The woman goes into the kitchen.");
        if (randomSayIfPersonality(PersonalityTrait.snobby, List.of(model.getParty().getLeader()), "It's kind of cramped in here...")) {
            leaderSay("Shh! She'll hear you.", FacialExpression.disappointed);
        }
        if (randomSayIfPersonality(PersonalityTrait.irritable, List.of(model.getParty().getLeader()),
                "What are we doing here. We're just wasting time...")) {
            leaderSay("Be quite, we're doing a good deed.", FacialExpression.disappointed);
        }
        println("The woman returns with a few cups of tea. She gestures to a portrait on the wall. It depicts a " +
                woman.getRace().getName().toLowerCase() + " in armor.");
        leaderSay("Your husband?");
        portraitSay("Yes. Arthur. He was very dear to me. But he... he...");
        model.getLog().waitForAnimationToFinish();
        getPortraitSubView().forceEyesClosed(true);
        getPortraitSubView().setWeeping(WeepingAmount.aLot);
        leaderSay("There there now.");

        GameCharacter performer = getPartyMemberToComfort(model);
        SkillCheckResult result = doPersuadeSkillCheck(model, performer);
        getPortraitSubView().forceEyesClosed(false);
        if (result.isSuccessful()) {
            if (hasGoodPersonality(performer)) {
                partyMemberSay(performer, "Losing a loved one is soul-wrenching. I feel for you. Please hold my hand.", FacialExpression.sad);
                println("The woman grasps " + performer.getFirstName() + "'s hand.");
                portraitSay("Thank you. How rare to encounter such kindness from a stranger.", FacialExpression.relief);
            } else if (hasMeanPersonality(performer)) {
                partyMemberSay(performer, "Yes, you lost your husband. We've all lost somebody. Get over it.", FacialExpression.disappointed);
                println("The woman gasps. Then looks sternly at you, as if accepting the truth of " + performer.getFirstName() + " candid words.");
            } else {
                partyMemberSay(performer, "My condolences. Won't you tell me more about him?", FacialExpression.relief);
                println("The woman takes a deep breath, and once again calms down.");
            }
            getPortraitSubView().setWeeping(WeepingAmount.none);
            getPortraitSubView().setFacialExpression(FacialExpression.none);
            println("The woman tells the party about Arthur, who was apparently the leader of the militia in this town. In a recent Orc raid on the town " +
                    "he was mortally wounded and passed away, leaving his wife and adult son to grieve him.");
            leaderSay("Thank you for telling us about Arthur. He sounds like a wonderful person.");
            portraitSay("He really was. But he's gone, and I should try to move on. But sometimes... sometimes I get so lonely.");
            leaderSay("What about your son? Doesn't he visit you?");
            portraitSay("He does but, well since Arthur passed, he's been more distant.");
            leaderSay("Talk to him. Now is your chance to strengthen your relationship.");
            portraitSay("How very true. Thank you so much for comforting me.");
            leaderSay("It was no trouble at all.");
            getPortraitSubView().setFacialExpression(FacialExpression.relief);
            portraitSay("You have a good heart. Just like my Arthur. You know. I have something here.");
            println("The woman opens a drawer, inside is a sword.");
            portraitSay("I was saving this for my son. But he's not much of a fighter. You seem more like the adventuring type. " +
                    "I know Arthur would have been pleased to know that his sword was in the hand of a good-hearted person.");
            List<Item> items = model.getItemDeck().draw(
                    List.of(new Longsword(), new Rapier(), new Broadsword(), new Claymore()),
                    1, Prevalence.unspecified, 0.99);
            println("The party receives " + items.getFirst().getName() + ".");
            items.getFirst().addYourself(model.getParty().getInventory());
            leaderSay("Thank you. It will be an honor to wield Arthur's sword.");
            println("You leave the woman's home, feeling sad about the story of Arthur's passing, but much uplifted by " +
                    "the experience of helping the woman handling her grief.");
            println("Each party member gains 30 XP.");
            MyLists.forEach(model.getParty().getPartyMembers(), gc -> model.getParty().giveXP(model, gc, 30));
            model.getLog().waitForAnimationToFinish();
        } else {
            if (hasMeanPersonality(performer)) {
                partyMemberSay(performer, "Oh just shut up and tell us about your dumb husband already!");
                model.getLog().waitForAnimationToFinish();
                println("The woman gasps.");
                if (model.getParty().getLeader() != performer) {
                    leaderSay(performer.getFirstName() + "! Really?", FacialExpression.disappointed);
                }
                getPortraitSubView().setFacialExpression(FacialExpression.angry);
            } else if (performer.hasPersonality(PersonalityTrait.jovial) || performer.hasPersonality(PersonalityTrait.naive)) {
                partyMemberSay(performer, "He was a soldier? I'm guessing he made the ultimate sacrifice, " +
                        "but you would have preferred a medium one?", FacialExpression.wicked);
                if (model.getParty().getLeader() != performer) {
                    leaderSay("This is no joking matter " + performer.getFirstName() + ".", FacialExpression.disappointed);
                }
                println("The woman looks angrily at " + performer.getFirstName() + ".");
                getPortraitSubView().setFacialExpression(FacialExpression.disappointed);
            } else {
                partyMemberSay(performer, "Come one, how emotional can somebody be?", FacialExpression.questioning);
                println("The woman looks at " + performer.getFirstName() + " and scowls.");
                getPortraitSubView().setFacialExpression(FacialExpression.disappointed);
            }

            getPortraitSubView().setWeeping(WeepingAmount.none);
            portraitSay("This was a mistake. To think for one minute that a stranger could offer me some comfort. What a fool I am.");
            leaderSay("No please, " + iOrWe() + "...");
            portraitSay("Please leave now.");
            println("Embarrassed, you get up and leave the house.");
            partyMemberSay(performer, "At least she stopped crying.");
        }
    }

    private void womanIsHysterical(Model model, AdvancedAppearance woman) {
        println("The woman starts sobbing even more loudly, and continues walking down the street.");
        getPortraitSubView().forceEyesClosed(false);
        getPortraitSubView().setWeeping(WeepingAmount.veryMuch);
        portraitSay("Ohhhh!!! Woe me! *sob* *sob* *sob*");
        print("Do you give up on the weeping woman? (Y/N) ");
        if (yesNoInput()) {
            leaderSay("Better leave this woman to her worries.");
            println("You leave the weeping woman.");
            return;
        }

        GameCharacter performer = getPartyMemberToComfort(model);
        SkillCheckResult result = doPersuadeSkillCheck(model, performer);
        if (result.isSuccessful()) {
            println(performer.getFirstName() + " manages to calm the woman down a little.");
            model.getLog().waitForAnimationToFinish();
            getPortraitSubView().setWeeping(WeepingAmount.aLittle);
            println("As you are talking to the woman, a young man walks up.");
            AdvancedAppearance son = PortraitSubView.makeRandomPortrait(new NoClass(MyColors.DARK_GREEN), woman.getRace(), false);
            showExplicitPortrait(model, son, "Young Man");
            portraitSay("Mother, who are these people?");
            showExplicitPortrait(model, woman, "Woman");
            getPortraitSubView().setWeeping(WeepingAmount.singleTear);
            getPortraitSubView().setFacialExpression(FacialExpression.sad);
            portraitSay("Some kind strangers. I'm sorry I'm such a bother!");
            showExplicitPortrait(model, son, "Young Man");
            portraitSay("Oh mother. You have to move on with your life, father has been dead almost a year now.");
            showExplicitPortrait(model, woman, "Woman");
            getPortraitSubView().setWeeping(WeepingAmount.singleTear);
            getPortraitSubView().setFacialExpression(FacialExpression.sad);
            portraitSay("I know, I know. I'm sorry. I just get so lonely.");
            showExplicitPortrait(model, son, "Young Man");
            portraitSay("Thank you for trying to cheer up my mother. We've had a tough time since my father passed away.");
            leaderSay("I understand. A death in the family is always upsetting.");
            portraitSay("Won't you accept this small gift, as a thank you?");
            List<Potion> potions = MyLists.take(List.of(new RejuvenationPotion(), new StaminaPotion(), new HealthPotion()), 2);
            println("You got " + MyLists.commaAndJoin(potions, Item::getName) + ".");
            for (Potion p : potions) {
                p.addYourself(model.getParty().getInventory());
            }
            leaderSay("Thank you. Good luck.");
            showExplicitPortrait(model, woman, "Woman");
            getPortraitSubView().setWeeping(WeepingAmount.singleTear);
            getPortraitSubView().setFacialExpression(FacialExpression.relief);
            portraitSay("Good bye. And thanks again.");
        } else {
            getPortraitSubView().forceEyesClosed(true);
            getPortraitSubView().setFacialExpression(FacialExpression.afraid);
            println(performer.getFirstName() + " only manages to agitate the woman even further.");
            portraitSay("Ooooh! Oooh! *sob*");
            println("In a hysterical fit, the woman runs away from the party.");
            leaderSay("Well, that went less than well.");
        }
    }

    private boolean hasMeanPersonality(GameCharacter other) {
        return other.hasPersonality(PersonalityTrait.irritable) ||
                other.hasPersonality(PersonalityTrait.cold) ||
                other.hasPersonality(PersonalityTrait.unkind);
    }

    private boolean hasGoodPersonality(GameCharacter other) {
        return other.hasPersonality(PersonalityTrait.benevolent) ||
                other.hasPersonality(PersonalityTrait.friendly) ||
                other.hasPersonality(PersonalityTrait.encouraging);
    }
}
