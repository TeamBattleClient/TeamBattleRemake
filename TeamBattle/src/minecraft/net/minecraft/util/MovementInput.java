package net.minecraft.util;

public class MovementInput {
	public boolean jump;

	/**
	 * The speed at which the player is moving forward. Negative numbers will
	 * move backwards.
	 */
	public float moveForward;
	/**
	 * The speed at which the player is strafing. Postive numbers to the left
	 * and negative to the right.
	 */
	public float moveStrafe;
	public boolean sneak;

	public void updatePlayerMoveState() {
	}
}
