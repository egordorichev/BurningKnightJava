local player = extend(creature)
local animation = loadAnimation("actor-gobbo", "-body")

defineCreature(player, "player", {
	hpMax = 8
})

defineState(player, "idle", {
	onEnter = function(state, self)
		
	end,

	onExit = function(state, self)

	end,

	update = function(state, self, dt)

	end
})

function player:init() 
	self.idleAnimation = animation:Get("idle")
	self.runAnimation = animation:Get("run")
	self.hurtAnimation = animation:Get("hurt")
	self.deadAnimation = animation:Get("dead")
	
	self.animation = self.idleAnimation
end

function player:update(dt) 
	if self.hurtTimer > 0 then
		self.animation = self.hurtAnimation
	elseif self.velocity.Length > 100 then
		self.animation = self.runAnimation
	else
		self.animation = self.idleAnimation
	end
	
	self.animation:Update(dt)
end

function player:draw()
	-- something like self:ApplyEffects()
	self.animation:Draw(self.Pos)
	-- self:RemoveEffects()
end