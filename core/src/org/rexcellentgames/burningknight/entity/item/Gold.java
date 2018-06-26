package org.rexcellentgames.burningknight.entity.item;

import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.util.Random;

import java.util.ArrayList;

public class Gold extends Item {
  public static ArrayList<ItemHolder> all = new ArrayList<>();

  {

    stackable = true;

    autoPickup = true;
    useable = false;

    identified = true;
  }

  @Override
  public void onPickup() {
    Audio.playSfx("coin");
  }

  @Override
  public void render(float x, float y, float w, float h, boolean flipped) {

  }

  @Override
  public void generate() {
    super.generate();
    this.count = Random.newInt(10, 20);
  }
}