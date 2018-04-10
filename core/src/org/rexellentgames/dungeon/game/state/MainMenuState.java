package org.rexellentgames.dungeon.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.game.input.Input;

public class MainMenuState extends State {
  private Stage stage;

  @Override
  public void init() {
    stage = new Stage(new ScreenViewport());

    // This is temporary, we should render a custom cursor.
    Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);

    Input.multiplexer.addProcessor(stage);

    setupUi();
  }

  private void setupUi() {
    VerticalGroup verticalGroup = new VerticalGroup();
    verticalGroup.columnCenter();
    verticalGroup.center();
    verticalGroup.space(25.0f);
    verticalGroup.setFillParent(true);

    Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

    TextButton playButton = new TextButton("Play", skin);

    playButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        Dungeon.goToLevel(0);
      }
    });

    verticalGroup.addActor(playButton);

    TextButton optionsButton = new TextButton("Options", skin);

    optionsButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        // Change to options state
      }
    });

    verticalGroup.addActor(optionsButton);

    TextButton exitButton = new TextButton("Exit", skin);

    exitButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        Gdx.app.exit();
      }
    });

    verticalGroup.addActor(exitButton);

    stage.addActor(verticalGroup);
  }

  @Override
  public void resize(int width, int height) {
    stage.getViewport().update(width, height, true);
  }

  @Override
  public void renderUi() {
    stage.act(Gdx.graphics.getDeltaTime());
    stage.draw();
  }

  @Override
  public void destroy() {
    stage.dispose();

    Input.multiplexer.removeProcessor(stage);
  }
}
