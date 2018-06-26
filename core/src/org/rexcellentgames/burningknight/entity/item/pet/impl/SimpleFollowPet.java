package org.rexcellentgames.burningknight.entity.item.pet.impl;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.util.PathFinder;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class SimpleFollowPet extends PetEntity {
  protected float maxDistance = 32f;
  protected Entity target;
  protected boolean dependOnDistance;
  protected boolean buildPath;
  protected Point next;
  protected boolean noAdd;

  @Override
  public void init() {
    super.init();

    this.target = this.owner;
  }

  public Point getCloser(Point target) {
    int from = (int) (Math.floor((this.x + this.w / 2) / 16) + Math.floor((this.y + this.h / 2) / 16) * Level.getWidth());
    int to = (int) (Math.floor((target.x + this.w / 2) / 16) + Math.floor((target.y + this.h / 2) / 16) * Level.getWidth());

    int step = PathFinder.getStep(from, to, Dungeon.level.getPassable());

    if (step != -1) {
      Point p = new Point();

      p.x = step % Level.getWidth() * 16;
      p.y = (float) (Math.floor(step / Level.getWidth()) * 16);

      return p;
    }

    return null;
  }

  @Override
  public void update(float dt) {
    super.update(dt);

    float dx = this.target.x + this.target.w / 2 - this.x - this.w / 2;
    float dy = this.target.y + this.target.h / 2 - this.y - this.h / 2;
    double d = Math.sqrt(dx * dx + dy * dy);

    if (this.buildPath) {
      if (d > this.maxDistance) {
        if (this.next == null) {
          this.next = this.getCloser(this.target);
        } else {
          dx = this.next.x + 8 - this.x - this.w / 2;
          dy = this.next.y + 8 - this.y - this.h / 2;
          d = Math.sqrt(dx * dx + dy * dy);

          if (d <= 4f) {
            this.next = null;
          } else {
            d *= 0.1f;

            this.vel.x += dx / d;
            this.vel.y += dy / d;
          }
        }
      }
    } else {
      if (d > maxDistance) {
        if (dependOnDistance) {
          d *= 0.25f;

          this.vel.x += dx / d;
          this.vel.y += dy / d;
        } else {
          float s = 10f;

          this.vel.x += dx / s;
          this.vel.y += dy / s;
        }
      }
    }

    if (!this.noAdd) {
      this.x += this.vel.x * dt;
      this.y += this.vel.y * dt;
    }

    this.vel.x *= 0.9f;
    this.vel.y *= 0.9f;
  }

  @Override
  public void renderShadow() {
    Graphics.shadow(this.x, this.y, this.w, this.h, this.z);
  }
}