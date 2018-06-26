item:consumable("bread", {
	pool = "accessory_all",

	use = function(self, owner)
		owner:heal()
	end,

	can_use = function(self, owner)
		return not owner:hasFullHealth()
	end
})