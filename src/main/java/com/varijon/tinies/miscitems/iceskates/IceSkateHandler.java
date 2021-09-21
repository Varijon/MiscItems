package com.varijon.tinies.miscitems.iceskates;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class IceSkateHandler 
{
	int counter = 0;
	MinecraftServer server;
	
	public IceSkateHandler()
	{
		server = FMLCommonHandler.instance().getMinecraftServerInstance();
	}
	
	@SubscribeEvent
	public void onWorldTick (WorldTickEvent event)
	{
		try
		{
			if(counter == 20)
			{
				for(EntityPlayerMP targetPlayer : server.getPlayerList().getPlayers())
				{
					if(CheckForIceSkates(targetPlayer.getArmorInventoryList()))
					{
						BlockPos pos = targetPlayer.getPosition().add(0, -1, 0);
						if(targetPlayer.getServerWorld().getBlockState(pos).getBlock() == Blocks.ICE || targetPlayer.getServerWorld().getBlockState(pos).getBlock() == Blocks.PACKED_ICE || targetPlayer.getServerWorld().getBlockState(pos).getBlock() == Blocks.FROSTED_ICE)
						{
							targetPlayer.addPotionEffect(new PotionEffect(Potion.getPotionById(1), 30, 9));
						}
					}
				}
				counter = 0;
				return;
			}
			counter++;
		}
		catch(Exception ex)
		{
			
		}
	}

	public boolean CheckForIceSkates(Iterable<ItemStack> itemList)
	{
		if(itemList != null)
		{
			for (ItemStack item : itemList) 
			{
				if(item != null)
				{
					if(item.hasTagCompound())
					{
						NBTTagCompound nbt = item.getTagCompound();
						if(nbt.hasKey("isIceSkate"))
						{
							return true;
						}
					}
				}
			}
		}
		return false;
	}
}
