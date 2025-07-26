package com.hbm.render.util;

import java.util.List;

import org.lwjgl.opengl.GL11;

import com.hbm.dim.CelestialBody;
import com.hbm.dim.SolarSystem;
import com.hbm.dim.SolarSystem.OrreryMetric;
import com.hbm.util.RenderUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class OrreryPronter {

	private static long lastTick;
	private static float lastPartial;

	private static List<OrreryMetric> metrics;

	public static void render(Minecraft mc, World world, float partialTicks) {
		Tessellator tessellator = Tessellator.instance;
		CelestialBody sun = CelestialBody.getStar(world);

		// Update metrics
		if(metrics == null || lastTick != world.getTotalWorldTime() || lastPartial != partialTicks) {
			metrics = SolarSystem.calculatePositionsOrrery(world, partialTicks);
			lastTick = world.getTotalWorldTime();
			lastPartial = partialTicks;
		}

		// Setup glow
		GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);

		GL11.glPushMatrix();
		{

			// Draw sun
			GL11.glPushMatrix();
			{

				mc.renderEngine.bindTexture(sun.texture);
				tessellator.disableColor();
				RenderUtil.renderBlock(tessellator, 0.375, 0.625);

			}
			GL11.glPopMatrix();

			// Scale so sun is 1x1x1
			double scale = 1 / Math.min(sun.radiusKm, SolarSystem.ORRERY_MAX_RADIUS);
			GL11.glScaled(scale, scale, scale);


			// Draw bodies
			for(OrreryMetric metric : metrics) {
				mc.renderEngine.bindTexture(metric.body.texture);

				GL11.glPushMatrix();
				{

					double bodyScale = metric.body.radiusKm;
					if(bodyScale < 2_000) bodyScale = (bodyScale / 63) * (bodyScale / 63) + 1_000;

					GL11.glTranslated(metric.position.xCoord, metric.position.zCoord, metric.position.yCoord);
					GL11.glScaled(bodyScale, bodyScale, bodyScale);

					tessellator.disableColor();
					RenderUtil.renderBlock(tessellator);

				}
				GL11.glPopMatrix();

				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glColor3f(metric.body.color[0], metric.body.color[1], metric.body.color[2]);

				tessellator.startDrawing(3);

				for(int i = 1; i < metric.orbitalPath.length; i++) {
					Vec3 from = metric.orbitalPath[i-1];
					Vec3 to = metric.orbitalPath[i];

					tessellator.addVertex(from.xCoord, from.zCoord, from.yCoord);
					tessellator.addVertex(to.xCoord, to.zCoord, to.yCoord);
				}

				Vec3 first = metric.orbitalPath[0];
				Vec3 last = metric.orbitalPath[metric.orbitalPath.length - 1];

				tessellator.addVertex(last.xCoord, last.zCoord, last.yCoord);
				tessellator.addVertex(first.xCoord, first.zCoord, first.yCoord);

				tessellator.draw();

				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glColor3f(1, 1, 1);
			}

		}
		GL11.glPopMatrix();


		// Reset state
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopAttrib();
	}

}