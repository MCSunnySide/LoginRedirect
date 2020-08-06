package com.mcsunnyside.loginredirect;

import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public final class LoginRedirect extends Plugin implements Listener {
    private Configuration configuration;
    private final ConfigurationProvider configurationProvider = ConfigurationProvider.getProvider(YamlConfiguration.class);
    private final List<RedirectRule> redirectRuleList = new ArrayList<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        readConfig();
        redirectRuleList.clear(); //RESET
        Configuration rules = configuration.getSection("rules");
        rules.getKeys().forEach((key) -> {
            Configuration rule = rules.getSection(key);
            String from = rule.getString("from");
            String to = rule.getString("to");
            List<String> host = rule.getStringList("host");
            if (from == null || to == null || host.isEmpty()) {
                getLogger().warning("Cannot load the rule: " + key);
                return;
            }
            redirectRuleList.add(new RedirectRule(from, to, host));
            getLogger().info("Successfully loaded rule: " + key);
        });
        getProxy().getPluginManager().registerListener(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void readConfig() {
        getDataFolder().mkdirs();
        if (!new File(getDataFolder(), "config.yml").exists()) {
            try {
                Files.copy(getResourceAsStream("config.yml"), new File(getDataFolder(), "config.yml").toPath());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        try {
            configuration = configurationProvider.load(new File(getDataFolder(), "config.yml"));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void saveConfig() {
        try {
            configurationProvider.save(configuration, new File(getDataFolder(), "config.yml"));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @EventHandler
    public void onLogin(PostLoginEvent e) {
        for (RedirectRule rule : redirectRuleList) {
            if (rule.matches("#", e.getPlayer().getPendingConnection().getVirtualHost().getHostName())) {
                e.getPlayer().connect(getProxy().getServerInfo(rule.getTo()));
                getLogger().info("Hook the player " + e.getPlayer().getName() + " to the server " + rule.getTo());
                return;
            }
        }
    }

    @EventHandler
    public void onSwitch(ServerSwitchEvent e) {
        String from;
        if (e.getFrom() == null) {
            from = "#";
        } else {
            from = e.getFrom().getName();
        }

        for (RedirectRule rule : redirectRuleList) {
            if (rule.matches(from, e.getPlayer().getPendingConnection().getVirtualHost().getHostName())) {
                e.getPlayer().connect(getProxy().getServerInfo(rule.getTo()));
                getLogger().info("Transfer/Hook the player " + e.getPlayer().getName() + " to the server " + rule.getTo());
                return;
            }
        }
    }
}
