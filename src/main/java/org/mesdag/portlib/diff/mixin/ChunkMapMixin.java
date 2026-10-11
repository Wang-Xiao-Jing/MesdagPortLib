package org.mesdag.portlib.diff.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.LevelChunk;
import org.apache.commons.lang3.mutable.MutableObject;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.wrapper.common.extensions.IPortChunkMapExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChunkMap.class)
public abstract class ChunkMapMixin implements IPortChunkMapExtension {
    // 不用这个方式是因为沉浸式传送门硬崩溃该方法
//    @Inject(method = "playerLoadedChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/DebugPackets;sendPoiPacketsForChunk(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/ChunkPos;)V", shift = At.Shift.AFTER))
//    private void fireChunkSent(ServerPlayer player, MutableObject<ClientboundLevelChunkWithLightPacket> packetCache, LevelChunk chunk, CallbackInfo ci) {
//        PortEventHooks.fireChunkSent(player, chunk, player.serverLevel());
//    }

    @WrapOperation(method = "updateChunkTracking", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ChunkMap;playerLoadedChunk(Lnet/minecraft/server/level/ServerPlayer;Lorg/apache/commons/lang3/mutable/MutableObject;Lnet/minecraft/world/level/chunk/LevelChunk;)V"))
    private void fireChunkSent1(ChunkMap instance, ServerPlayer player, MutableObject<ClientboundLevelChunkWithLightPacket> packetCache, LevelChunk chunk, Operation<Void> original) {
        original.call(instance, player, packetCache, chunk);
        PortEventHooks.fireChunkSent(player, chunk, player.serverLevel());
    }

    @WrapOperation(method = "lambda$prepareTickingChunk$44", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ChunkMap;playerLoadedChunk(Lnet/minecraft/server/level/ServerPlayer;Lorg/apache/commons/lang3/mutable/MutableObject;Lnet/minecraft/world/level/chunk/LevelChunk;)V", remap = true), remap = false)
    private void fireChunkSent2(ChunkMap instance, ServerPlayer player, MutableObject<ClientboundLevelChunkWithLightPacket> packetCache, LevelChunk chunk, Operation<Void> original) {
        original.call(instance, player, packetCache, chunk);
        PortEventHooks.fireChunkSent(player, chunk, player.serverLevel());
    }
}
