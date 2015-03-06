package net.minecraft.src;

import java.nio.ByteBuffer;
import java.util.Properties;

import net.minecraft.client.renderer.GLAllocation;

import org.lwjgl.opengl.GL11;

public class TextureAnimation {
	private int activeFrame = 0;
	private String dstTex = null;
	private int dstTextId = -1;
	private int dstX = 0;
	private int dstY = 0;
	private int frameHeight = 0;
	private CustomAnimationFrame[] frames = null;
	private int frameWidth = 0;
	private ByteBuffer imageData = null;
	private String srcTex = null;

	public TextureAnimation(String texFrom, byte[] srcData, String texTo,
			int dstTexId, int dstX, int dstY, int frameWidth, int frameHeight,
			Properties props, int durDef) {
		srcTex = texFrom;
		dstTex = texTo;
		dstTextId = dstTexId;
		this.dstX = dstX;
		this.dstY = dstY;
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		final int frameLen = frameWidth * frameHeight * 4;

		if (srcData.length % frameLen != 0) {
			Config.warn("Invalid animated texture length: " + srcData.length
					+ ", frameWidth: " + frameHeight + ", frameHeight: "
					+ frameHeight);
		}

		imageData = GLAllocation.createDirectByteBuffer(srcData.length);
		imageData.put(srcData);
		int numFrames = srcData.length / frameLen;

		if (props.get("tile.0") != null) {
			for (int durationDefStr = 0; props.get("tile." + durationDefStr) != null; ++durationDefStr) {
				numFrames = durationDefStr + 1;
			}
		}

		final String var21 = (String) props.get("duration");
		final int durationDef = Config.parseInt(var21, durDef);
		frames = new CustomAnimationFrame[numFrames];

		for (int i = 0; i < frames.length; ++i) {
			final String indexStr = (String) props.get("tile." + i);
			final int index = Config.parseInt(indexStr, i);
			final String durationStr = (String) props.get("duration." + i);
			final int duration = Config.parseInt(durationStr, durationDef);
			final CustomAnimationFrame frm = new CustomAnimationFrame(index,
					duration);
			frames[i] = frm;
		}
	}

	public int getActiveFrameIndex() {
		if (frames.length <= 0)
			return 0;
		else {
			if (activeFrame >= frames.length) {
				activeFrame = 0;
			}

			final CustomAnimationFrame frame = frames[activeFrame];
			return frame.index;
		}
	}

	public String getDstTex() {
		return dstTex;
	}

	public int getDstTextId() {
		return dstTextId;
	}

	public int getFrameCount() {
		return frames.length;
	}

	public String getSrcTex() {
		return srcTex;
	}

	public boolean nextFrame() {
		if (frames.length <= 0)
			return false;
		else {
			if (activeFrame >= frames.length) {
				activeFrame = 0;
			}

			final CustomAnimationFrame frame = frames[activeFrame];
			++frame.counter;

			if (frame.counter < frame.duration)
				return false;
			else {
				frame.counter = 0;
				++activeFrame;

				if (activeFrame >= frames.length) {
					activeFrame = 0;
				}

				return true;
			}
		}
	}

	public boolean updateTexture() {
		if (!nextFrame())
			return false;
		else {
			final int frameLen = frameWidth * frameHeight * 4;
			final int imgNum = getActiveFrameIndex();
			final int offset = frameLen * imgNum;

			if (offset + frameLen > imageData.capacity())
				return false;
			else {
				imageData.position(offset);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, dstTextId);
				GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, dstX, dstY,
						frameWidth, frameHeight, GL11.GL_RGBA,
						GL11.GL_UNSIGNED_BYTE, imageData);
				return true;
			}
		}
	}
}
