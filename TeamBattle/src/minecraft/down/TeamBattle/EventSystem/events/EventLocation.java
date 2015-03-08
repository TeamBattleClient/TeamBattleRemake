package down.TeamBattle.EventSystem.events;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import down.TeamBattle.EventSystem.Event;

public class EventLocation extends Event
{
	final Minecraft mc = Minecraft.getMinecraft();
private double x;
private double y;
private double z;
private boolean onGround;
private Packet posPacket;
private Packet lookPacket;

public EventLocation(double x, double y, double z, boolean onGround, Packet posPacket, Packet lookPacket)
{
  this.x = x;
  this.y = y;
  this.z = z;
  this.onGround = onGround;
  this.posPacket = posPacket;
  this.lookPacket = lookPacket;
}

public double getX() {
  return this.x;
}

public void setX(double x) {
  this.x = x;
}

public double getY() {
  return this.y;
}

public void setY(double y) {
  this.y = y;
}

public double getZ() {
  return this.z;
}

public void setZ(double z) {
  this.z = z;
}

public boolean getOnGround() {
  return this.onGround;
}

public void setOnGround(boolean onGround) {
  this.onGround = onGround;
}

public Packet getPosPacket() {
  return this.posPacket;
}

public Packet getLookPacket() {
  return this.lookPacket;
}

public void setLoctaion(double x, double y, double z, boolean onGround) {
  this.posPacket = new C03PacketPlayer.C04PacketPlayerPosition(x, y - 1.6D, y - 0.6D, z, onGround);
  this.lookPacket = new C03PacketPlayer.C06PacketPlayerPosLook(x, y - 1.6D, y - 0.6D, z, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, onGround);
}
}
