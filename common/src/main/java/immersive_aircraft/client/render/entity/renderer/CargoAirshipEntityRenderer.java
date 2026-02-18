package immersive_aircraft.client.render.entity.renderer;

import immersive_aircraft.Main;
import immersive_aircraft.client.render.entity.renderer.state.VehicleEntityRenderState;
import immersive_aircraft.entity.AirshipEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class CargoAirshipEntityRenderer<T extends AirshipEntity, S extends VehicleEntityRenderState> extends AirshipEntityRenderer<T, S> {
    private static final Identifier ID = Main.locate("cargo_airship");

    protected Identifier getModelId() {
        return ID;
    }

    public CargoAirshipEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.8f;
    }
}
