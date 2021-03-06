/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.item;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.client.render.IBPColoredItem;
import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.reference.Refs;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/**
 * @author MineMaarten
 */
public class ItemColorableOverlay extends ItemBase implements IBPColoredItem {

    public ItemColorableOverlay(String name) {
        super();
        setTranslationKey(name);
        setCreativeTab(BPCreativeTabs.items);
        setRegistryName(Refs.MODID + ":" + name);
        setHasSubtypes(true);
        setMaxStackSize(1);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            for (int i = 0; i < 16; i++) {
                items.add(new ItemStack(this, 1, i));
            }
        }
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
    
        return super.getTranslationKey() + "." + (stack.getItemDamage() >= 16 ? "empty" : MinecraftColor.values()[stack.getItemDamage()].name().toLowerCase());
    }

    @Override
    public int getColor(ItemStack itemStack, int renderPass) {
        return renderPass == 0 || itemStack.getItemDamage() >= 16 ? -1 : ItemDye.DYE_COLORS[15 - itemStack.getItemDamage()];
    }
}
