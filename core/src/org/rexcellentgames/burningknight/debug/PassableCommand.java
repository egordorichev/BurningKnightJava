package org.rexcellentgames.burningknight.debug;

import org.jetbrains.annotations.NotNull;
import org.rexcellentgames.burningknight.entity.level.Level;

public class PassableCommand extends ConsoleCommand {
  public PassableCommand() {
    super("/pas", "/p", "Some debug");
  }

  @Override
  public void run(@NotNull Console console, @NotNull String[] args) {
    Level.RENDER_PASSABLE = !Level.RENDER_PASSABLE;
  }
}