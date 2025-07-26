package com.hbm.util;

import net.minecraft.client.renderer.Tessellator;

public class RenderUtil {

	public static void renderBlock(Tessellator tessellator) {
		renderBlock(tessellator, 0, 1);
	}

	public static void renderBlock(Tessellator tessellator, double uvMin, double uvMax) {
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(-0.5, +0.5, -0.5, uvMax, uvMax);
		tessellator.addVertexWithUV(+0.5, +0.5, -0.5, uvMin, uvMax);
		tessellator.addVertexWithUV(+0.5, -0.5, -0.5, uvMin, uvMin);
		tessellator.addVertexWithUV(-0.5, -0.5, -0.5, uvMax, uvMin);

		tessellator.addVertexWithUV(-0.5, +0.5, +0.5, uvMax, uvMax);
		tessellator.addVertexWithUV(-0.5, +0.5, -0.5, uvMin, uvMax);
		tessellator.addVertexWithUV(-0.5, -0.5, -0.5, uvMin, uvMin);;
		tessellator.addVertexWithUV(-0.5, -0.5, +0.5, uvMax, uvMin);

		tessellator.addVertexWithUV(+0.5, +0.5, +0.5, uvMax, uvMax);
		tessellator.addVertexWithUV(-0.5, +0.5, +0.5, uvMin, uvMax);
		tessellator.addVertexWithUV(-0.5, -0.5, +0.5, uvMin, uvMin);
		tessellator.addVertexWithUV(+0.5, -0.5, +0.5, uvMax, uvMin);

		tessellator.addVertexWithUV(+0.5, +0.5, -0.5, uvMax, uvMax);
		tessellator.addVertexWithUV(+0.5, +0.5, +0.5, uvMin, uvMax);
		tessellator.addVertexWithUV(+0.5, -0.5, +0.5, uvMin, uvMin);
		tessellator.addVertexWithUV(+0.5, -0.5, -0.5, uvMax, uvMin);

		tessellator.addVertexWithUV(-0.5, -0.5, -0.5, uvMax, uvMax);
		tessellator.addVertexWithUV(+0.5, -0.5, -0.5, uvMin, uvMax);
		tessellator.addVertexWithUV(+0.5, -0.5, +0.5, uvMin, uvMin);
		tessellator.addVertexWithUV(-0.5, -0.5, +0.5, uvMax, uvMin);

		tessellator.addVertexWithUV(+0.5, +0.5, -0.5, uvMax, uvMax);
		tessellator.addVertexWithUV(-0.5, +0.5, -0.5, uvMin, uvMax);
		tessellator.addVertexWithUV(-0.5, +0.5, +0.5, uvMin, uvMin);
		tessellator.addVertexWithUV(+0.5, +0.5, +0.5, uvMax, uvMin);
		tessellator.draw();
	}

}
