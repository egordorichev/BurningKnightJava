package org.rexcellentgames.burningknight.game;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.level.SaveableEntity;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.ui.UiEntity;
import org.rexcellentgames.burningknight.util.Random;

import java.util.ArrayList;
import java.util.Comparator;

public class Area {
  private ArrayList<Entity> entities = new ArrayList<>();
  public static Comparator<Entity> comparator = (a, b) -> {
    // -1 - less than, 1 - greater than, 0 - equal
    float ad = b.getDepth();
    float bd = a.getDepth();

    if (ad == bd) {
      ad = a.y;
      bd = b.y;
    }

    return Float.compare(bd, ad);
  };
  private boolean showWhenPaused = false;
  private boolean hasSelectableEntity;

  public void setShowWhenPaused(boolean showWhenPaused) {
    this.showWhenPaused = showWhenPaused;
  }

  public Area() {

  }

  public Area(boolean showWhenPaused) {
    this.showWhenPaused = showWhenPaused;
  }

  public Entity add(Entity entity) {
    return add(entity, false);
  }

  public Entity add(Entity entity, boolean noInit) {
    this.entities.add(entity);

    entity.setArea(this);

    if (!noInit) {
      entity.init();
    }

    if (entity instanceof UiEntity && ((UiEntity) entity).isSelectable()) {
      if (selectedUiEntity == -1) {
        selectedUiEntity = this.entities.size() - 1;
      }
      
      this.hasSelectableEntity = true;
    }

    return entity;
  }

  public void remove(Entity entity) {
    this.entities.remove(entity);
  }

  private int selectedUiEntity = -1;

  public void selectFirst() {
  	this.selectedUiEntity = this.findFirstSelectableUiEntity();
  }

  public void update(float dt) {
    if (this.hasSelectableEntity) {
	    if (this.selectedUiEntity >= this.entities.size()) {
		    this.selectedUiEntity = findFirstSelectableUiEntity();

		    if (this.selectedUiEntity != -1) {
			    Entity e = this.entities.get(selectedUiEntity);

			    if (e instanceof UiEntity) {
				    ((UiEntity) e).select();
			    }
		    }
	    }
    }

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

    this.entities.sort(comparator);

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

  public void hide() {
    for (Entity entity: this.entities) {
      entity.setActive(false);
    }
  }

  public void show() {
    for (Entity entity: this.entities) {
      entity.setActive(true);
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

  public void select(UiEntity entity) {
	  if (!entity.isSelectable()) {
		  return;
	  }

	  for (Entity e : this.entities) {
		  if (e instanceof UiEntity && ((UiEntity) e).isSelected()) {
			  ((UiEntity) e).unselect();
		  }
	  }
  }

  public void destroy() {
    for (int i = this.entities.size() - 1; i >= 0; i--) {
      this.entities.get(i).destroy();
    }

    this.entities.clear();
    this.selectedUiEntity = 0;
    this.hasSelectableEntity = false;
  }

  private int findFirstSelectableUiEntity() {
    return findFirstSelectableUiEntity(0);
  }

  private int findFirstSelectableUiEntity(int offset) {
    for (int i = offset; i < this.entities.size(); i++) {
      Entity e = entities.get(i);

      if (e instanceof UiEntity && e.isOnScreen() && ((UiEntity) e).isSelectable()) {
        return i;
      }
    }

    return -1;
  }

  private int findLastSelectableUiEntity() {
    return findLastSelectableUiEntity(this.entities.size() - 1);
  }

  private int findLastSelectableUiEntity(int offset) {
    for (int i = offset; i >= 0; i--) {
	    Entity e = entities.get(i);

	    if (e instanceof UiEntity && e.isOnScreen() && ((UiEntity) e).isSelectable()) {
        return i;
      }
    }

    return -1;
  }

  public ArrayList<Entity> getEntities() {
    return this.entities;
  }
}