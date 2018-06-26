package org.rexcellentgames.burningknight.entity.creature.buff;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.creature.buff.fx.FlameFx;

public class BurningBuff extends Buff {
  private float lastFlame = 0;
  private float lastHit = 0;

  {
    duration = 5.0f;
    name = "Burning";
    description = "You are on fire!";

    bad = true;
  }

  @Override
  public void onStart() {
    super.onStart();
    this.owner.removeBuff(FreezeBuff.class);
  }

  @Override
  protected void onUpdate(float dt) {
    Dungeon.level.addLightInRadius(this.owner.x + this.owner.w / 2, this.owner.y + this.owner.h / 2, 1f, 0.9f, 0f, 0.9f, 3f, false);
    this.lastFlame += dt;

    if (this.lastFlame >= 0.1f) {
      this.addFlame();
      this.lastFlame = 0;
    }

    this.lastHit += dt;

    if (this.lastHit >= 1f) {
      this.lastHit = 0;
      this.owner.modifyHp(-1, null, true);
    }
  }

  public void addFlame() {
    Dungeon.area.add(new FlameFx(this.owner));
  }
}