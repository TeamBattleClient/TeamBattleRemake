package down.TeamBattle.EventSystem;

public interface Cancellable {
	public boolean isCancelled();

	public void setCancelled(boolean cancel);
}
