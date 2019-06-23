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
 *     
 *     @author Lumien
 */

package com.bluepowermod.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IPlantable;

public class SlotSeedBag extends Slot {
    
    public SlotSeedBag(IInventory par1iInventory, int par2, int par3, int par4) {
    
        super(par1iInventory, par2, par3, par4);
    }
    
    @Override
    public boolean isItemValid(ItemStack itemstack) {
    
        itemstack = itemstack.copy();
        itemstack.setCount(1);
        if (itemstack.getItem() instanceof IPlantable) {
            ItemStack seedType = ItemStack.EMPTY;
            
            for (int i = 0; i < this.inventory.getSizeInventory(); i++) {
                ItemStack is = this.inventory.getStackInSlot(i);
                if (!is.isEmpty()) {
                    seedType = is.copy();
                    seedType.setCount(1);
                    break;
                }
            }
            
            if (seedType.isEmpty()) {
                return true;
            } else {
                return ItemStack.areItemStacksEqual(itemstack, seedType);
            }
        }
        
        return false;
    }
}
