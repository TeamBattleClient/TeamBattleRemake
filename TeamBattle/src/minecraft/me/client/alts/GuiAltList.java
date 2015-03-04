package me.client.alts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import me.client.Client;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

public class GuiAltList extends GuiTeamBattleSlot
{
    private int selectedSlot;
    private Minecraft mc;
    public static ArrayList<Alt> alts = new ArrayList();

    public GuiAltList(Minecraft par1Minecraft, GuiScreen prevMenu)
    {
        super(par1Minecraft, prevMenu.width, prevMenu.height, 36, prevMenu.height - 56, 30);
        this.mc = par1Minecraft;
    }

    public static void sortAlts()
    {
        Collections.sort(alts, new Comparator()
        {
            public int compare(Alt o1, Alt o2)
            {
                return o1.name.compareToIgnoreCase(o2.name);
            }
            public int compare(Object x0, Object x1)
            {
                return this.compare((Alt)x0, (Alt)x1);
            }
        });
        ArrayList newAlts = new ArrayList();
        int i;

        for (i = 0; i < alts.size(); ++i)
        {
            if (!((Alt)alts.get(i)).cracked)
            {
                newAlts.add(alts.get(i));
            }
        }

        for (i = 0; i < alts.size(); ++i)
        {
            if (((Alt)alts.get(i)).cracked)
            {
                newAlts.add(alts.get(i));
            }
        }

        for (i = 0; i < newAlts.size(); ++i)
        {
            for (int i2 = 0; i2 < newAlts.size(); ++i2)
            {
                if (i != i2 && ((Alt)newAlts.get(i)).name.equals(((Alt)newAlts.get(i2)).name) && ((Alt)newAlts.get(i)).cracked == ((Alt)newAlts.get(i2)).cracked)
                {
                    newAlts.remove(i2);
                }
            }
        }

        alts = newAlts;
    }

    public boolean isSelected(int id)
    {
        return this.selectedSlot == id;
    }

    protected int getSelectedSlot()
    {
        return this.selectedSlot;
    }

    protected int getSize()
    {
        return alts.size();
    }

    protected void elementClicked(int var1, boolean var2, int var3, int var4)
    {
        this.selectedSlot = var1;
    }

    protected void drawBackground() {}

    protected void drawSlot(int id, int x, int y, int var4, Tessellator var5, int var6, int var7)
    {
        Alt alt = (Alt)alts.get(id);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
        int var10001 = x + 1;
        int var10002 = y + 1;
        GuiAltList var10006 = GuiAlts.altList;
        Client.instance.tahoma.drawString("Name: " + alt.name, x + 31, y + 3, 10526880);
        String var11 = "";

        for (int i = 0; i < alt.password.length(); ++i)
        {
            var11 = var11.concat("*");
        }

        Client.instance.tahoma.drawString(alt.cracked ? "cracked" : "Password: " + var11, x + 31, y + 15, 10526880);
    }
}