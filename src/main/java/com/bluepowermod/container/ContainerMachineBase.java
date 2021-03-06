package com.bluepowermod.container;

import com.bluepowermod.network.BPNetworkHandler;
import com.bluepowermod.network.message.MessageSyncMachineBacklog;
import com.bluepowermod.tile.TileMachineBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IContainerListener;

public abstract class ContainerMachineBase extends ContainerGhosts {

    private int backlogSize = -1;
    private final TileMachineBase machine;

    public ContainerMachineBase(TileMachineBase machine) {

        this.machine = machine;
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    @Override
    public void detectAndSendChanges() {

        super.detectAndSendChanges();

        for (Object crafter : listeners) {
            IContainerListener icrafting = (IContainerListener) crafter;
            if (backlogSize != machine.getBacklog().size() && icrafting instanceof EntityPlayerMP) {
                BPNetworkHandler.INSTANCE.sendTo(new MessageSyncMachineBacklog(machine, machine.getBacklog()), (EntityPlayerMP) icrafting);
            }
        }
        backlogSize = machine.getBacklog().size();

    }
}
