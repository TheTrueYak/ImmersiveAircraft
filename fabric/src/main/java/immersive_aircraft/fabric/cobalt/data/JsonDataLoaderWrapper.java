package immersive_aircraft.fabric.cobalt.data;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class JsonDataLoaderWrapper implements IdentifiableResourceReloadListener {

    private final Identifier id;
    private final PreparableReloadListener dataLoader;

    public JsonDataLoaderWrapper(Identifier id, PreparableReloadListener dataLoader) {
        this.id = id;
        this.dataLoader = dataLoader;
    }

    @Override
    public Identifier getFabricId() {
        return id;
    }

    @Override
    public CompletableFuture<Void> reload(SharedState synchronizer, Executor prepareExecutor, PreparationBarrier barrier, Executor applyExectutor) {
        return dataLoader.reload(synchronizer, prepareExecutor, barrier, applyExectutor);
    }

}
