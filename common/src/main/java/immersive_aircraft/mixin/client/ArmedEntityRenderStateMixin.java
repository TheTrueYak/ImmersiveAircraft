package immersive_aircraft.mixin.client;

import immersive_aircraft.entity.VehicleEntity;
import immersive_aircraft.injection.LivingEntityRenderStateInjector;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmedEntityRenderState.class)
public abstract class ArmedEntityRenderStateMixin implements LivingEntityRenderStateInjector {

    @Unique private float immersiveAircraft$lerpedXRot;
    @Unique private float immersiveAircraft$lerpedRoll;

    @Override
    public float immersiveAircraft$getLerpedXRot() {
        return this.immersiveAircraft$lerpedXRot;
    }

    @Override
    public float immersiveAircraft$getLerpedRoll() {
        return this.immersiveAircraft$lerpedRoll;
    }

    @Override
    public void immersiveAircraft$setlerpedXRot(float lerpedXRot) {
        this.immersiveAircraft$lerpedXRot = lerpedXRot;
    }

    @Override
    public void immersiveAircraft$setlerpedRoll(float lerpedRoll) {
        this.immersiveAircraft$lerpedRoll = lerpedRoll;
    }

    @Inject(method = "extractArmedEntityRenderState", at = @At("TAIL"))
    private static void ia$extractArmedEntityRenderState(LivingEntity livingEntity, ArmedEntityRenderState reusedState, ItemModelResolver modelResolver, float partialTick, CallbackInfo ci) {
        float xRot = 0, roll = 0;
        if (livingEntity.getRootVehicle() instanceof VehicleEntity vehicleEntity /*&& !(livingEntity.getStringUUID().equals(Minecraft.getInstance().player.getStringUUID()) && Minecraft.getInstance().options.getCameraType().isFirstPerson())*/) {
            //float partialTick = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(true);
            xRot = Mth.rotLerp(partialTick, vehicleEntity.xRotO, vehicleEntity.getXRot(partialTick));
            roll = Mth.rotLerp(partialTick, vehicleEntity.prevRoll, vehicleEntity.getRoll(partialTick));
        }
        ((LivingEntityRenderStateInjector) reusedState).immersiveAircraft$setlerpedXRot(xRot);
        ((LivingEntityRenderStateInjector) reusedState).immersiveAircraft$setlerpedRoll(roll);
    }
}
