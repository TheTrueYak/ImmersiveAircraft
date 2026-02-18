package immersive_aircraft.mixin.client;

import immersive_aircraft.injection.LivingEntityRenderStateInjector;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntityRenderState.class)
public abstract class LivingEntityRenderStateMixin extends EntityRenderState implements LivingEntityRenderStateInjector {

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
}
