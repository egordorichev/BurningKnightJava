package org.rexcellentgames.burningknight.entity.creature.mob.hall;

import org.rexcellentgames.burningknight.entity.item.weapon.gun.BadGun;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.Random;

public class RangedKnight extends Knight {
  public static Animation animations = Animation.make("actor-knight-v2", "-red");

  {
    hpMax = 5;
  }

  // todo: run away if too close

  public Animation getAnimation() {
    return animations;
  }

  @Override
  public void init() {
    super.init();

    this.sword = new BadGun();
    this.sword.setOwner(this);
  }

  @Override
  protected State getAi(String state) {
    switch (state) {
      case "preattack":
        return new PreAttackState();
      case "attack":
        return new AttackingState();
      case "runaway":
        return new RunAwayState();
    }

    return super.getAi(state);
  }

  public void checkForRun() {
    if (this.ai != null) {
      this.ai.checkForPlayer();
    }

    if (this.target == null) {
      return;
    }

    float d = this.getDistanceTo(this.target.x + this.target.w / 2, this.target.y + this.target.h / 2);

    if (d < 64f) {
      // todo: count doors!
      this.become("runaway");
    }
  }

  public class RunAwayState extends KnightState {
    private float last;

    @Override
    public void update(float dt) {
      super.update(dt);

      last += dt;

      if (this.last >= 0.25f) {
        last = 0;
        this.nextPathPoint = null;
      }

      this.checkForPlayer();

      this.moveFrom(self.lastSeen, 8f, 5f);

      if (self.target == null) {
        self.become("idle");
        return;
      }

      float d = self.getDistanceTo(self.target.x + self.target.w / 2, self.target.y + self.target.h / 2);

      if (d >= self.minAttack) {
        self.become("preattack");
      }
    }
  }

  public class AttackingState extends KnightState {
    @Override
    public void onEnter() {
      super.onEnter();
      self.sword.use();

      float r = Random.newFloat();

      if (r < 0.5f) {
        self.become("preattack");
      }
    }

    @Override
    public void update(float dt) {
      super.update(dt);

      if (self.sword.getDelay() == 0) {
        self.become("chase");
      }

      checkForRun();
    }
  }

  public class PreAttackState extends KnightState {
    @Override
    public void update(float dt) {
      super.update(dt);

      if (this.t > 0.5f) {
        self.become("attack");
      }

      checkForRun();
    }
  }
}