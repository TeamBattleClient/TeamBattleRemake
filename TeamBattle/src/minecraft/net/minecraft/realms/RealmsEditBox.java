package net.minecraft.realms;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ChatAllowedCharacters;

import org.lwjgl.opengl.GL11;

public class RealmsEditBox {
	public static final int BACKWARDS = -1;
	public static final int FORWARDS = 1;
	private boolean bordered;
	private boolean canLoseFocus;
	private int cursorPos;
	private int displayPos;
	private final FontRenderer font;
	private int frame;
	private final int height;
	private int highlightPos;
	private boolean inFocus;
	private boolean isEditable;
	private int maxLength;
	private int textColor;
	private int textColorUneditable;
	private String value;
	private boolean visible;
	private final int width;
	private final int x;
	private final int y;

	public RealmsEditBox(FontRenderer p_i1112_1_, int p_i1112_2_,
			int p_i1112_3_, int p_i1112_4_, int p_i1112_5_) {
		value = "";
		maxLength = 32;
		bordered = true;
		canLoseFocus = true;
		isEditable = true;
		textColor = 14737632;
		textColorUneditable = 7368816;
		visible = true;
		font = p_i1112_1_;
		x = p_i1112_2_;
		y = p_i1112_3_;
		width = p_i1112_4_;
		height = p_i1112_5_;
	}

	public RealmsEditBox(int p_i1111_1_, int p_i1111_2_, int p_i1111_3_,
			int p_i1111_4_) {
		this(Minecraft.getMinecraft().fontRenderer, p_i1111_1_, p_i1111_2_,
				p_i1111_3_, p_i1111_4_);
	}

	public void deleteChars(int p_deleteChars_1_) {
		if (value.length() != 0) {
			if (highlightPos != cursorPos) {
				insertText("");
			} else {
				final boolean var2 = p_deleteChars_1_ < 0;
				final int var3 = var2 ? cursorPos + p_deleteChars_1_
						: cursorPos;
				final int var4 = var2 ? cursorPos : cursorPos
						+ p_deleteChars_1_;
				String var5 = "";

				if (var3 >= 0) {
					var5 = value.substring(0, var3);
				}

				if (var4 < value.length()) {
					var5 = var5 + value.substring(var4);
				}

				value = var5;

				if (var2) {
					moveCursor(p_deleteChars_1_);
				}
			}
		}
	}

	public void deleteWords(int p_deleteWords_1_) {
		if (value.length() != 0) {
			if (highlightPos != cursorPos) {
				insertText("");
			} else {
				deleteChars(this.getWordPosition(p_deleteWords_1_) - cursorPos);
			}
		}
	}

	public int getCursorPosition() {
		return cursorPos;
	}

	public String getHighlighted() {
		final int var1 = cursorPos < highlightPos ? cursorPos : highlightPos;
		final int var2 = cursorPos < highlightPos ? highlightPos : cursorPos;
		return value.substring(var1, var2);
	}

	public int getHighlightPos() {
		return highlightPos;
	}

	public int getInnerWidth() {
		return isBordered() ? width - 8 : width;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public int getTextColor() {
		return textColor;
	}

	public int getTextColorUneditable() {
		return textColorUneditable;
	}

	public String getValue() {
		return value;
	}

	public int getWordPosition(int p_getWordPosition_1_) {
		return this.getWordPosition(p_getWordPosition_1_, getCursorPosition());
	}

	public int getWordPosition(int p_getWordPosition_1_,
			int p_getWordPosition_2_) {
		return this.getWordPosition(p_getWordPosition_1_, getCursorPosition(),
				true);
	}

	public int getWordPosition(int p_getWordPosition_1_,
			int p_getWordPosition_2_, boolean p_getWordPosition_3_) {
		int var4 = p_getWordPosition_2_;
		final boolean var5 = p_getWordPosition_1_ < 0;
		final int var6 = Math.abs(p_getWordPosition_1_);

		for (int var7 = 0; var7 < var6; ++var7) {
			if (var5) {
				while (p_getWordPosition_3_ && var4 > 0
						&& value.charAt(var4 - 1) == 32) {
					--var4;
				}

				while (var4 > 0 && value.charAt(var4 - 1) != 32) {
					--var4;
				}
			} else {
				final int var8 = value.length();
				var4 = value.indexOf(32, var4);

				if (var4 == -1) {
					var4 = var8;
				} else {
					while (p_getWordPosition_3_ && var4 < var8
							&& value.charAt(var4) == 32) {
						++var4;
					}
				}
			}
		}

		return var4;
	}

	public void insertText(String p_insertText_1_) {
		String var2 = "";
		final String var3 = ChatAllowedCharacters
				.filerAllowedCharacters(p_insertText_1_);
		final int var4 = cursorPos < highlightPos ? cursorPos : highlightPos;
		final int var5 = cursorPos < highlightPos ? highlightPos : cursorPos;
		final int var6 = maxLength - value.length() - (var4 - highlightPos);
		if (value.length() > 0) {
			var2 = var2 + value.substring(0, var4);
		}

		int var8;

		if (var6 < var3.length()) {
			var2 = var2 + var3.substring(0, var6);
			var8 = var6;
		} else {
			var2 = var2 + var3;
			var8 = var3.length();
		}

		if (value.length() > 0 && var5 < value.length()) {
			var2 = var2 + value.substring(var5);
		}

		value = var2;
		moveCursor(var4 - highlightPos + var8);
	}

	public boolean isBordered() {
		return bordered;
	}

	public boolean isCanLoseFocus() {
		return canLoseFocus;
	}

	public boolean isFocused() {
		return inFocus;
	}

	public boolean isIsEditable() {
		return isEditable;
	}

	public boolean isVisible() {
		return visible;
	}

	public boolean keyPressed(char p_keyPressed_1_, int p_keyPressed_2_) {
		if (!inFocus)
			return false;
		else {
			switch (p_keyPressed_1_) {
			case 1:
				moveCursorToEnd();
				setHighlightPos(0);
				return true;

			case 3:
				GuiScreen.setClipboardString(getHighlighted());
				return true;

			case 22:
				if (isEditable) {
					insertText(GuiScreen.getClipboardString());
				}

				return true;

			case 24:
				GuiScreen.setClipboardString(getHighlighted());

				if (isEditable) {
					insertText("");
				}

				return true;

			default:
				switch (p_keyPressed_2_) {
				case 14:
					if (GuiScreen.isCtrlKeyDown()) {
						if (isEditable) {
							deleteWords(-1);
						}
					} else if (isEditable) {
						deleteChars(-1);
					}

					return true;

				case 199:
					if (GuiScreen.isShiftKeyDown()) {
						setHighlightPos(0);
					} else {
						moveCursorToStart();
					}

					return true;

				case 203:
					if (GuiScreen.isShiftKeyDown()) {
						if (GuiScreen.isCtrlKeyDown()) {
							setHighlightPos(this.getWordPosition(-1,
									getHighlightPos()));
						} else {
							setHighlightPos(getHighlightPos() - 1);
						}
					} else if (GuiScreen.isCtrlKeyDown()) {
						moveCursorTo(this.getWordPosition(-1));
					} else {
						moveCursor(-1);
					}

					return true;

				case 205:
					if (GuiScreen.isShiftKeyDown()) {
						if (GuiScreen.isCtrlKeyDown()) {
							setHighlightPos(this.getWordPosition(1,
									getHighlightPos()));
						} else {
							setHighlightPos(getHighlightPos() + 1);
						}
					} else if (GuiScreen.isCtrlKeyDown()) {
						moveCursorTo(this.getWordPosition(1));
					} else {
						moveCursor(1);
					}

					return true;

				case 207:
					if (GuiScreen.isShiftKeyDown()) {
						setHighlightPos(value.length());
					} else {
						moveCursorToEnd();
					}

					return true;

				case 211:
					if (GuiScreen.isCtrlKeyDown()) {
						if (isEditable) {
							deleteWords(1);
						}
					} else if (isEditable) {
						deleteChars(1);
					}

					return true;

				default:
					if (ChatAllowedCharacters
							.isAllowedCharacter(p_keyPressed_1_)) {
						if (isEditable) {
							insertText(Character.toString(p_keyPressed_1_));
						}

						return true;
					} else
						return false;
				}
			}
		}
	}

	public void mouseClicked(int p_mouseClicked_1_, int p_mouseClicked_2_,
			int p_mouseClicked_3_) {
		final boolean var4 = p_mouseClicked_1_ >= x
				&& p_mouseClicked_1_ < x + width && p_mouseClicked_2_ >= y
				&& p_mouseClicked_2_ < y + height;

		if (canLoseFocus) {
			setFocus(var4);
		}

		if (inFocus && p_mouseClicked_3_ == 0) {
			int var5 = p_mouseClicked_1_ - x;

			if (bordered) {
				var5 -= 4;
			}

			final String var6 = font.trimStringToWidth(
					value.substring(displayPos), getInnerWidth());
			moveCursorTo(font.trimStringToWidth(var6, var5).length()
					+ displayPos);
		}
	}

	public void moveCursor(int p_moveCursor_1_) {
		moveCursorTo(highlightPos + p_moveCursor_1_);
	}

	public void moveCursorTo(int p_moveCursorTo_1_) {
		cursorPos = p_moveCursorTo_1_;
		final int var2 = value.length();

		if (cursorPos < 0) {
			cursorPos = 0;
		}

		if (cursorPos > var2) {
			cursorPos = var2;
		}

		setHighlightPos(cursorPos);
	}

	public void moveCursorToEnd() {
		moveCursorTo(value.length());
	}

	public void moveCursorToStart() {
		moveCursorTo(0);
	}

	public void render() {
		if (isVisible()) {
			if (isBordered()) {
				Gui.drawRect(x - 1, y - 1, x + width + 1, y + height + 1,
						-6250336);
				Gui.drawRect(x, y, x + width, y + height, -16777216);
			}

			final int var1 = isEditable ? textColor : textColorUneditable;
			final int var2 = cursorPos - displayPos;
			int var3 = highlightPos - displayPos;
			final String var4 = font.trimStringToWidth(
					value.substring(displayPos), getInnerWidth());
			final boolean var5 = var2 >= 0 && var2 <= var4.length();
			final boolean var6 = inFocus && frame / 6 % 2 == 0 && var5;
			final int var7 = bordered ? x + 4 : x;
			final int var8 = bordered ? y + (height - 8) / 2 : y;
			int var9 = var7;

			if (var3 > var4.length()) {
				var3 = var4.length();
			}

			if (var4.length() > 0) {
				final String var10 = var5 ? var4.substring(0, var2) : var4;
				var9 = font.drawStringWithShadow(var10, var7, var8, var1);
			}

			final boolean var13 = cursorPos < value.length()
					|| value.length() >= getMaxLength();
			int var11 = var9;

			if (!var5) {
				var11 = var2 > 0 ? var7 + width : var7;
			} else if (var13) {
				var11 = var9 - 1;
				--var9;
			}

			if (var4.length() > 0 && var5 && var2 < var4.length()) {
				font.drawStringWithShadow(var4.substring(var2), var9, var8,
						var1);
			}

			if (var6) {
				if (var13) {
					Gui.drawRect(var11, var8 - 1, var11 + 1, var8 + 1
							+ font.FONT_HEIGHT, -3092272);
				} else {
					font.drawStringWithShadow("_", var11, var8, var1);
				}
			}

			if (var3 != var2) {
				final int var12 = var7
						+ font.getStringWidth(var4.substring(0, var3));
				renderHighlight(var11, var8 - 1, var12 - 1, var8 + 1
						+ font.FONT_HEIGHT);
			}
		}
	}

	private void renderHighlight(int p_renderHighlight_1_,
			int p_renderHighlight_2_, int p_renderHighlight_3_,
			int p_renderHighlight_4_) {
		int var5;

		if (p_renderHighlight_1_ < p_renderHighlight_3_) {
			var5 = p_renderHighlight_1_;
			p_renderHighlight_1_ = p_renderHighlight_3_;
			p_renderHighlight_3_ = var5;
		}

		if (p_renderHighlight_2_ < p_renderHighlight_4_) {
			var5 = p_renderHighlight_2_;
			p_renderHighlight_2_ = p_renderHighlight_4_;
			p_renderHighlight_4_ = var5;
		}

		if (p_renderHighlight_3_ > x + width) {
			p_renderHighlight_3_ = x + width;
		}

		if (p_renderHighlight_1_ > x + width) {
			p_renderHighlight_1_ = x + width;
		}

		final Tessellator var6 = Tessellator.instance;
		GL11.glColor4f(0.0F, 0.0F, 255.0F, 255.0F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_COLOR_LOGIC_OP);
		GL11.glLogicOp(GL11.GL_OR_REVERSE);
		var6.startDrawingQuads();
		var6.addVertex(p_renderHighlight_1_, p_renderHighlight_4_, 0.0D);
		var6.addVertex(p_renderHighlight_3_, p_renderHighlight_4_, 0.0D);
		var6.addVertex(p_renderHighlight_3_, p_renderHighlight_2_, 0.0D);
		var6.addVertex(p_renderHighlight_1_, p_renderHighlight_2_, 0.0D);
		var6.draw();
		GL11.glDisable(GL11.GL_COLOR_LOGIC_OP);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public void setBordered(boolean p_setBordered_1_) {
		bordered = p_setBordered_1_;
	}

	public void setCanLoseFocus(boolean p_setCanLoseFocus_1_) {
		canLoseFocus = p_setCanLoseFocus_1_;
	}

	public void setFocus(boolean p_setFocus_1_) {
		if (p_setFocus_1_ && !inFocus) {
			frame = 0;
		}

		inFocus = p_setFocus_1_;
	}

	public void setHighlightPos(int p_setHighlightPos_1_) {
		final int var2 = value.length();

		if (p_setHighlightPos_1_ > var2) {
			p_setHighlightPos_1_ = var2;
		}

		if (p_setHighlightPos_1_ < 0) {
			p_setHighlightPos_1_ = 0;
		}

		highlightPos = p_setHighlightPos_1_;

		if (font != null) {
			if (displayPos > var2) {
				displayPos = var2;
			}

			final int var3 = getInnerWidth();
			final String var4 = font.trimStringToWidth(
					value.substring(displayPos), var3);
			final int var5 = var4.length() + displayPos;

			if (p_setHighlightPos_1_ == displayPos) {
				displayPos -= font.trimStringToWidth(value, var3, true)
						.length();
			}

			if (p_setHighlightPos_1_ > var5) {
				displayPos += p_setHighlightPos_1_ - var5;
			} else if (p_setHighlightPos_1_ <= displayPos) {
				displayPos -= displayPos - p_setHighlightPos_1_;
			}

			if (displayPos < 0) {
				displayPos = 0;
			}

			if (displayPos > var2) {
				displayPos = var2;
			}
		}
	}

	public void setIsEditable(boolean p_setIsEditable_1_) {
		isEditable = p_setIsEditable_1_;
	}

	public void setMaxLength(int p_setMaxLength_1_) {
		maxLength = p_setMaxLength_1_;

		if (value.length() > p_setMaxLength_1_) {
			value = value.substring(0, p_setMaxLength_1_);
		}
	}

	public void setTextColor(int p_setTextColor_1_) {
		textColor = p_setTextColor_1_;
	}

	public void setTextColorUneditable(int p_setTextColorUneditable_1_) {
		textColorUneditable = p_setTextColorUneditable_1_;
	}

	public void setValue(String p_setValue_1_) {
		if (p_setValue_1_.length() > maxLength) {
			value = p_setValue_1_.substring(0, maxLength);
		} else {
			value = p_setValue_1_;
		}

		moveCursorToEnd();
	}

	public void setVisible(boolean p_setVisible_1_) {
		visible = p_setVisible_1_;
	}

	public void tick() {
		++frame;
	}
}
