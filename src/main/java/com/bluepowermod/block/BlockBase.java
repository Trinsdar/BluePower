/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.block;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.block.worldgen.BlockStoneOre;
import com.bluepowermod.init.BPBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.reference.Refs;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public abstract class BlockBase extends Block {

    private boolean wip = false;
    
    public BlockBase(Material material) {
    
        super(material);
        setSoundType(SoundType.STONE);
        setCreativeTab(BPCreativeTabs.machines);
        blockHardness = 3.0F;
        BPBlocks.blockList.add(this);
    }


    public BlockBase setWIP(boolean wip) {

        this.wip = wip;
        return this;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        if(wip){
            tooltip.add(MinecraftColor.RED.getChatColor() + "WIP");
        }
    }


    @Override
    public String getTranslationKey() {
    
        return String.format("tile.%s:%s", Refs.MODID, getUnwrappedUnlocalizedName(super.getTranslationKey()));
    }
    
    protected String getUnwrappedUnlocalizedName(String name) {
    
        return name.substring(name.indexOf(".") + 1);
    }
    
}
