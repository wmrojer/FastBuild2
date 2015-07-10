package se.robo.fastbuild2.command;

import java.util.Arrays;
import java.util.List;

import se.robo.fastbuild2.config.ConfigHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class CommandFastbuild extends CommandBase {

	@Override
	public String getCommandName() {
		return "fastbuild";
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender icommandsender)
	{
		return true;
	}
	
	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "commands.fastbuild.usage";
	}

    @Override
    @SuppressWarnings("rawtypes")
	public List getCommandAliases()
    {
        return Arrays.asList(new String[] {"fb"});
    }

    @Override
	public void processCommand(ICommandSender sender, String[] cmd) {
		if ( cmd.length == 1) {
			if ( cmd[0].equalsIgnoreCase("show") ) {
				if (sender instanceof EntityPlayer) {
					EntityPlayer player = (EntityPlayer)sender;
					IChatComponent message = new ChatComponentText("MaxHorizontalBuild="+ConfigHandler.maxXZ+" MaxVerticalBuild="+ConfigHandler.maxY);
					player.addChatMessage(message);
					return;
				}
			}
		}
		if ( cmd.length == 2) {
			int val = parseIntBounded(sender, cmd[1], 1, 64);
			if ( cmd[0].equalsIgnoreCase("h") ) {
				ConfigHandler.maxXZ = val;
				ConfigHandler.setConfigurationProperty("MaxHorizontalBuild", val);
				return;
			} else if ( cmd[0].equalsIgnoreCase("v") ) {
				ConfigHandler.maxY = val;
				ConfigHandler.setConfigurationProperty("MaxVerticalBuild", val);
				return;
			}
		}
		throw new WrongUsageException("commands.fastbuild.usage", new Object[0]);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] cmd) {
		return cmd.length == 1 ? getListOfStringsMatchingLastWord(cmd, new String[] {"show","h","v"}) : null;
	}

}
