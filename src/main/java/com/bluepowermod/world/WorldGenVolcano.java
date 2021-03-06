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

package com.bluepowermod.world;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.init.Config;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * 
 * @author MineMaarten
 */
public class WorldGenVolcano {

    private static final int MAX_VOLCANO_RADIUS = 200; // absolute max radius a volcano can have, this should be a
    // magnitude bigger than an average volcano radius.
    private final HashMap<BlockPos, Integer> volcanoMap = new HashMap<BlockPos, Integer>();
    private static final Block[] ALTAR_BLOCKS = new Block[] { BPBlocks.amethyst_block, BPBlocks.ruby_block, BPBlocks.sapphire_block,
            BPBlocks.tungsten_block };

    public void generate(World world, Random rand, int middleX, int volcanoHeight, int middleZ) {
        List<Pos>[] distMap = calculateDistMap();
        boolean first = true;
        int centreWorldHeight = world.getHeight(middleX, middleZ);
        for (int dist = 0; dist < distMap.length; dist++) {// Loop through every XZ position of the volcano, in order of how close the positions are
            // from the center. The volcano will be generated from the center to the edge.
            List<Pos> distList = distMap[dist];
            boolean isFinished = true;// Will stay true as long as there were still blocks being generated at this distance from the volcano.
            for (Pos p : distList) {
                int worldHeight = world.getHeight(p.x + middleX, p.z + middleZ) - 1;
                int posHeight = first ? volcanoHeight : getNewVolcanoHeight(worldHeight, p, rand, dist);
                if (posHeight >= 0 && (posHeight > worldHeight || canReplace(world, p.x + middleX, posHeight, p.z + middleZ))) {// If the calculated
                    // desired volcano
                    // height is higher
                    // than the world
                    // height, generate.
                    volcanoMap.put(new BlockPos(p.x, 0, p.z), posHeight);
                    if (!first) {
                        for (int i = posHeight; i > 0 && (i > worldHeight || canReplace(world, p.x + middleX, i, p.z + middleZ)); i--) {
                            world.setBlockState(new BlockPos(p.x + middleX, i, p.z + middleZ), BPBlocks.basalt.getDefaultState(), 2);
                        }
                        for (int i = posHeight + 1; i < volcanoHeight; i++) {
                            if (canReplace(world, p.x + middleX, i, p.z + middleZ)
                                    && world.getBlockState(new BlockPos(p.x + middleX, i, p.z + middleZ)).getMaterial() != Material.WATER)
                                world.setBlockToAir(new BlockPos(p.x + middleX, i, p.z + middleZ));
                        }
                    }
                    isFinished = false;
                }
                first = false;
            }
            if (isFinished)
                break;
        }
        generateLavaColumn(world, middleX, volcanoHeight, middleZ, rand);
        generateLootChamber(world, middleX, rand.nextInt(volcanoHeight - 20 - centreWorldHeight) + centreWorldHeight, middleZ, rand);
    }

    private boolean canReplace(World world, int x, int y, int z) {

        if (world.isAirBlock(new BlockPos(x, y, z)))
            return true;
        Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();
        Material material = world.getBlockState(new BlockPos(x, y, z)).getMaterial();
        return material == Material.WOOD || material == Material.CACTUS || material == Material.LEAVES || material == Material.PLANTS
                || material == Material.VINE || block == Blocks.WATER|| block == Blocks.FLOWING_WATER;
    }

    private void generateLavaColumn(World world, int x, int topY, int z, Random rand) {

        // world.setBlock(x, topY, z, Blocks.lava);
        if (rand.nextDouble() < Config.volcanoActiveToInactiveRatio) {
            world.setBlockState(new BlockPos(x, topY, z), BPBlocks.cracked_basalt_lava.getDefaultState());
        } else {
            world.setBlockState(new BlockPos(x, topY + 1, z), Blocks.LAVA.getDefaultState());
            world.setBlockState(new BlockPos(x, topY, z), Blocks.LAVA.getDefaultState());// This block set, which does update neighbors, will make the lava above update.
        }
        for (int y = topY - 1; y >= 10; y--) {
            if (world.getBlockState(new BlockPos(x, y, z)) != Blocks.BEDROCK.getDefaultState()) {
                world.setBlockState(new BlockPos(x + 1, y, z), BPBlocks.basalt.getDefaultState(), 2);
                world.setBlockState(new BlockPos(x - 1, y, z), BPBlocks.basalt.getDefaultState(), 2);
                world.setBlockState(new BlockPos(x, y, z + 1), BPBlocks.basalt.getDefaultState(), 2);
                world.setBlockState(new BlockPos(x, y, z - 1), BPBlocks.basalt.getDefaultState(), 2);
                world.setBlockState(new BlockPos(x, y, z), Blocks.LAVA.getDefaultState(), 2);
            }
        }
    }

    /**
     * Saves an array of relative Positions with distance to origin. The index is the distance, the element the positions with that distance to the
     * origin.
     */
    @SuppressWarnings("unchecked")
    private List<Pos>[] calculateDistMap() {

        List<Pos>[] distMap = new List[MAX_VOLCANO_RADIUS];
        for (int x = -MAX_VOLCANO_RADIUS; x <= MAX_VOLCANO_RADIUS; x++) {
            for (int z = -MAX_VOLCANO_RADIUS; z <= MAX_VOLCANO_RADIUS; z++) {
                int dist = (int) Math.sqrt(x * x + z * z);
                if (dist < MAX_VOLCANO_RADIUS) {
                    List<Pos> distList = distMap[dist];
                    if (distList == null) {
                        distList = new ArrayList<Pos>();
                        distMap[dist] = distList;
                    }
                    distList.add(new Pos(x, z));
                }
            }
        }
        return distMap;
    }

    /**
     * Calculates a height for the requested position. It looks at the adjacent (already generated) volcano heights, takes the average, and blends in
     * a bit of randomness. If there are no neighbors this is the first volcano block generated, meaning it's the center, meaning it should get the
     * max height.
     *
     * @param worldHeight Terrain height
     * @param requestedPos New volcano position
     * @param rand
     * @param distFromCenter
     * @return
     */
    private int getNewVolcanoHeight(int worldHeight, Pos requestedPos, Random rand, int distFromCenter) {

        int neighborCount = 0;
        int totalHeight = 0;
        for (int x = requestedPos.x - 1; x <= requestedPos.x + 1; x++) {
            for (int z = requestedPos.z - 1; z <= requestedPos.z + 1; z++) {
                Integer neighborHeight = volcanoMap.get(new BlockPos(x, 0, z));
                if (neighborHeight != null) {
                    neighborCount++;
                    totalHeight += neighborHeight;
                }
            }
        }
        if (neighborCount != 0) {
            double avgHeight = (double) totalHeight / neighborCount;
            if ((int) avgHeight < worldHeight + 2 && rand.nextInt(5) != 0)
                return (int) avgHeight - 2;
            // Formula that defines how fast the volcano descends. Using a square function to make it steeper at the top, and added randomness.
            int blocksDown;
            if (distFromCenter < 2) {
                blocksDown = 0;
            } else if (distFromCenter == 2) {
                blocksDown = rand.nextInt(2);
            } else {
                blocksDown = (int) (Math.pow(avgHeight - worldHeight + 1, 1.2) * 0.005D + (rand.nextDouble() - 0.5) * 3 + 0.4D);
            }
            if (blocksDown < 0)
                blocksDown = 0;
            int newHeight = (int) avgHeight - blocksDown;
            return newHeight;
        } else {
            return -1;
        }
    }

    private void generateLootChamber(World world, int middleX, int startY, int middleZ, Random rand) {
        int roomSize = 9;
        int roomHeight = 5;
        int startX = middleX - roomSize / 2;
        int startZ = middleZ - roomSize / 2;

        for (int x = startX; x < startX + roomSize; x++) {
            for (int y = startY; y < startY + roomHeight; y++) {
                for (int z = startZ; z < startZ + roomSize; z++) {
                    int xOffset = Math.abs(x - middleX);
                    int zOffset = Math.abs(z - middleZ);
                    if (xOffset != 0 || zOffset != 0) {
                        boolean spawnGlass = xOffset <= 1 && zOffset <= 1;
                        world.setBlockState(new BlockPos(x, y, z), spawnGlass ? BPBlocks.reinforced_sapphire_glass.getDefaultState() : Blocks.AIR.getDefaultState(), 0);
                    }
                }
            }
        }

        for (EnumFacing d : EnumFacing.VALUES) {
            if (d != EnumFacing.UP && d != EnumFacing.DOWN) {
                if (rand.nextInt(2) == 0) {
                    generateAltar(world, middleX + d.getXOffset() * roomSize / 2, startY - 1, middleZ + d.getZOffset() * roomSize / 2, rand, d);
                }
            }
        }
    }

    private void generateAltar(World world, int startX, int startY, int startZ, Random rand, EnumFacing dir) {
        generateLootChest(world, new BlockPos(startX, startY + 1, startZ), rand, dir);
        EnumFacing opDir = dir.getOpposite();
        Block altarBlock = ALTAR_BLOCKS[rand.nextInt(ALTAR_BLOCKS.length)];
        setAltarBlockAndPossiblyTrap(world, startX, startY, startZ, rand, altarBlock);
        setAltarBlockAndPossiblyTrap(world, startX + opDir.getXOffset(), startY, startZ + opDir.getZOffset(), rand, altarBlock);
        EnumFacing sideDir = EnumFacing.DOWN;
        setAltarBlockAndPossiblyTrap(world, startX + sideDir.getXOffset(), startY, startZ + sideDir.getZOffset(), rand, altarBlock);
        setAltarBlockAndPossiblyTrap(world, startX + sideDir.getXOffset() + opDir.getXOffset(), startY, startZ + sideDir.getZOffset() + opDir.getZOffset(), rand,
                altarBlock);
        sideDir = sideDir.getOpposite();
        setAltarBlockAndPossiblyTrap(world, startX + sideDir.getXOffset(), startY, startZ + sideDir.getZOffset(), rand, altarBlock);
        setAltarBlockAndPossiblyTrap(world, startX + sideDir.getXOffset() + opDir.getXOffset(), startY, startZ + sideDir.getZOffset() + opDir.getZOffset(), rand,
                altarBlock);

    }

    private void setAltarBlockAndPossiblyTrap(World world, int x, int y, int z, Random rand, Block altarBlock) {
        world.setBlockState(new BlockPos(x, y, z), altarBlock.getDefaultState(), 2);
        if (rand.nextInt(6) == 0) {
            world.setBlockState(new BlockPos(x, y - 1, z), Blocks.TNT.getDefaultState(), 2);
            world.setBlockState(new BlockPos(x, y - 2, z), Blocks.REDSTONE_BLOCK.getDefaultState(), 2);
        }
    }

    private void generateLootChest(World world, BlockPos pos, Random rand, EnumFacing dir) {
        world.setBlockState(pos, Blocks.CHEST.getDefaultState(), dir.getOpposite().ordinal());
        if (rand.nextInt(5) == 0) {
            ((TileEntityChest) world.getTileEntity(pos)).setInventorySlotContents(13,
                    new ItemStack(BPItems.tungsten_ingot, 5 + rand.nextInt(10)));
        } else {
            ((TileEntityChest) world.getTileEntity(pos)).setLootTable(LootTableList.CHESTS_SIMPLE_DUNGEON, rand.nextInt());//Possibly to be added with IC designs from the community.
        }
    }

    private static class Pos {

        public final int x, z;

        public Pos(int x, int z) {

            this.x = x;
            this.z = z;
        }

        @Override
        public boolean equals(Object object) {
            if (object instanceof Pos) {
                Pos pos = (Pos) object;
                return pos.x == x && pos.z == z;
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return (x << 13) + z;
        }
    }
}
