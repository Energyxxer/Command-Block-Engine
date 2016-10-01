package com.energyxxer.util;

import java.io.File;
import java.util.ArrayList;

/**
 * A long and painfully written list of all things Minecraft.
 * */
public class MinecraftConstants {
	public static ArrayList<String> entities = new ArrayList<String>();
	public static ArrayList<String> entities_new = new ArrayList<String>();
	
	static {
		entities.add("Player");
		entities.add("Bat");
		entities.add("Chicken");
		entities.add("Cow");
		entities.add("MushroomCow");
		entities.add("Pig");
		entities.add("Rabbit");
		entities.add("Sheep");
		entities.add("Squid");
		entities.add("Villager");
		entities.add("Enderman");
		entities.add("PolarBear");
		entities.add("Spider");
		entities.add("CaveSpider");
		entities.add("PigZombie");
		entities.add("Blaze");
		entities.add("Creeper");
		entities.add("Endermite");
		entities.add("Ghast");
		entities.add("Guardian");
		entities.add("LavaSlime");
		entities.add("Shulker");
		entities.add("Silverfish");
		entities.add("Skeleton");
		entities.add("Slime");
		entities.add("Witch");
		entities.add("Zombie");
		entities.add("EntityHorse");
		entities.add("Ozelot");
		entities.add("Wolf");
		entities.add("VillagerGolem");
		entities.add("SnowMan");
		entities.add("EnderDragon");
		entities.add("WitherBoss");
		entities.add("Giant");
		entities.add("FallingSand");
		entities.add("PrimedTnt");
		entities.add("Boat");
		entities.add("MinecartRideable");
		entities.add("MinecartChest");
		entities.add("MinecartCommandBlock");
		entities.add("MinecartFurnace");
		entities.add("MinecartHopper");
		entities.add("MinecartTnt");
		entities.add("MinecartSpawner");
		entities.add("SmallFireball");
		entities.add("DragonFireball");
		entities.add("Fireball");
		entities.add("SpectralArrow");
		entities.add("Arrow");
		entities.add("ThrownExpBottle");
		entities.add("ThrownEgg");
		entities.add("ThrownEnderpearl");
		entities.add("EyeOfEnderSignal");
		entities.add("Snowball");
		entities.add("ThrownPotion");
		entities.add("WitherSkull");
		entities.add("ArmorStand");
		entities.add("EnderCrystal");
		entities.add("ItemFrame");
		entities.add("LeashKnot");
		entities.add("Painting");
		entities.add("XPOrb");
		entities.add("Item");
		entities.add("LightningBolt");
		entities.add("FireworksRocketEntity");
		entities.add("AreaEffectCloud");
	}
	
	static {
		entities_new.add("player");
		entities_new.add("bat");
		entities_new.add("chicken");
		entities_new.add("cow");
		entities_new.add("mooshroom");
		entities_new.add("pig");
		entities_new.add("rabbit");
		entities_new.add("sheep");
		entities_new.add("squid");
		entities_new.add("villager");
		entities_new.add("enderman");
		entities_new.add("polar_bear");
		entities_new.add("spider");
		entities_new.add("cave_spider");
		entities_new.add("zombie_pigman");
		entities_new.add("blaze");
		entities_new.add("creeper");
		entities_new.add("endermite");
		entities_new.add("ghast");
		entities_new.add("guardian");
		entities_new.add("elder_guardian");
		entities_new.add("magma_cube");
		entities_new.add("shulker");
		entities_new.add("silverfish");
		entities_new.add("skeleton");
		entities_new.add("stray");
		entities_new.add("wither_skeleton");
		entities_new.add("slime");
		entities_new.add("witch");
		entities_new.add("zombie");
		entities_new.add("husk");
		entities_new.add("zombie_villager");
		entities_new.add("horse");
		entities_new.add("donkey");
		entities_new.add("mule");
		entities_new.add("zombie_horse");
		entities_new.add("skeleton_horse");
		entities_new.add("ocelot");
		entities_new.add("wolf");
		entities_new.add("villager_golem");
		entities_new.add("snowman");
		entities_new.add("ender_dragon");
		entities_new.add("wither");
		entities_new.add("giant");
		entities_new.add("falling_block");
		entities_new.add("tnt");
		entities_new.add("boat");
		entities_new.add("minecart");
		entities_new.add("chest_minecart");
		entities_new.add("commandblock_minecart");
		entities_new.add("furnace_minecart");
		entities_new.add("hopper_minecart");
		entities_new.add("tnt_minecart");
		entities_new.add("spawner_minecart");
		entities_new.add("small_fireball");
		entities_new.add("dragon_fireball");
		entities_new.add("fireball");
		entities_new.add("spectral_arrow");
		entities_new.add("arrow");
		entities_new.add("xp_bottle");
		entities_new.add("egg");
		entities_new.add("ender_pearl");
		entities_new.add("eye_of_ender_signal");
		entities_new.add("snowball");
		entities_new.add("shulker_bullet");
		entities_new.add("potion");
		entities_new.add("wither_skull");
		entities_new.add("armor_stand");
		entities_new.add("ender_crystal");
		entities_new.add("item_frame");
		entities_new.add("leash_knot");
		entities_new.add("painting");
		entities_new.add("xp_orb");
		entities_new.add("item");
		entities_new.add("lightning_bolt");
		entities_new.add("fireworks_rocket");
		entities_new.add("area_effect_cloud");
	}
	
	public static String getMinecraftDir() {
		String workingDirectory;
		//here, we assign the name of the OS, according to Java, to a variable...
		String OS = (System.getProperty("os.name")).toUpperCase();
		//to determine what the workingDirectory is.
		//if it is some version of Windows
		if (OS.contains("WIN"))
		{
		    //it is simply the location of the "AppData" folder
		    workingDirectory = System.getenv("AppData");
		}
		//Otherwise, we assume Linux or Mac
		else
		{
		    //in either case, we would start in the user's home directory
		    workingDirectory = System.getProperty("user.home");
		    //if we are on a Mac, we are not done, we look for "Application Support"
		    workingDirectory += "/Library/Application Support";
		}
		
		workingDirectory += File.separator + ".minecraft";
		
		return workingDirectory;
	}
}
