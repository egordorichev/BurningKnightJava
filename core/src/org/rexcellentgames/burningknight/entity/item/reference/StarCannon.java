package org.rexcellentgames.burningknight.entity.item.reference;

import org.rexcellentgames.burningknight.entity.item.weapon.gun.Gun;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.bullet.Star;

public class StarCannon extends Gun {
  {

    damage = 4;
    ammo = Star.class;
    setAccuracy(1f);
    penetrates = true;  }
}