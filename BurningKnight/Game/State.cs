namespace BurningKnight.game
{
  public class State
  {
    public static InGameState INGAME = new InGameState();
    protected Area Area = new Area();

    public virtual void Init()
    {
    }

    public virtual void Destroy()
    {
    }

    public virtual void Update(float dt)
    {
    }

    public virtual void Draw()
    {
    }
  }
}