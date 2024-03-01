package com.roguedm.jrpg;

import com.badlogic.gdx.utils.Array;
import com.roguedm.jrpg.object.Glider;
import com.roguedm.jrpg.object.hero.Hero;
import com.roguedm.jrpg.object.hero.Wizard;
import com.roguedm.jrpg.world.World;

import squidpony.squidmath.Coord;

public class GameState {

    private World world;

    private Glider actor;

    private Array<Hero> party;

    public GameState()  {
        party = new Array<Hero>();

        Hero hero1 = new Wizard();
        hero1.init();
        party.add(hero1);

        Hero hero2 = new Hero(3, 21);
        hero2.init();
        party.add(hero2);


        actor = new Glider(1, 19);
        actor.set(Coord.get(1, 1));

        world = new World("maps/test.tmx");
        world.initialize(actor);
    }

    public Glider getActor() {
        return actor;
    }

    public World getWorld() {
        return world;
    }

    public Array<Hero> getParty() {
        return party;
    }

    public void dispose() {
    }

}
