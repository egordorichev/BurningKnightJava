package org.rexcellentgames.burningknight.entity.item.pet.orbital;

import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.item.pet.Pet;
import org.rexcellentgames.burningknight.entity.item.pet.impl.Orbital;
import org.rexcellentgames.burningknight.entity.item.pet.impl.PetEntity;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.ArrowProjectile;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.FireballProjectile;

public class FlyingStar extends Pet {  @Override
  public PetEntity create() {
    return new Impl();
  }

  public static class Impl extends Orbital {
    {

    }

    @Override
    protected void onHit(Entity entity) {
      super.onHit(entity);

      if (entity instanceof BulletProjectile || entity instanceof ArrowProjectile || entity instanceof FireballProjectile) {
        this.owner.modifyMana(2);
      }
    }
  }
}