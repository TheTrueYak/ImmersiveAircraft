package immersive_aircraft.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import immersive_aircraft.entity.VehicleEntity;
import immersive_aircraft.injection.LivingEntityRenderStateInjector;
import net.minecraft.client.Minecraft;
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
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> extends EntityRenderer<T, S> implements RenderLayerParent<S, M> {

    protected LivingEntityRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("HEAD"))
    private void ia$modifyRenderState(T livingEntity, S livingEntityRenderState, float f, CallbackInfo ci) {
        float xRot = 0, roll = 0;
        if (livingEntity.getRootVehicle() instanceof VehicleEntity vehicleEntity /*&& !(livingEntity.getStringUUID().equals(Minecraft.getInstance().player.getStringUUID()) && Minecraft.getInstance().options.getCameraType().isFirstPerson())*/) {
            //float partialTick = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(true);
            xRot = Mth.rotLerp(f, vehicleEntity.xRotO, vehicleEntity.getXRot(f));
            roll = Mth.rotLerp(f, vehicleEntity.prevRoll, vehicleEntity.getRoll(f));
        }

        //((LivingEntityRenderStateInjector) livingEntityRenderState).immersiveAircraft$setlerpedXRot(xRot);
        //((LivingEntityRenderStateInjector) livingEntityRenderState).immersiveAircraft$setlerpedRoll(roll);
    }

    @Inject(method = "setupRotations", at = @At("TAIL"))
    public void ia$aircraftSitting(S renderState, PoseStack poseStack, float bodyRot, float scale, CallbackInfo ci) {
        float xRot = ((LivingEntityRenderStateInjector) renderState).immersiveAircraft$getLerpedXRot();
        float roll = ((LivingEntityRenderStateInjector) renderState).immersiveAircraft$getLerpedRoll();
        if (xRot != 0 && roll != 0) {
            //poseStack.mulPose(Axis.XP.rotationDegrees(-xRot));
            //poseStack.mulPose(Axis.ZP.rotationDegrees(-roll));
        }
    }
}
