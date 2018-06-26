item:equipable("black_heart", {
	sprite = "item-gold_shovel",
	pool = "accessory_all",

	on_equip = function(self, owner)
		self:set("hurt_callback", owner:registerCallback("on_hurt", function(owner)
			local room = owner:getRoom()

			for _, mob in pairs(room:getMobs()) do
				mob:modifyHp(-1, owner, true)
			end
		end))
	end,

	on_unequip = function(self, owner)
		owner:removeCallback("on_hurt", self:get("hurt_callback"))
	end
})