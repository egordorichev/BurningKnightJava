local gold = item:create("gold", {
  sprite = "item-gold_shovel",
  use = function()
    print("used!")
  end
})

return gold