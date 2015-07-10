package se.robo.fastbuild2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import se.robo.fastbuild2.command.CommandFastbuild;
import se.robo.fastbuild2.config.ConfigHandler;
import se.robo.fastbuild2.info.ModInfo;
import se.robo.fastbuild2.proxies.CommonProxy;

@Mod(name = ModInfo.NAME, modid = ModInfo.MODID, version = ModInfo.VERSION, guiFactory = ModInfo.GUI_FACTORY_CLASS, dependencies=ModInfo.DEPENDENCY)
public class FastBuild2 {

    @SidedProxy(clientSide = ModInfo.CLIENT_PROXY_CLASS, serverSide = ModInfo.SERVER_PROXY_CLASS)
    public static CommonProxy proxy;

    public static Logger log = LogManager.getLogger(ModInfo.MODID);


    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	ConfigHandler.init(event.getSuggestedConfigurationFile());
    	FMLCommonHandler.instance().bus().register(new ConfigHandler());
    	proxy.initKeybinds();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {

    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    }
    
    @EventHandler
    public static void serverLoad(FMLServerStartingEvent event)
    {
    	event.registerServerCommand(new CommandFastbuild());
    }
    

}