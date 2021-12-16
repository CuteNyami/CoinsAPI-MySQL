package me.cutenyami.coinsapi.api;

import java.util.Map;
import java.util.UUID;


public class CoinsUser implements ICoinsUser {

    private final CoinsAPI api;

    private final UUID uniqueId;
    private final String name;
    private int coins;

    public CoinsUser(CoinsAPI api, UUID uniqueId, String name, int bits) {
        this.api = api;
        this.uniqueId = uniqueId;
        this.name = name;
        this.coins = bits;
    }

    public static ICoinsUser deserialize(CoinsAPI api, Map<String, Object> values) {
        return new CoinsUser(
                api,
                UUID.fromString((String) values.get("UUID")),
                ((String) values.get("NAME")),
                ((Integer) values.get("COINS")));
    }

    @Override
    public Object[] serialize() {
        return new Object[]{uniqueId, name, coins};
    }

    @Override
    public void setCoins(int coins) {
        this.coins = coins;
    }

    @Override
    public void addCoins(int bits) {
        this.coins += bits;
    }

    @Override
    public void removeCoins(int bits) {
        this.coins -= bits;
    }

    @Override
    public void update(boolean async) {
        api.process(update -> {
            update.setWhere("UUID").setWhereValues(uniqueId);
            update.setKeys("NAME", "COINS").changes(name, coins);
        }, async);
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getCoins() {
        return coins;
    }
}