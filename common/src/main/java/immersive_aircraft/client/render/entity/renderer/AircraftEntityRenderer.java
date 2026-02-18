package immersive_aircraft.client.render.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import immersive_aircraft.client.render.entity.renderer.state.VehicleEntityRenderState;
import immersive_aircraft.client.render.entity.renderer.utils.ModelPartRenderHandler;
import immersive_aircraft.entity.AircraftEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.CameraRenderState;
import org.joml.Vector3f;

public abstract class AircraftEntityRenderer<T extends AircraftEntity, S extends VehicleEntityRenderState> extends InventoryVehicleRenderer<T, S> {
    public AircraftEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    // Because this is used in plugins, changing to generic T is no longer possible
    protected abstract ModelPartRenderHandler<T> getModel(T entity);

    @Override
    public void renderLocal(T entity, VehicleEntityRenderState renderState, PoseStack matrixStack, PoseStack.Pose peek, MultiBufferSource vertexConsumerProvider, CameraRenderState cameraRenderState, float partialTick) {
        // Wind effect
        Vector3f effect = entity.onGround() ? new Vector3f(0.0f, 0.0f, 0.0f) : entity.getWindEffect();
        matrixStack.mulPose(Axis.XP.rotationDegrees(effect.z));
        matrixStack.mulPose(Axis.ZP.rotationDegrees(effect.x));

        super.renderLocal(entity, renderState, matrixStack, peek, vertexConsumerProvider, cameraRenderState, partialTick);

        //Render trails
        entity.getTrails().forEach(t -> TrailRenderer.render(t, vertexConsumerProvider, peek));
    }

}

