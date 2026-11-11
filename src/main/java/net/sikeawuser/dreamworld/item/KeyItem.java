package net.sikeawuser.dreamworld.item;

import net.minecraft.ChatFormatting;
import net.minecraft.world.item.TooltipFlag;
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
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class KeyItem extends Item {

    private static final int COOLDOWN_TICKS = 100;
    public static boolean SET_DESC = true;
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
            SET_DESC = false;
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.fail(stack);
    }

    private static boolean isNight(Level level) {
        long time = level.getDayTime() % 24000L;

        return  time >= 13000L && time <= 23000L;
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {

        if (SET_DESC) {
            p_41423_.add(Component.translatable("tooltip.dreamworld.key.tooltip1").withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
        } else {
            p_41423_.add(Component.translatable("tooltip.dreamworld.key.tooltip2").withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
        }
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
    }
}
