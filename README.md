# Coinbound game mode
Everything is locked, gather coins to choose what to unlock next!
![chooseCard](assets/chooseCard.png)

## Gameplay
You start off with next to nothing unlocked (see the full list below). With every new peak wealth reached, you may open a new booster pack of unlock cards and choose one, slowly unlocking the game one unlock at a time.

### Rules
* You can only use content that you have unlocked. The plugin will notify you when you try to use locked content as best as it can.
* Your main progression is through gathering coins. Every time you reach a new peak wealth (you surpass a threshold of total coins in your inventory) you will be able to open a new booster pack and choose an unlock card.
* You must use the latest unlock as main source of money making. If you for example unlock a shop type, you must use that shop to make money before you can open your next booster pack.
  * Exceptions to this rule are made for unlocks that don't directly make money, such as quests or bosses.
  * Even if you have multiple packs to open, you must still earn at least 10% towards the next pack before opening the next.

## Important note
The plugin uses al-kharid flyers to block inventory spaces. At the start of the game mode it will ask you to gather a certain amount.
Once you've collected enough they will turn into locked icons. They cannot be interacted with or dragged, so if you want a different inventory layout, disable the plugin and drag the flyers to the spaces you want.

![LockedIcon](assets/LockedIcon.png)


## Screenshots
![buypackButton](assets/buypackButton.png)
![lockedGear](assets/lockedGear.png)
![lockedInventory](assets/lockedInventory.png)
![lockedPrayer](assets/lockedPrayer.png)
![lockedSkills](assets/lockedSkills.png)
![lockedSpecial](assets/lockedSpecial.png)
![lockedSpells](assets/lockedSpells.png)
![nextPackOverlay](assets/nextPackOverlay.png)
![packavailable](assets/packavailable.png)

## Locked content list
Nearly all of these are enforced by the plugin, meaning you will get a message when you try to do something locked.
* Gain XP in any skill except hitpoints. (Hitpoints is unlocked at the start)
* Restoration of stats.
  * Though food
  * Through potions
  * Though praying at altars
  * Through pools of refreshment (E.G clan wars or in house)
* Complete quests. (You unlock quests one by one)
* Use protection prayers. (You unlock them one at a time)
* Equip items. You unlock each slot separately.
* Buy or sell items from/to shopkeepers. You unlock them per shop type (E.G Plate body shops unlock all shops with platebody icon)
* Minigames. Allow you to participate and gain rewards in specific minigames.
* Opening clue boxes. (Note, you're allowed to complete the clue scroll, just not open the casket.)
* Bosses. Unlock each specific boss.
* High/low level alchemy. (One unlock for both)
* Using special attacks. (One unlock for all weapons)
* Transport options. 
  * Fairy Rings
  * Spirit Trees
  * Teleport using spells
  * Minigame Teleports
  * Charter Ships
  * Agility Shortcuts
  * Balloon Transport
  * Gnome Gliders
  * Teleport Jewelry
  * Canoes

## Credits

Thanks to the following plugins for inspiration and code ideas:
* [Skill lock](https://github.com/Ventyrian/skill-lock)
* [Accidental teleport blocker](https://github.com/bielie993-ui/accidental-teleport-blocker.git)
* [Chance man](https://github.com/ChunkyAtlas/chance-man.git)
* [Choicer](https://github.com/Attoz/choicer.git)