knight = setmetatable({},{__index = mob})

function knight:init()
	mob:init(self)

end

function knight:idle()

end