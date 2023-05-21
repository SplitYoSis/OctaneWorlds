package com.octanepvp.splityosis.octaneworlds.utils;

import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    public static final String LOG_PREFIX = "&8[&eOctaneWorlds&8]";

    public static void sendMessage(CommandSender to, String message){
        to.sendMessage(colorize(message));
    }

    public static void sendMessage(CommandSender to, List<String> message){
        message.forEach(s -> {
            sendMessage(to, s);
        });
    }

    public static void broadcast(String message){
        for (Player onlinePlayer : Bukkit.getOnlinePlayers())
            sendMessage(onlinePlayer, message);
        log(message);
    }

    public static void broadcast(List<String> message){
        for (Player onlinePlayer : Bukkit.getOnlinePlayers())
            sendMessage(onlinePlayer, message);
        log(message);
    }

    public static void log(String message){
        sendMessage(Bukkit.getServer().getConsoleSender(), LOG_PREFIX+" "+message);
    }

    public static void log(List<String> message){
        List<String> msg = new ArrayList<>(message);
        if (!msg.isEmpty())
            msg.set(0, LOG_PREFIX+" "+msg.get(0));
        sendMessage(Bukkit.getServer().getConsoleSender(), msg);
    }

    private static final Pattern HEX_PATTERN = Pattern.compile("&(#\\w{6})");
    public static String colorize(String str) {
        Matcher matcher = HEX_PATTERN.matcher(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', str));
        StringBuffer buffer = new StringBuffer();

        while (matcher.find())
            matcher.appendReplacement(buffer, net.md_5.bungee.api.ChatColor.of(matcher.group(1)).toString());

        return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
    }

    public static List<String> colorize(List<String> lst){
        if (lst == null) return null;
        List<String> newList = new ArrayList<>();
        lst.forEach(s -> {
            newList.add(colorize(s));
        });
        return newList;
    }

    public static ItemStack createItemStack(Material material, int amount, String name, List<String> lore){
        ItemStack itemStack = new ItemStack(material, amount);
        ItemMeta meta = itemStack.getItemMeta();
        if (name != null)
            meta.setDisplayName(colorize(name));
        meta.setLore(colorize(lore));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static ItemStack createItemStack(Material material, int amount, String name, String... lore){
        return createItemStack(material, amount, name, Arrays.asList(lore));
    }

    public static ItemStack createItemStack(Material material, int amount){
        return createItemStack(material, amount, null);
    }

    public static List<String> replaceList(List<String> lst, String from, String to){
        if (lst == null) return null;
        List<String> newList = new ArrayList<>();
        lst.forEach(s -> {
            newList.add(s.replace(from, to));
        });
        return newList;
    }

    public static ItemStack getItemFromSection(ConfigurationSection itemSection){
        if (itemSection == null) return null;
        String material = itemSection.getString("material");
        int amount = itemSection.getInt("amount");
        if (amount == 0) amount = 1;
        String customName = itemSection.getString("custom-name");
        List<String> lore = itemSection.getStringList("custom-lore");

        Map<Enchantment, Integer> enchants = new HashMap<>();
        ConfigurationSection enchantsSection = itemSection.getConfigurationSection("enchants");
        if (enchantsSection != null)
            for (String key : enchantsSection.getKeys(false))
                enchants.put(Enchantment.getByName(key), enchantsSection.getInt(key));

        ItemStack item = createItemStack(Material.getMaterial(Objects.requireNonNull(material)), amount, customName, lore);
        item.addUnsafeEnchantments(enchants);
        return item;
    }

    public static void setItemInConfig(ConfigurationSection section, String path, ItemStack item){
        String name = null;
        List<String> lore = null;

        if (item.getItemMeta() != null){
            if (item.getItemMeta().hasDisplayName())
                name = item.getItemMeta().getDisplayName();
            if (item.getItemMeta().hasLore())
                lore = item.getItemMeta().getLore();
        }

        section.set(path+".material", item.getType().name());
        section.set(path+".custom-name", name);
        section.set(path+".custom-lore", lore);

        Map<Enchantment, Integer> enchs = item.getEnchantments();
        for (Enchantment enchantment : enchs.keySet()) {
            section.set(path+".enchants."+enchantment.getName(), enchs.get(enchantment));
        }
    }

    public static String locationToString(Location location){
        String world = location.getWorld().getName();
        String x = String.valueOf(location.getBlockX());
        String y = String.valueOf(location.getBlockY());
        String z = String.valueOf(location.getBlockZ());
        return world + "_" + x + "_" + y + "_" + z;
    }

    public static Location locationFromString(String str){
        String[] arr = str.split("_");
        World world = Bukkit.getWorld(arr[0]);
        double x = Double.parseDouble(arr[1]);
        double y = Double.parseDouble(arr[2]);
        double z = Double.parseDouble(arr[3]);
        return new Location(world, x, y, z);
    }

    public static String getFormattedEntityName(EntityType entityType){
        StringBuilder builder = new StringBuilder();
        for (String s : entityType.name().toLowerCase().split("_"))
            builder.append(s.substring(0, 1).toUpperCase()).append(s.substring(1)).append(" ");
        return builder.substring(0, builder.length()-1);
    }
}
