package org.rexcellentgames.burningknight.entity.item.reference;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.util.Tween;

public class BlueWatch extends Item {
  @Override
  public void use() {
    super.use();

    if (Dungeon.speed == 1) {
      Tween.to(new Tween.Task(0.5f, 0.3f) {
        @Override
        public float getValue() {
          return Dungeon.speed;
        }

        @Override
        public void setValue(float value) {
          Dungeon.speed = value;
        }
      });
    } else {
      Tween.to(new Tween.Task(1, 0.3f) {
        @Override
        public float getValue() {
          return Dungeon.speed;
        }

        @Override
        public void setValue(float value) {
          Dungeon.speed = value;
        }
      });
    }
  }
}