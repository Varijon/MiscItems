package com.varijon.tinies.miscitems.custombeacon;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CustomBeaconBreakHandler 
{
	ArrayList<TileEntityBeacon> lstBeaconTile;
	public CustomBeaconBreakHandler()
	{
		lstBeaconTile = new ArrayList<TileEntityBeacon>();
	}
	@SubscribeEvent
	public void onInteract(RightClickBlock event)
	{
		if(event.getEntityPlayer() == null)
		{
			return;
		}
		if(event.getEntityPlayer().isCreative())
		{
			return;
		}
		if(event.getWorld().getBlockState(event.getPos()).getBlock() == Blocks.BEACON)
		{
			TileEntityBeacon beaconTile = (TileEntityBeacon) event.getWorld().getTileEntity(event.getPos());
			if(beaconTile == null)
			{
				return;
			}
			if(customBeaconEffect(beaconTile))
			{
				event.getEntityPlayer().sendMessage(new TextComponentString(TextFormatting.RED + "You cannot edit custom beacon effects!"));
				event.setCanceled(true);
			}
		}
	}
	
	
	@SubscribeEvent
	public void onBreak(BreakEvent event)
	{
		if(event.isCanceled())
		{
			return;
		}
		if(event.getPlayer() == null)
		{
			return;
		}
		if(event.getPlayer().isCreative())
		{
			return;
		}
		if(event.getState().getBlock() == Blocks.BEACON)
		{
			TileEntityBeacon beaconTile = (TileEntityBeacon) event.getWorld().getTileEntity(event.getPos());
			lstBeaconTile.add(beaconTile);
			beaconTile.invalidate();
		}
	}
	@SubscribeEvent
	public void onHarvest(HarvestDropsEvent event)
	{
		if(event.isCanceled())
		{
			return;
		}
		if(event.getHarvester() == null)
		{
			return;
		}
		if(!(event.getHarvester() instanceof EntityPlayerMP))
		{
			return;
		}
		if(event.getDrops() == null)
		{
			return;
		}

		if(event.getDrops().isEmpty())
		{
			return;
		}
		if(event.getDrops().get(0) == null)
		{
			return;
		}
		//beacons dropped twice don't seem to get here, new location maybe be broken tile entity.. invalidate?
		if(event.getDrops().get(0).getItem() == Item.getItemFromBlock(Blocks.BEACON))
		{
			TileEntityBeacon beaconTile = getBeaconTile(event.getPos());
			if(beaconTile == null)
			{
				return;
			}
			NBTTagCompound tileData = beaconTile.writeToNBT(new NBTTagCompound());
			if(customBeaconEffect(beaconTile))
			{	
				ItemStack beacon = new ItemStack(Blocks.BEACON);
				NBTTagCompound beaconData = new NBTTagCompound();
				beaconData.setTag("BlockEntityTag", new NBTTagCompound());
				beaconData.getCompoundTag("BlockEntityTag").setInteger("Primary", tileData.getInteger("Primary"));

				beaconData.setTag("display", new NBTTagCompound());
				beaconData.getCompoundTag("display").setString("Name", TextFormatting.RESET + StringUtils.capitalize(Potion.getPotionById(tileData.getInteger("Primary")).getRegistryName().getResourcePath().replace("_", " ")) + " " + beacon.getDisplayName());	
				NBTTagList loreList = new NBTTagList();
				loreList.appendTag(new NBTTagString(TextFormatting.GRAY + "Effect cannot be edited."));
				beaconData.getCompoundTag("display").setTag("Lore", loreList);
				beacon.setTagCompound(beaconData);
				event.getDrops().clear();
				event.getDrops().add(beacon);
			}
			lstBeaconTile.remove(beaconTile);
			
		}
	}
	
	public boolean customBeaconEffect(TileEntityBeacon beacon)
	{
		NBTTagCompound tileData = beacon.writeToNBT(new NBTTagCompound());
		int primary = tileData.getInteger("Primary");
		if(primary != -1)
		{
			if(primary == 1 || primary == 3 || primary == 5 || primary == 8 || primary == 10 || primary == 11)
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		else
		{
			return false;
		}
	}
	
	public TileEntityBeacon getBeaconTile(BlockPos loc)
	{
		for(TileEntityBeacon beaconTile : lstBeaconTile)
		{
			if(beaconTile.getPos().getX() == loc.getX() && beaconTile.getPos().getY() == loc.getY() && beaconTile.getPos().getZ() == loc.getZ())
			{
				return beaconTile;
			}
		}
		return null;
	}
}
