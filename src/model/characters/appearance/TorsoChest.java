package model.characters.appearance;

import view.MyColors;
import view.sprites.PortraitSprite;

import java.io.Serializable;

public abstract class TorsoChest implements Serializable {
    public abstract PortraitSprite makeNakedSprite();

    public abstract PortraitSprite getTunicSprite(MyColors color);

    public abstract PortraitSprite getLooseShirtSprite(MyColors color);

    public abstract PortraitSprite getFancySprite(MyColors color);
}
