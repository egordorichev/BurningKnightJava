using BurningKnight.util.files;

namespace BurningKnight.entity.Creature
{
  public class StatedCreature : Creature
  {
    private string _lastState;

    private State<StatedCreature> _state;

    public State<StatedCreature> CurrentState
    {
      get { return _state; }
      set
      {
        _state?.OnExit();
        _state = value;
        _state?.OnEnter();
      }
    }

    public virtual State<StatedCreature> GetState(string id)
    {
      return null;
    }

    public void Become(string id)
    {
      if (id == _lastState) return;

      _lastState = id;
      State<StatedCreature> state = GetState(id);

      if (state == null)
      {
        Log.error("State '" + id + "' is not defined for " + GetType().Name);
        return;
      }

      CurrentState = state;
    }

    public override void Update(float dt)
    {
      base.Update(dt);
      _state?.Update(dt);
    }

    public class State<T> where T : StatedCreature
    {
      protected float _t;

      public virtual void OnEnter()
      {
      }

      public virtual void Update(float dt)
      {
        _t += dt;
      }

      public virtual void OnExit()
      {
      }
    }
  }
}