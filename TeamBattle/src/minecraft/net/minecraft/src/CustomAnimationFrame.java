package net.minecraft.src;

public class CustomAnimationFrame {
	public int counter = 0;
	public int duration = 0;
	public int index = 0;

	public CustomAnimationFrame(int index, int duration) {
		this.index = index;
		this.duration = duration;
	}
}
