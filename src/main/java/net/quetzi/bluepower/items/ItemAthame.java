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

package net.quetzi.bluepower.items;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.common.util.EnumHelper;
import net.quetzi.bluepower.references.Refs;

public class ItemAthame extends ItemSword {

    private float damageBonus;
    private static ToolMaterial athameMaterial = EnumHelper.addToolMaterial("SILVER", 0, 100, 6.0F, 2.0F, 10);

    public ItemAthame() {

        super(athameMaterial);
        this.setMaxDamage(100);
        this.setUnlocalizedName(Refs.ITEMATHAME_NAME);
        this.setTextureName(Refs.MODID + ":" + this.getUnlocalizedName().substring(5));
        this.maxStackSize = 1;
    }

    @Override
    public float func_150931_i() {

        return this.athameMaterial.getDamageVsEntity() + this.damageBonus;
    }

    @Override
    public boolean hitEntity(ItemStack itemStack, EntityLivingBase entityLivingBase, EntityLivingBase player) {

        if ((entityLivingBase instanceof EntityEnderman) || (entityLivingBase instanceof EntityDragon)) {
            this.damageBonus = 10.0F;
        } else {
            this.damageBonus = 0.0F;
        }
        itemStack.damageItem(1, player);
        return true;
    }

}
