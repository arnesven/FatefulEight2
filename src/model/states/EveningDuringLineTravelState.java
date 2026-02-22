package model.states;

import model.Model;

public class EveningDuringLineTravelState extends EveningState{
    public EveningDuringLineTravelState(Model model) {
        super(model);
    }

    public void noLodging(Model model) {
        notLodging(model);
    }
}
