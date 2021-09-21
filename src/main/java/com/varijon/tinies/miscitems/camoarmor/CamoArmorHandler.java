package com.varijon.tinies.miscitems.camoarmor;


import java.awt.Color;
import java.util.ArrayList;
import java.util.UUID;

import io.netty.handler.codec.http.multipart.Attribute;
import net.minecraft.block.material.MapColor;
import net.minecraft.entity.ai.attributes.AttributeModifier;
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
import net.minecraft.world.World;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class CamoArmorHandler 
{
	MinecraftServer server;
	public CamoArmorHandler()
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
				for(ItemStack armorItem : targetPlayer.getArmorInventoryList())
				{
					if(getCamoArmor(armorItem))
					{
						NBTTagCompound nbt = armorItem.getTagCompound();
						if(armorItem.getItem() == Items.LEATHER_BOOTS)
						{
							if(!nbt.hasKey("AttributeModifiers"))
							{
								NBTTagList attributeModifiers = new NBTTagList();
								NBTTagCompound attribute = new NBTTagCompound();
								attribute.setString("AttributeName", "generic.movementSpeed");
								attribute.setString("Name", "Move Speed");
								attribute.setString("Slot", "feet");
								attribute.setInteger("Operation", 1);
								attribute.setDouble("Amount", 0.5d);
								attribute.setInteger("UUIDMost", 80);	
								attribute.setInteger("UUIDLeast", 10);	
								attributeModifiers.appendTag(attribute);
								
								attribute = new NBTTagCompound();
								attribute.setString("AttributeName", "generic.armor");
								attribute.setString("Name", "Armor");
								attribute.setString("Slot", "feet");
								attribute.setInteger("Operation", 0);
								attribute.setDouble("Amount", 1);
								attribute.setInteger("UUIDMost", 90);	
								attribute.setInteger("UUIDLeast", 20);						
									
								attributeModifiers.appendTag(attribute);
								nbt.setTag("AttributeModifiers", attributeModifiers);
							}
						}
						if(!nbt.hasKey("display"))
						{
							nbt.setTag("display", new NBTTagCompound());
						}
						NBTTagCompound displayNBT = nbt.getCompoundTag("display");
						
						BlockPos pos1 = targetPlayer.getPosition().add(0, -1, 0);
						BlockPos pos2 = targetPlayer.getPosition();
						World world = targetPlayer.getServerWorld();
						if(world.getBlockState(pos2).getBlock() != Blocks.AIR)
						{
							MapColor color = world.getBlockState(pos2).getMapColor(world, pos2);
							if(color.colorValue != 0)
							{
								displayNBT.setInteger("color", color.colorValue);									
							}
						}
						else
						{
							if(world.getBlockState(pos1).getBlock() != Blocks.AIR)
							{
								MapColor color = world.getBlockState(pos1).getMapColor(world, pos1);
								if(color.colorValue != 0)
								{
									displayNBT.setInteger("color", color.colorValue);
								}
							}	
						}
						
						nbt.setTag("display", displayNBT);
						armorItem.setTagCompound(nbt);
					}
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public boolean getCamoArmor(ItemStack itemToCheck)
	{
		if(itemToCheck != null)
		{
			if(itemToCheck.hasTagCompound())
			{
				NBTTagCompound nbt = itemToCheck.getTagCompound();
				if(nbt.hasKey("isCamoArmor"))
				{
					return true;
				}
			}
		}
		return false;
	}
}
