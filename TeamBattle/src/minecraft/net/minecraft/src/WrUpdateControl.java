package net.minecraft.src;

public class WrUpdateControl implements IWrUpdateControl {
	private final boolean hasForge;

	public WrUpdateControl() {
		hasForge = Reflector.ForgeHooksClient.exists();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	public void setRenderPass(int renderPass) {
	}
}
