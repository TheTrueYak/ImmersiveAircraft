package immersive_aircraft.client.render.entity.renderer.bullet;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import immersive_aircraft.client.render.entity.renderer.state.TinyTNTRenderState;
import immersive_aircraft.entity.bullet.TinyTNT;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TntMinecartRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;

public class TinyTNTRenderer extends EntityRenderer<TinyTNT, TinyTNTRenderState> {
    private final BlockRenderDispatcher blockRenderer;

    public TinyTNTRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.2f;
        this.blockRenderer = context.getBlockRenderDispatcher();
    }

    @Override
    public void submit(TinyTNTRenderState renderState, PoseStack matrixStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        float partialTicks = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks();

        matrixStack.pushPose();
        matrixStack.translate(0.0, 0.5, 0.0);
        int i = renderState.fuseRemainingInTicks;
        if ((float)i - partialTicks + 1.0f < 10.0f) {
            float f = 1.0f - ((float)i - partialTicks + 1.0f) / 10.0f;
            f = Mth.clamp(f, 0.0f, 1.0f);
            f *= f;
            f *= f;
            float g = 1.0f + f * 0.3f;
            matrixStack.scale(g, g, g);
        }
        matrixStack.scale(0.375f, 0.375f, 0.375f);
        matrixStack.mulPose(Axis.YP.rotationDegrees(-90.0f));
        matrixStack.translate(-0.5, -0.5, 0.5);
        matrixStack.mulPose(Axis.YP.rotationDegrees(90.0f));
        TntMinecartRenderer.submitWhiteSolidBlock(Blocks.TNT.defaultBlockState(), matrixStack, nodeCollector, renderState.lightCoords, i / 5 % 2 == 0, renderState.outlineColor);
        matrixStack.popPose();
        super.submit(renderState, matrixStack, nodeCollector, cameraRenderState);
    }

    @Override
    public TinyTNTRenderState createRenderState() {
        return new TinyTNTRenderState();
    }

    @Override
    public void extractRenderState(TinyTNT entity, TinyTNTRenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.entityId = entity.getId();
        reusedState.fuseRemainingInTicks = entity.getFuse();
    }

    public Identifier getTextureLocation(TinyTNT entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}

