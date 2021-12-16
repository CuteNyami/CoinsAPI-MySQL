package me.cutenyami.coinsapi.api;

import java.util.UUID;

public interface ICoinsUser {

    Object[] serialize();

    void setCoins(int coins);

    void addCoins(int bits);

    void removeCoins(int bits);

    void update(boolean async);

    UUID getUniqueId();

    String getName();

    int getCoins();
}