package net.minecraft.client.model;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Vec3;

public class TexturedQuad {
	private boolean invertNormal;
	public int nVertices;
	public PositionTextureVertex[] vertexPositions;

	public TexturedQuad(PositionTextureVertex[] p_i1152_1_) {
		vertexPositions = p_i1152_1_;
		nVertices = p_i1152_1_.length;
	}

	public TexturedQuad(PositionTextureVertex[] p_i1153_1_, int p_i1153_2_,
			int p_i1153_3_, int p_i1153_4_, int p_i1153_5_, float p_i1153_6_,
			float p_i1153_7_) {
		this(p_i1153_1_);
		final float var8 = 0.0F / p_i1153_6_;
		final float var9 = 0.0F / p_i1153_7_;
		p_i1153_1_[0] = p_i1153_1_[0].setTexturePosition(p_i1153_4_
				/ p_i1153_6_ - var8, p_i1153_3_ / p_i1153_7_ + var9);
		p_i1153_1_[1] = p_i1153_1_[1].setTexturePosition(p_i1153_2_
				/ p_i1153_6_ + var8, p_i1153_3_ / p_i1153_7_ + var9);
		p_i1153_1_[2] = p_i1153_1_[2].setTexturePosition(p_i1153_2_
				/ p_i1153_6_ + var8, p_i1153_5_ / p_i1153_7_ - var9);
		p_i1153_1_[3] = p_i1153_1_[3].setTexturePosition(p_i1153_4_
				/ p_i1153_6_ - var8, p_i1153_5_ / p_i1153_7_ - var9);
	}

	public void draw(Tessellator p_78236_1_, float p_78236_2_) {
		final Vec3 var3 = vertexPositions[1].vector3D
				.subtract(vertexPositions[0].vector3D);
		final Vec3 var4 = vertexPositions[1].vector3D
				.subtract(vertexPositions[2].vector3D);
		final Vec3 var5 = var4.crossProduct(var3).normalize();
		p_78236_1_.startDrawingQuads();

		if (invertNormal) {
			p_78236_1_.setNormal(-((float) var5.xCoord),
					-((float) var5.yCoord), -((float) var5.zCoord));
		} else {
			p_78236_1_.setNormal((float) var5.xCoord, (float) var5.yCoord,
					(float) var5.zCoord);
		}

		for (int var6 = 0; var6 < 4; ++var6) {
			final PositionTextureVertex var7 = vertexPositions[var6];
			p_78236_1_.addVertexWithUV((float) var7.vector3D.xCoord
					* p_78236_2_, (float) var7.vector3D.yCoord * p_78236_2_,
					(float) var7.vector3D.zCoord * p_78236_2_,
					var7.texturePositionX, var7.texturePositionY);
		}

		p_78236_1_.draw();
	}

	public void flipFace() {
		final PositionTextureVertex[] var1 = new PositionTextureVertex[vertexPositions.length];

		for (int var2 = 0; var2 < vertexPositions.length; ++var2) {
			var1[var2] = vertexPositions[vertexPositions.length - var2 - 1];
		}

		vertexPositions = var1;
	}
}
