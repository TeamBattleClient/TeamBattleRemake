package net.minecraft.src;

import java.util.Properties;

import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

public class CustomSkyLayer {
	public static final int BLEND_ADD = 0;
	public static final int BLEND_BURN = 4;
	public static final int BLEND_DODGE = 3;
	public static final int BLEND_MULTIPLY = 2;
	public static final int BLEND_REPLACE = 6;
	public static final int BLEND_SCREEN = 5;
	public static final int BLEND_SUBSTRACT = 1;
	public static final float[] DEFAULT_AXIS = new float[] { 1.0F, 0.0F, 0.0F };
	private float[] axis;
	private int blend = 0;
	private int endFadeIn = -1;
	private int endFadeOut = -1;
	private boolean rotate = false;
	public String source = null;
	private float speed = 1.0F;
	private int startFadeIn = -1;
	private int startFadeOut = -1;
	public int textureId;

	public CustomSkyLayer(Properties props, String defSource) {
		axis = DEFAULT_AXIS;
		textureId = -1;
		source = props.getProperty("source", defSource);
		startFadeIn = parseTime(props.getProperty("startFadeIn"));
		endFadeIn = parseTime(props.getProperty("endFadeIn"));
		startFadeOut = parseTime(props.getProperty("startFadeOut"));
		endFadeOut = parseTime(props.getProperty("endFadeOut"));
		blend = parseBlend(props.getProperty("blend"));
		rotate = parseBoolean(props.getProperty("rotate"), true);
		speed = parseFloat(props.getProperty("speed"), 1.0F);
		axis = parseAxis(props.getProperty("axis"), DEFAULT_AXIS);
	}

	private float getFadeBrightness(int timeOfDay) {
		int timeFadeOut;
		int timeDiff;

		if (timeBetween(timeOfDay, startFadeIn, endFadeIn)) {
			timeFadeOut = normalizeTime(endFadeIn - startFadeIn);
			timeDiff = normalizeTime(timeOfDay - startFadeIn);
			return (float) timeDiff / (float) timeFadeOut;
		} else if (timeBetween(timeOfDay, endFadeIn, startFadeOut))
			return 1.0F;
		else if (timeBetween(timeOfDay, startFadeOut, endFadeOut)) {
			timeFadeOut = normalizeTime(endFadeOut - startFadeOut);
			timeDiff = normalizeTime(timeOfDay - startFadeOut);
			return 1.0F - (float) timeDiff / (float) timeFadeOut;
		} else
			return 0.0F;
	}

	public boolean isActive(int timeOfDay) {
		return !timeBetween(timeOfDay, endFadeOut, startFadeIn);
	}

	public boolean isValid(String path) {
		if (source == null) {
			Config.warn("No source texture: " + path);
			return false;
		} else {
			source = TextureUtils.fixResourcePath(source,
					TextureUtils.getBasePath(path));

			if (startFadeIn >= 0 && endFadeIn >= 0 && endFadeOut >= 0) {
				final int timeFadeIn = normalizeTime(endFadeIn - startFadeIn);

				if (startFadeOut < 0) {
					startFadeOut = normalizeTime(endFadeOut - timeFadeIn);
				}

				final int timeOn = normalizeTime(startFadeOut - endFadeIn);
				final int timeFadeOut = normalizeTime(endFadeOut - startFadeOut);
				final int timeOff = normalizeTime(startFadeIn - endFadeOut);
				final int timeSum = timeFadeIn + timeOn + timeFadeOut + timeOff;

				if (timeSum != 24000) {
					Config.warn("Invalid fadeIn/fadeOut times, sum is more than 24h: "
							+ timeSum);
					return false;
				} else if (speed < 0.0F) {
					Config.warn("Invalid speed: " + speed);
					return false;
				} else
					return true;
			} else {
				Config.warn("Invalid times, required are: startFadeIn, endFadeIn and endFadeOut.");
				return false;
			}
		}
	}

	private int normalizeTime(int timeMc) {
		while (timeMc >= 24000) {
			timeMc -= 24000;
		}

		while (timeMc < 0) {
			timeMc += 24000;
		}

		return timeMc;
	}

	private float[] parseAxis(String str, float[] defVal) {
		if (str == null)
			return defVal;
		else {
			final String[] strs = Config.tokenize(str, " ");

			if (strs.length != 3) {
				Config.warn("Invalid axis: " + str);
				return defVal;
			} else {
				final float[] fs = new float[3];

				for (int ax = 0; ax < strs.length; ++ax) {
					fs[ax] = Config.parseFloat(strs[ax], Float.MIN_VALUE);

					if (fs[ax] == Float.MIN_VALUE) {
						Config.warn("Invalid axis: " + str);
						return defVal;
					}

					if (fs[ax] < -1.0F || fs[ax] > 1.0F) {
						Config.warn("Invalid axis values: " + str);
						return defVal;
					}
				}

				final float var9 = fs[0];
				final float ay = fs[1];
				final float az = fs[2];

				if (var9 * var9 + ay * ay + az * az < 1.0E-5F) {
					Config.warn("Invalid axis values: " + str);
					return defVal;
				} else {
					final float[] as = new float[] { az, ay, -var9 };
					return as;
				}
			}
		}
	}

	private int parseBlend(String str) {
		if (str == null)
			return 0;
		else if (str.equals("add"))
			return 0;
		else if (str.equals("subtract"))
			return 1;
		else if (str.equals("multiply"))
			return 2;
		else if (str.equals("dodge"))
			return 3;
		else if (str.equals("burn"))
			return 4;
		else if (str.equals("screen"))
			return 5;
		else if (str.equals("replace"))
			return 6;
		else {
			Config.warn("Unknown blend: " + str);
			return 0;
		}
	}

	private boolean parseBoolean(String str, boolean defVal) {
		if (str == null)
			return defVal;
		else if (str.toLowerCase().equals("true"))
			return true;
		else if (str.toLowerCase().equals("false"))
			return false;
		else {
			Config.warn("Unknown boolean: " + str);
			return defVal;
		}
	}

	private float parseFloat(String str, float defVal) {
		if (str == null)
			return defVal;
		else {
			final float val = Config.parseFloat(str, Float.MIN_VALUE);

			if (val == Float.MIN_VALUE) {
				Config.warn("Invalid value: " + str);
				return defVal;
			} else
				return val;
		}
	}

	private int parseTime(String str) {
		if (str == null)
			return -1;
		else {
			final String[] strs = Config.tokenize(str, ":");

			if (strs.length != 2) {
				Config.warn("Invalid time: " + str);
				return -1;
			} else {
				final String hourStr = strs[0];
				final String minStr = strs[1];
				int hour = Config.parseInt(hourStr, -1);
				final int min = Config.parseInt(minStr, -1);

				if (hour >= 0 && hour <= 23 && min >= 0 && min <= 59) {
					hour -= 6;

					if (hour < 0) {
						hour += 24;
					}

					final int time = hour * 1000
							+ (int) (min / 60.0D * 1000.0D);
					return time;
				} else {
					Config.warn("Invalid time: " + str);
					return -1;
				}
			}
		}
	}

	public void render(int timeOfDay, float celestialAngle, float rainBrightness) {
		float brightness = rainBrightness * getFadeBrightness(timeOfDay);
		brightness = Config.limit(brightness, 0.0F, 1.0F);

		if (brightness >= 1.0E-4F) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
			setupBlend(brightness);
			GL11.glPushMatrix();

			if (rotate) {
				GL11.glRotatef(celestialAngle * 360.0F * speed, axis[0],
						axis[1], axis[2]);
			}

			final Tessellator tess = Tessellator.instance;
			GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
			renderSide(tess, 4);
			GL11.glPushMatrix();
			GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
			renderSide(tess, 1);
			GL11.glPopMatrix();
			GL11.glPushMatrix();
			GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
			renderSide(tess, 0);
			GL11.glPopMatrix();
			GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
			renderSide(tess, 5);
			GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
			renderSide(tess, 2);
			GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
			renderSide(tess, 3);
			GL11.glPopMatrix();
		}
	}

	private void renderSide(Tessellator tess, int side) {
		final double tx = side % 3 / 3.0D;
		final double ty = side / 3 / 2.0D;
		tess.startDrawingQuads();
		tess.addVertexWithUV(-100.0D, -100.0D, -100.0D, tx, ty);
		tess.addVertexWithUV(-100.0D, -100.0D, 100.0D, tx, ty + 0.5D);
		tess.addVertexWithUV(100.0D, -100.0D, 100.0D, tx + 0.3333333333333333D,
				ty + 0.5D);
		tess.addVertexWithUV(100.0D, -100.0D, -100.0D,
				tx + 0.3333333333333333D, ty);
		tess.draw();
	}

	void setupBlend(float brightness) {
		switch (blend) {
		case 0:
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, brightness);
			break;

		case 1:
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ZERO);
			GL11.glColor4f(brightness, brightness, brightness, 1.0F);
			break;

		case 2:
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glColor4f(brightness, brightness, brightness, brightness);
			break;

		case 3:
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
			GL11.glColor4f(brightness, brightness, brightness, 1.0F);
			break;

		case 4:
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_ZERO, GL11.GL_ONE_MINUS_SRC_COLOR);
			GL11.glColor4f(brightness, brightness, brightness, 1.0F);
			break;

		case 5:
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_COLOR);
			GL11.glColor4f(brightness, brightness, brightness, 1.0F);
			break;

		case 6:
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, brightness);
		}

		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	private boolean timeBetween(int timeOfDay, int timeStart, int timeEnd) {
		return timeStart <= timeEnd ? timeOfDay >= timeStart
				&& timeOfDay <= timeEnd : timeOfDay >= timeStart
				|| timeOfDay <= timeEnd;
	}
}
