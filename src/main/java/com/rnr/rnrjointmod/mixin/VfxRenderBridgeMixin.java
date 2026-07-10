package com.rnr.rnrjointmod.mixin;

import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Cascade renders its particles at AFTER_TRANSLUCENT_BLOCKS, but vanilla draws
 * clouds later in the frame, so clouds paint over fireworks. Redirect the stage
 * check to AFTER_WEATHER (after clouds) so fireworks render on top of clouds.
 */
@Pseudo
@Mixin(targets = "dev.chocoboy.cascade.neoforge.client.VfxRenderBridge", remap = false)
public class VfxRenderBridgeMixin {

    @Redirect(
            method = "onRenderLevelStage",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/neoforged/neoforge/client/event/RenderLevelStageEvent$Stage;AFTER_TRANSLUCENT_BLOCKS:Lnet/neoforged/neoforge/client/event/RenderLevelStageEvent$Stage;",
                    opcode = Opcodes.GETSTATIC
            )
    )
    private RenderLevelStageEvent.Stage rnrjointmod$renderAboveClouds() {
        return RenderLevelStageEvent.Stage.AFTER_WEATHER;
    }
}
