package me.client.font;

import static org.lwjgl.opengl.GL11.GL_NICEST;
import static org.lwjgl.opengl.GL11.GL_POLYGON_SMOOTH_HINT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glHint;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScaled;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2d;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import me.client.utils.TextureUtils;


public class CFont {
	public static final int IMAGE_WIDTH = 512;
	public static final int IMAGE_HEIGHT = 512;
	private int texID;
	private final IntObject[] chars = new IntObject[256];
	private final Font font;
	private boolean antiAlias;
	private int fontHeight = -1;
	private int charOffset = 8;

	public CFont(Font font, boolean antiAlias, int charOffset) {
		this.font = font;
		this.antiAlias = antiAlias;
		this.charOffset = charOffset;
		setupTexture(antiAlias);
	}

	public CFont(Font font, boolean antiAlias) {
		this.font = font;
		this.antiAlias = antiAlias;
		this.charOffset = 8;
		setupTexture(antiAlias);
	}

	private void setupTexture(boolean antiAlias) {
		// Image of 
		BufferedImage img = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) img.getGraphics();
		g.setFont(font);

		g.setColor(new Color(255, 255, 255, 0));
		g.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
		g.setColor(Color.white);
		
		int rowHeight = 0;
		int positionX = 0;
		int positionY = 0;
		// Stolen from the Slick library, it loads the characters into the image used for font rendering.
		for (int i = 0; i < 256; i++) {
			char ch = (char) i;
			// Load an image with the character on it.
			BufferedImage fontImage = getFontImage(ch, antiAlias);

			IntObject newIntObject = new IntObject();

			newIntObject.width = fontImage.getWidth();
			newIntObject.height = fontImage.getHeight();

			if (positionX + newIntObject.width >= IMAGE_WIDTH) {
				positionX = 0;
				positionY += rowHeight;
				rowHeight = 0;
			}

			newIntObject.storedX = positionX;
			newIntObject.storedY = positionY;

			if (newIntObject.height > fontHeight) {
				fontHeight = newIntObject.height;
			}

			if (newIntObject.height > rowHeight) {
				rowHeight = newIntObject.height;
			}
			chars[i] = newIntObject;
			// Draw the image we made for this character and put into the final image we'll be using to render this font in game.
			g.drawImage(fontImage, positionX, positionY, null);

			positionX += newIntObject.width;
		}

		// Render the finished bitmap into the Minecraft
		// graphics engine.
		try {
			texID = TextureUtils.func_110989_a(TextureUtils.func_110996_a(), img, true, true);
		} catch (final NullPointerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Stolen from the Slick library, loads an image with the character specified.
	 * */
	private BufferedImage getFontImage(char ch, boolean antiAlias) {
		// Create a buffered image that we will use as a dummy to figure the spacing for the final image.
		BufferedImage tempfontImage = new BufferedImage(1, 1,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) tempfontImage.getGraphics();
		// Set the rendering hints for text anti aliasing.
		if (antiAlias) {
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		} else
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		// Assign the graphics object a new font.
		g.setFont(font);
		// Get the font metrics and size the image up.
		FontMetrics fontMetrics = g.getFontMetrics();
		int charwidth = fontMetrics.charWidth(ch) + 8;

		if (charwidth <= 0) {
			charwidth = 7;
		}
		int charheight = fontMetrics.getHeight() + 3;
		if (charheight <= 0) {
			charheight = font.getSize();
		}
		// Create a buffered image that we will draw our character onto.
		BufferedImage fontImage = new BufferedImage(charwidth, charheight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D gt = (Graphics2D) fontImage.getGraphics();
		// Set the rendering hints for this graphics
		if (antiAlias) {
			gt.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		} else
			gt.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		// Set the font
		gt.setFont(font);
		// Set the color
		gt.setColor(Color.WHITE);
		int charx = 3;
		int chary = 1;
		gt.drawString(String.valueOf(ch), (charx),
				(chary) + fontMetrics.getAscent());

		return fontImage;

	}
	
	/**
	 * Private drawing method used within other drawing methods.
	 */
	public void drawChar(char c, float x, float y)
			throws ArrayIndexOutOfBoundsException {
		try {
			drawQuad(x, y, (float) chars[c].width, (float) chars[c].height, chars[c].storedX, chars[c].storedY, (float) chars[c].width, (float) chars[c].height);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Again, stolen from the Slick library. Renders at the given image coordinates.
	 * */
	private void drawQuad(float x, float y, float width, float height, float srcX, float srcY, float srcWidth, float srcHeight) {
		float renderSRCX = srcX / IMAGE_WIDTH,
		renderSRCY = srcY / IMAGE_HEIGHT,
		renderSRCWidth = (srcWidth) / IMAGE_WIDTH,
		renderSRCHeight = (srcHeight) / IMAGE_HEIGHT;
		glBegin(GL_TRIANGLES);
		glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
		glVertex2d(x + width, y);
		glTexCoord2f(renderSRCX, renderSRCY);
		glVertex2d(x, y);
		glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
		glVertex2d(x, y + height);
		glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
		glVertex2d(x , y + height);
		glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY + renderSRCHeight);
		glVertex2d(x + width, y + height);
		glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
		glVertex2d(x + width, y);
		glEnd();
	}

	/**
	 * Renders the text.
	 * */
	public void drawString(String text, double x, double y,
			Color color, boolean shadow) {
		x *= 2;
		y = (y * 2) - 6;
		glPushMatrix();
		glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);
		
		glScaled(0.5D, 0.5D, 0.5D);
		glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, texID);
		glColor(shadow ? new Color(0.05F, 0.05F, 0.05F, (float) color.getAlpha() / 255F):color);
		int size = text.length();
		for (int indexInString = 0; indexInString < size; indexInString++) {
			char character = text.charAt(indexInString);
			if (character < chars.length && character >= 0) {
				drawChar(character, (float) x, (float) y);
				x += chars[character].width - charOffset;
			}
		}
		glPopMatrix();
	}
	
	/**
	 * OpenGL coloring.
	 * */
	public void glColor(Color color) {
		float red = (float) color.getRed() / 255F, green = (float) color.getGreen() / 255F, blue = (float) color.getBlue() / 255F, alpha = (float) color.getAlpha() / 255F;
		glColor4f(red, green, blue, alpha);
	}

	/**
	 * @return The height of the given string.
	 */
	public int getStringHeight(String text) {
		return (fontHeight - charOffset) / 2;
	}
	
	/**
	 * @return Total height that the current font can take up.
	 * */
	public int getHeight() {
		return (fontHeight - charOffset) / 2;
	}

	/**
	 * @return The width of the given string.
	 */
	public int getStringWidth(String text) {
		int width = 0;
		for (char c : text.toCharArray()) {
			if (c < chars.length && c >= 0)
				width += chars[c].width - charOffset;
		}
		return width / 2;
	}
	
	public boolean isAntiAlias() {
		return antiAlias;
	}

	public void setAntiAlias(boolean antiAlias) {
		if (this.antiAlias != antiAlias) {
			this.antiAlias = antiAlias;
			setupTexture(antiAlias);
		}
	}
	
	public Font getFont() {
		return font;
	}

	private class IntObject {
		public int width;
		public int height;
		public int storedX;
		public int storedY;
	}
}
