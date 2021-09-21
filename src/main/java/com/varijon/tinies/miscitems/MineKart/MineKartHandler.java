package com.varijon.tinies.miscitems.MineKart;


import java.awt.Color;
import java.util.ArrayList;
import java.util.UUID;

import io.netty.handler.codec.http.multipart.Attribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class MineKartHandler 
{
	MinecraftServer server;
	public MineKartHandler()
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
			if(!event.world.getWorldInfo().getWorldName().equals("world"))
			{
				return;
			}
			for(EntityPlayerMP targetPlayer : server.getPlayerList().getPlayers())
			{
				if(!targetPlayer.isRiding())
				{
					System.out.println("fail1");
					return;
				}
				Entity entityRiding = targetPlayer.getRidingEntity();
				if(entityRiding == null)
				{
					System.out.println("fail2");
					return;
				}
				if(!(entityRiding instanceof EntityMinecart))
				{
					System.out.println("fail3");
					return;
				}
				EntityMinecart rideCart = (EntityMinecart) entityRiding;
//				BlockPos pos = rideCart.getPosition();
//				pos.add(0, -1, 0);
//				if(event.world.getBlockState(pos).getBlock() == Blocks.CONCRETE)
//				{
					if(targetPlayer.isSprinting())
					{
						System.out.println("testye");
						float yaw = targetPlayer.rotationYaw;
						float pitch = targetPlayer.rotationPitch;
						float f = 0.5f;
						double motionX = (double)(-MathHelper.sin(yaw / 180.0F * (float)Math.PI) * MathHelper.cos(pitch / 180.0F * (float)Math.PI) * (f*2));
						double motionZ = (double)(MathHelper.cos(yaw / 180.0F * (float)Math.PI) * MathHelper.cos(pitch / 180.0F * (float)Math.PI) * (f*2));
						double motionY = (double)(-MathHelper.sin((pitch) / 180.0F * (float)Math.PI) * f);
						rideCart.addVelocity(motionX, motionY, motionZ);
						rideCart.rotationYaw = yaw;
					}
//				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public boolean getRainbowArmor(ItemStack itemToCheck)
	{
		if(itemToCheck != null)
		{
			if(itemToCheck.hasTagCompound())
			{
				NBTTagCompound nbt = itemToCheck.getTagCompound();
				if(nbt.hasKey("isRainbowArmor"))
				{
					return true;
				}
			}
		}
		return false;
	}
}
