package me.client.alts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;

import me.client.Client;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;

import org.lwjgl.opengl.GL11;

public class GuiAlts extends GuiScreen implements GuiYesNoCallback
{
    private GuiScreen prevMenu;
    private boolean shouldAsk = true;
    private int errorTimer;
    public static GuiAltList altList;

    public GuiAlts(GuiScreen par1GuiScreen)
    {
        this.prevMenu = par1GuiScreen;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        altList = new GuiAltList(mc, this);
        altList.func_148134_d(7, 8);
        altList.elementClicked(-1, false, 0, 0);
        GuiAltList var10000 = altList;
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 154, this.height - 52, 100, 20, "Use"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 50, this.height - 52, 100, 20, "Direct Login"));
        this.buttonList.add(new GuiButton(2, this.width / 2 + 54, this.height - 52, 100, 20, "Add"));
        this.buttonList.add(new GuiButton(3, this.width / 2 - 154, this.height - 28, 100, 20, "Edit"));
        this.buttonList.add(new GuiButton(4, this.width / 2 - 50, this.height - 28, 100, 20, "Delete"));
        this.buttonList.add(new GuiButton(5, this.width / 2 + 54, this.height - 28, 100, 20, "Done"));
        this.buttonList.add(new GuiButton(6, this.width / 2 + 54, 2, 100, 20, "Load File"));
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        GuiButton var10000 = (GuiButton)this.buttonList.get(0);
        GuiAltList var10001 = altList;
        var10000.enabled = !GuiAltList.alts.isEmpty() && altList.getSelectedSlot() != -1;
        var10000 = (GuiButton)this.buttonList.get(3);
        var10001 = altList;
        var10000.enabled = !GuiAltList.alts.isEmpty() && altList.getSelectedSlot() != -1;
        var10000 = (GuiButton)this.buttonList.get(4);
        var10001 = altList;
        var10000.enabled = !GuiAltList.alts.isEmpty() && altList.getSelectedSlot() != -1;
    }

    protected void actionPerformed(GuiButton clickedButton)
    {
        if (clickedButton.enabled)
        {
            Alt alt;
            String deleteQuestion;
            GuiAltList var10000;

            if (clickedButton.id == 0)
            {
                var10000 = altList;
                alt = (Alt)GuiAltList.alts.get(altList.getSelectedSlot());

                if (alt.cracked)
                {
                    AltUtilsLogin.changeCrackedName(alt.name);
                    mc.displayGuiScreen(this.prevMenu);
                }
                else
                {
                    deleteQuestion = AltUtilsLogin.login(alt.name, alt.password);

                    if (deleteQuestion.equals(""))
                    {
                        mc.displayGuiScreen(this.prevMenu);
                    }
                    else
                    {
                        this.errorTimer = 8;

                        if (deleteQuestion.equals("\u00a74\u00a7lWrong password!"))
                        {
                            var10000 = altList;
                            GuiAltList var10001 = altList;
                            GuiAltList.alts.remove(GuiAltList.alts.indexOf(alt));
                            /**
                             * Save alts
                             */
                        }
                    }
                }
            }
            else if (clickedButton.id == 1)
            {
                mc.displayGuiScreen(new GuiAltLogin(this));
            }
            else if (clickedButton.id == 2)
            {
                mc.displayGuiScreen(new GuiAltAdd(this));
            }
            else if (clickedButton.id == 3)
            {
                var10000 = altList;
                alt = (Alt)GuiAltList.alts.get(altList.getSelectedSlot());
                mc.displayGuiScreen(new GuiAltEdit(this, alt));
            }
            else if (clickedButton.id == 4)
            {
                var10000 = altList;
                alt = (Alt)GuiAltList.alts.get(altList.getSelectedSlot());
                deleteQuestion = "Are you sure you want to remove this alt?";
                String deleteWarning = "\"" + alt.name + "\" will be lost forever! (A long time!)";
                mc.displayGuiScreen(new GuiYesNo(this, deleteQuestion, deleteWarning, "Delete", "Cancel", 1));
            }
            else if (clickedButton.id == 5)
            {
            	/**
            	 * Save alts
            	 */
                mc.displayGuiScreen(this.prevMenu);
            }
            else if(clickedButton.id == 6) {
            	JFileChooser fileChooser = new JFileChooser();
            	fileChooser.showOpenDialog(null);
            	File selectedFile = fileChooser.getSelectedFile();
            	BufferedReader reader;
            	
            	try{
            		reader = new BufferedReader(new FileReader(selectedFile));
            		
            		for(String line; (line = reader.readLine()) != null;) {
            			String username = line.split(":")[0];
            			String password = line.split(":")[1];
            			
            		    altList.alts.add(new Alt(username, password));
                    	/**
                    	 * Save alts
                    	 */
            		}
            	}catch(Exception e) {}
            }
        }
    }

    public void confirmClicked(boolean par1, int par2)
    {
        GuiAltList var10000;

        if (par2 == 1)
        {
            var10000 = altList;
            Alt var4 = (Alt)GuiAltList.alts.get(altList.getSelectedSlot());

            if (par1)
            {
                var10000 = altList;
                GuiAltList var10001 = altList;
                GuiAltList.alts.remove(GuiAltList.alts.indexOf(var4));
            	/**
            	 * Save alts
            	 */
            }
        }

        mc.displayGuiScreen(this);
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2)
    {
        if (par2 == 28 || par2 == 156)
        {
            this.actionPerformed((GuiButton)this.buttonList.get(0));
        }
    }

    /**
     * Called when the mouse is clicked.
     * @throws IOException 
     */
    protected void mouseClicked(int par1, int par2, int par3)
    {
        if (par2 >= 36 && par2 <= this.height - 57 && (par1 >= this.width / 2 + 140 || par1 <= this.width / 2 - 126))
        {
            altList.elementClicked(-1, false, 0, 0);
        }

        super.mouseClicked(par1, par2, par3);
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        this.drawDefaultBackground();
        altList.func_148128_a(par1, par2, par3);

        if (altList.getSelectedSlot() != -1)
        {
            GuiAltList var10000 = altList;
            Alt alt = (Alt)GuiAltList.alts.get(altList.getSelectedSlot());
        }

        Client.instance.tahoma.drawString("Logged in as: " + mc.session.username, 5, 5, -1);
        Client.instance.tahoma.drawCenteredString("Alt Manager", this.width / 2, 8, 16777215);
        StringBuilder var10002 = (new StringBuilder()).append("Alts: ");
        GuiAltList var10003 = altList;
        Client.instance.tahoma.drawCenteredString(var10002.append(GuiAltList.alts.size()).toString(), this.width / 2, 20, 16777215);

        super.drawScreen(par1, par2, par3);
    }
}
