package org.rexcellentgames.burningknight;

import com.badlogic.gdx.ApplicationListener;

public class Client implements ApplicationListener {
  private Dungeon dungeon;

  @Override
  public void create() {
    this.dungeon = new Dungeon();
    this.dungeon.create();
  }

  @Override
  public void resize(int width, int height) {
    this.dungeon.resize(width, height);
  }

  @Override
  public void render() {
    this.dungeon.render();
  }

  @Override
  public void pause() {

  }

  @Override
  public void resume() {

  }

  @Override
  public void dispose() {
    this.dungeon.dispose();
  }
}