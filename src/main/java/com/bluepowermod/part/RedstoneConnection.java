package com.bluepowermod.part;

import net.minecraftforge.common.util.ForgeDirection;

import com.qmunity.lib.helper.RedstoneHelper;
import com.qmunity.lib.part.IPart;
import com.qmunity.lib.part.IPartFace;
import com.qmunity.lib.util.Dir;

public class RedstoneConnection {

    private IPart part;
    private Dir dir;

    private int out = 0;
    private int in = 0;
    private boolean caching = true;

    private boolean enabled = false;

    public RedstoneConnection(IPart part, Dir direction) {

        this.part = part;
        dir = direction;
    }

    public Dir getDirection() {

        return dir;
    }

    public int getOutput() {

        return out;
    }

    public int getInput() {

        if (caching)
            return in;

        caching = true;
        update();
        caching = false;
        return in;
    }

    public RedstoneConnection setOutput(int out) {

        int last = this.out;
        this.out = out;

        if (last != out)
            RedstoneHelper.notifyRedstoneUpdate(part.getWorld(), part.getX(), part.getY(), part.getZ(), getFD(), true);

        return this;
    }

    public boolean isEnabled() {

        return enabled;
    }

    public RedstoneConnection setEnabled(boolean enabled) {

        this.enabled = enabled;

        return this;
    }

    public RedstoneConnection enable() {

        enabled = true;

        return this;
    }

    public RedstoneConnection disable() {

        enabled = false;

        return this;
    }

    public void update() {

        if (caching) {
            if (part instanceof IPartFace) {
                in = RedstoneHelper.getInput(part.getWorld(), part.getX(), part.getY(), part.getZ(), getFD(), ((IPartFace) part).getFace());
            } else {
                in = RedstoneHelper.getInput(part.getWorld(), part.getX(), part.getY(), part.getZ(), getFD());
            }
        }
    }

    private ForgeDirection getFD() {

        if (part instanceof IPartFace) {
            if (part instanceof BPPartFaceRotate) {
                return getDirection().toForgeDirection(((IPartFace) part).getFace(), ((BPPartFaceRotate) part).getRotation());
            } else {
                return getDirection().toForgeDirection(((IPartFace) part).getFace(), 0);
            }
        }
        return getDirection().getFD();
    }

}