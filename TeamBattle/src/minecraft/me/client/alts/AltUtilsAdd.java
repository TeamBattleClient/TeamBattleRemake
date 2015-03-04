package me.client.alts;

import java.net.Proxy;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

public class AltUtilsAdd
{
    private static final Logger logger = LogManager.getLogger();

    public static String check(String name, String password)
    {
        YggdrasilAuthenticationService authenticationService = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        YggdrasilUserAuthentication authentication = (YggdrasilUserAuthentication)authenticationService.createUserAuthentication(Agent.MINECRAFT);
        authentication.setUsername(name);
        authentication.setPassword(password);
        String displayText = "";

        try
        {
            authentication.logIn();
            displayText = "";
        }
        catch (AuthenticationUnavailableException var6)
        {
            displayText = "\u00a74\u00a7lCannot contact authentication server!";
        }
        catch (AuthenticationException var7)
        {
            if (!var7.getMessage().contains("Invalid username or password.") && !var7.getMessage().toLowerCase().contains("account migrated"))
            {
                displayText = "\u00a74\u00a7lCannot contact authentication server!";
            }

            logger.error(var7.getMessage());
        }
        catch (NullPointerException var8)
        {
            displayText = "\u00a74\u00a7lWeird error: This alt doesn\'t have a username!";
        }

        return displayText;
    }
}
