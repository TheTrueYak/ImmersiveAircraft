package immersive_aircraft.client.render.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import immersive_aircraft.client.render.entity.renderer.state.VehicleEntityRenderState;
import immersive_aircraft.client.render.entity.renderer.utils.BBModelRenderer;
import immersive_aircraft.client.render.entity.renderer.utils.ModelPartRenderHandler;
import immersive_aircraft.entity.VehicleEntity;
import immersive_aircraft.resources.BBModelLoader;
import immersive_aircraft.resources.bbmodel.BBAnimationVariables;
import immersive_aircraft.resources.bbmodel.BBModel;
import immersive_aircraft.resources.bbmodel.BBObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public abstract class VehicleEntityRenderer<T extends VehicleEntity, S extends VehicleEntityRenderState> extends EntityRenderer<T, S> {
    public VehicleEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    protected abstract ModelPartRenderHandler<T> getModel(T entity);

    protected abstract Identifier getModelId();

    @Override
    public void submit(S renderState, PoseStack matrixStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        PoseStack.Pose peek = matrixStack.last();
        //float partialTick = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(true);
        float partialTick = renderState.partialTick;

        // evil hack
        Entity entity = Minecraft.getInstance().level.getEntity(renderState.entityId);
        if (entity instanceof VehicleEntity vehicle) {
            T vehicleEntity = (T) vehicle;
            matrixStack.pushPose();

            // Rotation
            //matrixStack.mulPose(Axis.YP.rotationDegrees(-vehicleEntity.getYRot()));
            //matrixStack.mulPose(Axis.XP.rotationDegrees(vehicleEntity.getViewXRot(partialTick)));
            //matrixStack.mulPose(Axis.ZP.rotationDegrees(vehicleEntity.getRoll(partialTick)));
            matrixStack.mulPose(Axis.YP.rotationDegrees(-Mth.rotLerp(renderState.partialTick, vehicleEntity.yRotO, vehicleEntity.getYRot())));
            matrixStack.mulPose(Axis.XP.rotationDegrees(Mth.rotLerp(renderState.partialTick, vehicleEntity.xRotO, vehicleEntity.getViewXRot(partialTick))));
            matrixStack.mulPose(Axis.ZP.rotationDegrees(Mth.rotLerp(renderState.partialTick, vehicleEntity.prevRoll, vehicleEntity.getRoll(partialTick))));

            // Render model, weapons, etc.
            renderLocal(vehicleEntity, renderState, matrixStack, peek, Minecraft.getInstance().renderBuffers().bufferSource(), cameraRenderState, partialTick);

            matrixStack.popPose();
        }
        super.submit(renderState, matrixStack, nodeCollector, cameraRenderState);
    }

    /*@Override
    public void render(T entity, float yaw, float tickDelta, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int light) {
        PoseStack.Pose peek = matrixStack.last();

        matrixStack.pushPose();

        // Rotation
        matrixStack.mulPose(Axis.YP.rotationDegrees(-yaw));
        matrixStack.mulPose(Axis.XP.rotationDegrees(entity.getViewXRot(tickDelta)));
        matrixStack.mulPose(Axis.ZP.rotationDegrees(entity.getRoll(tickDelta)));

        // Render model, weapons, etc.
        renderLocal(entity, yaw, tickDelta, matrixStack, peek, vertexConsumerProvider, light);

        matrixStack.popPose();

        super.render(entity, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
    }*/

    public void renderLocal(T entity, VehicleEntityRenderState renderState, PoseStack matrixStack, PoseStack.Pose peek, MultiBufferSource vertexConsumerProvider, CameraRenderState cameraRenderState, float partialTick) {
        //Wobble
        float h = (float) entity.getDamageWobbleTicks() - partialTick;
        float j = entity.getDamageWobbleStrength() - partialTick;
        if (j < 0.0f) {
            j = 0.0f;
        }
        if (h > 0.0f) {
            matrixStack.mulPose(Axis.XP.rotationDegrees(Mth.sin(h) * h * j / 10.0f * (float) entity.getDamageWobbleSide()));
        }

        // Updated variables
        float time = (entity.level().getGameTime() % 24000 + partialTick) / 20.0f;
        BBAnimationVariables.set("time", time);
        entity.setAnimationVariables(partialTick);

        // Render model
        BBModel bbModel = BBModelLoader.MODELS.get(getModelId());
        if (bbModel != null) {
            float health = entity.getHealth();
            float r = health * 0.6f + 0.4f;
            float g = health * 0.4f + 0.6f;
            float b = health * 0.4f + 0.6f;
            BBModelRenderer.renderModel(bbModel, matrixStack, vertexConsumerProvider, renderState.lightCoords, time, entity, getModel(entity), r, g, b, 1.0f);
        }
    }

    @Override
    public S createRenderState() {
        return (S) new VehicleEntityRenderState();
    }

    @Override
    public void extractRenderState(T entity, S reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        /*reusedState.pitch = entity.getXRot();
        reusedState.yaw = entity.getYRot();
        reusedState.roll = entity.getRoll();
        reusedState.damageWobbleTicks = entity.getDamageWobbleTicks();
        reusedState.damageWobbleStrength = entity.getDamageWobbleStrength();
        reusedState.damageWobbleSide = entity.getDamageWobbleSide();
        reusedState.pressingInterpolatedX = entity.pressingInterpolatedX.getSmooth(partialTick);
        reusedState.pressingInterpolatedY = entity.pressingInterpolatedY.getSmooth(partialTick);
        reusedState.pressingInterpolatedZ = entity.pressingInterpolatedZ.getSmooth(partialTick);
        Vec3 vec = entity.getSpeedVector();
        reusedState.xSpeed = vec.x;
        reusedState.ySpeed = vec.y;
        reusedState.zSpeed = vec.z;*/
        reusedState.entityId = entity.getId();
        reusedState.partialTick = partialTick;
    }

    public void renderOptionalObject(String name, BBModel model, MultiBufferSource vertexConsumerProvider, T entity, PoseStack matrixStack, int light, float time) {
        renderOptionalObject(name, model, vertexConsumerProvider, entity, matrixStack, light, time, 1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void renderOptionalObject(String name, BBModel model, MultiBufferSource vertexConsumerProvider, T entity, PoseStack matrixStack, int light, float time, float red, float green, float blue, float alpha) {
        BBObject object = model.objectsByName.get(name);
        if (object != null) {
            BBModelRenderer.renderObject(model, object, matrixStack, vertexConsumerProvider, light, time, entity, null, red, green, blue, alpha);
        }
    }

    @Override
    public boolean shouldRender(T entity, Frustum frustum, double x, double y, double z) {
        if (!entity.shouldRender(x, y, z)) {
            return false;
        }
        AABB box = entity.getBoundingBox().inflate(getCullingBoundingBoxInflation());
        return frustum.isVisible(box);
    }

    protected double getCullingBoundingBoxInflation() {
        return 2.0;
    }

    private static final Identifier TEXTURE = Identifier.parse("invalid");

    public static Identifier getTEXTURE() {
        return TEXTURE;
    }
}

