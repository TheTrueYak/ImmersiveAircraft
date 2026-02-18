package immersive_aircraft.mixin.client;

import immersive_aircraft.entity.VehicleEntity;
import net.minecraft.client.renderer.debug.EntityHitboxDebugRenderer;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityHitboxDebugRenderer.class)
public class EntityHitboxDebugRendererMixin {
    /*@Inject(method = "renderHitbox(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/entity/Entity;FFFF)V", at = @At("HEAD"))
    private static void ia$inject$renderHitbox(PoseStack poseStack, VertexConsumer buffer, Entity entity, float red, float green, float blue, float alpha, CallbackInfo ci) {
        if (entity instanceof VehicleEntity vehicle) {
            for (AABB aABB : vehicle.getAdditionalShapes()) {
                LevelRenderer.renderLineBox(poseStack, buffer, aABB.move(-entity.getX(), -entity.getY(), -entity.getZ()), 1.0f, 1.0f, 1.0f, 1.0f);
            }
        }
    }*/

    @Inject(method = "showHitboxes", at = @At("HEAD"))
    private void ia$inject$renderHitbox(Entity entity, float partialTick, boolean isServerEntity, CallbackInfo ci) {
        if (entity instanceof VehicleEntity vehicle) {
            for (AABB aABB : vehicle.getAdditionalShapes()) {
                //Vec3 vec35 = aABB.getCenter();
                //Vec3 vec36 = aABB.getPosition(partialTick);
                //Vec3 vec37 = vec36.subtract(vec35);
                Gizmos.cuboid(aABB, GizmoStyle.stroke(ARGB.colorFromFloat(1.0F, 0.25F, 1.0F, 0.0F)));
                //LevelRenderer.renderLineBox(poseStack, buffer, aABB.move(-entity.getX(), -entity.getY(), -entity.getZ()), 1.0f, 1.0f, 1.0f, 1.0f);
            }
        }
    }
}
