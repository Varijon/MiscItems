package com.varijon.tinies.miscitems.gmaxparticle;


import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumAlcremie;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class GmaxParticleHandler 
{
	MinecraftServer server;
	public GmaxParticleHandler()
	{
		server = FMLCommonHandler.instance().getMinecraftServerInstance();
	}

	@SubscribeEvent
	public void onWorldTick (WorldTickEvent event)
	{
		try
		{
			if(event.phase != Phase.END)
			{
				return;
			}
			if(server.getTickCounter() % 20 != 0)
			{
				return;
			}
			for(Entity entity : event.world.loadedEntityList)
			{
				if(entity instanceof EntityPixelmon)
				{
					EntityPixelmon pixelmon = (EntityPixelmon) entity;
					NBTTagCompound pixelmonNBT = pixelmon.writeToNBT(new NBTTagCompound());
					if(pixelmonNBT.getByte("GigantamaxFactor") == 1)
					{
						WorldServer world = (WorldServer) event.world;
						world.spawnParticle(EnumParticleTypes.REDSTONE, pixelmon.getPositionVector().x,pixelmon.getPositionVector().y, pixelmon.getPositionVector().z, 3, pixelmon.width/2, pixelmon.width/2, pixelmon.width/2, 0, new int[]{});
					}
					
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
}
