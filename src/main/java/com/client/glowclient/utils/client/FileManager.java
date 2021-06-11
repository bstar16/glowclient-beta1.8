package com.client.glowclient.utils.client;

import java.nio.file.*;
import com.client.glowclient.utils.base.setting.builder.*;
import org.apache.commons.io.*;
import java.util.*;
import com.client.glowclient.utils.mod.mods.friends.*;
import java.io.*;

public class FileManager
{
    private static final FileManager INSTANCE;
    private final File baseDirectory;
    private final File settingsDirectory;

    public static FileManager getInstance() {
        return FileManager.INSTANCE;
    }

    private static File getWorkingDir() {
        return Paths.get("").toAbsolutePath().toFile();
    }

    private FileManager() {
        (this.baseDirectory = new File(getWorkingDir(), "config/glowclient")).mkdirs();
        this.settingsDirectory = new File(getWorkingDir(), "config/glowclient/settings");
        this.baseDirectory.mkdirs();
    }

    public void saveConfig() {
        final String config = SettingManager.encodeConfig();
        try {
            final File file = new File(this.settingsDirectory.getPath() + File.separatorChar + "Config.json");
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            IOUtils.write(config.getBytes(), new FileOutputStream(file));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveFriends() {
        try {
            final File file = new File(this.settingsDirectory, "Friends.json");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            FriendManager.getFriends();
            for (final Friend friend : FriendManager.friendsList) {
                out.write(friend.getName());
                out.write("\r\n");
            }
            out.close();
        }
        catch (Exception ex) {}
    }

    public void saveEnemies() {
        try {
            final File file = new File(this.settingsDirectory, "Enemies.json");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            EnemyManager.getEnemies();
            for (final Enemy enemy : EnemyManager.enemyList) {
                out.write(enemy.getName());
                out.write("\r\n");
            }
            out.close();
        }
        catch (Exception ex) {}
    }

    public void loadConfig() {
        String launcherJson = null;
        final File file = new File(this.settingsDirectory.getPath() + File.separatorChar + "Config.json");
        try {
            if (!file.exists()) {
                file.createNewFile();
                this.saveConfig();
                launcherJson = "{}";
            }
            else {
                launcherJson = IOUtils.toString(new FileInputStream(file));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        try {
            SettingManager.decodeConfig(launcherJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadFriends() {
        try {
            final File file = new File(this.settingsDirectory, "Friends.json");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                final String curLine = line.trim();
                final String name = curLine.split(":")[0];
                FriendManager.getFriends().addFriend(name);
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadEnemies() {
        try {
            final File file = new File(this.settingsDirectory, "Enemies.json");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                final String curLine = line.trim();
                final String name = curLine.split(":")[0];
                EnemyManager.getEnemies().addEnemy(name);
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveAll() {
        this.saveConfig();
        this.saveFriends();
        this.saveEnemies();
    }

    public void loadAll() {
        this.loadConfig();
        this.loadEnemies();
        this.loadFriends();
    }

    static {
        INSTANCE = new FileManager();
    }
}
