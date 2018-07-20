package org.rexcellentgames.burningknight.entity.item.weapon.sword.butcher;

import org.rexcellentgames.burningknight.entity.item.weapon.sword.Sword;

public class Butcher extends Sword {
	{
		maxAngle = 90;
		timeA = 0.05f;
		timeB = 0.15f;
		useTime = 0.3f;
		timeDelay = useTime - timeA - timeB;
		penetrates = true;
	}
}