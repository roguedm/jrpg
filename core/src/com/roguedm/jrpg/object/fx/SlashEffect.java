package com.roguedm.jrpg.object.fx;


import com.roguedm.jrpg.AssetManager;

public class SlashEffect extends Effect {

    public SlashEffect(int size, int offsetX, int offsetY) {
        super(new AnimatedImage(.1f, size,
                AssetManager.getInstance().getSmallEffect(1),
                AssetManager.getInstance().getSmallEffect(2)), offsetX, offsetY);
    }

}
