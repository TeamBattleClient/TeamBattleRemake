package net.minecraft.client.gui;

import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.network.LanServerDetector;

import com.google.common.collect.Lists;

public class ServerSelectionList extends GuiListExtended {
	private final GuiListExtended.IGuiListEntry field_148196_n = new ServerListEntryLanScan();
	private int field_148197_o = -1;
	private final List field_148198_l = Lists.newArrayList();
	private final List field_148199_m = Lists.newArrayList();
	private final GuiMultiplayer field_148200_k;

	public ServerSelectionList(GuiMultiplayer p_i45049_1_,
			Minecraft p_i45049_2_, int p_i45049_3_, int p_i45049_4_,
			int p_i45049_5_, int p_i45049_6_, int p_i45049_7_) {
		super(p_i45049_2_, p_i45049_3_, p_i45049_4_, p_i45049_5_, p_i45049_6_,
				p_i45049_7_);
		field_148200_k = p_i45049_1_;
	}

	@Override
	protected int func_148137_d() {
		return super.func_148137_d() + 30;
	}

	@Override
	public int func_148139_c() {
		return super.func_148139_c() + 85;
	}

	@Override
	public GuiListExtended.IGuiListEntry func_148180_b(int p_148180_1_) {
		if (p_148180_1_ < field_148198_l.size())
			return (GuiListExtended.IGuiListEntry) field_148198_l
					.get(p_148180_1_);
		else {
			p_148180_1_ -= field_148198_l.size();

			if (p_148180_1_ == 0)
				return field_148196_n;
			else {
				--p_148180_1_;
				return (GuiListExtended.IGuiListEntry) field_148199_m
						.get(p_148180_1_);
			}
		}
	}

	public void func_148192_c(int p_148192_1_) {
		field_148197_o = p_148192_1_;
	}

	public int func_148193_k() {
		return field_148197_o;
	}

	public void func_148194_a(List p_148194_1_) {
		field_148199_m.clear();
		final Iterator var2 = p_148194_1_.iterator();

		while (var2.hasNext()) {
			final LanServerDetector.LanServer var3 = (LanServerDetector.LanServer) var2
					.next();
			field_148199_m.add(new ServerListEntryLanDetected(field_148200_k,
					var3));
		}
	}

	public void func_148195_a(ServerList p_148195_1_) {
		field_148198_l.clear();

		for (int var2 = 0; var2 < p_148195_1_.countServers(); ++var2) {
			field_148198_l.add(new ServerListEntryNormal(field_148200_k,
					p_148195_1_.getServerData(var2)));
		}
	}

	@Override
	protected int getSize() {
		return field_148198_l.size() + 1 + field_148199_m.size();
	}

	@Override
	protected boolean isSelected(int p_148131_1_) {
		return p_148131_1_ == field_148197_o;
	}
}
