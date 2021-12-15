package me.cutenyami.coinsapi.plugin.utils;

import me.cutenyami.coinsapi.json.JsonFile;
import eu.byncing.sql.lib.Profile;
import me.cutenyami.coinsapi.plugin.CoinsAPIMain;

public class CoinsAPIConfig extends JsonFile {

    private int pools = 3;

    private Profile profile = new Profile("127.0.0.1", 3306, "root", "coins", null);

    public CoinsAPIConfig() {
        super(CoinsAPIMain.GSON, "plugins/CoinsAPI/mysql.json");

        if (!exists()) {
            create();
            append("pools", pools);
            append("profile", profile);
            save();
        }
        load();
        this.pools = get("pools", Integer.class);
        this.profile = get("profile", Profile.class);
    }

    public int getPools() {
        return pools;
    }

    public Profile getProfile() {
        return profile;
    }
}