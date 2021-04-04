# Forge Hammers
Hammers is a **Minecraft 1.7.10 (+ 1.16. in the future)** modification that adds special
functionality to every miner, that wants to break large areas at once.
This modification adds new ores, machines and items to a players daily life and world.
Although it maintains high efficiency, performance and quality it keeps improving the players game experience.
I want to mention, that this project is not the best I produced, as it is just for fun but still fully documented
with JavaDocs and made with quality in mind. So please do not mind mispellings or that the code looks a bit noisy.
This modification is compatible with the most modifications for Minecraft, as it
is made with independence in mind, without any library required, except Forge.<br><br>
<a href="https://github.com/mindcubr/Forge-Hammers/releases/tag/1.7.10-3.0.3a"><img src="https://img.shields.io/badge/-download_latest-blue?style=for-the-badge&logo=flipboard"></img></a>
<a href="http://mindev.biz/projects/Forge-Hammers/docs/"><img src="https://img.shields.io/badge/-javadocs-red?style=for-the-badge&logo=java"></img></a><br>
*This modification is in alpha state and is made in a 10h challenge context, it is not the highest*<br>
*quality but made with performance in mind. The code is pretty basic and does not offer a high reusability.*

<br><br>
![image](https://user-images.githubusercontent.com/47287352/113494211-92ba3d80-94e6-11eb-9dfc-5dad47879f8d.png)

## Hammers
### What is a "Hammer"?<br>
<img src="https://i.gyazo.com/e80bda161c94f4b1be304843cb9c10d7.gif" align="right" width="300"></img>
A hammer is a craftable item within Minecraft, that is behaving like a pickaxe itself<br>
but still adds functionality to the gameplay, as it destroys blocks nearby the breaking center<br>
at a given radius, that is determined by the **Level** of the hammer.<br>
At the moment, there are 6 tool materials, that a hammer can exist of:<br>
> *Wood*, *Stone*, *Gold*, *Iron*, *Diamond* and *Unbreaking*.<br>

These tool materials, except unbreaking, are representing the default pickaxe<br>
behaviour related to their material. In other words, a *wooden hammer* is as effective<br>
as a wooden pickaxe, in terms of the amount of time it takes to destory the block in the center<br>
and effectiveness against certain blocks and ores. So you cannot destroy diamond ores<br>
connected to the center of the destruction nor the centered block itself.<br>
In terms of durability, every hammer has its own. The higher the level, the higher the<br>
maximum capacity of blocks destroyable and therefore its maximum damage.<br>
The better the tool material, the higher the maximum life span of this hammer and therefore<br>
a higher maximum damage capability.<br>

### Hammer Recipes
You can craft the named tool materials as follows. Please read the [#Leveling](https://github.com/mindcubr/Forge-Hammers/blob/master/README.md#Leveling) part to understand<br>
how leveling works and [#Unbreaking Ore](https://github.com/mindcubr/Forge-Hammers/blob/master/README.md#unbreaking-ore) and [#Party The Unbreaking](https://github.com/mindcubr/Forge-Hammers/blob/master/README.md#party-the-unbreaking) to understand the Unbreaking Ore<br>
and its ingots. Remember, that the default Level of all hammers is the first (Level I), when crafted.<br>
If you do not understand the materials or items used, just read further within this Readme and come back.<br><br>
![wooden](https://user-images.githubusercontent.com/47287352/112946645-14e8e180-9136-11eb-9449-2d85e9696475.png)
![stone](https://user-images.githubusercontent.com/47287352/112946652-161a0e80-9136-11eb-9ed2-86188520bd41.png)
![iron](https://user-images.githubusercontent.com/47287352/112946657-174b3b80-9136-11eb-95a4-7e7ad1938b01.png)
![golden](https://user-images.githubusercontent.com/47287352/112946661-187c6880-9136-11eb-8517-5ed10850a343.png)
![diamond](https://user-images.githubusercontent.com/47287352/112946670-1adec280-9136-11eb-8246-ad176003c5a9.png)
![unbreaking](https://user-images.githubusercontent.com/47287352/112946679-1b775900-9136-11eb-9644-df9fb41efa53.png)


## Unbreaking Ore
As mentioned there are currently six tool materials, whereas one of them is the *Unbreaking* material.<br>
It breaks blocks faster than a diamond pickaxe and is effective against every block material,<br>
except Bedrock. Of course. A unbreaking hammer is crafted with *Unbreaking Ingots* that are the<br>
result of a smelting recipe from the ore itself. The *Unbreaking Ore* is really rare, rarer than<br>

diamonds, but can be found much easier in the *Nether* and *End* dimension, but spawns in every dimension.<br>
By default it just spawns at heights of 5 to 20 but may vary depending on a funky randomization algorithm<br>
and the dimension the chunk is generated in.<br>
<br><br>
![gif](https://i.gyazo.com/e67bcde5769979664f784a3a561ba7b8.gif)
<br><br>
## Party the Unbreaking
![image](https://user-images.githubusercontent.com/47287352/112860495-25f10e80-90b4-11eb-9c8b-f52c3cb96871.png)
<br><br>
The Unbreaking Ingot is really strong and can be used to make any breakable item unbreakable. Forever.<br>
This is why the Unbreaking Ore is really rare and just gives you one ingot after cooking it for so long!<br>
But however, the *Unbreaking Ore* might be one of the most powerful ores you can find.<br>
The Unbreaking Ingot works with **every** breakable item, even from third party modifications.

# Leveling
The level system of this modification is pretty simple but still very efficient.<br>
At the moment there are 3 levels existing, that will take a part of the declaration<br>
of a hammers life span, so maximum durability or so called "damage".<br>
This is not the only thing a Level can change for a hammer.<br>
The higher the level, the more blocks it destroys nearby a destroyed block center.<br>
There is a basic mathematical expression to calculate the cubic metres destroyed around<br>
a center using leveling:
```math
Cubic metres = 3 + (Level - 1)
```
> The higher the level, the more durability a hammer has and the more blocks it destroys
> around any block center, as soon as the centered block is destroyed.

## Leveling on Hammers
<img src="https://i.gyazo.com/df513a002c4c456b59ae830e009f8303.gif" width="600"></img>
<br><br>
As you can see, applying a **Level** to a hammer is pretty simple.<br>
The order of the different required recipe items does not matter.<br>
You can apply any level to a hammer, except the level it has currently equipped,<br>
sounds logic, doesn't it?<br>

## Level I
This is the default base Level a hammer has. If no upgrade is applied to a hammer,<br>
the default Level is set to this one.<br>
Cubic metres destroyed nearby: **3 m³**<br><br>
![image](https://user-images.githubusercontent.com/47287352/112857096-cc3b1500-90b0-11eb-9f8a-8c49f4749310.png)
<br><br>
## Level II
This level is an upgrade to `Level I`.<br>
Cubic metres destroyed nearby: **4 m³**<br><br>
![image](https://user-images.githubusercontent.com/47287352/112857155-d9580400-90b0-11eb-8841-ec3b00502a2d.png)
<br><br>
## Level III
This level is an upgrade to `Level II`.<br>
This is the current final level possible to reach via crafting.<br>
Cubic metres destroyed nearby: **5 m³**<br><br>
![image](https://user-images.githubusercontent.com/47287352/112857220-ebd23d80-90b0-11eb-922a-f08815959d2f.png)


<br><br>
# Unbreaking Tools
Since *3.0.0-alpha* there are new tools added to the game, which you can use infinitely.<br>
Those tools are almost behaving the same as their diamond materials, but in some<br>
there is a difference, which is listed with the following crafting recipes:
<br><br>
## Tools
![unbreaking_shovel](https://user-images.githubusercontent.com/47287352/113337426-0e21c080-9328-11eb-8c28-a18c4816f2e5.png)
![unbreaking_axe](https://user-images.githubusercontent.com/47287352/113337429-0eba5700-9328-11eb-945a-1f7887e11030.png)
![unbreaking_hoe](https://user-images.githubusercontent.com/47287352/113337431-0f52ed80-9328-11eb-8946-2ad906d96ceb.png)
![unbreaking_pickaxe](https://user-images.githubusercontent.com/47287352/113337437-0f52ed80-9328-11eb-9eef-4bcd0f2c1138.png)

## Unbreaking Sword
![unbreaking_sword](https://user-images.githubusercontent.com/47287352/113337438-0f52ed80-9328-11eb-90ad-e2d08093142c.png)<br><br>
The unbreaking sword has an attack damage of **+8** on an entity and shares<br>
same attributes as a diamond sword, except the durability and damage against entities.<br>

