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

package com.bluepowermod.item;

import com.bluepowermod.helper.BPItemTier;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.DamageSource;

import com.bluepowermod.init.BPItems;
import com.bluepowermod.reference.Refs;

public class ItemAthame extends SwordItem {
    
    private float               damageDealt;
    private static IItemTier athameMaterial = new BPItemTier(100, 6.0F, 2.0F, 0, 10, Ingredient.fromItems(BPItems.silver_ingot));
    
    public ItemAthame() {
        super(athameMaterial, 1, 1, new Properties());
        this.setRegistryName(Refs.MODID + ":" + Refs.ATHAME_NAME);
        BPItems.itemList.add(this);
    }

    public float getDamageDealt() {
        return this.damageDealt;
    }
    
    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity entity, LivingEntity player) {
    
        this.damageDealt = athameMaterial.getAttackDamage();
        if ((entity instanceof EndermanEntity) || (entity instanceof EnderDragonEntity)) {
            this.damageDealt += 18.0F;
        }
        entity.attackEntityFrom(DamageSource.causePlayerDamage((PlayerEntity) player), this.damageDealt);
        return super.hitEntity(stack, entity, player);
    }

    @Override
    public String getTranslationKey(ItemStack stack) {

        return String.format("item.%s:%s", Refs.MODID, getUnwrappedUnlocalizedName(super.getTranslationKey()));
    }

    protected String getUnwrappedUnlocalizedName(String name) {

        return name.substring(name.indexOf(".") + 1);
    }

    @Override
    public boolean isRepairable() {

        return canRepair && isDamageable();
    }

    @Override
    public boolean getIsRepairable(ItemStack is1, ItemStack is2) {

        return ((is1.getItem() == this || is2.getItem() == this) && (is1.getItem() == BPItems.silver_ingot || is2.getItem() == BPItems.silver_ingot));
    }
}
