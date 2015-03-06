package net.minecraft.client.gui;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.src.Config;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;

public class FontRenderer implements IResourceManagerReloadListener {
	private static final ResourceLocation[] unicodePageLocations = new ResourceLocation[256];

	/**
	 * Digests a string for nonprinting formatting characters then returns a
	 * string containing only that formatting.
	 */
	private static String getFormatFromString(String par0Str) {
		String var1 = "";
		int var2 = -1;
		final int var3 = par0Str.length();

		while ((var2 = par0Str.indexOf(167, var2 + 1)) != -1) {
			if (var2 < var3 - 1) {
				final char var4 = par0Str.charAt(var2 + 1);

				if (isFormatColor(var4)) {
					var1 = "\u00a7" + var4;
				} else if (isFormatSpecial(var4)) {
					var1 = var1 + "\u00a7" + var4;
				}
			}
		}

		return var1;
	}

	private static ResourceLocation getHdFontLocation(ResourceLocation fontLoc) {
		if (!Config.isCustomFonts())
			return fontLoc;
		else if (fontLoc == null)
			return fontLoc;
		else {
			String fontName = fontLoc.getResourcePath();
			final String texturesStr = "textures/";
			final String mcpatcherStr = "mcpatcher/";

			if (!fontName.startsWith(texturesStr))
				return fontLoc;
			else {
				fontName = fontName.substring(texturesStr.length());
				fontName = mcpatcherStr + fontName;
				final ResourceLocation fontLocHD = new ResourceLocation(
						fontLoc.getResourceDomain(), fontName);
				return Config.hasResource(Config.getResourceManager(),
						fontLocHD) ? fontLocHD : fontLoc;
			}
		}
	}

	/**
	 * Checks if the char code is a hexadecimal character, used to set colour.
	 */
	private static boolean isFormatColor(char par0) {
		return par0 >= 48 && par0 <= 57 || par0 >= 97 && par0 <= 102
				|| par0 >= 65 && par0 <= 70;
	}

	/**
	 * Checks if the char code is O-K...lLrRk-o... used to set special
	 * formatting.
	 */
	private static boolean isFormatSpecial(char par0) {
		return par0 >= 107 && par0 <= 111 || par0 >= 75 && par0 <= 79
				|| par0 == 114 || par0 == 82;
	}

	/** Used to speify new alpha value for the current color. */
	private float alpha;
	/**
	 * If true, the Unicode Bidirectional Algorithm should be run before
	 * rendering any string.
	 */
	private boolean bidiFlag;

	/** Used to specify new blue value for the current color. */
	private float blue;

	/** Set if the "l" style (bold) is active in currently rendering string */
	private boolean boldStyle;

	/** Array of width of all the characters in default.png */
	private final float[] charWidth = new float[256];

	/**
	 * Array of RGB triplets defining the 16 standard chat colors followed by 16
	 * darker version of the same colors for drop shadows.
	 */
	public final int[] colorCode = new int[32];

	public boolean enabled = true;

	/** the height in pixels of default text */
	public int FONT_HEIGHT = 9;

	public Random fontRandom = new Random();

	public GameSettings gameSettings;

	/**
	 * Array of the start/end column (in upper/lower nibble) for every glyph in
	 * the /font directory.
	 */
	private final byte[] glyphWidth = new byte[65536];

	/** Used to specify new green value for the current color. */
	private float green;

	/** Set if the "o" style (italic) is active in currently rendering string */
	private boolean italicStyle;

	private ResourceLocation locationFontTexture;

	public ResourceLocation locationFontTextureBase;

	/** Current X coordinate at which to draw the next character. */
	private float posX;

	/** Current Y coordinate at which to draw the next character. */
	private float posY;

	/** Set if the "k" style (random) is active in currently rendering string */
	private boolean randomStyle;
	/** Used to specify new red value for the current color. */
	private float red;
	/** The RenderEngine used to load and setup glyph textures. */
	private final TextureManager renderEngine;
	public float scaleFactor = 1.0F;

	/**
	 * Set if the "m" style (strikethrough) is active in currently rendering
	 * string
	 */
	private boolean strikethroughStyle;

	/** Text color of the currently rendering string. */
	private int textColor;

	/**
	 * Set if the "n" style (underlined) is active in currently rendering string
	 */
	private boolean underlineStyle;

	/**
	 * If true, strings should be rendered with Unicode fonts instead of the
	 * default.png font
	 */
	private boolean unicodeFlag;

	public FontRenderer(GameSettings par1GameSettings,
			ResourceLocation par2ResourceLocation,
			TextureManager par3TextureManager, boolean par4) {
		gameSettings = par1GameSettings;
		locationFontTextureBase = par2ResourceLocation;
		locationFontTexture = par2ResourceLocation;
		renderEngine = par3TextureManager;
		unicodeFlag = par4;
		locationFontTexture = getHdFontLocation(locationFontTextureBase);
		par3TextureManager.bindTexture(locationFontTexture);

		for (int var5 = 0; var5 < 32; ++var5) {
			final int var6 = (var5 >> 3 & 1) * 85;
			int var7 = (var5 >> 2 & 1) * 170 + var6;
			int var8 = (var5 >> 1 & 1) * 170 + var6;
			int var9 = (var5 >> 0 & 1) * 170 + var6;

			if (var5 == 6) {
				var7 += 85;
			}

			if (par1GameSettings.anaglyph) {
				final int var10 = (var7 * 30 + var8 * 59 + var9 * 11) / 100;
				final int var11 = (var7 * 30 + var8 * 70) / 100;
				final int var12 = (var7 * 30 + var9 * 70) / 100;
				var7 = var10;
				var8 = var11;
				var9 = var12;
			}

			if (var5 >= 16) {
				var7 /= 4;
				var8 /= 4;
				var9 /= 4;
			}

			colorCode[var5] = (var7 & 255) << 16 | (var8 & 255) << 8 | var9
					& 255;
		}

		readGlyphSizes();
	}

	/**
	 * Splits and draws a String with wordwrap (maximum length is parameter k)
	 */
	public void drawSplitString(String par1Str, int par2, int par3, int par4,
			int par5) {
		resetStyles();
		textColor = par5;
		par1Str = trimStringNewline(par1Str);
		renderSplitString(par1Str, par2, par3, par4, false);
	}

	/**
	 * Draws the specified string.
	 */
	public int drawString(String par1Str, int par2, int par3, int par4) {
		return !enabled ? 0 : this.drawString(par1Str, par2, par3, par4, false);
	}

	/**
	 * Draws the specified string. Args: string, x, y, color, dropShadow
	 */
	public int drawString(String par1Str, int par2, int par3, int par4,
			boolean par5) {
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		resetStyles();
		int var6;

		if (par5) {
			var6 = renderString(par1Str, par2 + 1, par3 + 1, par4, true);
			var6 = Math.max(var6,
					renderString(par1Str, par2, par3, par4, false));
		} else {
			var6 = renderString(par1Str, par2, par3, par4, false);
		}

		return var6;
	}

	/**
	 * Draws the specified string with a shadow.
	 */
	public int drawStringWithShadow(String par1Str, int par2, int par3, int par4) {
		return this.drawString(par1Str, par2, par3, par4, true);
	}

	private String func_147647_b(String p_147647_1_) {
		try {
			final Bidi var3 = new Bidi(new ArabicShaping(8).shape(p_147647_1_),
					127);
			var3.setReorderingMode(0);
			return var3.writeReordered(2);
		} catch (final ArabicShapingException var31) {
			return p_147647_1_;
		}
	}

	/**
	 * Get bidiFlag that controls if the Unicode Bidirectional Algorithm should
	 * be run before rendering any string
	 */
	public boolean getBidiFlag() {
		return bidiFlag;
	}

	/**
	 * Returns the width of this character as rendered.
	 */
	public int getCharWidth(char par1) {
		return Math.round(getCharWidthFloat(par1));
	}

	private float getCharWidthFloat(char par1) {
		if (par1 == 167)
			return -1.0F;
		else if (par1 == 32)
			return charWidth[32];
		else {
			final int var2 = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000"
					.indexOf(par1);

			if (par1 > 0 && var2 != -1 && !unicodeFlag)
				return charWidth[var2];
			else if (glyphWidth[par1] != 0) {
				int var3 = glyphWidth[par1] >>> 4;
				int var4 = glyphWidth[par1] & 15;

				if (var4 > 7) {
					var4 = 15;
					var3 = 0;
				}

				++var4;
				return (var4 - var3) / 2 + 1;
			} else
				return 0.0F;
		}
	}

	/**
	 * Returns the width of this string. Equivalent of
	 * FontMetrics.stringWidth(String s).
	 */
	public int getStringWidth(String par1Str) {
		if (par1Str == null)
			return 0;
		else {
			float var2 = 0.0F;
			boolean var3 = false;

			for (int var4 = 0; var4 < par1Str.length(); ++var4) {
				char var5 = par1Str.charAt(var4);
				float var6 = getCharWidthFloat(var5);

				if (var6 < 0.0F && var4 < par1Str.length() - 1) {
					++var4;
					var5 = par1Str.charAt(var4);

					if (var5 != 108 && var5 != 76) {
						if (var5 == 114 || var5 == 82) {
							var3 = false;
						}
					} else {
						var3 = true;
					}

					var6 = 0.0F;
				}

				var2 += var6;

				if (var3 && var6 > 0.0F) {
					++var2;
				}
			}

			return (int) var2;
		}
	}

	/**
	 * Get unicodeFlag controlling whether strings should be rendered with
	 * Unicode fonts instead of the default.png font.
	 */
	public boolean getUnicodeFlag() {
		return unicodeFlag;
	}

	private ResourceLocation getUnicodePageLocation(int par1) {
		if (unicodePageLocations[par1] == null) {
			unicodePageLocations[par1] = new ResourceLocation(String.format(
					"textures/font/unicode_page_%02x.png",
					new Object[] { Integer.valueOf(par1) }));
			unicodePageLocations[par1] = getHdFontLocation(unicodePageLocations[par1]);
		}

		return unicodePageLocations[par1];
	}

	/**
	 * Breaks a string into a list of pieces that will fit a specified width.
	 */
	public List listFormattedStringToWidth(String par1Str, int par2) {
		return Arrays.asList(wrapFormattedStringToWidth(par1Str, par2).split(
				"\n"));
	}

	/**
	 * Load one of the /font/glyph_XX.png into a new GL texture and store the
	 * texture ID in glyphTextureName array.
	 */
	private void loadGlyphTexture(int par1) {
		renderEngine.bindTexture(getUnicodePageLocation(par1));
	}

	@Override
	public void onResourceManagerReload(IResourceManager par1ResourceManager) {
		locationFontTexture = getHdFontLocation(locationFontTextureBase);

		for (int i = 0; i < unicodePageLocations.length; ++i) {
			unicodePageLocations[i] = null;
		}

		readFontTexture();
	}

	private void readCustomCharWidths() {
		final String fontFileName = locationFontTexture.getResourcePath();
		final String suffix = ".png";

		if (fontFileName.endsWith(suffix)) {
			final String fileName = fontFileName.substring(0,
					fontFileName.length() - suffix.length())
					+ ".properties";

			try {
				final ResourceLocation e = new ResourceLocation(
						locationFontTexture.getResourceDomain(), fileName);
				final InputStream in = Config.getResourceStream(
						Config.getResourceManager(), e);

				if (in == null)
					return;

				Config.log("Loading " + fileName);
				final Properties props = new Properties();
				props.load(in);
				final Set keySet = props.keySet();
				final Iterator iter = keySet.iterator();

				while (iter.hasNext()) {
					final String key = (String) iter.next();
					final String prefix = "width.";

					if (key.startsWith(prefix)) {
						final String numStr = key.substring(prefix.length());
						final int num = Config.parseInt(numStr, -1);

						if (num >= 0 && num < charWidth.length) {
							final String value = props.getProperty(key);
							final float width = Config.parseFloat(value, -1.0F);

							if (width >= 0.0F) {
								charWidth[num] = width;
							}
						}
					}
				}
			} catch (final FileNotFoundException var15) {
				;
			} catch (final IOException var16) {
				var16.printStackTrace();
			}
		}
	}

	private void readFontTexture() {
		BufferedImage bufferedimage;

		try {
			bufferedimage = ImageIO.read(Minecraft.getMinecraft()
					.getResourceManager().getResource(locationFontTexture)
					.getInputStream());
		} catch (final IOException var18) {
			throw new RuntimeException(var18);
		}

		final int imgWidth = bufferedimage.getWidth();
		final int imgHeight = bufferedimage.getHeight();
		final int charW = imgWidth / 16;
		final int charH = imgHeight / 16;
		final float kx = imgWidth / 128.0F;
		scaleFactor = kx;
		final int[] ai = new int[imgWidth * imgHeight];
		bufferedimage.getRGB(0, 0, imgWidth, imgHeight, ai, 0, imgWidth);
		int k = 0;

		while (k < 256) {
			final int cx = k % 16;
			final int cy = k / 16;
			int var19 = charW - 1;

			while (true) {
				if (var19 >= 0) {
					final int x = cx * charW + var19;
					boolean flag = true;

					for (int py = 0; py < charH && flag; ++py) {
						final int ypos = (cy * charH + py) * imgWidth;
						final int col = ai[x + ypos];
						final int al = col >> 24 & 255;

						if (al > 16) {
							flag = false;
						}
					}

					if (flag) {
						--var19;
						continue;
					}
				}

				if (k == 32) {
					if (charW <= 8) {
						var19 = (int) (2.0F * kx);
					} else {
						var19 = (int) (1.5F * kx);
					}
				}

				charWidth[k] = (var19 + 1) / kx + 1.0F;
				++k;
				break;
			}
		}

		readCustomCharWidths();
	}

	private void readGlyphSizes() {
		try {
			final InputStream var2 = Minecraft.getMinecraft()
					.getResourceManager()
					.getResource(new ResourceLocation("font/glyph_sizes.bin"))
					.getInputStream();
			var2.read(glyphWidth);
		} catch (final IOException var21) {
			throw new RuntimeException(var21);
		}
	}

	/**
	 * Pick how to render a single character and return the width used.
	 */
	private float renderCharAtPos(int par1, char par2, boolean par3) {
		return par2 == 32 ? charWidth[par2]
				: par2 == 32 ? 4.0F
						: "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000"
								.indexOf(par2) != -1 && !unicodeFlag ? renderDefaultChar(
								par1, par3) : renderUnicodeChar(par2, par3);
	}

	/**
	 * Render a single character with the default.png font at current
	 * (posX,posY) location...
	 */
	private float renderDefaultChar(int par1, boolean par2) {
		final float var3 = par1 % 16 * 8;
		final float var4 = par1 / 16 * 8;
		final float var5 = par2 ? 1.0F : 0.0F;
		renderEngine.bindTexture(locationFontTexture);
		final float var6 = 7.99F;
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glTexCoord2f(var3 / 128.0F, var4 / 128.0F);
		GL11.glVertex3f(posX + var5, posY, 0.0F);
		GL11.glTexCoord2f(var3 / 128.0F, (var4 + 7.99F) / 128.0F);
		GL11.glVertex3f(posX - var5, posY + 7.99F, 0.0F);
		GL11.glTexCoord2f((var3 + var6 - 1.0F) / 128.0F, var4 / 128.0F);
		GL11.glVertex3f(posX + var6 - 1.0F + var5, posY, 0.0F);
		GL11.glTexCoord2f((var3 + var6 - 1.0F) / 128.0F,
				(var4 + 7.99F) / 128.0F);
		GL11.glVertex3f(posX + var6 - 1.0F - var5, posY + 7.99F, 0.0F);
		GL11.glEnd();
		return charWidth[par1];
	}

	/**
	 * Perform actual work of rendering a multi-line string with wordwrap and
	 * with darker drop shadow color if flag is set
	 */
	private void renderSplitString(String par1Str, int par2, int par3,
			int par4, boolean par5) {
		final List var6 = listFormattedStringToWidth(par1Str, par4);

		for (final Iterator var7 = var6.iterator(); var7.hasNext(); par3 += FONT_HEIGHT) {
			final String var8 = (String) var7.next();
			renderStringAligned(var8, par2, par3, par4, textColor, par5);
		}
	}

	/**
	 * Render single line string by setting GL color, current (posX,posY), and
	 * calling renderStringAtPos()
	 */
	private int renderString(String par1Str, int par2, int par3, int par4,
			boolean par5) {
		if (par1Str == null)
			return 0;
		else {
			if (bidiFlag) {
				par1Str = func_147647_b(par1Str);
			}

			if ((par4 & -67108864) == 0) {
				par4 |= -16777216;
			}

			if (par5) {
				par4 = (par4 & 16579836) >> 2 | par4 & -16777216;
			}

			red = (par4 >> 16 & 255) / 255.0F;
			blue = (par4 >> 8 & 255) / 255.0F;
			green = (par4 & 255) / 255.0F;
			alpha = (par4 >> 24 & 255) / 255.0F;
			GL11.glColor4f(red, blue, green, alpha);
			posX = par2;
			posY = par3;
			renderStringAtPos(par1Str, par5);
			return (int) posX;
		}
	}

	/**
	 * Render string either left or right aligned depending on bidiFlag
	 */
	private int renderStringAligned(String par1Str, int par2, int par3,
			int par4, int par5, boolean par6) {
		if (bidiFlag) {
			final int var7 = getStringWidth(func_147647_b(par1Str));
			par2 = par2 + par4 - var7;
		}

		return renderString(par1Str, par2, par3, par5, par6);
	}

	/**
	 * Render a single line string at the current (posX,posY) and update posX
	 */
	private void renderStringAtPos(String par1Str, boolean par2) {
		for (int var3 = 0; var3 < par1Str.length(); ++var3) {
			final char var4 = par1Str.charAt(var3);
			int var5;
			int var6;

			if (var4 == 167 && var3 + 1 < par1Str.length()) {
				var5 = "0123456789abcdefklmnor".indexOf(par1Str.toLowerCase()
						.charAt(var3 + 1));

				if (var5 < 16) {
					randomStyle = false;
					boldStyle = false;
					strikethroughStyle = false;
					underlineStyle = false;
					italicStyle = false;

					if (var5 < 0 || var5 > 15) {
						var5 = 15;
					}

					if (par2) {
						var5 += 16;
					}

					var6 = colorCode[var5];
					textColor = var6;
					GL11.glColor4f((var6 >> 16) / 255.0F,
							(var6 >> 8 & 255) / 255.0F, (var6 & 255) / 255.0F,
							alpha);
				} else if (var5 == 16) {
					randomStyle = true;
				} else if (var5 == 17) {
					boldStyle = true;
				} else if (var5 == 18) {
					strikethroughStyle = true;
				} else if (var5 == 19) {
					underlineStyle = true;
				} else if (var5 == 20) {
					italicStyle = true;
				} else if (var5 == 21) {
					randomStyle = false;
					boldStyle = false;
					strikethroughStyle = false;
					underlineStyle = false;
					italicStyle = false;
					GL11.glColor4f(red, blue, green, alpha);
				}

				++var3;
			} else {
				var5 = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000"
						.indexOf(var4);

				if (randomStyle && var5 != -1) {
					do {
						var6 = fontRandom.nextInt(charWidth.length);
					} while ((int) charWidth[var5] != (int) charWidth[var6]);

					var5 = var6;
				}

				final float var11 = unicodeFlag ? 0.5F : 1.0F / scaleFactor;
				final boolean var7 = (var4 == 0 || var5 == -1 || unicodeFlag)
						&& par2;

				if (var7) {
					posX -= var11;
					posY -= var11;
				}

				float var8 = renderCharAtPos(var5, var4, italicStyle);

				if (var7) {
					posX += var11;
					posY += var11;
				}

				if (boldStyle) {
					posX += var11;

					if (var7) {
						posX -= var11;
						posY -= var11;
					}

					renderCharAtPos(var5, var4, italicStyle);
					posX -= var11;

					if (var7) {
						posX += var11;
						posY += var11;
					}

					var8 += var11;
				}

				Tessellator var9;

				if (strikethroughStyle) {
					var9 = Tessellator.instance;
					GL11.glDisable(GL11.GL_TEXTURE_2D);
					var9.startDrawingQuads();
					var9.addVertex(posX, posY + FONT_HEIGHT / 2, 0.0D);
					var9.addVertex(posX + var8, posY + FONT_HEIGHT / 2, 0.0D);
					var9.addVertex(posX + var8, posY + FONT_HEIGHT / 2 - 1.0F,
							0.0D);
					var9.addVertex(posX, posY + FONT_HEIGHT / 2 - 1.0F, 0.0D);
					var9.draw();
					GL11.glEnable(GL11.GL_TEXTURE_2D);
				}

				if (underlineStyle) {
					var9 = Tessellator.instance;
					GL11.glDisable(GL11.GL_TEXTURE_2D);
					var9.startDrawingQuads();
					final int var10 = underlineStyle ? -1 : 0;
					var9.addVertex(posX + var10, posY + FONT_HEIGHT, 0.0D);
					var9.addVertex(posX + var8, posY + FONT_HEIGHT, 0.0D);
					var9.addVertex(posX + var8, posY + FONT_HEIGHT - 1.0F, 0.0D);
					var9.addVertex(posX + var10, posY + FONT_HEIGHT - 1.0F,
							0.0D);
					var9.draw();
					GL11.glEnable(GL11.GL_TEXTURE_2D);
				}

				posX += var8;
			}
		}
	}

	/**
	 * Render a single Unicode character at current (posX,posY) location using
	 * one of the /font/glyph_XX.png files...
	 */
	private float renderUnicodeChar(char par1, boolean par2) {
		if (glyphWidth[par1] == 0)
			return 0.0F;
		else {
			final int var3 = par1 / 256;
			loadGlyphTexture(var3);
			final int var4 = glyphWidth[par1] >>> 4;
			final int var5 = glyphWidth[par1] & 15;
			final float var6 = var4;
			final float var7 = var5 + 1;
			final float var8 = par1 % 16 * 16 + var6;
			final float var9 = (par1 & 255) / 16 * 16;
			final float var10 = var7 - var6 - 0.02F;
			final float var11 = par2 ? 1.0F : 0.0F;
			GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
			GL11.glTexCoord2f(var8 / 256.0F, var9 / 256.0F);
			GL11.glVertex3f(posX + var11, posY, 0.0F);
			GL11.glTexCoord2f(var8 / 256.0F, (var9 + 15.98F) / 256.0F);
			GL11.glVertex3f(posX - var11, posY + 7.99F, 0.0F);
			GL11.glTexCoord2f((var8 + var10) / 256.0F, var9 / 256.0F);
			GL11.glVertex3f(posX + var10 / 2.0F + var11, posY, 0.0F);
			GL11.glTexCoord2f((var8 + var10) / 256.0F, (var9 + 15.98F) / 256.0F);
			GL11.glVertex3f(posX + var10 / 2.0F - var11, posY + 7.99F, 0.0F);
			GL11.glEnd();
			return (var7 - var6) / 2.0F + 1.0F;
		}
	}

	/**
	 * Reset all style flag fields in the class to false; called at the start of
	 * string rendering
	 */
	private void resetStyles() {
		randomStyle = false;
		boldStyle = false;
		italicStyle = false;
		underlineStyle = false;
		strikethroughStyle = false;
	}

	/**
	 * Set bidiFlag to control if the Unicode Bidirectional Algorithm should be
	 * run before rendering any string.
	 */
	public void setBidiFlag(boolean par1) {
		bidiFlag = par1;
	}

	/**
	 * Set unicodeFlag controlling whether strings should be rendered with
	 * Unicode fonts instead of the default.png font.
	 */
	public void setUnicodeFlag(boolean par1) {
		unicodeFlag = par1;
	}

	/**
	 * Determines how many characters from the string will fit into the
	 * specified width.
	 */
	private int sizeStringToWidth(String par1Str, int par2) {
		final int var3 = par1Str.length();
		float var4 = 0.0F;
		int var5 = 0;
		int var6 = -1;

		for (boolean var7 = false; var5 < var3; ++var5) {
			final char var8 = par1Str.charAt(var5);

			switch (var8) {
			case 10:
				--var5;
				break;

			case 167:
				if (var5 < var3 - 1) {
					++var5;
					final char var9 = par1Str.charAt(var5);

					if (var9 != 108 && var9 != 76) {
						if (var9 == 114 || var9 == 82 || isFormatColor(var9)) {
							var7 = false;
						}
					} else {
						var7 = true;
					}
				}

				break;

			case 32:
				var6 = var5;

			default:
				var4 += getCharWidthFloat(var8);

				if (var7) {
					++var4;
				}
			}

			if (var8 == 10) {
				++var5;
				var6 = var5;
				break;
			}

			if (var4 > par2) {
				break;
			}
		}

		return var5 != var3 && var6 != -1 && var6 < var5 ? var6 : var5;
	}

	/**
	 * Returns the width of the wordwrapped String (maximum length is parameter
	 * k)
	 */
	public int splitStringWidth(String par1Str, int par2) {
		return FONT_HEIGHT * listFormattedStringToWidth(par1Str, par2).size();
	}

	/**
	 * Remove all newline characters from the end of the string
	 */
	private String trimStringNewline(String par1Str) {
		while (par1Str != null && par1Str.endsWith("\n")) {
			par1Str = par1Str.substring(0, par1Str.length() - 1);
		}

		return par1Str;
	}

	/**
	 * Trims a string to fit a specified Width.
	 */
	public String trimStringToWidth(String par1Str, int par2) {
		return this.trimStringToWidth(par1Str, par2, false);
	}

	/**
	 * Trims a string to a specified width, and will reverse it if par3 is set.
	 */
	public String trimStringToWidth(String par1Str, int par2, boolean par3) {
		final StringBuilder var4 = new StringBuilder();
		float var5 = 0.0F;
		final int var6 = par3 ? par1Str.length() - 1 : 0;
		final int var7 = par3 ? -1 : 1;
		boolean var8 = false;
		boolean var9 = false;

		for (int var10 = var6; var10 >= 0 && var10 < par1Str.length()
				&& var5 < par2; var10 += var7) {
			final char var11 = par1Str.charAt(var10);
			final float var12 = getCharWidthFloat(var11);

			if (var8) {
				var8 = false;

				if (var11 != 108 && var11 != 76) {
					if (var11 == 114 || var11 == 82) {
						var9 = false;
					}
				} else {
					var9 = true;
				}
			} else if (var12 < 0.0F) {
				var8 = true;
			} else {
				var5 += var12;

				if (var9) {
					++var5;
				}
			}

			if (var5 > par2) {
				break;
			}

			if (par3) {
				var4.insert(0, var11);
			} else {
				var4.append(var11);
			}
		}

		return var4.toString();
	}

	/**
	 * Inserts newline and formatting into a string to wrap it within the
	 * specified width.
	 */
	String wrapFormattedStringToWidth(String par1Str, int par2) {
		final int var3 = sizeStringToWidth(par1Str, par2);

		if (par1Str.length() <= var3)
			return par1Str;
		else {
			final String var4 = par1Str.substring(0, var3);
			final char var5 = par1Str.charAt(var3);
			final boolean var6 = var5 == 32 || var5 == 10;
			final String var7 = getFormatFromString(var4)
					+ par1Str.substring(var3 + (var6 ? 1 : 0));
			return var4 + "\n" + wrapFormattedStringToWidth(var7, par2);
		}
	}
}
