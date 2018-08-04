knight = extend(enemy)

define_state(knight, "idle", {
	onEnter = function() 
	
	end,
	
	onExit = function() 
		
	end,
	
	update = function()
		
	end
})

define_enemy(knight, "knight", {
	-- stats
	hpMax = 10
}, { 
	castle = 1 -- spawn chance in castle
})