package org.rexcellentgames.burningknight.entity;

import com.badlogic.gdx.graphics.OrthographicCamera;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.game.Area;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class Entity extends Point {
  public int depth = 0;
  public float w = 16;
  public float h = 16;
  public boolean onScreen = true;
  public boolean alwaysActive = false;
  public boolean alwaysRender = false;
  public boolean done = false;
  public int id;
  protected Area area;
  protected boolean active = true;

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public float getDistanceTo(float x, float y) {
    float dx = x - this.x - this.w / 2;
    float dy = y - this.y - this.h / 2;
    return (float) Math.sqrt(dx * dx + dy * dy);
  }

  public float getAngleTo(float x, float y) {
    float dx = x - this.x - this.w / 2;
    float dy = y - this.y - this.h / 2;
    return (float) Math.atan2(dy, dx);
  }

  public void setDone(boolean done) {
    this.done = done;
  }

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void init() {

  }

  public void destroy() {

  }

  public void update(float dt) {

  }

  public void render() {

  }

  public void renderShadow() {

  }

  public int getDepth() {
    return this.depth;
  }

  public Area getArea() {
    return this.area;
  }

  public void setArea(Area area) {
    this.area = area;
  }

  public void onCollision(Entity entity) {

  }

  public void onCollisionEnd(Entity entity) {

  }

  public boolean isOnScreen() {
    OrthographicCamera camera = Camera.game;

    float zoom = camera.zoom;

    return this.x + this.w * 2 >= camera.position.x - Display.GAME_WIDTH / 2 * zoom &&
      this.y + this.h * 2 >= camera.position.y - Display.GAME_HEIGHT / 2 * zoom &&
      this.x <= camera.position.x + Display.GAME_WIDTH / 2 * zoom &&
      this.y <= camera.position.y + this.h + Display.GAME_HEIGHT / 2 * zoom;
  }

  public long playSfx(String sound) {
    if (this instanceof Player) {
      return Audio.playSfx(sound);
    }

    if (!this.onScreen) {
      return -1;
    }

    float d = this.getDistanceTo(Player.instance.x + 8, Player.instance.y + 8);

    if (d >= 256f) {
      return -1;
    }

    return Audio.playSfx(sound, (256f - d) / 256f, 0.9f + Random.newFloat(0.3f));
  }
}