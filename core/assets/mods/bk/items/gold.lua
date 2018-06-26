item:create("gold", {
  sprite = "item-coin",
  use = function(self, owner)
	  print(self:getName() .. " " .. self:getDescription())
  end
})