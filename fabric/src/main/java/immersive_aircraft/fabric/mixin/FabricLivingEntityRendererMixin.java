package immersive_aircraft.fabric.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import immersive_aircraft.entity.VehicleEntity;
import immersive_aircraft.fabric.ClientFabric;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class FabricLivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> extends EntityRenderer<T, S> implements RenderLayerParent<S, M> {

    protected FabricLivingEntityRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("HEAD"))
    private void ia$modifyRenderState(T livingEntity, S livingEntityRenderState, float f, CallbackInfo ci) {
        float xRot = 0, roll = 0;
        if (livingEntity.getRootVehicle() instanceof VehicleEntity vehicleEntity /*&& !(livingEntity.getStringUUID().equals(Minecraft.getInstance().player.getStringUUID()) && Minecraft.getInstance().options.getCameraType().isFirstPerson())*/) {
            xRot = Mth.rotLerp(f, vehicleEntity.xRotO, vehicleEntity.getXRot(f));
            roll = Mth.rotLerp(f, vehicleEntity.prevRoll, vehicleEntity.getRoll(f));
        }
        livingEntityRenderState.setData(ClientFabric.LERPED_X_ROT, xRot);
        livingEntityRenderState.setData(ClientFabric.LERPED_ROLL, roll);
    }

    @Inject(method = "setupRotations", at = @At("TAIL"))
    public void ia$aircraftSitting(S renderState, PoseStack poseStack, float bodyRot, float scale, CallbackInfo ci) {
        if (renderState.getData(ClientFabric.LERPED_X_ROT) != null && renderState.getData(ClientFabric.LERPED_ROLL) != null) {
            float xRot = renderState.getData(ClientFabric.LERPED_X_ROT).floatValue();
            float roll = renderState.getData(ClientFabric.LERPED_ROLL).floatValue();
            if (xRot != 0 && roll != 0) {
                poseStack.mulPose(Axis.XP.rotationDegrees(-xRot));
                poseStack.mulPose(Axis.ZP.rotationDegrees(-roll));
            }
        }
    }
}
