package event.events;

import net.minecraft.entity.Entity;
import event.Cancellable;
import event.Event;
public class EventPreAttack extends Event implements Cancellable {


private Entity attacker;
private Entity target;

public EventPreAttack(Entity attacker, Entity target)
{
  this.attacker = attacker;
  this.target = target;
}

public Entity getAttacker() {
  return attacker;
}

public Entity getTarget() {
  return target;
}

@Override
public boolean isCancelled() {
	
	return false;
}

@Override
public void setCancelled(boolean cancel) {
	
	
}
}