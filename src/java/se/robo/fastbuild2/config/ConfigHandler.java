package se.robo.fastbuild2.config;

import java.io.File;

import se.robo.fastbuild2.FastBuild2;
import se.robo.fastbuild2.info.ModInfo;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {

	public static Configuration config;

	public static int maxXZ = 0;
	public static int maxY = 0;

	
	public static void init(File file) {
		if (config == null)
		{
			config = new Configuration(file);
			try 
			{
				config.load();
			}
			catch (Exception e)
			{
				FastBuild2.log.error("Error loading config! " + e.getMessage());
			}
			updateConfig();
		}
	}

	private static void updateConfig()
	{
		maxXZ = config.get(Configuration.CATEGORY_GENERAL, "MaxHorizontalBuild", 24, "Max length to build horizontally").setRequiresMcRestart(false).getInt();
		maxY = config.get(Configuration.CATEGORY_GENERAL, "MaxVerticalBuild", 12, "Max length to build vertically").setRequiresMcRestart(false).getInt();

		if (config.hasChanged()) {
			config.save();
		}
	}

	public static void setConfigurationProperty(String key, int value) 
	{
		config.getCategory(Configuration.CATEGORY_GENERAL).get(key).set(value);
		config.save();
	}
	
	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if(event.modID.equalsIgnoreCase(ModInfo.MODID))
		{
			updateConfig();
		}
	}
}
