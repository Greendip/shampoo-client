package me.alpha432.oyvey.util;

import me.alpha432.oyvey.util.traits.Util;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;


public final class PlayerUtil implements Util
{
    public static float getLocalPlayerHealth()
    {
        return mc.player.getHealth() + mc.player.getAbsorptionAmount();
    }

    // from MC source
    public static int computeFallDamage(float fallDistance, float damageMultiplier)
    {
        if (mc.player.getType().isIn(EntityTypeTags.FALL_DAMAGE_IMMUNE))
        {
            return 0;
        }
        else
        {
            final StatusEffectInstance statusEffectInstance = mc.player.getStatusEffect(StatusEffects.JUMP_BOOST);
            final float f = statusEffectInstance == null ? 0.0F : (float) (statusEffectInstance.getAmplifier() + 1);
            return MathHelper.ceil((fallDistance - 3.0F - f) * damageMultiplier);
        }
    }

    public static boolean isHolding(final Item item)
    {
        ItemStack itemStack = mc.player.getMainHandStack();
        if (!itemStack.isEmpty() && itemStack.getItem() == item)
        {
            return true;
        }
        itemStack = mc.player.getOffHandStack();
        return !itemStack.isEmpty() && itemStack.getItem() == item;
    }

    public static boolean isHotbarKeysPressed()
    {
        for (KeyBinding binding : mc.options.hotbarKeys)
        {
            if (binding.isPressed())
            {
                return true;
            }
        }
        return false;
    }


}
