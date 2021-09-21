package com.varijon.tinies.miscitems;


import com.varijon.tinies.miscitems.MineKart.MineKartHandler;
import com.varijon.tinies.miscitems.camoarmor.CamoArmorHandler;
import com.varijon.tinies.miscitems.custombeacon.CustomBeaconBreakHandler;
import com.varijon.tinies.miscitems.gmaxparticle.GmaxParticleHandler;
import com.varijon.tinies.miscitems.iceskates.IceSkateHandler;
import com.varijon.tinies.miscitems.rainbowarmor.RainbowArmorHandler;
import com.varijon.tinies.miscitems.snomnom.SnomNomHandler;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid="miscitems", version="1.0.3", acceptableRemoteVersions="*")
public class MiscItems
{
	public static String MODID = "modid";
	public static String VERSION = "version";

		
	@EventHandler
	public void preInit(FMLPreInitializationEvent e)
	{

	}
	
	@EventHandler
	public void init(FMLInitializationEvent e)
	{
		MinecraftForge.EVENT_BUS.register(new RainbowArmorHandler());
		MinecraftForge.EVENT_BUS.register(new CamoArmorHandler());
		MinecraftForge.EVENT_BUS.register(new GmaxParticleHandler());
//		MinecraftForge.EVENT_BUS.register(new SnomNomHandler());
		MinecraftForge.EVENT_BUS.register(new IceSkateHandler());
//		MinecraftForge.EVENT_BUS.register(new MineKartHandler());
		MinecraftForge.EVENT_BUS.register(new CustomBeaconBreakHandler());
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent e)
	{
	}

}