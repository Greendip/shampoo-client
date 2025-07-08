package me.alpha432.oyvey.features.modules.combat;

import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.Stage;
import me.alpha432.oyvey.event.impl.*;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.ExplosionUtil;
import me.alpha432.oyvey.util.InventoryUtil;
import me.alpha432.oyvey.util.PlayerUtil;
import me.alpha432.oyvey.util.SneakBlocks;
import me.alpha432.oyvey.util.models.CacheTimer;
import me.alpha432.oyvey.util.models.Timer;
import me.alpha432.oyvey.util.models.Timer2;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.network.packet.s2c.play.HealthUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.hit.BlockHitResult;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


public final class Offhand extends Module
{
    private static Offhand INSTANCE;

    public Setting<OffhandItem> item = mode("Mode", OffhandItem.TOTEM);
    public Setting<Float> healthPlayer = num("Health", 14.0f,0.0f , 36.0f);
    public Setting<Boolean> gapple = bool("Offhand-Gapple", false);
    public Setting<Boolean> crapple = bool("Crapple", false);
    public Setting<Boolean> lethal = bool("Lethal", false);
    public Setting<Boolean> mainhandTotem = bool("Mainhand", false).setParent();
    public Setting<Integer> totemSlot = register(new Setting<>("Health", 1, 1, 9,v->mainhandTotem.isOpen()));
    public Setting<Boolean> alternative = bool("Alternative", false);
    public Setting<Boolean> debug = bool("Debug", false);

    private int lastHotbarSlot, lastTotemCount;
    private Item lastHotbarItem;
    private Item offhandItem;
    private boolean replacing;
    private long replaceTime;

    private final Timer2 mainhandSwapTimer = new CacheTimer();
    private boolean totemInMainhand;

    public Offhand()
    {
        super("Offhand", "Automatically replenishes the totem in your offhand", Category.COMBAT,true,false,false);
        INSTANCE = this;
    }

    public static Offhand getInstance()
    {
        return INSTANCE;
    }

    @Override
    public String getDisplayInfo()
    {
        return String.valueOf(InventoryUtil.count(Items.TOTEM_OF_UNDYING));
    }

    @Override
    public void onDisable()
    {
        // This comment is funny, check the commit
        super.onDisable();
        lastHotbarSlot = -1;
        lastHotbarItem = null;
        offhandItem = null;
        totemInMainhand = false;
    }

    @Subscribe
    public void onLoadWorld(LoadWorldEvent event)
    {
        lastTotemCount = InventoryUtil.count(Items.TOTEM_OF_UNDYING);
    }

    @Override
    public void onTick()
    {


        if (mainhandTotem.getValue() && mainhandSwapTimer.passed(200))
        {
            int totemSlot1 = totemSlot.getValue() - 1;
            ItemStack totemSlotStack = mc.player.getInventory().getStack(totemSlot1);
            totemSlot1 += 36;
            if (totemSlotStack.getItem() != Items.TOTEM_OF_UNDYING)
            {
                int n = 35;
                while (n >= 0)
                {
                    if (mc.player.getInventory().getStack(n).getItem() == Items.TOTEM_OF_UNDYING)
                    {
                        int slot = n < 9 ? n + 36 : n;
                        replacing = true;
                        if (alternative.getValue())
                        {
                            mc.interactionManager.clickSlot(0, slot, totemSlot1, SlotActionType.SWAP, mc.player);
                            replacing = false;
                        }
                        else
                        {
                            if (mc.player.currentScreenHandler.getCursorStack().getItem() != Items.TOTEM_OF_UNDYING)
                            {
                                mc.interactionManager.clickSlot(0, slot, 0, SlotActionType.PICKUP, mc.player);
                            }
                            if (mc.player.currentScreenHandler.getCursorStack().getItem() == Items.TOTEM_OF_UNDYING)
                            {
                                mc.interactionManager.clickSlot(0, totemSlot1, 0, SlotActionType.PICKUP, mc.player);
                                lastTotemCount = InventoryUtil.count(Items.TOTEM_OF_UNDYING) - 1;
                            }
                            replacing = false;
                            if (!mc.player.currentScreenHandler.getCursorStack().isEmpty() && mc.player.getOffHandStack().getItem() == Items.TOTEM_OF_UNDYING)
                            {
                                mc.interactionManager.clickSlot(0, slot, 0, SlotActionType.PICKUP, mc.player);
                                return;
                            }
                        }
                    }
                    n--;
                }
            }

            totemInMainhand = checkMainhandTotem();
            if (totemInMainhand)
            {
                int totemSlot = -1;
                for (int i = 0; i < 9; i++)
                {
                    ItemStack stack = mc.player.getInventory().getStack(i);
                    if (stack.getItem() == Items.TOTEM_OF_UNDYING)
                    {
                        totemSlot = i;
                        break;
                    }
                }
                if (totemSlot != -1)
                {
                    InventoryUtil.setClientSlot(totemSlot);
                }
            }
        }
        else
        {
            totemInMainhand = false;
        }

        offhandItem = item.getValue().getItem();
        if (checkLethal())
        {
            offhandItem = Items.TOTEM_OF_UNDYING;
        }
        else
        {
            // If offhand gap is enabled & the use key is pressed down, equip a golden apple.
            final Item mainHandItem = mc.player.getMainHandStack().getItem();
            if (gapple.getValue() && mc.options.useKey.isPressed()
                    && (mainHandItem instanceof SwordItem
                    || mainHandItem instanceof TridentItem
                    || mainHandItem instanceof AxeItem)
                    && PlayerUtil.getLocalPlayerHealth() >= healthPlayer.getValue())
            {
                if (mc.crosshairTarget instanceof BlockHitResult result)
                {
                    BlockState interactBlock = mc.world.getBlockState(result.getBlockPos());
                    if (!SneakBlocks.isSneakBlock(interactBlock))
                    {
                        offhandItem = getGoldenAppleType();
                    }
                }
                else
                {
                    offhandItem = getGoldenAppleType();
                }
            }
        }

        if (mc.player.getOffHandStack().getItem() == offhandItem)
        {
            return;
        }
        int n = 35;
        if (lastHotbarSlot != -1 && lastHotbarItem != null)
        {
            final ItemStack stack = mc.player.getInventory().getStack(lastHotbarSlot);
            if (stack.getItem().equals(offhandItem) && lastHotbarItem.equals(mc.player.getOffHandStack().getItem()))
            {
                final int tmp = lastHotbarSlot;
                lastHotbarSlot = -1;
                lastHotbarItem = null;
                n = tmp;
            }
        }
        while (n >= 0)
        {
            if (mc.player.getInventory().getStack(n).getItem() == offhandItem)
            {
                if (n < 9)
                {
                    lastHotbarItem = offhandItem;
                    lastHotbarSlot = n;
                }
                int slot = n < 9 ? n + 36 : n;
                replacing = true;
                if (alternative.getValue())
                {
                    mc.interactionManager.clickSlot(0, slot, 40, SlotActionType.SWAP, mc.player);
                    replacing = false;
                }
                else
                {
                    if (mc.player.currentScreenHandler.getCursorStack().getItem() != offhandItem)
                    {
                        mc.interactionManager.clickSlot(0, slot, 0, SlotActionType.PICKUP, mc.player);
                    }
                    if (mc.player.currentScreenHandler.getCursorStack().getItem() == offhandItem)
                    {
                        mc.interactionManager.clickSlot(0, 45, 0, SlotActionType.PICKUP, mc.player);
                        lastTotemCount = InventoryUtil.count(Items.TOTEM_OF_UNDYING) - 1;
                    }
                    replacing = false;
                    if (!mc.player.currentScreenHandler.getCursorStack().isEmpty() && mc.player.getOffHandStack().getItem() == offhandItem)
                    {
                        mc.interactionManager.clickSlot(0, slot, 0, SlotActionType.PICKUP, mc.player);
                        return;
                    }
                }
            }
            n--;
        }
    }

    @Subscribe
    public void onPacketInbound(final PacketEvent.Receive event)
    {
        if (mc.player == null || mc.world == null)
        {
            return;
        }
        if (event.getPacket() instanceof HealthUpdateS2CPacket packet
                && packet.getHealth() <= 0.0f && debug.getValue())
        {
            if (lastTotemCount <= 0)
            {
                return;
            }
            final Set<String> failureReasonsSet = getFailureReasons();

        }
        // Server should only send this when we pop a totem
        if (event.getPacket() instanceof ScreenHandlerSlotUpdateS2CPacket packet
                && packet.getSlot() == 45 && offhandItem == Items.TOTEM_OF_UNDYING)
        {
            if (mc.player.getOffHandStack().getItem() != Items.TOTEM_OF_UNDYING || !packet.getStack().isEmpty())
            {
                return;
            }
            replaceTime = System.currentTimeMillis();
        }
    }

    private Set<String> getFailureReasons()
    {
        final Set<String> failureReasonsSet = new LinkedHashSet<>();
        if (mc.player.currentScreenHandler.syncId != 0)
        {
            failureReasonsSet.add("Current screen handler is not the player inventory");
        }
        if (!mc.player.currentScreenHandler.getCursorStack().isEmpty())
        {
            failureReasonsSet.add("Totem was not placed in offhand on time");
        }
        return failureReasonsSet;
    }

    @Subscribe
    public void onUpdate(ClientEvent event)
    {
        if (event.getSetting() == totemSlot)
        {
            mainhandSwapTimer.reset();
        }
    }

    private boolean checkLethal()
    {
        // If the player's health (+absorption) falls below the "safe" amount, equip a totem
        final float health = PlayerUtil.getLocalPlayerHealth();
        return health <= healthPlayer.getValue() || lethal.getValue() && checkLethalCrystal(health) ||
                PlayerUtil.computeFallDamage(mc.player.fallDistance, 1.0f) + 0.5f > mc.player.getHealth();
    }

    private boolean checkLethalCrystal(float health)
    {
        final List<Entity> entities = Lists.newArrayList(mc.world.getEntities());
        for (Entity e : entities)
        {
            if (e == null || !e.isAlive() || !(e instanceof EndCrystalEntity crystal))
            {
                continue;
            }
            if (mc.player.squaredDistanceTo(e) > 144.0)
            {
                continue;
            }
            double potential = ExplosionUtil.getDamageTo(mc.player, crystal.getPos(), false);
            if (health + 0.5 > potential)
            {
                continue;
            }
            return true;
        }

        return false;
    }

    private Item getGoldenAppleType()
    {
        if (crapple.getValue() && InventoryUtil.hasItemInInventory(Items.GOLDEN_APPLE, true)
                && (mc.player.hasStatusEffect(StatusEffects.ABSORPTION)
                || !InventoryUtil.hasItemInInventory(Items.ENCHANTED_GOLDEN_APPLE, true)))
        {
            return Items.GOLDEN_APPLE;
        }
        return Items.ENCHANTED_GOLDEN_APPLE;
    }

    private boolean checkMainhandTotem()
    {
        if (mc.player.getMainHandStack().getItem() == Items.TOTEM_OF_UNDYING)
        {
            return false;
        }
        return checkLethalCrystal(PlayerUtil.getLocalPlayerHealth());
    }

    public boolean isTotemInMainhand()
    {
        return totemInMainhand;
    }

    public boolean isReplacing()
    {
        return replacing;
    }

    private enum OffhandItem
    {
        TOTEM(Items.TOTEM_OF_UNDYING),
        GAPPLE(Items.ENCHANTED_GOLDEN_APPLE),
        CRYSTAL(Items.END_CRYSTAL);

        private final Item item;

        OffhandItem(Item item)
        {
            this.item = item;
        }

        public Item getItem()
        {
            return item;
        }
    }
}