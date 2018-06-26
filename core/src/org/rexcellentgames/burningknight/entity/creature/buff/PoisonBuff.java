package org.rexcellentgames.burningknight.entity.creature.buff;

public class PoisonBuff extends Buff {
  private float last;

  {
    name = "Poisoned";
    description = "You are slowly losing your life";
    duration = 60f;

    bad = true;
  }

  @Override
  public void update(float dt) {
    super.update(dt);
    this.last += dt;

    if (this.last >= 0.5f) {
      this.last = 0;
      this.owner.modifyHp(-1, null, true);
    }
  }
}