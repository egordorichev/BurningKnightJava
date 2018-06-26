item:create("gold", {
  sprite = "item-gold_shovel",
  use = function(self, owner)
	  print(self:getName() .. " " .. self:getDescription())
  end
})