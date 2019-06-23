/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.enchant;

import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;

public class EnchantmentVorpal extends Enchantment {

    public EnchantmentVorpal(int enchantId, Rarity rarity) {
        super(rarity, EnchantmentType.WEAPON, EquipmentSlotType.values());
    }

    @Override
    public int getMaxLevel() {

        return 2;
    }

    @Override
    public int getMinEnchantability(int par1) {

        return 10 + 20 * (par1 - 1);
    }

}
