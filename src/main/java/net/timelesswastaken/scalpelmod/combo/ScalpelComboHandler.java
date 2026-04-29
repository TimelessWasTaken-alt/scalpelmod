package net.timelesswastaken.scalpelmod.combo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.timelesswastaken.scalpelmod.ScalpelmodMod;
import net.timelesswastaken.scalpelmod.init.ScalpelmodModItems;

@Mod.EventBusSubscriber(modid = ScalpelmodMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ScalpelComboHandler {
	private static final int COMBO_WINDOW_TICKS = 24;
	private static final int MAX_COMBO_STACKS = 5;
	private static final float DAMAGE_PER_STACK = 0.25f;

	private static final Map<UUID, ComboState> COMBOS = new HashMap<>();

	private ScalpelComboHandler() {
	}

	@SubscribeEvent
	public static void onLivingHurt(LivingHurtEvent event) {
		if (event.getEntity().level().isClientSide())
			return;

		Entity attacker = event.getSource().getEntity();
		Entity directAttacker = event.getSource().getDirectEntity();
		if (!(attacker instanceof ServerPlayer player) || directAttacker != player)
			return;
		if (!isHoldingScalpel(player))
			return;

		LivingEntity target = event.getEntity();
		ComboState state = COMBOS.computeIfAbsent(player.getUUID(), ignored -> new ComboState());
		long now = player.level().getGameTime();
		boolean sameTarget = target.getUUID().equals(state.lastTargetId);
		boolean stillInsideWindow = now - state.lastHitGameTime <= COMBO_WINDOW_TICKS;
		int comboStacks = sameTarget && stillInsideWindow ? Math.min(state.comboStacks + 1, MAX_COMBO_STACKS) : 0;

		event.setAmount(event.getAmount() * getComboMultiplier(comboStacks));
		state.recordHit(target, comboStacks, now);
	}

	@SubscribeEvent
	public static void onAttackEntity(AttackEntityEvent event) {
		Player player = event.getEntity();
		if (player.level().isClientSide() || !isHoldingScalpel(player))
			return;

		if (!(event.getTarget() instanceof LivingEntity))
			reset(player);
	}

	@SubscribeEvent
	public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
		Player player = event.getEntity();
		if (!player.level().isClientSide() && isHoldingScalpel(player))
			reset(player);
	}

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide())
			return;

		ComboState state = COMBOS.get(event.player.getUUID());
		if (state == null)
			return;

		if (!isHoldingScalpel(event.player)) {
			reset(event.player);
			return;
		}

		long now = event.player.level().getGameTime();
		if (now - state.lastHitGameTime > COMBO_WINDOW_TICKS)
			reset(event.player);
	}

	@SubscribeEvent
	public static void onLivingDeath(LivingDeathEvent event) {
		if (event.getEntity().level().isClientSide())
			return;

		UUID deadEntityId = event.getEntity().getUUID();
		Iterator<ComboState> iterator = COMBOS.values().iterator();
		while (iterator.hasNext()) {
			ComboState state = iterator.next();
			if (deadEntityId.equals(state.lastTargetId))
				iterator.remove();
		}
	}

	@SubscribeEvent
	public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
		reset(event.getEntity());
	}

	public static void reset(Player player) {
		COMBOS.remove(player.getUUID());
	}

	private static boolean isHoldingScalpel(Player player) {
		ItemStack mainHandItem = player.getMainHandItem();
		return mainHandItem.is(ScalpelmodModItems.SCALPEL.get());
	}

	private static float getComboMultiplier(int comboStacks) {
		return 1.0f + DAMAGE_PER_STACK * comboStacks;
	}

	private static class ComboState {
		private UUID lastTargetId;
		private int comboStacks;
		private long lastHitGameTime;

		private void recordHit(LivingEntity target, int comboStacks, long gameTime) {
			this.lastTargetId = target.getUUID();
			this.comboStacks = comboStacks;
			this.lastHitGameTime = gameTime;
		}
	}
}
