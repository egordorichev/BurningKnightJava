package org.rexcellentgames.burningknight.entity.level.entities;

import com.badlogic.gdx.math.Rectangle;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.weapon.WeaponBase;
import org.rexcellentgames.burningknight.entity.level.entities.fx.WellFx;
import org.rexcellentgames.burningknight.entity.pool.ModifierPool;

public class WeaponAltar extends UsableProp {
  private boolean s;
  private WellFx fx;

  {

    collider = new Rectangle(1, 10, 26, 6);
  }

  @Override
  public void update(float dt) {
    super.update(dt);

    if (!s) {
      s = true;
      Dungeon.level.setPassable((int) (this.x / 16), (int) ((this.y + 8) / 16), false);
    }
  }

  @Override
  public boolean use() {
    Item item = Player.instance.getInventory().getSlot(Player.instance.getInventory().active);

    ((WeaponBase) item).setModifier(ModifierPool.instance.generate());    this.used = true;

    return true;
  }

  @Override
  public void onCollision(Entity entity) {
    if (entity instanceof Player && !this.used) {
      this.fx = new WellFx(this, "bless_a_weapon");
      Dungeon.area.add(fx);
    }
  }

  @Override
  public void onCollisionEnd(Entity entity) {
    if (this.fx != null && entity instanceof Player) {
      this.fx.remove();
      this.fx = null;
    }
  }
}