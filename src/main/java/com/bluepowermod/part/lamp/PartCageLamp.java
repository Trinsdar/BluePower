/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.part.lamp;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.client.render.IconSupplier;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3dHelper;

import java.util.ArrayList;
import java.util.List;

;

/**
 *
 * @author Koen Beckers (K4Unl), Amadornes
 *
 */
public class PartCageLamp extends PartLamp {

    public PartCageLamp(MinecraftColor color, Boolean inverted) {

        super(color, inverted);
    }

    @Override
    protected String getLampType() {

        return "cagelamp";
    }

    /**
     * @author Koen Beckers (K4Unl), Amadornes
     */
    @Override
    public List<Vec3dCube> getSelectionBoxes() {

        List<Vec3dCube> boxes = new ArrayList<Vec3dCube>();

        boxes.add(new Vec3dCube(3 / 16D, 0.0, 3 / 16D, 13 / 16D, 2 / 16D, 1.0 - 3 / 16D).rotate(getFace(), Vec3dHelper.CENTER));
        boxes.add(new Vec3dCube(4 / 16D, 2 / 16D, 4 / 16D, 12 / 16D, 12 / 16D, 12 / 16D).rotate(getFace(), Vec3dHelper.CENTER));

        return boxes;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderLamp(RenderHelper renderer) {

        Vec3dCube vector = new Vec3dCube(3 / 16D, 0.0, 3 / 16D, 13 / 16D, 2 / 16D, 13 / 16D);
        TextureAtlasSprite topIcon = IconSupplier.cagedLampFootTop;
        TextureAtlasSprite sideIcon = IconSupplier.cagedLampFootSide;

        renderer.renderBox(vector, topIcon, topIcon, sideIcon, sideIcon, sideIcon, sideIcon);

        vector = new Vec3dCube(4 / 16D, 2 / 16D, 4 / 16D, 12 / 16D, 12 / 16D, 12 / 16D);
        topIcon = IconSupplier.cagedLampCageTop;
        sideIcon = IconSupplier.cagedLampCageSide;

        renderer.setRenderSide(EnumFacing.DOWN, false);

        for (int i = 0; i < 2; i++) {
            renderer.setRenderFromInside(i == 1);
            renderer.renderBox(vector, topIcon, topIcon, sideIcon, sideIcon, sideIcon, sideIcon);
        }
        renderer.setRenderFromInside(false);

        vector = new Vec3dCube(5 / 16D, 2 / 16D, 5 / 16D, 11 / 16D, 11 / 16D, 11 / 16D);
        if (inverted ? (power & 0xFF) == 255 : power == 0) {
            sideIcon = IconSupplier.cagedLampLampInactive;
            topIcon = IconSupplier.cagedLampLampInactiveTop;
        } else {
            sideIcon = IconSupplier.cagedLampLampActive;
            topIcon = IconSupplier.cagedLampLampActiveTop;
        }

        renderer.setColor(color.getHex());
        renderer.renderBox(vector, topIcon, topIcon, sideIcon, sideIcon, sideIcon, sideIcon);
        renderer.setColor(0xFFFFFF);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderGlow(int pass) {

        Vec3dCube vector = new Vec3dCube(5 / 16D, 2 / 16D, 5 / 16D, 11 / 16D, 11 / 16D, 11 / 16D).rotate(getFace(), Vec3dHelper.CENTER);

        double r = ((color.getHex() & 0xFF0000) >> 16) / 256D;
        double g = ((color.getHex() & 0x00FF00) >> 8) / 256D;
        double b = (color.getHex() & 0x0000FF) / 256D;

        if (pass == 1) {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glBegin(GL11.GL_QUADS);
            com.bluepowermod.client.render.RenderHelper.drawColoredCube(vector.clone().expand(0.5 / 16D), r, g, b,
                    ((inverted ? 255 - (power & 0xFF) : (power & 0xFF)) / 255D) * 0.625);
            GL11.glEnd();
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glDisable(GL11.GL_BLEND);
        }

    }

}
