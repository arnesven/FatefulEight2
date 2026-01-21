package model.states;

import model.Model;

public class EveningWithoutQuestState extends EveningState {
    public EveningWithoutQuestState(Model model, boolean freeLodging, boolean freeRation, boolean autosave) {
        super(model, freeLodging, freeRation, autosave);
    }

    @Override
    protected void checkForQuest(Model model) { }
}
