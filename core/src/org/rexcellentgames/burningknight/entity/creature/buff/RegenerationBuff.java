package org.rexcellentgames.burningknight.entity.creature.buff;

public class RegenerationBuff extends Buff {
  private float last;

  {
    name = "Regeneration";
    description = "You are full of powerful energy";
    duration = 30f;

  }

  @Override
  public void update(float dt) {
    super.update(dt);
    this.last += dt;

    if (this.last >= 0.5f) {
      this.last = 0;
      this.owner.modifyHp(1, null);
    }
  }
}