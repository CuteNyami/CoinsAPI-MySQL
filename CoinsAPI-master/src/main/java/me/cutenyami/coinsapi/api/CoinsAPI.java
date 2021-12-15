package me.cutenyami.coinsapi.api;

import eu.byncing.scheduler.Scheduler;
import eu.byncing.scheduler.pool.Pool;
import eu.byncing.sql.lib.DataTypes;
import eu.byncing.sql.lib.Profile;
import eu.byncing.sql.lib.SqlLib;
import eu.byncing.sql.lib.table.Table;
import eu.byncing.sql.lib.table.TableFetch;
import eu.byncing.sql.lib.table.TableUpdate;

import java.util.UUID;
import java.util.function.Consumer;

public class CoinsAPI {

    private final static CoinsAPI instance = new CoinsAPI();

    private final SqlLib lib = new SqlLib();

    private Pool pool;

    private Table table;

    public void connect(int pools, Profile profile) {
        lib.connect(profile);
        if (isConnected()) {
            pool = lib.getScheduler().pool(pools);
            table = lib.table("coins").setKeys("UUID", "NAME", "COINS").setTypes(DataTypes.STRING, DataTypes.STRING, DataTypes.INTEGER);
            table.createTable();
        }
    }

    public void disconnect() {
        if (!lib.isConnected()) lib.close();
    }

    public void process(Consumer<TableUpdate> consumer, boolean async) {
        if (!isConnected()) return;
        TableUpdate update = table.update();
        if (!async) consumer.accept(update);
        else pool.execute(() -> consumer.accept(update));
    }

    public boolean existsUser(UUID uniqueId) {
        if (!isConnected()) return false;
        TableFetch fetch = table.fetch();
        return fetch.find("UUID", uniqueId.toString());
    }

    public void addUser(UUID uniqueId, String name) {
        if (!isConnected()) return;
        pool.execute(() -> table.insert(uniqueId, name, 0));
    }

    public ICoinsAPIUser getUser(UUID uniqueId) {
        if (!isConnected()) return null;
        TableFetch fetch = table.fetch();
        fetch.setWhere("UUID").setWhereValues(uniqueId);
        return CoinsAPIUser.deserialize(this, fetch.single("UUID", "NAME", "COINS"));
    }

    public static CoinsAPI getInstance() {
        return instance;
    }

    public boolean isConnected() {
        return lib.isConnected();
    }

    public Scheduler getScheduler() {
        return lib.getScheduler();
    }
}