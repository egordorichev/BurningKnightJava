using BurningKnight.Util.Files;

namespace BurningKnight.Entities.Creature
{
  public class StatefulCreature : Creature
  {
    private string lastState;

    private State<StatefulCreature> state;

    public State<StatefulCreature> CurrentState
    {
      get { return state; }
      set
      {
        state?.OnExit();
        state = value;
        state?.OnEnter();
      }
    }

    public virtual State<StatefulCreature> GetState(string id)
    {
      return null;
    }

    public void Become(string id)
    {
      if (id == lastState) return;

      lastState = id;
      State<StatefulCreature> state = GetState(id);

      if (state == null)
      {
        Log.Error("State '" + id + "' is not defined for " + GetType().Name);
        return;
      }

      CurrentState = state;
    }

    public override void Update(float dt)
    {
      base.Update(dt);
      state?.Update(dt);
    }

    public class State<T> where T : StatefulCreature
    {
      protected float t;

      public virtual void OnEnter()
      {
      }

      public virtual void Update(float dt)
      {
        t += dt;
      }

      public virtual void OnExit()
      {
      }
    }
  }
}