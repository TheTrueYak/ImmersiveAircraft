package immersive_aircraft;

import com.google.gson.JsonElement;
import immersive_aircraft.cobalt.registration.Registration;
import immersive_aircraft.data.UpgradeDataLoader;
import immersive_aircraft.data.VehicleDataLoader;
import immersive_aircraft.resources.BBModelLoader;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class DataLoaders {
    public static final ResourceKey<Registry<JsonElement>> AIRCRAFT_UPGRADES = createRegistryKey("aircraft_upgrades");
    public static final ResourceKey<Registry<JsonElement>> AIRCRAFT = createRegistryKey("aircraft");

    public static void bootstrap() {
        // nop
    }

    static {
        Registration.registerDataLoader("aircraft_upgrades", new UpgradeDataLoader());
        Registration.registerDataLoader("aircraft", new VehicleDataLoader());

        Registration.registerResourceLoader("objects_bbmodel", new BBModelLoader());
    }

    private static <T> ResourceKey<Registry<T>> createRegistryKey(String name) {
        return ResourceKey.createRegistryKey(Identifier.withDefaultNamespace(name));
    }
}
