package net.sikeawuser.dreamworld.item;

import net.sikeawuser.dreamworld.registry.DMDimensions;
import net.sikeawuser.dreamworld.teleport.TeleportManager;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

public class KeyItem extends Item {

    private static final int COOLDOWN_TICKS = 100;

    public KeyItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);

        TextColor astrocolor = TextColor.fromRgb(0x483475);
        Component CANTUSEKYITEMTEXT = Component.literal("The Key only responds under the night sky."
        ).withStyle(Style.EMPTY.withColor(astrocolor));
        boolean successful;

        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.fail(stack);
        }

        if (!isNight(level)) {

            if (!level.isClientSide()) {
                player.displayClientMessage(CANTUSEKYITEMTEXT , true);
            }

            return InteractionResultHolder.fail(stack);
        }

        if (level.isClientSide()) {
            return InteractionResultHolder.success(stack);
        }

        if (!(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResultHolder.fail(stack);
        }

        if (serverPlayer.level().dimension().equals(DMDimensions.DREAMWORLD)) {
            successful = TeleportManager.leaveDreamworld(serverPlayer);
        } else {
            successful = TeleportManager.enterDreamWorld(serverPlayer);
        }
        if (successful) {
            player.getCooldowns().addCooldown(this , COOLDOWN_TICKS);
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.fail(stack);
    }

    private static boolean isNight(Level level) {
        long time = level.getDayTime() % 24000L;

        return  time >= 13000L && time <= 23000L;
    }
}
