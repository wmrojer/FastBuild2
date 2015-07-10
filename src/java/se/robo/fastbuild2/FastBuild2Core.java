package se.robo.fastbuild2;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import se.robo.fastbuild2.info.ModInfo;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;
/*
* Don't let any access transformer stuff accidentally modify our classes. A list of package prefixes for FML to ignore
*/
@TransformerExclusions({ "se.robo.fastbuild2" })
@MCVersion(value = ModInfo.MCVERSION)
public class FastBuild2Core implements IFMLLoadingPlugin
{
    public static Logger log = LogManager.getLogger(ModInfo.MODID);

	@Override
	public String[] getASMTransformerClass() {
		return new String[] { "se.robo.fastbuild2.asm.ItemBlockTransformer" };
	}
	
	@Override
	public String getModContainerClass() {
		return null;
	}
	
	@Override
	public String getSetupClass() {
		return null;
	}
	
	@Override
	public void injectData(Map<String, Object> data) {
	}
	
	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}
