package com.rnr.rnrjointmod.screen.custom;

import com.rnr.rnrjointmod.block.entity.FireworkCakeEntity;
import com.rnr.rnrjointmod.screen.ModMenuTypes;
import imgui.ImGui;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

public class FireworkCakeMenu extends AbstractContainerMenu {

    public final FireworkCakeEntity blockEntity;
    private final Level level;

    public FireworkCakeMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }
    public FireworkCakeMenu(int containerId, Inventory inv, BlockEntity blockEntity) {
        super(ModMenuTypes.FIREWORK_CAKE_MENU.get(), containerId);
        this.blockEntity = ((FireworkCakeEntity) blockEntity);
        this.level = inv.player.level();
    }


    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return false;
    }
}
