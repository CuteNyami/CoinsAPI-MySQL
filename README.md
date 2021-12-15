# » CoinsAPI
A MySQL based Coins API
### Gradle
````gradle
repositories {
    maven { url('SOON') }
}

dependencies {
    implementation('SOON')
}
````
&nbsp;
&nbsp;
### » Spigot Example
````java
@EventHandler
public void handle(PlayerJoinEvent event) {
    Player player = event.getPlayer();

    CoinsAPI instance = CoinsAPI.getInstance();
    if(instance.existsUser(player.getUniqueId())) return;
    ICoinsAPIUser user = instance.getUser(player.getUniqueId());

    user.addCoins(50);
    //Update Coins in a pool
    user.update(true);

    player.sendMessage("Your Coins: " + user.getCoins());
}
````
&nbsp;
&nbsp;


### » Proxy Example
You can use the API on a BungeeCord Proxy too!

````java
//You can of course also do the same with velocity it is important that you always connect the BitsAPI to the database!
@Override
public void onEnable() {
    CoinsAPI.getInstance().connect(3, new Profile("127.0.0.1", 3306, "root", "coins", null));
}
@Override
public void onDisable() {
    BitsAPI.getInstance().disconnect();
}
````
&nbsp;
&nbsp;

The player is registered in the database because it does not exist, but the spigot does it automatically!
````java
@EventHandler
public void handle(PostLoginEvent event) {
    ProxiedPlayer player = event.getPlayer();
    CoinsAPI instance = CoinsAPI.getInstance();
    if(!instance.existsUser(player.getUniqueId())) {
        instance.addUser(player.getUniqueId(), player.getName());
    }
}
````
