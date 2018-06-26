item:equipable("halo", {
  pool = "accessory_all",
  on_equip = function(self, owner)
    local had = owner:hasFullHealth()
    owner:modifyHpMax(2)

    if had then
      owner:modifyHp(2)
    end
  end,
  on_unequip = function(self, owner)
    owner:modifyHpMax(-2)
  end
})