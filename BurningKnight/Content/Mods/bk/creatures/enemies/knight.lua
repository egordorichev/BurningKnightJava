knight = extend(enemy)

defineEnemy(knight, "knight", {
	-- stats
	hpMax = 10
}, {
	castle = 1 -- spawn chance in castle
})

defineState(knight, "idle", {
	onEnter = function(state, self) 

	end,
	
	onExit = function(state, self)

	end,
	
	update = function(state, self, dt)

	end
})