namespace BurningKnight.Game
{
  public class State
  {
    public static InGameState InGame { get; } = new InGameState(); // I hope this works
    protected Area area = new Area();

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