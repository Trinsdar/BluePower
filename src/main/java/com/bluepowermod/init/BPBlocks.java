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

package com.bluepowermod.init;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.block.BlockContainerFacingBase;
import com.bluepowermod.block.gates.BlockGateBase;
import com.bluepowermod.block.machine.*;
import com.bluepowermod.block.machine.BlockLamp;
import com.bluepowermod.block.machine.BlockLampRGB;
import com.bluepowermod.block.power.BlockBattery;
import com.bluepowermod.block.power.BlockEngine;
import com.bluepowermod.block.power.BlockSolarPanel;
import com.bluepowermod.block.power.BlockWindmill;
import com.bluepowermod.block.worldgen.*;
import com.bluepowermod.reference.GuiIDs;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier1.*;
import com.bluepowermod.tile.tier2.*;
import com.bluepowermod.tile.tier3.TileCircuitDatabase;
import com.bluepowermod.tile.tier3.TileManager;
import com.bluepowermod.util.Dependencies;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = Refs.MODID)
public class BPBlocks {

    public static List<Block> blockList = new ArrayList<>();
    public static Block basalt;
    public static Block marble;
    public static Block basalt_cobble;
    public static Block basalt_brick;
    public static Block marble_brick;
    public static Block cracked_basalt_lava;

    public static Block basaltbrick_cracked;
    public static Block basalt_brick_small;
    public static Block marble_brick_small;
    public static Block fancy_basalt;
    public static Block fancy_marble;
    public static Block marble_tile;
    public static Block basalt_tile;
    public static Block marble_paver;
    public static Block basalt_paver;
    public static Block tiles;

    public static Block teslatite_ore;
    public static Block ruby_ore;
    public static Block sapphire_ore;
    public static Block amethyst_ore;
    public static Block copper_ore;
    public static Block silver_ore;
    public static Block zinc_ore;
    public static Block tungsten_ore;
    public static Block malachite_ore;

    public static Block ruby_block;
    public static Block sapphire_block;
    public static Block amethyst_block;
    public static Block teslatite_block;
    public static Block copper_block;
    public static Block silver_block;
    public static Block zinc_block;
    public static Block tungsten_block;
    public static Block malachite_block;

    public static Block rubber_log;
    public static Block rubber_leaves;
    public static Block rubber_sapling;

    public static Block sapphire_glass;
    public static Block reinforced_sapphire_glass;

    public static Block flax_crop;
    public static BlockBush indigo_flower;

    public static Block alloyfurnace;
    public static Block block_breaker;
    public static Block igniter;
    public static Block buffer;
    public static Block deployer;
    public static Block transposer;
    public static Block sorting_machine;
    public static Block project_table;
    public static Block auto_project_table;
    public static Block circuit_table;
    public static Block circuit_database;
    public static Block ejector;
    public static Block relay;
    public static Block filter;
    public static Block retriever;
    public static Block regulator;
    public static Block item_detector;
    public static Block manager;
    public static Block battery;
    public static Block engine;
    public static Block kinetic_generator;
    public static Block windmill;
    public static Block solarpanel;

    // public static Block cpu;
    // public static Block monitor;
    // public static Block disk_drive;
    // public static Block io_expander;

    public static Block[] blockLamp;
    public static Block[] blockLampInverted;
    public static Block[] cagedLamp;
    public static Block[] cagedLampInverted;
    public static Block[] fixedLamp;
    public static Block[] fixedLampInverted;

    public static Block blockLampRGB;
    public static Block blockLampRGBInverted;
    public static Block blockGateAND;
    public static Block blockGateNAND;

    public static Block[] blockAlloyWire;

    public static Block sortron;

    public static void init() {
        instantiateBlocks();
        initModDependantBlocks();
    }

    private static void instantiateBlocks() {

        basalt = new BlockBasalt(Refs.BASALT_NAME).setResistance(25.0F);
        marble = new BlockStoneOre(Refs.MARBLE_NAME).setResistance(1.0F).setHardness(1.5F);
        basalt_cobble = new BlockStoneOre(Refs.BASALTCOBBLE_NAME);
        basalt_brick = new BlockStoneOre(Refs.BASALTBRICK_NAME);
        marble_brick = new BlockStoneOre(Refs.MARBLEBRICK_NAME);
        cracked_basalt_lava = new BlockCrackedBasalt(Refs.CRACKED_BASALT);

        basaltbrick_cracked = new BlockStoneOre(Refs.CRACKEDBASALTBRICK_NAME);
        basalt_brick_small = new BlockStoneOre(Refs.SMALLBASALTBRICK_NAME);
        marble_brick_small = new BlockStoneOre(Refs.SMALLMARBLEBRICK_NAME);
        fancy_basalt = new BlockStoneOre(Refs.CHISELEDBASALTBRICK_NAME);
        fancy_marble = new BlockStoneOre(Refs.CHISELEDMARBLEBRICK_NAME);
        marble_tile = new BlockStoneOreConnected(Refs.MARBLETILE_NAME);
        basalt_tile = new BlockStoneOreConnected(Refs.BASALTTILE_NAME);
        marble_paver = new BlockStoneOre(Refs.MARBLEPAVER_NAME);
        basalt_paver = new BlockStoneOre(Refs.BASALTPAVER_NAME);
        tiles = new BlockStoneOre(Refs.TILES);

        teslatite_ore = new BlockTeslatiteOre(Refs.TESLATITEORE_NAME);
        ruby_ore = new BlockRubyOre(Refs.RUBYORE_NAME);
        sapphire_ore = new BlockSapphireOre(Refs.SAPPHIREORE_NAME);
        amethyst_ore = new BlockAmethystOre(Refs.AMETHYSTORE_NAME);
        malachite_ore = new BlockMalachiteOre(Refs.MALACHITEORE_NAME);

        copper_ore = new BlockStoneOre(Refs.COPPERORE_NAME);
        silver_ore = new BlockStoneOre(Refs.SILVERORE_NAME).setToolLevel(2);
        zinc_ore = new BlockStoneOre(Refs.ZINCORE_NAME);
        tungsten_ore = new BlockStoneOre(Refs.TUNGSTENORE_NAME).setToolLevel(3).setResistance(6.0F).setHardness(15.0F);

        ruby_block = new BlockStoneOre(Refs.RUBYBLOCK_NAME).setToolLevel(2);
        sapphire_block = new BlockStoneOre(Refs.SAPPHIREBLOCK_NAME).setToolLevel(2);
        amethyst_block = new BlockStoneOre(Refs.AMETHYSTBLOCK_NAME).setToolLevel(2);
        teslatite_block = new BlockStoneOre(Refs.TESLATITEBLOCK_NAME).setToolLevel(2);
        malachite_block = new BlockStoneOre(Refs.MALACHITEBLOCK_NAME).setToolLevel(2);
        copper_block = new BlockStoneOre(Refs.COPPERBLOCK_NAME);
        silver_block = new BlockStoneOre(Refs.SILVERBLOCK_NAME).setToolLevel(2);
        zinc_block = new BlockStoneOre(Refs.ZINCBLOCK_NAME);
        tungsten_block = new BlockStoneOre(Refs.TUNGSTENBLOCK_NAME).setToolLevel(3).setResistance(25.0F).setHardness(5.0F);

        rubber_leaves = new BlockRubberLeaves();
        rubber_log = new BlockRubberLog();
        rubber_sapling = new BlockRubberSapling();

        sapphire_glass = new BlockStoneOreConnected(Refs.SAPPHIREGLASS_NAME).setTransparent(true).setHardness(10).setResistance(10000);
        reinforced_sapphire_glass = new BlockStoneOreConnected(Refs.REINFORCEDSAPPHIREGLASS_NAME).setTransparent(true).setWitherproof(true).setHardness(30).setResistance(Integer.MAX_VALUE);

        flax_crop = new BlockCrop();
        indigo_flower = new BlockCustomFlower(Refs.INDIGOFLOWER_NAME);

        alloyfurnace = new BlockAlloyFurnace();
        block_breaker = new BlockContainerFacingBase(Material.ROCK, TileBlockBreaker.class).setRegistryName(Refs.MODID, Refs.BLOCKBREAKER_NAME).setTranslationKey(Refs.BLOCKBREAKER_NAME);
        igniter = new BlockIgniter();
        buffer = new BlockContainerFacingBase(Material.ROCK, TileBuffer.class).setGuiId(GuiIDs.BUFFER).setRegistryName(Refs.MODID, Refs.BLOCKBUFFER_NAME).setTranslationKey(Refs.BLOCKBUFFER_NAME);
        deployer = new BlockContainerFacingBase(Material.ROCK, TileDeployer.class).setGuiId(GuiIDs.DEPLOYER_ID)
                .setRegistryName(Refs.MODID, Refs.BLOCKDEPLOYER_NAME).setTranslationKey(Refs.BLOCKDEPLOYER_NAME);
        transposer = new BlockContainerFacingBase(Material.ROCK, TileTransposer.class).setRegistryName(Refs.MODID, Refs.TRANSPOSER_NAME).setTranslationKey(Refs.TRANSPOSER_NAME);
        sorting_machine = new BlockContainerFacingBase(Material.ROCK, TileSortingMachine.class).setGuiId(GuiIDs.SORTING_MACHINE).setWIP(true)
                .setRegistryName(Refs.MODID, Refs.SORTING_MACHINE_NAME).setTranslationKey(Refs.SORTING_MACHINE_NAME);
        project_table = new BlockProjectTable().setGuiId(GuiIDs.PROJECTTABLE_ID);
        auto_project_table = new BlockProjectTable(TileAutoProjectTable.class).setGuiId(GuiIDs.PROJECTTABLE_ID).setRegistryName(Refs.MODID, Refs.AUTOPROJECTTABLE_NAME).setTranslationKey(Refs.AUTOPROJECTTABLE_NAME);
        circuit_table = new BlockProjectTable(TileCircuitTable.class).setGuiId(GuiIDs.CIRCUITTABLE_ID).setWIP(true).setRegistryName(Refs.MODID, Refs.CIRCUITTABLE_NAME).setTranslationKey(Refs.CIRCUITTABLE_NAME);
        circuit_database = new BlockCircuitDatabase(TileCircuitDatabase.class).setGuiId(GuiIDs.CIRCUITDATABASE_MAIN_ID).setWIP(true)
                .setRegistryName(Refs.MODID, Refs.CIRCUITDATABASE_NAME).setTranslationKey(Refs.CIRCUITDATABASE_NAME);
        ejector = new BlockContainerFacingBase(Material.ROCK, TileEjector.class).setGuiId(GuiIDs.EJECTOR_ID).setRegistryName(Refs.MODID, Refs.EJECTOR_NAME).setTranslationKey(Refs.EJECTOR_NAME);
        relay = new BlockContainerFacingBase(Material.ROCK, TileRelay.class).setGuiId(GuiIDs.RELAY_ID).setWIP(true).setRegistryName(Refs.MODID, Refs.RELAY_NAME).setTranslationKey(Refs.RELAY_NAME);
        filter = new BlockContainerFacingBase(Material.ROCK, TileFilter.class).setGuiId(GuiIDs.FILTER_ID).setWIP(true).setRegistryName(Refs.MODID, Refs.FILTER_NAME).setTranslationKey(Refs.FILTER_NAME);
        retriever = new BlockContainerFacingBase(Material.ROCK, TileRetriever.class).setGuiId(GuiIDs.RETRIEVER_ID).setWIP(true).setRegistryName(Refs.MODID, Refs.RETRIEVER_NAME).setTranslationKey(Refs.RETRIEVER_NAME);
        regulator = new BlockContainerFacingBase(Material.ROCK, TileRegulator.class).setGuiId(GuiIDs.REGULATOR_ID).emitsRedstone().setWIP(true)
                .setRegistryName(Refs.MODID, Refs.REGULATOR_NAME).setTranslationKey(Refs.REGULATOR_NAME);
        item_detector = new BlockContainerFacingBase(Material.ROCK, TileItemDetector.class).setGuiId(GuiIDs.ITEMDETECTOR_ID).emitsRedstone().setWIP(true)
                .setRegistryName(Refs.MODID, Refs.ITEMDETECTOR_NAME).setTranslationKey(Refs.ITEMDETECTOR_NAME);
        manager = new BlockRejecting(Material.ROCK, TileManager.class).setGuiId(GuiIDs.MANAGER_ID).emitsRedstone().setWIP(true).setRegistryName(Refs.MODID, Refs.MANAGER_NAME).setTranslationKey(Refs.MANAGER_NAME);

        battery = new BlockBattery().setWIP(true);
        engine = new BlockEngine().setWIP(true);
        kinetic_generator = new BlockKineticGenerator().setWIP(true);
        //windmill = new BlockWindmill();
        solarpanel = new BlockSolarPanel().setWIP(true);

        // cpu = new BlockCPU();
        // monitor = new BlockMonitor();
        // disk_drive = new BlockDiskDrive();
        // io_expander = new BlockIOExpander();

        blockLamp = new Block[MinecraftColor.VALID_COLORS.length];
        blockLampInverted = new Block[MinecraftColor.VALID_COLORS.length];
        cagedLamp = new Block[MinecraftColor.VALID_COLORS.length];
        cagedLampInverted = new Block[MinecraftColor.VALID_COLORS.length];
        fixedLamp = new Block[MinecraftColor.VALID_COLORS.length];
        fixedLampInverted = new Block[MinecraftColor.VALID_COLORS.length];

        //Regular Lamp
        blockLampRGB = new BlockLampRGB(Refs.LAMP_NAME,false).setWIP(true);
        for (int i = 0; i < MinecraftColor.VALID_COLORS.length; i++)
            blockLamp[i] = new BlockLamp(Refs.LAMP_NAME, false, MinecraftColor.VALID_COLORS[i]);
        blockLampRGBInverted = new BlockLampRGB(Refs.LAMP_NAME,true).setWIP(true);
        for (int i = 0; i < MinecraftColor.VALID_COLORS.length; i++)
            blockLampInverted[i] = new BlockLamp(Refs.LAMP_NAME, true, MinecraftColor.VALID_COLORS[i]);

        //Cage Lamp
        blockLampRGB = new BlockLampRGBSurface(Refs.CAGELAMP_NAME,false, Refs.CAGELAMP_AABB).setWIP(true);
        for (int i = 0; i < MinecraftColor.VALID_COLORS.length; i++)
            cagedLamp[i] = new BlockLampSurface(Refs.CAGELAMP_NAME, false, MinecraftColor.VALID_COLORS[i], Refs.CAGELAMP_AABB);
        blockLampRGBInverted = new BlockLampRGBSurface(Refs.CAGELAMP_NAME,true, Refs.CAGELAMP_AABB).setWIP(true);
        for (int i = 0; i < MinecraftColor.VALID_COLORS.length; i++)
            cagedLampInverted[i] = new BlockLampSurface(Refs.CAGELAMP_NAME, true, MinecraftColor.VALID_COLORS[i], Refs.CAGELAMP_AABB);

        //Fixture Lamp
        blockLampRGB = new BlockLampRGBSurface(Refs.FIXTURELAMP_NAME,false, Refs.FIXTURELAMP_AABB).setWIP(true);
        for (int i = 0; i < MinecraftColor.VALID_COLORS.length; i++)
            fixedLamp[i] = new BlockLampSurface(Refs.FIXTURELAMP_NAME, false, MinecraftColor.VALID_COLORS[i], Refs.FIXTURELAMP_AABB);
        blockLampRGBInverted = new BlockLampRGBSurface(Refs.FIXTURELAMP_NAME,true, Refs.FIXTURELAMP_AABB).setWIP(true);
        for (int i = 0; i < MinecraftColor.VALID_COLORS.length; i++)
            fixedLampInverted[i] = new BlockLampSurface(Refs.FIXTURELAMP_NAME, true, MinecraftColor.VALID_COLORS[i], Refs.FIXTURELAMP_AABB);

        //Gates
        blockGateAND = new BlockGateBase("gate_and").setRegistryName("bluepower:gate_and");
        blockGateNAND = new BlockGateBase("gate_nand"){
            @Override
            public byte computeRedstone(byte back, byte left, byte right){
                return (byte)((left == 0 || right == 0 || back == 0 ) ?  16 : 0);
            }
        }.setRegistryName("bluepower:gate_nand");

        //Wires
        blockAlloyWire = new Block[MinecraftColor.VALID_COLORS.length * 2 + 2];

        blockAlloyWire[0] = new BlockAlloyWire(RedwireType.BLUESTONE.getName()).setWIP(true);
        blockAlloyWire[MinecraftColor.VALID_COLORS.length + 1] =  new BlockAlloyWire(RedwireType.RED_ALLOY.getName()).setWIP(true);

        for (int i = 0; i < MinecraftColor.VALID_COLORS.length; i++) {
            blockAlloyWire[i+1] = new BlockInsulatedAlloyWire(RedwireType.BLUESTONE.getName(), MinecraftColor.VALID_COLORS[i]).setWIP(true);
            blockAlloyWire[i + MinecraftColor.VALID_COLORS.length +2] = new BlockInsulatedAlloyWire(RedwireType.RED_ALLOY.getName(), MinecraftColor.VALID_COLORS[i]).setWIP(true);
        }
    }

    private static void initModDependantBlocks() {

        if (Loader.isModLoaded(Dependencies.COMPUTER_CRAFT) || Loader.isModLoaded(Dependencies.OPEN_COMPUTERS)) {
            sortron = new BlockSortron();
        }
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        for(Block block : blockList) {
            event.getRegistry().register(block);

        }
    }
    @SubscribeEvent
    public static void registerBlockItems(RegistryEvent.Register<Item> event) {
        for (Block block : blockList) {
            if (block.getRegistryName() != null && !(block instanceof BlockCrop)) { // Crops have seeds rather than blocks
                event.getRegistry().register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
            }
        }
    }

}
