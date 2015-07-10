package se.robo.fastbuild2.proxies;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.client.settings.KeyBinding;
import se.robo.fastbuild2.BuildHandler;



public class ClientProxy extends CommonProxy
{
    
    public void initSounds()
    {
        
    }
    
    public void initRenders()
    {
    	
    }
    
    public void initKeybinds()
    {
    	BuildHandler.triggerKey = new KeyBinding("key.fastbuildKey",Keyboard.KEY_LCONTROL,"key.categories.gameplay");
    	ClientRegistry.registerKeyBinding(BuildHandler.triggerKey);
    }
}
