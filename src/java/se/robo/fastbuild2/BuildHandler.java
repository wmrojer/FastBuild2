package se.robo.fastbuild2;

import java.util.HashMap;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import se.robo.fastbuild2.config.ConfigHandler;

public class BuildHandler {

	public static KeyBinding triggerKey;
	
	private static HashMap<Thread, BuildHandler> lock = new HashMap<Thread, BuildHandler>();
	private int built = 0;
	private int x, y, z;

	public static final boolean canStart() {
		if(lock.containsKey(Thread.currentThread())) {
			return false;
		}
		/* If we already started doing it in one thread, we HAVE to do it in the other too! */
		if(!lock.isEmpty()) {
			return true;
		}
		return triggerKey.getIsKeyPressed();
	}

	// Dummy method just to make the "new" ItemBlock compile.
	public static boolean build(se.robo.fastbuild2.items.ItemBlock itemBlock, ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		return false;
	}
	
	public static boolean build(ItemBlock itemBlock, ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		boolean ret = false;
		try {
			BuildHandler fb = start();
			fb.reset(side);
			while( itemBlock.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ) && fb.canBuild()) {
				x += fb.x;
				y += fb.y;
				z += fb.z;
				++fb.built;
				ret = true;
			}
		} catch (Exception e) {
			FastBuild2.log.error("Unexpected error! " + e.getMessage());
		} finally {
			BuildHandler.stop();
		}
		return ret;
	}

	private static final BuildHandler start() {
		BuildHandler fb = new BuildHandler();
		lock.put(Thread.currentThread(), fb);
		return fb;
	}

	private static final void stop() {
		lock.remove(Thread.currentThread());
	}

	private final boolean canBuild() {
		if(y != 0) {
			return built < ConfigHandler.maxY;
		}
		return built < ConfigHandler.maxXZ;
	}

	private final void reset(int rot) {
		x = y = z = 0;
		switch (rot) {
			case 0:
				--y;
				break;
			case 1:
				++y;
				break;
			case 2:
				--z;
				break;
			case 3:
				++z;
				break;
			case 4:
				--x;
				break;
			case 5:
				++x;
				break;
		}
		built = 1;
	}
	
}
