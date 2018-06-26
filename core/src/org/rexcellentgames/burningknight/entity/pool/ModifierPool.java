package org.rexcellentgames.burningknight.entity.pool;

import org.rexcellentgames.burningknight.entity.item.weapon.modifier.*;
import org.rexcellentgames.burningknight.util.Random;

import java.util.ArrayList;

public class ModifierPool {
  public static ModifierPool instance = new ModifierPool();
  protected ArrayList<Modifier> classes = new ArrayList<>();
  protected ArrayList<Float> chances = new ArrayList<>();

  public ModifierPool() {
    add(new FastModifier(), 1f);
    add(new SlowModifier(), 1f);
    add(new StrongModifier(), 1f);
    add(new BrokenModifier(), 1f);
    add(new NoChanceModifier(), 1f);
    add(new CriticalModifier(), 1f);
  }

  public Modifier getModifier(int id) {
    return classes.get(id);
  }

  protected void add(Modifier type, float chance) {
    type.id = this.classes.size();

    classes.add(type);
    chances.add(chance);
  }

  public Modifier generate() {
    return classes.get(Random.chances(chances.toArray(new Float[0])));
  }
}