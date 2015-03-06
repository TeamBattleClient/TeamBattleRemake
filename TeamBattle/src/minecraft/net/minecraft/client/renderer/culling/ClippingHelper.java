package net.minecraft.client.renderer.culling;

public class ClippingHelper {
	public float[] clippingMatrix = new float[16];
	public float[][] frustum = new float[16][16];
	public float[] modelviewMatrix = new float[16];
	public float[] projectionMatrix = new float[16];

	/**
	 * Returns true if the box is inside all 6 clipping planes, otherwise
	 * returns false.
	 */
	public boolean isBoxInFrustum(double minX, double minY, double minZ,
			double maxX, double maxY, double maxZ) {
		for (int i = 0; i < 6; ++i) {
			final float minXf = (float) minX;
			final float minYf = (float) minY;
			final float minZf = (float) minZ;
			final float maxXf = (float) maxX;
			final float maxYf = (float) maxY;
			final float maxZf = (float) maxZ;

			if (frustum[i][0] * minXf + frustum[i][1] * minYf + frustum[i][2]
					* minZf + frustum[i][3] <= 0.0F
					&& frustum[i][0] * maxXf + frustum[i][1] * minYf
							+ frustum[i][2] * minZf + frustum[i][3] <= 0.0F
					&& frustum[i][0] * minXf + frustum[i][1] * maxYf
							+ frustum[i][2] * minZf + frustum[i][3] <= 0.0F
					&& frustum[i][0] * maxXf + frustum[i][1] * maxYf
							+ frustum[i][2] * minZf + frustum[i][3] <= 0.0F
					&& frustum[i][0] * minXf + frustum[i][1] * minYf
							+ frustum[i][2] * maxZf + frustum[i][3] <= 0.0F
					&& frustum[i][0] * maxXf + frustum[i][1] * minYf
							+ frustum[i][2] * maxZf + frustum[i][3] <= 0.0F
					&& frustum[i][0] * minXf + frustum[i][1] * maxYf
							+ frustum[i][2] * maxZf + frustum[i][3] <= 0.0F
					&& frustum[i][0] * maxXf + frustum[i][1] * maxYf
							+ frustum[i][2] * maxZf + frustum[i][3] <= 0.0F)
				return false;
		}

		return true;
	}

	public boolean isBoxInFrustumFully(double minX, double minY, double minZ,
			double maxX, double maxY, double maxZ) {
		for (int i = 0; i < 6; ++i) {
			final float minXf = (float) minX;
			final float minYf = (float) minY;
			final float minZf = (float) minZ;
			final float maxXf = (float) maxX;
			final float maxYf = (float) maxY;
			final float maxZf = (float) maxZ;

			if (i < 4) {
				if (frustum[i][0] * minXf + frustum[i][1] * minYf
						+ frustum[i][2] * minZf + frustum[i][3] <= 0.0F
						|| frustum[i][0] * maxXf + frustum[i][1] * minYf
								+ frustum[i][2] * minZf + frustum[i][3] <= 0.0F
						|| frustum[i][0] * minXf + frustum[i][1] * maxYf
								+ frustum[i][2] * minZf + frustum[i][3] <= 0.0F
						|| frustum[i][0] * maxXf + frustum[i][1] * maxYf
								+ frustum[i][2] * minZf + frustum[i][3] <= 0.0F
						|| frustum[i][0] * minXf + frustum[i][1] * minYf
								+ frustum[i][2] * maxZf + frustum[i][3] <= 0.0F
						|| frustum[i][0] * maxXf + frustum[i][1] * minYf
								+ frustum[i][2] * maxZf + frustum[i][3] <= 0.0F
						|| frustum[i][0] * minXf + frustum[i][1] * maxYf
								+ frustum[i][2] * maxZf + frustum[i][3] <= 0.0F
						|| frustum[i][0] * maxXf + frustum[i][1] * maxYf
								+ frustum[i][2] * maxZf + frustum[i][3] <= 0.0F)
					return false;
			} else if (frustum[i][0] * minXf + frustum[i][1] * minYf
					+ frustum[i][2] * minZf + frustum[i][3] <= 0.0F
					&& frustum[i][0] * maxXf + frustum[i][1] * minYf
							+ frustum[i][2] * minZf + frustum[i][3] <= 0.0F
					&& frustum[i][0] * minXf + frustum[i][1] * maxYf
							+ frustum[i][2] * minZf + frustum[i][3] <= 0.0F
					&& frustum[i][0] * maxXf + frustum[i][1] * maxYf
							+ frustum[i][2] * minZf + frustum[i][3] <= 0.0F
					&& frustum[i][0] * minXf + frustum[i][1] * minYf
							+ frustum[i][2] * maxZf + frustum[i][3] <= 0.0F
					&& frustum[i][0] * maxXf + frustum[i][1] * minYf
							+ frustum[i][2] * maxZf + frustum[i][3] <= 0.0F
					&& frustum[i][0] * minXf + frustum[i][1] * maxYf
							+ frustum[i][2] * maxZf + frustum[i][3] <= 0.0F
					&& frustum[i][0] * maxXf + frustum[i][1] * maxYf
							+ frustum[i][2] * maxZf + frustum[i][3] <= 0.0F)
				return false;
		}

		return true;
	}
}
