package view.subviews;

import model.Model;
import view.MyColors;
import view.SpriteManager;
import view.sprites.Sprite;

public class ImageSubView extends SubView {
    private Sprite[][] imgsprite;
    private final String title;
    private final String undertext;
    private final String imageName;
    private final boolean convert;

    public ImageSubView(String imageName, String title, String undertext, boolean convert) {
        this.imageName = imageName;
        this.title = title;
        this.undertext = undertext;
        this.convert = convert;
    }

    public ImageSubView(String imageName, String title, String undertext) {
        this(imageName, title, undertext, true);
    }

    @Override
    public void drawArea(Model model) {
        if (imgsprite == null) {
            makeImage();
        }
        model.getScreenHandler().clearSpace(X_OFFSET, X_MAX, Y_OFFSET, Y_MAX);
        for (int x = 0; x < 32; ++x) {
            for (int y = 0; y < 38; ++y) {
                model.getScreenHandler().put(X_OFFSET+x, Y_OFFSET+y, imgsprite[x][y]);
            }
        }
    }

    private void makeImage() {
        imgsprite = new Sprite[32][38];
        boolean alreadyConverted = SpriteManager.isRegistered(imageName+0+":"+0+":" + "0");
        if (alreadyConverted) {
            for (int x = 0; x < 32; ++x) {
                for (int y = 0; y < 38; ++y) {
                    this.imgsprite[x][y] = SpriteManager.getSprite(imageName+x+":"+y+":"+"0");
                }
            }
        } else {
            for (int x = 0; x < 32; ++x) {
                for (int y = 0; y < 38; ++y) {
                    this.imgsprite[x][y] = new Sprite(imageName+x+":"+y+":", imageName + ".png", x, y, 8, 8);
                    if (convert) {
                        MyColors.transformImage(this.imgsprite[x][y]);
                    }
                }
            }
        }
        
    }

    @Override
    protected String getUnderText(Model model) {
        return undertext;
    }

    @Override
    protected String getTitleText(Model model) {
        return title;
    }
}
