item:consumable("bread", {
  pool = "accessory_all",
  use = function(self, owner)
    owner:heal()
    game:slowmo(0.5, 2.5)
  end,
  can_use = function(self, owner)
    return not owner:hasFullHealth()
  end
})