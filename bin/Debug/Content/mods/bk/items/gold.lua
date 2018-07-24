local gold = create(item)

function gold:onPickup()
	print("IT IS WORKING!")
end
g = gold:extend()

m = g()
m:Test()

return gold