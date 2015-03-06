package net.minecraft.client.gui.inventory;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatAllowedCharacters;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiEditSign extends GuiScreen {
	private final TileEntitySign field_146848_f;
	private int field_146849_g;
	private int field_146851_h;
	private GuiButton field_146852_i;

	public GuiEditSign(TileEntitySign p_i1097_1_) {
		field_146848_f = p_i1097_1_;
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.enabled) {
			if (p_146284_1_.id == 0) {
				field_146848_f.onInventoryChanged();
				mc.displayGuiScreen((GuiScreen) null);
			}
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj,
				I18n.format("sign.edit", new Object[0]), width / 2, 40,
				16777215);
		GL11.glPushMatrix();
		GL11.glTranslatef(width / 2, 0.0F, 50.0F);
		final float var4 = 93.75F;
		GL11.glScalef(-var4, -var4, -var4);
		GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
		final Block var5 = field_146848_f.getBlockType();

		if (var5 == Blocks.standing_sign) {
			final float var6 = field_146848_f.getBlockMetadata() * 360 / 16.0F;
			GL11.glRotatef(var6, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(0.0F, -1.0625F, 0.0F);
		} else {
			final int var8 = field_146848_f.getBlockMetadata();
			float var7 = 0.0F;

			if (var8 == 2) {
				var7 = 180.0F;
			}

			if (var8 == 4) {
				var7 = 90.0F;
			}

			if (var8 == 5) {
				var7 = -90.0F;
			}

			GL11.glRotatef(var7, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(0.0F, -1.0625F, 0.0F);
		}

		if (field_146849_g / 6 % 2 == 0) {
			field_146848_f.field_145918_i = field_146851_h;
		}

		TileEntityRendererDispatcher.instance.func_147549_a(field_146848_f,
				-0.5D, -0.75D, -0.5D, 0.0F);
		field_146848_f.field_145918_i = -1;
		GL11.glPopMatrix();
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		buttons.clear();
		Keyboard.enableRepeatEvents(true);
		buttons.add(field_146852_i = new GuiButton(0, width / 2 - 100,
				height / 4 + 120, I18n.format("gui.done", new Object[0])));
		field_146848_f.func_145913_a(false);
	}

	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
		if (p_73869_2_ == 200) {
			field_146851_h = field_146851_h - 1 & 3;
		}

		if (p_73869_2_ == 208 || p_73869_2_ == 28 || p_73869_2_ == 156) {
			field_146851_h = field_146851_h + 1 & 3;
		}

		if (p_73869_2_ == 14
				&& field_146848_f.field_145915_a[field_146851_h].length() > 0) {
			field_146848_f.field_145915_a[field_146851_h] = field_146848_f.field_145915_a[field_146851_h]
					.substring(0, field_146848_f.field_145915_a[field_146851_h]
							.length() - 1);
		}

		if (ChatAllowedCharacters.isAllowedCharacter(p_73869_1_)
				&& field_146848_f.field_145915_a[field_146851_h].length() < 15) {
			field_146848_f.field_145915_a[field_146851_h] = field_146848_f.field_145915_a[field_146851_h]
					+ p_73869_1_;
		}

		if (p_73869_2_ == 1) {
			actionPerformed(field_146852_i);
		}
	}

	/**
	 * "Called when the screen is unloaded. Used to disable keyboard repeat events."
	 */
	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
		final NetHandlerPlayClient var1 = mc.getNetHandler();

		if (var1 != null) {
			var1.addToSendQueue(new C12PacketUpdateSign(
					field_146848_f.field_145851_c,
					field_146848_f.field_145848_d,
					field_146848_f.field_145849_e,
					field_146848_f.field_145915_a));
		}

		field_146848_f.func_145913_a(true);
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen() {
		++field_146849_g;
	}
}
