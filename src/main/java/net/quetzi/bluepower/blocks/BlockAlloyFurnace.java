package net.quetzi.bluepower.blocks;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.quetzi.bluepower.machines.TileEntityAlloyFurnace;

public class BlockAlloyFurnace extends BlockContainer {
    public static boolean isActivated = false;
    public BlockAlloyFurnace() {
        super(Material.rock);
    }
    
    private static IIcon iconSide;
    private static IIcon iconTop;
    private static IIcon iconFront;
    
    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new TileEntityAlloyFurnace();
    }
    
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        return side == 1 ? this.iconFront : (side == 0 ? this.iconFront : (side != meta ? this.iconSide : this.iconTop));
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.iconSide = iconRegister.registerIcon("alloyfurnace_side");
        this.iconFront = iconRegister.registerIcon(this.isActivated ? "alloyfurnace_front_on" : "alloyfurnace_front_off");
        this.iconTop = iconRegister.registerIcon("alloyfurnace_top");
    }
}
