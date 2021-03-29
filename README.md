# Forge-Hammers
Hammers is a **Minecraft 1.7.10 - 1.14.0** modification that adds special functionality to every miner, that wants<br>
to break large areas at once. This modification adds new ores, machines and items to a players<br>
daily life and world. Although it maintains high efficiency, performance and quality it keeps<br>
improving the players game experience.<br><br>
![image](https://user-images.githubusercontent.com/47287352/112851626-744ddf80-90ab-11eb-94e2-c7b6efcfde0e.png)


## Hammers
### What is a "Hammer"?<br>
A hammer is an craftable item within Minecraft, that is behaving like a pickaxe itself<br>
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

## Leveling
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

### Level I
This is the default base Level a hammer has. If no upgrade is applied to a hammer,<br>
the default Level is set to this one.<br>
Cubic metres destroyed nearby: **3 m³**<br><br>
![image](https://user-images.githubusercontent.com/47287352/112857096-cc3b1500-90b0-11eb-9f8a-8c49f4749310.png)
<br><br>
### Level II
This level is an upgrade to `Level I`.<br>
Cubic metres destroyed nearby: **4 m³**<br><br>
![image](https://user-images.githubusercontent.com/47287352/112857155-d9580400-90b0-11eb-8841-ec3b00502a2d.png)
<br><br>
### Level III
This level is an upgrade to `Level II`.<br>
This is the current final level possible to reach via crafting.<br>
Cubic metres destroyed nearby: **5 m³**<br><br>
![image](https://user-images.githubusercontent.com/47287352/112857220-ebd23d80-90b0-11eb-922a-f08815959d2f.png)

