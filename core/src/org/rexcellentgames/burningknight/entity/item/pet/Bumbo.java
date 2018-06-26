package org.rexcellentgames.burningknight.entity.item.pet;

import org.rexcellentgames.burningknight.entity.item.pet.impl.BumboPet;
import org.rexcellentgames.burningknight.entity.item.pet.impl.PetEntity;

public class Bumbo extends Pet {  @Override
  public PetEntity create() {
    return new BumboPet();
  }
}