gold = extend(item)

function gold:onPickup() 
	
end

function gold:onDrop()

end

define_item("gold", gold)

-- 3rd arg is quality, if nil, wont spawn in chests / shops
-- 4th is warrior percent, from 0 to 1 or nil
-- 5th is mage percent
-- 6th is ranger percent  