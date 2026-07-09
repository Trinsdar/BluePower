package com.bluepowermod.api.wire.redstone;

import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.multipart.IBPMultipartTile;
import com.bluepowermod.api.multipart.IBPPartTile;
import com.bluepowermod.block.BlockBPMultipart;
import com.bluepowermod.helper.MathHelper;
import com.bluepowermod.helper.RedstoneHelper;
import com.bluepowermod.redstone.RedstoneApi;
import com.bluepowermod.redstone.RedstoneConnectionCache;
import com.bluepowermod.tile.TileBPMultipart;
import com.bluepowermod.tile.tier1.TileWire;
import com.bluepowermod.util.MultipartUtils;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class RedstoneStorage implements IRedstoneDevice, IRedConductor {
    private final RedstoneConnectionCache redstoneConnections = RedstoneApi.getInstance().createRedstoneConnectionCache(this);
    byte power = 0;
    private final IRedwire wire;
    private final Direction face;
    private Pair<Direction, Integer> input = null;

    public RedstoneStorage(IRedwire wire, Direction face) {
        this.wire = wire;
        this.face = face;
    }


    @Override
    public boolean canConnect(Direction side, IRedstoneDevice dev, ConnectionType type) {
        return true;
    }

    @Override
    public RedstoneConnectionCache getRedstoneConnectionCache() {
        return redstoneConnections;
    }

    @Override
    public byte getRedstonePower(Direction side) {
        if (input != null && input.first() == side) return 0;
        return power;
    }

    @Override
    public byte getVanillaRedstonePower(Direction side) {
        if (!RedstoneApi.getInstance().shouldWiresOutputPower(this.hasLoss(side))) return 0;
        if (side != null && !wire.canOutputPower(side)) return 0;
        return (byte) MathHelper.map(getRedstonePower(side) & 0xFF, 0, 255, 0, 15);
    }

    @Override
    public void setRedstonePower(Direction side, byte power) {
        this.power = power;
    }

    @Override
    public boolean onRedstoneUpdate() {
        if (this.getLevel() == null) return false;
        if (getLevel().isClientSide()) return false;
        IBPMultipartTile multipart = wire instanceof IBPPartTile partTile ? partTile.getMultipart() : null;
        byte oldPower = power;
        int tRedstone = 0;
        Pair<Direction, Integer> oldInput = this.input;
        if (oldInput != null){
            byte currentInput = (byte) getRedstoneAtSide(oldInput.first(), getDeviceAtSide(oldInput.first()));
            if (power != currentInput){
                power = currentInput;
                input = (power & 0xFF) == 0 ? null : Pair.of(input.first(), power & 0xFF);
            }
        }
        Map<Direction, Boolean> sidesToUpdate = new HashMap<>();
        if (input != null && input != oldInput){ //original input has changed
            for (Direction side : Direction.values()){
                Pair<BlockState, BlockEntity> p = getBlockEntityAtSide(side);
                boolean isinThisBlock = false;
                if (multipart != null){
                    BlockState partState = multipart.getStateByFacing(side.getOpposite());
                    BlockEntity partTE = partState != null ? multipart.getTileForState(partState) : null;
                    if (partState != null){
                        p = Pair.of(partState, partTE);
                        isinThisBlock = true;
                    }
                }
                if (!(p.second() instanceof IRedwire)){
                    sidesToUpdate.put(side, isinThisBlock);
                }
            }
            RedstoneApi.getInstance().setWiresOutputPower(false, hasLoss(null));
            RedstoneApi.getInstance().setWiresHandleUpdates(false);
            for (var side : sidesToUpdate.entrySet()) {
                if (side.getValue() && multipart != null){
                    BlockState partState = multipart.getStateByFacing(side.getKey().getOpposite());
                    if (partState != null){
                        partState.neighborChanged(getLevel(), getBlockPos(), partState.getBlock(), getBlockPos(), false);
                    }
                } else {
                    updateBlock(side.getKey());
                }
            }
            RedstoneApi.getInstance().setWiresHandleUpdates(true);
            RedstoneApi.getInstance().setWiresOutputPower(true, hasLoss(null));
        }
        boolean updateMultipart = false;
        for (Direction side : Direction.values()){
            if (!wire.canReceivePower(side)) continue;
            if (oldInput != null && oldInput.first() == side) continue;
            IRedstoneDevice device = getDeviceAtSide(side);
            if (device == null) sidesToUpdate.put(side, false);
            tRedstone = getRedstoneAtSide(side,device);
            if (tRedstone > (power & 0xFF) && tRedstone != (oldPower & 0xFF) - 1){
                power = (byte) tRedstone;
                input = Pair.of(side, power & 0xFF);
            }
        }
        if (power != oldPower){
            RedstoneApi.getInstance().setWiresHandleUpdates(false);
            for (var side : sidesToUpdate.entrySet()) {
                if (side.getValue() && multipart != null){
                    BlockState partState = multipart.getStateByFacing(side.getKey().getOpposite());
                    if (partState != null){
                        partState.neighborChanged(getLevel(), getBlockPos(), partState.getBlock(), getBlockPos(), false);
                    }
                } else {
                    updateBlock(side.getKey());
                }
            }
            RedstoneApi.getInstance().setWiresHandleUpdates(true);
            return true;
        }

        return false;
    }

    private void updateBlock(Direction side){
        BlockState state = getLevel().getBlockState(getBlockPos());
        getLevel().markAndNotifyBlock(getBlockPos(), getLevel().getChunkAt(getBlockPos()), state, state, 1, 512);
        BlockPos neighbor = getBlockPos().relative(side);
        BlockState neighborState = getLevel().getBlockState(neighbor);
        getLevel().updateNeighborsAtExceptFromFacing(neighbor, neighborState.getBlock(), side.getOpposite());
    }

    public int getRedstoneAtSide(Direction side, IRedstoneDevice device) {
        if (side == null) return 0;
        if (!wire.canReceivePower(side)) return 0;
        int[] in = new int[]{0};

        if (device != null){
            Direction d = device.getBlockPos().equals(this.getBlockPos()) ? face.getOpposite() : side.getOpposite();
            in[0] = device.getRedstonePower(d) & 0xFF;
            if (in[0] > 0) return in[0] - (hasLoss(side) ? 1 : 0);
            else if (device.getBlockPos().equals(this.getBlockPos())) return 0;
        }
        BlockState state = getLevel().getBlockState(getBlockPos().relative(side));
        // Do not accept Redstone coming from any Redstone Sink! (Such as Droppers or Dispensers)
        if(RedstoneHelper.isVanillaRedstoneSink(state)) return 0;
        int redstoneLevel = MultipartUtils.getRedstonePower(side, face, getLevel(), getBlockPos());
        in[0] = redstoneLevel * 17;
        return in[0];
    }

    public IRedstoneDevice getDeviceAtSide(Direction side){
        IRedstoneDevice[] device = new IRedstoneDevice[1];
        if (wire instanceof IBPPartTile partTile && partTile.getMultipart() != null){
            BlockState partState = partTile.getMultipart().getStateByFacing(side.getOpposite());
            if (partState != null){
                BlockEntity partBE = partTile.getMultipart().getTileForState(partState);
                if (partBE != null){
                    partBE.getCapability(CapabilityRedstoneDevice.UNINSULATED_CAPABILITY, face.getOpposite()).ifPresent(r -> device[0] = r);
                }
            }
        }
        BlockEntity tDelegator = getLevel().getBlockEntity(getBlockPos().relative(side));
        if (tDelegator != null){
            tDelegator.getCapability(CapabilityRedstoneDevice.UNINSULATED_CAPABILITY, side.getOpposite()).ifPresent(r -> device[0] = r);
        }
        return device[0];
    }

    public Pair<BlockState, BlockEntity> getBlockEntityAtSide(Direction side){
        BlockEntity neighborBE = getLevel().getBlockEntity(getBlockPos().relative(side));
        if (neighborBE instanceof IBPMultipartTile multipartTile){
            BlockState partState = multipartTile.getStateByFacing(face);
            if (partState != null){
                return Pair.of(partState, multipartTile.getTileForState(partState));
            }
        }
        return Pair.of(getLevel().getBlockState(getBlockPos().relative(side)) ,neighborBE);
    }

    @Override
    public boolean isNormalFace(Direction side) {
        return false;
    }

    @Override
    public BlockPos getBlockPos() {
        return wire.getBlockPos();
    }

    @Override
    public Level getLevel() {
        return wire.getLevel();
    }

    @Override
    public boolean hasLoss(Direction side) {
        return wire.getRedwireType(side).hasLoss();
    }

    @Override
    public boolean isAnalogue(Direction side) {
        return wire.getRedwireType(side).isAnalogue();
    }
}
