package fuzs.bettertridents.capability;

import fuzs.puzzleslib.api.capability.v2.data.CapabilityComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface TridentSlotCapability extends CapabilityComponent {

    void setSlot(int slot);

    int getSlot();

    default boolean addItemToInventory(Player player, ItemStack itemStack) {
        this.verifyEquippedItem(itemStack);
        Inventory inventory = player.getInventory();
        int slot = this.findSlotAtIndex(inventory, this.getSlot());
        if (slot != -1) {
            inventory.setItem(slot, itemStack.copy());
            inventory.getItem(slot).setPopTime(5);
            itemStack.setCount(0);
            return true;
        } else {
            return inventory.add(itemStack);
        }
    }

    private void verifyEquippedItem(ItemStack itemStack) {
        CompoundTag compoundtag = itemStack.getTag();
        if (compoundtag != null) {
            itemStack.getItem().verifyTagAfterLoad(compoundtag);
        }
    }

    private int findSlotAtIndex(Inventory inventory, int slot) {
        if (slot != -1 && inventory.getItem(slot).isEmpty()) {
            return slot;
        } else if (inventory.getSelected().isEmpty()) {
            // try to return to main hand as secondary option
            return inventory.selected;
        }
        return -1;
    }
}
