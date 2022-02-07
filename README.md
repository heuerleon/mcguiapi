![GitHub release (latest by date)](https://img.shields.io:/github/v/release/heuerleon/mcguiapi)
[![](https://jitpack.io/v/heuerleon/mcguiapi.svg)](https://jitpack.io/#heuerleon/mcguiapi)
![GitHub](https://img.shields.io:/github/license/heuerleon/mcguiapi)
![GitHub last commit](https://img.shields.io:/github/last-commit/heuerleon/mcguiapi)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=heuerleon_mcguiapi&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=heuerleon_mcguiapi)
# MC GUI API
MC GUI API is an API for creating graphical user interfaces using inventories in minecraft.
- [Docs](https://heuerleon.github.io/mcguiapi/)

## Adding MC GUI API to your dependencies
If you use Maven:
```xml
<repositories>
  <repository>
    <id>jitpack</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>com.github.heuerleon</groupId>
    <artifactId>mcguiapi</artifactId>
    <version>v1.0.1</version>
  </dependency>
</dependencies>
```

If you use Gradle:
```groovy
repositories {
  maven {
    url 'https://jitpack.io'
  }
}

dependencies {
  implementation 'com.github.heuerleon:mcguiapi:v1.0.1'
}
```

Do not forget to shadow the dependency into your jar. This is not a standalone Minecraft plugin and you will get errors otherwise.

## Starting to use the API
You first need to create a GUI Factory.
```java
GUIFactory guiFactory = new GUIFactory(this); // replace 'this' with your Java Plugin instance if you don't call this in your main.
```

Next, you need to create a new GUI.
```java
GUI gui = guiFactory.createGUI(rows, title); // You can also leave out title if you don't wish to set a custom title.
```

The reason why you need to use the GUIFactory is because every GUI has their own click listener. Bukkit requires a Java Plugin instance for registering a listener, which will be your plugin. The GUIFactory automatically handles this for you.

Now you can start designing your GUI by adding some items and adding some click actions.
```java
gui.set(row, column, item, (event) -> {
  // insert anything you'd like to do when the item is clicked
});
// you can use the ItemBuilder class, which is included in the API, for adding items
```

Once you have finished, you can show the GUI to a player.
```java
gui.show(player);
```

These are the basics of creating GUIs. For more information, read through the [Javadoc](https://heuerleon.github.io/mcguiapi/).
