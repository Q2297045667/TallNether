# TallNether
## About
基于[TallNether](https://github.com/OtakuMegane/TallNether)
Spigot和Paper的一个插件，将原版下界的高度增加了一倍，并允许对生成器进行一些自定义。

## Usage
官方页面、下载和说明可在此处找到: [TallNether（原作者）](https://www.spigotmc.org/resources/tallnether.22561/)

## Compiling
要自己编译插件，您需要:
 - Maven
 - The Spigot jars for each NMS revision of Minecraft 1.15 - 1.18
 - The Paper jars for each NMS revision of Minecraft 1.12 and 1.15 - 1.18
 
Any necessary dependencies are specified in the pom.xml for each module. Spigot/CraftBukkit APIs will be downloaded automatically when building the module but the CraftBukkit, Spigot and Paper jars will need to be compiled and added to your local Maven repo.
 
Once dependencies are in place the plugin can be built by doing `mvn install` on the parent project. The plugin will be found in the `TallNether/target/` directory.
