package immersive_aircraft.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import immersive_aircraft.entity.VehicleEntity;
import immersive_aircraft.injection.LivingEntityRenderStateInjector;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public abstract class PlayerEntityRendererMixin<AvatarlikeEntity extends Avatar & ClientAvatarEntity> extends LivingEntityRenderer<AvatarlikeEntity, AvatarRenderState, PlayerModel> {
    public PlayerEntityRendererMixin(EntityRendererProvider.Context context, PlayerModel model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    /*@Inject(method = "setupRotations(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;FF)V", at = @At("TAIL"))
    private void setModelPose(AvatarRenderState renderState, PoseStack poseStack, float f, float g, CallbackInfo ci) {
        float xRot = ((LivingEntityRenderStateInjector) renderState).immersiveAircraft$getLerpedXRot();
        float roll = ((LivingEntityRenderStateInjector) renderState).immersiveAircraft$getLerpedRoll();
        if (xRot != 0 && roll != 0) {
            poseStack.mulPose(Axis.XP.rotationDegrees(-xRot));
            poseStack.mulPose(Axis.ZP.rotationDegrees(-roll));
        }
        /*if (avatarRenderState.getRootVehicle() instanceof VehicleEntity) {
            PlayerModel<AbstractClientPlayer> playerEntityModel = this.getModel();
            playerEntityModel.crouching = false;
        }// TODO: fix
    }*/
}
