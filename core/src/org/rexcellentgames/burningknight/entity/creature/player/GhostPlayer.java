package org.rexcellentgames.burningknight.entity.creature.player;

import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.buff.InvisibilityBuff;
import org.rexcellentgames.burningknight.util.Log;

public class GhostPlayer extends Player {
  private Player owner;
  private boolean tpd;

  {
    hpMax = 50;
  }

  public GhostPlayer() {
    super("ghost");

    this.owner = Player.instance;
  }

  @Override
  public void init() {
    super.init();
    this.alwaysActive = true;
  }

  @Override
  public void onHurt(float a, Creature creature) {
    super.onHurt(a, creature);

    // FIXME: doesnt remove it

    if (this.hp <= 0 && Player.instance != null) {
      Log.info("remove");
      Player.instance.removeBuff(InvisibilityBuff.class);
    }
  }

  @Override
  public void destroy() {
    super.destroy();
    if (Player.instance != null) {
      Log.info("remove");
      this.owner.removeBuff(InvisibilityBuff.class);
    }
  }

  @Override
  public void update(float dt) {
    // Log.info(this.hp + " " + Player.instance + " " + this);

    if (!this.tpd) {
      tpd = true;
      tp(Player.instance.x, Player.instance.y);
    }

    super.update(dt);
  }

  @Override
  public void render() {
    super.render();
  }
}