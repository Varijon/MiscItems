package com.varijon.tinies.miscitems.rainbowarmor;


import java.awt.Color;
import java.util.ArrayList;
import java.util.UUID;

import io.netty.handler.codec.http.multipart.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class RainbowArmorHandler 
{
	MinecraftServer server;
	ArrayList<Color> colors;
	public RainbowArmorHandler()
	{
		server = FMLCommonHandler.instance().getMinecraftServerInstance();	
		colors = new ArrayList<Color>();
		for (int r=0; r<100; r++) colors.add(new Color(r*255/100,       255,         0));
		for (int g=100; g>0; g--) colors.add(new Color(      255, g*255/100,         0));
		for (int b=0; b<100; b++) colors.add(new Color(      255,         0, b*255/100));
		for (int r=100; r>0; r--) colors.add(new Color(r*255/100,         0,       255));
		for (int g=0; g<100; g++) colors.add(new Color(        0, g*255/100,       255));
		for (int b=100; b>0; b--) colors.add(new Color(        0,       255, b*255/100));
		                          colors.add(new Color(        0,       255,         0));
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
				int colorIndex = -1;
				for(ItemStack armorItem : targetPlayer.getArmorInventoryList())
				{
					if(getRainbowArmor(armorItem))
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
						if(!displayNBT.hasKey("colorIndex"))
						{
							displayNBT.setInteger("colorIndex", 0);
						}
						if(colorIndex == -1)
						{
							colorIndex = displayNBT.getInteger("colorIndex");
						}
						if(colorIndex < colors.size())
						{
							displayNBT.setInteger("color", colors.get(colorIndex).getRGB());
							displayNBT.setInteger("colorIndex", colorIndex + 1);						
						}
						else
						{
							colorIndex = 0;
							displayNBT.setInteger("color", colors.get(colorIndex).getRGB());
							displayNBT.setInteger("colorIndex", colorIndex + 1);	
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
