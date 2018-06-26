package org.rexcellentgames.burningknight.entity.item.pet.orbital;

import org.rexcellentgames.burningknight.entity.item.pet.Pet;
import org.rexcellentgames.burningknight.entity.item.pet.impl.Orbital;
import org.rexcellentgames.burningknight.entity.item.pet.impl.PetEntity;

public class JellyOrbital extends Pet {  @Override
  public PetEntity create() {
    return new Impl();
  }

  public static class Impl extends Orbital {
    {

    }
  }
}