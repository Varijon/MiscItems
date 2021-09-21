package com.varijon.tinies.miscitems.snomnom;


import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumAlcremie;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class SnomNomHandler 
{
	MinecraftServer server;
	public SnomNomHandler()
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
			for(Entity entity : event.world.loadedEntityList)
			{
				if(entity instanceof EntityPixelmon)
				{
					EntityPixelmon pixelmon = (EntityPixelmon) entity;
					if(pixelmon.getSpecies() == EnumSpecies.Snom)
					{
						if(event.world.getBlockState(pixelmon.getPosition()).getBlock() == Blocks.SNOW_LAYER)
						{
							event.world.destroyBlock(pixelmon.getPosition(), false);
							event.world.playSound(null, pixelmon.getPosition(), SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.BLOCKS, 1f, 1f);
							event.world.spawnParticle(EnumParticleTypes.SNOW_SHOVEL, pixelmon.getPositionVector().x,pixelmon.getPositionVector().y, pixelmon.getPositionVector().z, 5, 0, 0, new int[]{});
						}
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
