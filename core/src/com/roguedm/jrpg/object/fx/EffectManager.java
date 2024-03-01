package com.roguedm.jrpg.object.fx;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.utils.Array;

public class EffectManager {

    private Array<Effect> effects;

    public EffectManager() {
        effects = new Array<Effect>();
    }

    public void addEffect(Effect effect) {
        effects.add(effect);
    }

    public void removeEffect(Effect effect) {
        if (effects != null) {
            effects.removeValue(effect, true);
        }
    }

    public void render(final SpriteBatch batch) {
        if(effects != null) {
            for(Effect e : effects) {
                if(e != null) {
                    e.draw(batch);
                }
            }
        }
    }

    public void act(float delta) {
        if(effects != null && effects.size > 0) {
            Array<Effect> temp = new Array<Effect>(effects);
            for(Effect e : temp) {
                if(e != null) {
                    e.act(delta);
                    if(e.isFinished()) {
                        for(EventListener listener : e.getListeners()) {
                            listener.handle(null);
                        }
                        removeEffect(e);
                    }
                }
            }
            temp.clear();
            temp = null;
        }
    }

    public void dispose() {
        effects.clear();
        effects = null;
    }

}
