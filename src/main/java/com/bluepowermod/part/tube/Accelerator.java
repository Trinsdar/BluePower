/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.part.tube;

import com.bluepowermod.client.render.IconSupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.IPartCustomPlacement;
import uk.co.qmunity.lib.part.IPartPlacement;
import uk.co.qmunity.lib.part.compat.MultipartCompatibility;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3dHelper;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

;

/**
 * Accelerator extends PneumaticTube, as that's much easier routing wise.
 *
 * @author MineMaarten
 *
 */
public class Accelerator extends PneumaticTube implements IPartCustomPlacement {

    private EnumFacing rotation = EnumFacing.UP;

    @Override
    public String getType() {

        return "accelerator";
    }

    @Override
    public String getUnlocalizedName() {

        return getType();
    }

    public void setRotation(EnumFacing rotation) {

        this.rotation = rotation;
    }

    /**
     * Gets all the occlusion boxes for this block
     *
     * @return A list with the occlusion boxes
     */
    @Override
    public List<Vec3dCube> getOcclusionBoxes() {

        List<Vec3dCube> aabbs = new ArrayList<Vec3dCube>();

        if (rotation == EnumFacing.DOWN || rotation == EnumFacing.UP) {
            aabbs.add(new Vec3dCube(0, 4 / 16D, 0, 1, 12 / 16D, 1));
        } else if (rotation == EnumFacing.NORTH || rotation == EnumFacing.SOUTH) {
            aabbs.add(new Vec3dCube(0, 0, 4 / 16D, 1, 1, 12 / 16D));
        } else {
            aabbs.add(new Vec3dCube(4 / 16D, 0, 0, 12 / 16D, 1, 1));
        }
        return aabbs;
    }

    @Override
    public void update() {

        super.update();
        TubeLogic logic = getLogic();
        for (TubeStack stack : logic.tubeStacks) {
            PneumaticTube tube = getPartCache(stack.heading);
            if (tube instanceof MagTube && isPowered()) {
                stack.setSpeed(1);
            } else {
                stack.setSpeed(TubeStack.ITEM_SPEED);
            }
        }

    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);
        tag.setByte("rotation", (byte) rotation.ordinal());
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        rotation = EnumFacing.getFront(tag.getByte("rotation"));
    }

    @Override
    public void writeUpdateData(DataOutput buffer) throws IOException {

        super.writeUpdateData(buffer);
        buffer.writeInt(rotation.ordinal());
    }

    @Override
    public void readUpdateData(DataInput buffer) throws IOException {

        super.readUpdateData(buffer);
        rotation = EnumFacing.getFront(buffer.readInt());
    }

    @Override
    public boolean isConnected(EnumFacing dir, PneumaticTube otherTube) {

        if (dir == rotation || dir.getOpposite() == rotation) {
            return getWorld() == null
                    || !MultipartCompatibility.checkOcclusion(getWorld(), getPos(), sideBB.clone().rotate(dir, Vec3dHelper.CENTER));
        } else {
            return false;
        }
    }

    private boolean isPowered() {

        return true;// TODO implement powah!
    }

    // @Override
    // @SideOnly(Side.CLIENT)
    // protected TextureAtlasSprite getSideIcon(EnumFacing side) {
    //
    // return getPartCache(side) instanceof MagTube ? IconSupplier.magTubeSide : IconSupplier.pneumaticTubeSide;
    // }

    @Override
    @SideOnly(Side.CLIENT)
    protected TextureAtlasSprite getSideIcon() {

        return IconSupplier.pneumaticTubeSide;
    }


    @Override
    @SideOnly(Side.CLIENT)
    public void renderDynamic(Vec3d loc, double delta, int pass) {

        super.renderDynamic(loc, delta, pass);
        if (pass == 0) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            Tessellator t = Tessellator.getInstance();

            GL11.glPushMatrix();
            GL11.glTranslatef((float) loc.xCoord + 0.5F, (float) loc.yCoord + 0.5F, (float) loc.zCoord + 0.5F);
            if (rotation == EnumFacing.NORTH || rotation == EnumFacing.SOUTH) {
                GL11.glRotated(90, 1, 0, 0);
            } else if (rotation == EnumFacing.EAST || rotation == EnumFacing.WEST) {
                GL11.glRotated(90, 0, 0, 1);
            }
            GL11.glTranslatef((float) -loc.xCoord - 0.5F, (float) -loc.yCoord - 0.5F, (float) -loc.zCoord - 0.5F);

            t.startDrawingQuads();

            t.setColorOpaque_F(1, 1, 1);
            t.addTranslation((float) loc.xCoord, (float) loc.yCoord, (float) loc.zCoord);

            TextureAtlasSprite icon = isPowered() ? IconSupplier.acceleratorFrontPowered : IconSupplier.acceleratorFront;

            double minX = icon.getInterpolatedU(0);
            double maxX = icon.getInterpolatedU(16);
            double minY = icon.getInterpolatedV(0);
            double maxY = icon.getInterpolatedV(16);

            t.setNormal(0, -1, 0);
            t.addVertexWithUV(0, 4 / 16D, 0, maxX, maxY);// minY
            t.addVertexWithUV(1, 4 / 16D, 0, minX, maxY);
            t.addVertexWithUV(1, 4 / 16D, 1, minX, minY);
            t.addVertexWithUV(0, 4 / 16D, 1, maxX, minY);

            t.setNormal(0, 1, 1);
            t.addVertexWithUV(0, 12 / 16D, 0, maxX, maxY);// maxY
            t.addVertexWithUV(0, 12 / 16D, 1, minX, maxY);
            t.addVertexWithUV(1, 12 / 16D, 1, minX, minY);
            t.addVertexWithUV(1, 12 / 16D, 0, maxX, minY);

            icon = isPowered() ? IconSupplier.acceleratorSidePowered : IconSupplier.acceleratorSide;

            minX = icon.getInterpolatedU(4);
            maxX = icon.getInterpolatedU(12);
            minY = icon.getInterpolatedV(0);
            maxY = icon.getInterpolatedV(16);

            t.setNormal(0, 0, 1);
            t.addVertexWithUV(0, 4 / 16D, 1, maxX, minY);// maxZ
            t.addVertexWithUV(1, 4 / 16D, 1, maxX, maxY);
            t.addVertexWithUV(1, 12 / 16D, 1, minX, maxY);
            t.addVertexWithUV(0, 12 / 16D, 1, minX, minY);

            t.setNormal(0, 0, -1);
            t.addVertexWithUV(0, 4 / 16D, 0, minX, maxY);// minZ
            t.addVertexWithUV(0, 12 / 16D, 0, maxX, maxY);
            t.addVertexWithUV(1, 12 / 16D, 0, maxX, minY);
            t.addVertexWithUV(1, 4 / 16D, 0, minX, minY);

            t.setNormal(-1, 0, 0);
            t.addVertexWithUV(0, 4 / 16D, 0, maxX, minY);// minX
            t.addVertexWithUV(0, 4 / 16D, 1, maxX, maxY);
            t.addVertexWithUV(0, 12 / 16D, 1, minX, maxY);
            t.addVertexWithUV(0, 12 / 16D, 0, minX, minY);

            t.setNormal(1, 0, 0);
            t.addVertexWithUV(1, 4 / 16D, 0, maxX, maxY);// maxX
            t.addVertexWithUV(1, 12 / 16D, 0, minX, maxY);
            t.addVertexWithUV(1, 12 / 16D, 1, minX, minY);
            t.addVertexWithUV(1, 4 / 16D, 1, maxX, minY);

            icon = IconSupplier.acceleratorInside;

            minX = icon.getInterpolatedU(4);
            maxX = icon.getInterpolatedU(12);
            minY = icon.getInterpolatedV(4);
            maxY = icon.getInterpolatedV(12);

            t.addVertexWithUV(0, 4 / 16D, 6 / 16D, minX, minY);// inside maxZ
            t.addVertexWithUV(1, 4 / 16D, 6 / 16D, maxX, maxY);
            t.addVertexWithUV(1, 12 / 16D, 6 / 16D, maxX, maxY);
            t.addVertexWithUV(0, 12 / 16D, 6 / 16D, minX, minY);

            t.addVertexWithUV(0, 4 / 16D, 10 / 16D, minX, maxY);// inside minZ
            t.addVertexWithUV(0, 12 / 16D, 10 / 16D, minX, minY);
            t.addVertexWithUV(1, 12 / 16D, 10 / 16D, maxX, minY);
            t.addVertexWithUV(1, 4 / 16D, 10 / 16D, maxX, maxY);

            t.addVertexWithUV(10 / 16D, 4 / 16D, 0, minX, minY);// inside minX
            t.addVertexWithUV(10 / 16D, 4 / 16D, 1, maxX, maxY);
            t.addVertexWithUV(10 / 16D, 12 / 16D, 1, maxX, maxY);
            t.addVertexWithUV(10 / 16D, 12 / 16D, 0, minX, minY);

            t.addVertexWithUV(6 / 16D, 4 / 16D, 0, minX, minY);// inside maxX
            t.addVertexWithUV(6 / 16D, 12 / 16D, 0, minX, maxY);
            t.addVertexWithUV(6 / 16D, 12 / 16D, 1, maxX, maxY);
            t.addVertexWithUV(6 / 16D, 4 / 16D, 1, maxX, minY);

            t.addTranslation((float) -loc.xCoord, (float) -loc.yCoord, (float) -loc.zCoord);
            t.draw();
            GL11.glPopMatrix();
        }

    }

    @Override
    public IPartPlacement getPlacement(IPart part, World world, BlockPos location, EnumFacing face, RayTraceResult mop,
                                       EntityPlayer player) {

        return new PartPlacementAccelerator(player);
    }

    @Override
    protected boolean shouldRenderFully() {

        return true;
    }

}
