package down.TeamBattle.EventSystem.events;

import down.TeamBattle.EventSystem.Event;

public class EventRenderAsNormalBlock extends Event {
	private boolean renderAsNormalBlock;

	public EventRenderAsNormalBlock(boolean renderAsNormalBlock) {
		this.renderAsNormalBlock = renderAsNormalBlock;
	}

	public void setRenderAsNormalBlock(boolean renderAsNormalBlock) {
		this.renderAsNormalBlock = renderAsNormalBlock;
	}

	public boolean shouldRenderAsNormalBlock() {
		return renderAsNormalBlock;
	}
}
