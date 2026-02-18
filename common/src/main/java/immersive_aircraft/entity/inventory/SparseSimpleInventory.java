package immersive_aircraft.entity.inventory;

import immersive_aircraft.cobalt.network.NetworkHandler;
import immersive_aircraft.entity.InventoryVehicleEntity;
import immersive_aircraft.network.c2s.InventoryRequest;
import immersive_aircraft.network.s2c.InventoryUpdateMessage;
import immersive_aircraft.screen.VehicleScreenHandler;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class SparseSimpleInventory extends SimpleContainer {
    private final NonNullList<ItemStack> tracked;
    private boolean inventoryRequested = false;

    public SparseSimpleInventory(int size) {
        super(size);

        tracked = NonNullList.withSize(size, ItemStack.EMPTY);
    }

    @Override
    public void fromItemList(ValueInput.TypedInputList<ItemStack> input) {
        super.fromItemList(input);
    }

    public void load(ValueInput.TypedInputList<ItemStackWithSlot> input) {
        this.tracked.clear();

        for (ItemStackWithSlot itemStackWithSlot : input) {
            if (itemStackWithSlot.isValidInContainer(this.tracked.size())) {
                this.setItem(itemStackWithSlot.slot(), itemStackWithSlot.stack());
            }
        }
    }

    public void save(NonNullList<ItemStack> list, ValueOutput.TypedOutputList<ItemStackWithSlot> output) {
        for (int i = 0; i < list.size(); i++) {
            ItemStack stack = list.get(i);
            if (!stack.isEmpty()) {
                output.add(new ItemStackWithSlot(i, stack));
            }
        }
    }

    @Override
    public void storeAsItemList(ValueOutput.TypedOutputList<ItemStack> output) {
        super.storeAsItemList(output);
    }

    public void tick(InventoryVehicleEntity entity) {
        if (entity.level().isClientSide()) {
            // Sync initial inventory
            if (!inventoryRequested) {
                NetworkHandler.sendToServer(new InventoryRequest(entity.getId()));
                inventoryRequested = true;
            }
        } else {
            // Sync changed slots (excluding trailing inventory slots since they won't affect behavior)
            int lastSyncIndex = entity.getInventoryDescription().getLastSyncIndex();
            if (lastSyncIndex == 0) return;
            int index = entity.tickCount % lastSyncIndex;
            ItemStack stack = getItem(index);
            ItemStack trackedStack = tracked.get(index);
            if (!ItemStack.isSameItem(stack, trackedStack)) {
                tracked.set(index, stack.copy());
                entity.level().players().forEach(p -> {
                    if (!(p.containerMenu instanceof VehicleScreenHandler vehicleScreenHandler && vehicleScreenHandler.getVehicle() == entity)) {
                        NetworkHandler.sendToPlayer(new InventoryUpdateMessage(entity, index, stack), (ServerPlayer) p);
                    }
                });
            }
        }
    }
}
