item:consumable("pill", {
	pool = "accessory_all",

	use = function(self, owner)
		owner:heal()
		game:slowmo(0.5, 5)
		game:glitch(4)
	end,

	can_use = function(self, owner)
		return not owner:hasFullHealth()
	end
})