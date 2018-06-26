item:create("gold", {
  use = function(self, owner)
    print(self:getName() .. " " .. self:getDescription())
  end
})