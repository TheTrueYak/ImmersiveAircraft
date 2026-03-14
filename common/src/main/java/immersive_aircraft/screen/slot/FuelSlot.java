package immersive_aircraft.screen.slot;

import immersive_aircraft.util.Utils;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class FuelSlot extends Slot {
    public FuelSlot(Container inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return Utils.getFuelTime(stack) > 0;
        //return stack.is(ItemTags.FURNACE_MINECART_FUEL);
    }
}

