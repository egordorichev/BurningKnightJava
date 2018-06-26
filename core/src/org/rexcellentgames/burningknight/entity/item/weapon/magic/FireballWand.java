package org.rexcellentgames.burningknight.entity.item.weapon.magic;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.FireballProjectile;

public class FireballWand extends MagicMissileWand {
  {    damage = 3;
  }

  @Override
  public void spawnProjectile(float x, float y, float a) {
    FireballProjectile fireball = new FireballProjectile();

    fireball.owner = this.owner;
    fireball.x = x;
    fireball.damage = this.rollDamage();
    fireball.crit = this.lastCrit;
    fireball.y = y - 4;

    double ra = Math.toRadians(a);

    fireball.vel.x = (float) Math.cos(ra) * 60;
    fireball.vel.y = (float) Math.sin(ra) * 60;

    Dungeon.area.add(fireball);
  }
}