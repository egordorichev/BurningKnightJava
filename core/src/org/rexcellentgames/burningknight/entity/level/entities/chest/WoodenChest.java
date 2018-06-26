package org.rexcellentgames.burningknight.entity.level.entities.chest;

import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.pool.item.WoodenChestPool;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;

public class WoodenChest extends Chest {
  private static Animation animation = Animation.make("chest", "-wooden");
  private static AnimationData closed = animation.get("idle");
  private static AnimationData open = animation.get("opening");
  private static AnimationData openend = animation.get("open");

  @Override
  public Item generate() {
    return WoodenChestPool.instance.generate();
  }

  @Override
  protected AnimationData getClosedAnim() {
    return closed;
  }

  @Override
  protected AnimationData getOpenAnim() {
    return open;
  }

  @Override
  protected AnimationData getOpenedAnim() {
    return openend;
  }
}