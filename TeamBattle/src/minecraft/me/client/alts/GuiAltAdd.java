package me.client.alts;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import org.lwjgl.input.Keyboard;

public class GuiAltAdd extends GuiScreen
{
    private GuiScreen prevMenu;
    private GuiTextField nameBox;
    private GuiPasswordField passwordBox;
    private static final String __OBFID = "CL_00000709";
    private String displayText = "";
    private int errorTimer;

    public GuiAltAdd(GuiScreen par1GuiScreen)
    {
        this.prevMenu = par1GuiScreen;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        this.nameBox.updateCursorCounter();
        this.passwordBox.updateCursorCounter();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, "Add"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, "Cancel"));
        this.nameBox = new GuiTextField(this.fontRendererObj, this.width / 2 - 100, 60, 200, 20);
        this.nameBox.setFocused(true);
        this.nameBox.setText(Minecraft.getMinecraft().session.getUsername());
        this.passwordBox = new GuiPasswordField(this.fontRendererObj, this.width / 2 - 100, 100, 200, 20);
        this.passwordBox.setFocused(false);
    }

    /**
     * "Called when the screen is unloaded. Used to disable keyboard repeat events."
     */
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    protected void actionPerformed(GuiButton clickedButton)
    {
        if (clickedButton.enabled)
        {
            if (clickedButton.id == 1)
            {
                mc.displayGuiScreen(this.prevMenu);
            }
            else if (clickedButton.id == 0)
            {
                GuiAltList var10000;

                if (this.passwordBox.getText().length() == 0)
                {
                    var10000 = GuiAlts.altList;
                    GuiAltList.alts.add(new Alt(this.nameBox.getText(), (String)null));
                    this.displayText = "";
                }
                else
                {
                    this.displayText = AltUtilsAdd.check(this.nameBox.getText(), this.passwordBox.getText());

                    if (this.displayText.equals(""))
                    {
                        var10000 = GuiAlts.altList;
                        GuiAltList.alts.add(new Alt(this.nameBox.getText(), this.passwordBox.getText()));
                    }
                }

                if (this.displayText.equals(""))
                {
                    var10000 = GuiAlts.altList;
                    GuiAltList.sortAlts();
                    /**
                     * Save alts
                     */
                    mc.displayGuiScreen(this.prevMenu);
                }
            }
        }
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2)
    {
        this.nameBox.textboxKeyTyped(par1, par2);
        this.passwordBox.textboxKeyTyped(par1, par2);

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
        super.mouseClicked(par1, par2, par3);
        this.nameBox.mouseClicked(par1, par2, par3);
        this.passwordBox.mouseClicked(par1, par2, par3);

        if (this.nameBox.isFocused() || this.passwordBox.isFocused())
        {
            this.displayText = "";
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, "Add an Alt", this.width / 2, 20, 16777215);
        drawString(this.fontRendererObj, "Name or E-Mail", this.width / 2 - 100, 47, 10526880);
        drawString(this.fontRendererObj, "Password", this.width / 2 - 100, 87, 10526880);
        this.drawCenteredString(this.fontRendererObj, this.displayText, this.width / 2, 142, 16777215);
        this.nameBox.drawTextBox();
        this.passwordBox.drawTextBox();

        super.drawScreen(par1, par2, par3);
    }
}
