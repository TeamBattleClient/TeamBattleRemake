package me.client.modules.movement;


import java.util.Iterator;

import me.client.events.EventPostMotion;
import me.client.modules.Module;
import me.client.modules.ModuleCategory;
import net.minecraft.block.Block;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Timer;

import org.lwjgl.input.Keyboard;

import com.darkmagician6.eventapi.EventTarget;

public class Speed extends Module {
	
    public static int speedUpdate;
    public static int speedUpdate2;
    public static int speedcounter;
    public double sprintspeed = 2.6D;
    private static int onground;
    private long resetMS;
    private static int ongroundtick;
	
	public Speed() {
		super("Speed (NCP+)", "Speed (NCP+)", ModuleCategory.MOVEMENT);
		this.setKeybind(Keyboard.KEY_X);
	}

	@EventTarget
	public void beforeUpdate(EventPostMotion event) {
    	if(this.isEnabled()) {
        	boolean water = Block.getIdFromBlock(mc.theWorld.getBlock((int)Math.floor(mc.thePlayer.posX), (int)Math.floor(mc.thePlayer.boundingBox.minY) - 1, (int)Math.floor(mc.thePlayer.posZ))) == 9 || Block.getIdFromBlock(mc.theWorld.getBlock((int)Math.floor(mc.thePlayer.posX), (int)Math.floor(mc.thePlayer.boundingBox.minY), (int)Math.floor(mc.thePlayer.posZ))) == 9;
            boolean strafe = mc.thePlayer.moveStrafing != 0.0F;
            boolean walkable = true;
            Iterator e = mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.expand(0.5D, 0.0D, 0.5D)).iterator();


            onground = this.isOnground(20);

            if (walkable && !mc.thePlayer.isSneaking() && (mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F) && !mc.thePlayer.isOnLadder() && !water)
            {
                double e2 = mc.thePlayer.isSprinting() ? 0.021D : 0.4D;
                double r = strafe ? this.sprintspeed + 0.02D : this.sprintspeed + 0.04D;
                double a = r + e2;
                Timer var10000;

                if (onground == 1)
                {
                    if (speedUpdate == 0)
                    {
                        ++speedUpdate;
                        var10000 = mc.timer;
                        Timer.timerSpeed = 1.1F;
                        mc.thePlayer.motionX *= a;
                        mc.thePlayer.motionZ *= a;
                        mc.thePlayer.boundingBox.offset(0.0D, 0.01D, 0.0D);
                    }
                    else if (speedUpdate >= 1)
                    {
                        var10000 = mc.timer;
                        Timer.timerSpeed = 1.0F;
                        mc.thePlayer.motionX /= 1.5D;
                        mc.thePlayer.motionZ /= 1.5D;
                        speedUpdate = 0;
                    }
                }
                else if (onground == 0)
                {
                    var10000 = mc.timer;
                    Timer.timerSpeed = 1.0F;
                }
                else if (onground == 2)
                {
                    boolean backwards = mc.thePlayer.moveForward < 0.0F;
                    boolean strafee = mc.thePlayer.moveStrafing != 0.0F;
                    boolean forward = mc.thePlayer.moveForward > 0.0F;
                    double ae = mc.thePlayer.isSprinting() ? 0.02754D : 1.3352D;
                    double speed = this.sprintspeed;
                    speed *= 2.0D;
                    System.out.println(speed);

                    if (speedUpdate <= 2 && !mc.thePlayer.isSneaking() && (mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F) && !mc.thePlayer.isOnLadder())
                    {
                        ++speedUpdate;
                        var10000 = mc.timer;
                        Timer.timerSpeed = 1.0F;

                        if (speedUpdate == 2)
                        {
                            var10000 = mc.timer;
                            Timer.timerSpeed = 1.15F;
                            speedUpdate = 0;

                            if (!backwards && (strafe && !forward || !strafe && forward))
                            {
                                mc.thePlayer.boundingBox.offset(mc.thePlayer.motionX * (speed + ae), 0.01D, mc.thePlayer.motionZ * (speed + e2));
                            }
                            else if (backwards)
                            {
                                mc.thePlayer.boundingBox.offset(mc.thePlayer.motionX * (speed / 1.2D + 0.02D), 0.01D, mc.thePlayer.motionZ * (speed / 1.2D + 0.02D));
                            }
                            else if (strafe && (backwards || forward))
                            {
                                mc.thePlayer.boundingBox.offset(mc.thePlayer.motionX * (speed / 1.1D + 0.02D), 0.01D, mc.thePlayer.motionZ * (speed / 1.1D + 0.02D));
                            }
                        }
                    }
                }
            }
    	}
    }
    
    private boolean hasDelayrun(int aps)
    {
        long currentMS = System.currentTimeMillis();
        return currentMS >= this.resetMS + (long)aps;
    }

    private int isOnground(int i)
    {
        int x = MathHelper.floor_double(mc.thePlayer.posX);
        int z = MathHelper.floor_double(mc.thePlayer.posZ);
        int y = MathHelper.floor_double(mc.thePlayer.posY) - 2;

        if ((double)mc.theWorld.getBlock(x, y, z).slipperiness > 0.7D)
        {
            return 2;
        }
        else
        {
            if (ongroundtick <= i && mc.thePlayer.onGround && !mc.thePlayer.capabilities.allowFlying)
            {
                if (ongroundtick < i)
                {
                    ++ongroundtick;
                }

                if (ongroundtick == i)
                {
                    return 1;
                }
            }

            if ((!mc.thePlayer.onGround || ongroundtick > i) && mc.thePlayer.capabilities.allowFlying)
            {
                ongroundtick = 0;
                return 0;
            }
            else
            {
                return (mc.thePlayer.capabilities.allowFlying) && !mc.thePlayer.onGround ? 2 : (mc.thePlayer.capabilities.allowFlying && mc.thePlayer.onGround ? 1 : 0);
            }
        }
    }

}
