item:equipable("black_heart", {
	sprite = "item-black_heart",
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

local black_heart = setmetatable({},{__index = item})

function black_heart:on_equip()
	self.id = self.owner:registerCallback("on_hurt", function()
		local room = self.owner:getRoom()

		for _, mob in pairs(room:getMobs()) do
			mob:modifyHp(-1, self.owner, true)
		end
	end)
end

function black_heart:on_unequip()
	self.owner:removeCallback("on_hurt", self.id)
end