package org.rexcellentgames.burningknight.game;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.level.SaveableEntity;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.util.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Area {
  private ArrayList<Entity> entities = new ArrayList<>();
  private Comparator<Entity> comparator;
  private boolean showWhenPaused;

  public Area() {
    this.comparator = new Comparator<Entity>() {
      @Override
      public int compare(Entity a, Entity b) {
        // -1 - less than, 1 - greater than, 0 - equal
        float ad = b.getDepth();
        float bd = a.getDepth();

        if (ad == bd) {
          ad = a.y;
          bd = b.y;
        }

        return Float.compare(bd, ad);
      }
    };
  }

  public Area(boolean showWhenPaused) {
    this();

    this.showWhenPaused = showWhenPaused;
  }

  public Entity add(Entity entity) {
    this.entities.add(entity);

    entity.setArea(this);
    entity.init();

    return entity;
  }

  public void remove(Entity entity) {
    this.entities.remove(entity);
  }

  public void update(float dt) {
    for (int i = this.entities.size() - 1; i >= 0; i--) {
      Entity entity = this.entities.get(i);

      if (!entity.isActive()) {
        continue;
      }

      entity.onScreen = entity.isOnScreen();

      if (entity.onScreen || entity.alwaysActive) {
        entity.update(dt);
      }

      if (entity.done) {
        if (entity instanceof SaveableEntity) {
          SaveableEntity saveableEntity = (SaveableEntity) entity;
          LevelSave.remove(saveableEntity);
        }

        entity.destroy();
        this.entities.remove(i);
      }
    }
  }

  public void render() {
    if (Dungeon.game.getState().isPaused() && !this.showWhenPaused) {
      return;
    }

    Collections.sort(this.entities, this.comparator);

    for (int i = 0; i < this.entities.size(); i++) {
      Entity entity = this.entities.get(i);

      if (!entity.isActive()) {
        continue;
      }

      if (entity.onScreen || entity.alwaysRender) {
        entity.render();
      }
    }
  }

  public Entity getRandomEntity(Class<? extends Entity> type) {
    ArrayList<Entity> list = new ArrayList<>();

    for (Entity entity: this.entities) {
      if (type.isInstance(entity)) {
        list.add(entity);
      }
    }

    if (list.size() == 0) {
      return null;
    }

    return list.get(Random.newInt(list.size()));
  }

  public void destroy() {
    for (int i = this.entities.size() - 1; i >= 0; i--) {
      this.entities.get(i).destroy();
    }

    this.entities.clear();
  }

  public ArrayList<Entity> getEntities() {
    return this.entities;
  }
}