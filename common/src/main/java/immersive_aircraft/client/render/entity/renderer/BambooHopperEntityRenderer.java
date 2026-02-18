package immersive_aircraft.client.render.entity.renderer;

import immersive_aircraft.Main;
import immersive_aircraft.client.render.entity.renderer.state.VehicleEntityRenderState;
import immersive_aircraft.client.render.entity.renderer.utils.ModelPartRenderHandler;
import immersive_aircraft.entity.AircraftEntity;
import immersive_aircraft.entity.BambooHopperEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class BambooHopperEntityRenderer<T extends BambooHopperEntity, S extends VehicleEntityRenderState> extends AircraftEntityRenderer<T, S> {
    private static final Identifier ID = Main.locate("bamboo_hopper");

    protected Identifier getModelId() {
        return ID;
    }

    private final ModelPartRenderHandler<T> model = new ModelPartRenderHandler<>();

    public BambooHopperEntityRenderer(EntityRendererProvider.Context context) {
        super(context);

        this.shadowRadius = 2.0f;
    }

    @Override
    protected ModelPartRenderHandler<T> getModel(T entity) {
        return model;
    }
}
