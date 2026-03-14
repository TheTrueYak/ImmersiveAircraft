package immersive_aircraft.fabric.cobalt.registration;

import immersive_aircraft.Main;
import immersive_aircraft.cobalt.registration.CobaltFuelRegistry;
import immersive_aircraft.util.Utils;
import net.fabricmc.fabric.api.registry.FuelRegistryEvents;
import net.minecraft.world.item.ItemStack;

public class CobaltFuelRegistryImpl extends CobaltFuelRegistry {
    public CobaltFuelRegistryImpl() {
        INSTANCE = this;
    }

    @Override
    public int get(ItemStack stack) {
        return Utils.fuelRegistry.burnDuration(stack); // FIX
    }
}
