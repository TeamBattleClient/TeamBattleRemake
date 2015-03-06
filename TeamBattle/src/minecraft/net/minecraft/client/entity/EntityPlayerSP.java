package net.minecraft.client.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiCommandBlock;
import net.minecraft.client.gui.GuiEnchantment;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.gui.inventory.GuiBeacon;
import net.minecraft.client.gui.inventory.GuiBrewingStand;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.gui.inventory.GuiDispenser;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.client.gui.inventory.GuiScreenHorseInventory;
import net.minecraft.client.particle.EntityCrit2FX;
import net.minecraft.client.particle.EntityPickupFX;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityMinecartHopper;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MouseFilter;
import net.minecraft.util.MovementInput;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import net.minecraft.world.World;
import down.TeamBattle.TeamBattleClient;
import down.TeamBattle.EventSystem.events.EventFOVChange;
import down.TeamBattle.EventSystem.events.EventPlayerMovement;
import down.TeamBattle.EventSystem.events.EventPushOutOfBlocks;

public class EntityPlayerSP extends AbstractClientPlayer {
	private final MouseFilter field_71160_ci = new MouseFilter();
	private final MouseFilter field_71161_cj = new MouseFilter();

	private final MouseFilter field_71162_ch = new MouseFilter();

	private float horseJumpPower;
	private int horseJumpPowerCounter;
	protected Minecraft mc;
	public MovementInput movementInput;
	public float prevRenderArmPitch;
	public float prevRenderArmYaw;
	/** The amount of time an entity has been in a Portal the previous tick */
	public float prevTimeInPortal;
	public float renderArmPitch;
	public float renderArmYaw;
	/** Ticks left before sprinting is disabled. */
	public int sprintingTicksLeft;

	/**
	 * Used to tell if the player pressed forward twice. If this is at 0 and
	 * it's pressed (And they are allowed to sprint, aka enough food on the
	 * ground etc) it sets this to 7. If it's pressed and it's greater than 0
	 * enable sprinting.
	 */
	protected int sprintToggleTimer;

	/** The amount of time an entity has been in a Portal */
	public float timeInPortal;

	public EntityPlayerSP(Minecraft p_i1238_1_, World p_i1238_2_,
			Session p_i1238_3_, int p_i1238_4_) {
		super(p_i1238_2_, p_i1238_3_.func_148256_e());
		mc = p_i1238_1_;
		dimension = p_i1238_4_;
	}

	@Override
	public void addChatComponentMessage(IChatComponent p_146105_1_) {
		mc.ingameGUI.getChatGUI().func_146227_a(p_146105_1_);
	}

	/**
	 * Notifies this sender of some sort of information. This is for messages
	 * intended to display to the user. Used for typical output (like
	 * "you asked for whether or not this game rule is set, so here's your answer"
	 * ), warnings (like "I fetched this block for you by ID, but I'd like you
	 * to know that every time you do this, I die a little
	 * inside"), and errors (like "it's not called iron_pixacke, silly").
	 */
	@Override
	public void addChatMessage(IChatComponent p_145747_1_) {
		mc.ingameGUI.getChatGUI().func_146227_a(p_145747_1_);
	}

	/**
	 * Returns true if the command sender is allowed to use the given command.
	 */
	@Override
	public boolean canCommandSenderUseCommand(int p_70003_1_, String p_70003_2_) {
		return p_70003_1_ <= 0;
	}

	/**
	 * set current crafting inventory back to the 2x2 square
	 */
	@Override
	public void closeScreen() {
		super.closeScreen();
		mc.displayGuiScreen((GuiScreen) null);
	}

	/**
	 * Displays the GUI for interacting with an anvil.
	 */
	@Override
	public void displayGUIAnvil(int p_82244_1_, int p_82244_2_, int p_82244_3_) {
		mc.displayGuiScreen(new GuiRepair(inventory, worldObj, p_82244_1_,
				p_82244_2_, p_82244_3_));
	}

	/**
	 * Displays the GUI for interacting with a book.
	 */
	@Override
	public void displayGUIBook(ItemStack p_71048_1_) {
		final Item var2 = p_71048_1_.getItem();

		if (var2 == Items.written_book) {
			mc.displayGuiScreen(new GuiScreenBook(this, p_71048_1_, false));
		} else if (var2 == Items.writable_book) {
			mc.displayGuiScreen(new GuiScreenBook(this, p_71048_1_, true));
		}
	}

	/**
	 * Displays the GUI for interacting with a chest inventory. Args:
	 * chestInventory
	 */
	@Override
	public void displayGUIChest(IInventory p_71007_1_) {
		mc.displayGuiScreen(new GuiChest(inventory, p_71007_1_));
	}

	@Override
	public void displayGUIEnchantment(int p_71002_1_, int p_71002_2_,
			int p_71002_3_, String p_71002_4_) {
		mc.displayGuiScreen(new GuiEnchantment(inventory, worldObj, p_71002_1_,
				p_71002_2_, p_71002_3_, p_71002_4_));
	}

	@Override
	public void displayGUIHopperMinecart(EntityMinecartHopper p_96125_1_) {
		mc.displayGuiScreen(new GuiHopper(inventory, p_96125_1_));
	}

	@Override
	public void displayGUIHorse(EntityHorse p_110298_1_, IInventory p_110298_2_) {
		mc.displayGuiScreen(new GuiScreenHorseInventory(inventory, p_110298_2_,
				p_110298_1_));
	}

	@Override
	public void displayGUIMerchant(IMerchant p_71030_1_, String p_71030_2_) {
		mc.displayGuiScreen(new GuiMerchant(inventory, p_71030_1_, worldObj,
				p_71030_2_));
	}

	/**
	 * Displays the crafting GUI for a workbench.
	 */
	@Override
	public void displayGUIWorkbench(int p_71058_1_, int p_71058_2_,
			int p_71058_3_) {
		mc.displayGuiScreen(new GuiCrafting(inventory, worldObj, p_71058_1_,
				p_71058_2_, p_71058_3_));
	}

	protected void func_110318_g() {
	}

	@Override
	protected boolean func_145771_j(double p_145771_1_, double p_145771_3_,
			double p_145771_5_) {
		final EventPushOutOfBlocks event = new EventPushOutOfBlocks();
		TeamBattleClient.getEventManager().call(event);
		if (event.isCancelled())
			return false;
		final int var7 = MathHelper.floor_double(p_145771_1_);
		final int var8 = MathHelper.floor_double(p_145771_3_);
		final int var9 = MathHelper.floor_double(p_145771_5_);
		final double var10 = p_145771_1_ - var7;
		final double var12 = p_145771_5_ - var9;

		if (isBlockTranslucent(var7, var8, var9)
				|| isBlockTranslucent(var7, var8 + 1, var9)) {
			final boolean var14 = !isBlockTranslucent(var7 - 1, var8, var9)
					&& !isBlockTranslucent(var7 - 1, var8 + 1, var9);
			final boolean var15 = !isBlockTranslucent(var7 + 1, var8, var9)
					&& !isBlockTranslucent(var7 + 1, var8 + 1, var9);
			final boolean var16 = !isBlockTranslucent(var7, var8, var9 - 1)
					&& !isBlockTranslucent(var7, var8 + 1, var9 - 1);
			final boolean var17 = !isBlockTranslucent(var7, var8, var9 + 1)
					&& !isBlockTranslucent(var7, var8 + 1, var9 + 1);
			byte var18 = -1;
			double var19 = 9999.0D;

			if (var14 && var10 < var19) {
				var19 = var10;
				var18 = 0;
			}

			if (var15 && 1.0D - var10 < var19) {
				var19 = 1.0D - var10;
				var18 = 1;
			}

			if (var16 && var12 < var19) {
				var19 = var12;
				var18 = 4;
			}

			if (var17 && 1.0D - var12 < var19) {
				var19 = 1.0D - var12;
				var18 = 5;
			}

			final float var21 = 0.1F;

			if (var18 == 0) {
				motionX = -var21;
			}

			if (var18 == 1) {
				motionX = var21;
			}

			if (var18 == 4) {
				motionZ = -var21;
			}

			if (var18 == 5) {
				motionZ = var21;
			}
		}

		return false;
	}

	@Override
	public void func_146093_a(TileEntityHopper p_146093_1_) {
		mc.displayGuiScreen(new GuiHopper(inventory, p_146093_1_));
	}

	@Override
	public void func_146095_a(CommandBlockLogic p_146095_1_) {
		mc.displayGuiScreen(new GuiCommandBlock(p_146095_1_));
	}

	@Override
	public void func_146098_a(TileEntityBrewingStand p_146098_1_) {
		mc.displayGuiScreen(new GuiBrewingStand(inventory, p_146098_1_));
	}

	@Override
	public void func_146100_a(TileEntity p_146100_1_) {
		if (p_146100_1_ instanceof TileEntitySign) {
			mc.displayGuiScreen(new GuiEditSign((TileEntitySign) p_146100_1_));
		} else if (p_146100_1_ instanceof TileEntityCommandBlock) {
			mc.displayGuiScreen(new GuiCommandBlock(
					((TileEntityCommandBlock) p_146100_1_).func_145993_a()));
		}
	}

	@Override
	public void func_146101_a(TileEntityFurnace p_146101_1_) {
		mc.displayGuiScreen(new GuiFurnace(inventory, p_146101_1_));
	}

	@Override
	public void func_146102_a(TileEntityDispenser p_146102_1_) {
		mc.displayGuiScreen(new GuiDispenser(inventory, p_146102_1_));
	}

	@Override
	public void func_146104_a(TileEntityBeacon p_146104_1_) {
		mc.displayGuiScreen(new GuiBeacon(inventory, p_146104_1_));
	}

	/**
	 * Gets the player's field of view multiplier. (ex. when flying)
	 */
	public float getFOVMultiplier() {
		float var1 = 1.0F;

		if (capabilities.isFlying) {
			var1 *= 1.1F;
		}

		final IAttributeInstance var2 = getEntityAttribute(SharedMonsterAttributes.movementSpeed);
		var1 = (float) (var1 * ((var2.getAttributeValue()
				/ capabilities.getWalkSpeed() + 1.0D) / 2.0D));

		if (capabilities.getWalkSpeed() == 0.0F || Float.isNaN(var1)
				|| Float.isInfinite(var1)) {
			var1 = 1.0F;
		}

		if (isUsingItem() && getItemInUse().getItem() == Items.bow) {
			final int var3 = getItemInUseDuration();
			float var4 = var3 / 20.0F;

			if (var4 > 1.0F) {
				var4 = 1.0F;
			} else {
				var4 *= var4;
			}

			var1 *= 1.0F - var4 * 0.15F;
		}

		final EventFOVChange event = new EventFOVChange(var1);
		TeamBattleClient.getEventManager().call(event);
		return event.getFOV();
	}

	public float getHorseJumpPower() {
		return horseJumpPower;
	}

	/**
	 * Return the position for this command sender.
	 */
	@Override
	public ChunkCoordinates getPlayerCoordinates() {
		return new ChunkCoordinates(MathHelper.floor_double(posX + 0.5D),
				MathHelper.floor_double(posY + 0.5D),
				MathHelper.floor_double(posZ + 0.5D));
	}

	private boolean isBlockTranslucent(int p_71153_1_, int p_71153_2_,
			int p_71153_3_) {
		return worldObj.getBlock(p_71153_1_, p_71153_2_, p_71153_3_)
				.isNormalCube();
	}

	/**
	 * Returns whether the entity is in a local (client) world
	 */
	@Override
	public boolean isClientWorld() {
		return true;
	}

	public boolean isRidingHorse() {
		return ridingEntity != null && ridingEntity instanceof EntityHorse;
	}

	/**
	 * Returns if this entity is sneaking.
	 */
	@Override
	public boolean isSneaking() {
		return movementInput.sneak && !sleeping;
	}

	@Override
	public void moveEntity(double motionX, double motionY, double motionZ) {
		final EventPlayerMovement event = new EventPlayerMovement(motionX,
				motionY, motionZ);
		TeamBattleClient.getEventManager().call(event);
		motionX = event.getMotionX();
		motionY = event.getMotionY();
		motionZ = event.getMotionZ();
		super.moveEntity(motionX, motionY, motionZ);
	}

	/**
	 * Called when the player performs a critical hit on the Entity. Args:
	 * entity that was hit critically
	 */
	@Override
	public void onCriticalHit(Entity p_71009_1_) {
		mc.effectRenderer.addEffect(new EntityCrit2FX(mc.theWorld, p_71009_1_));
	}

	@Override
	public void onEnchantmentCritical(Entity p_71047_1_) {
		final EntityCrit2FX var2 = new EntityCrit2FX(mc.theWorld, p_71047_1_,
				"magicCrit");
		mc.effectRenderer.addEffect(var2);
	}

	/**
	 * Called whenever an item is picked up from walking over it. Args:
	 * pickedUpEntity, stackSize
	 */
	@Override
	public void onItemPickup(Entity p_71001_1_, int p_71001_2_) {
		mc.effectRenderer.addEffect(new EntityPickupFX(mc.theWorld, p_71001_1_,
				this, -0.5F));
	}

	/**
	 * Called frequently so the entity can update its state every tick as
	 * required. For example, zombies and skeletons use this to react to
	 * sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate() {
		if (sprintingTicksLeft > 0) {
			--sprintingTicksLeft;

			if (sprintingTicksLeft == 0) {
				setSprinting(false);
			}
		}

		if (sprintToggleTimer > 0) {
			--sprintToggleTimer;
		}

		if (mc.playerController.enableEverythingIsScrewedUpMode()) {
			posX = posZ = 0.5D;
			posX = 0.0D;
			posZ = 0.0D;
			rotationYaw = ticksExisted / 12.0F;
			rotationPitch = 10.0F;
			posY = 68.5D;
		} else {
			prevTimeInPortal = timeInPortal;

			if (inPortal) {
				if (mc.currentScreen != null) {
					// mc.displayGuiScreen((GuiScreen) null); Removed so you can
					// do shit.
				}

				if (timeInPortal == 0.0F) {
					mc.getSoundHandler().playSound(
							PositionedSoundRecord.func_147674_a(
									new ResourceLocation("portal.trigger"),
									rand.nextFloat() * 0.4F + 0.8F));
				}

				timeInPortal += 0.0125F;

				if (timeInPortal >= 0.3F) {
					timeInPortal = 1.0F;
				}

				inPortal = false;
			} else {
				if (timeInPortal > 0.0F) {
					timeInPortal -= 0.05F;
				}

				if (timeInPortal < 0.0F) {
					timeInPortal = 0.0F;
				}
			}

			// TeamBattleClient
			// } else if (this.isPotionActive(Potion.confusion)
			// && getActivePotionEffect(Potion.confusion).getDuration() > 60) {
			// timeInPortal += 0.006666667F;
			//
			// if (timeInPortal > 1.0F) {
			// timeInPortal = 1.0F;
			// }
			// }

			if (timeUntilPortal > 0) {
				--timeUntilPortal;
			}

			final boolean var1 = movementInput.jump;
			final float var2 = 0.8F;
			final boolean var3 = movementInput.moveForward >= var2;
			movementInput.updatePlayerMoveState();

			if (movementInput.sneak && ySize < 0.2F) {
				ySize = 0.2F;
			}

			func_145771_j(posX - width * 0.35D, boundingBox.minY + 0.5D, posZ
					+ width * 0.35D);
			func_145771_j(posX - width * 0.35D, boundingBox.minY + 0.5D, posZ
					- width * 0.35D);
			func_145771_j(posX + width * 0.35D, boundingBox.minY + 0.5D, posZ
					- width * 0.35D);
			func_145771_j(posX + width * 0.35D, boundingBox.minY + 0.5D, posZ
					+ width * 0.35D);
			final boolean var4 = getFoodStats().getFoodLevel() > 6.0F
					|| capabilities.allowFlying;

			if (onGround && !var3 && movementInput.moveForward >= var2
					&& !isSprinting() && var4 && !isUsingItem()
					&& !this.isPotionActive(Potion.blindness)) {
				if (sprintToggleTimer <= 0
						&& !mc.gameSettings.keyBindSprint.getIsKeyPressed()) {
					sprintToggleTimer = 7;
				} else {
					setSprinting(true);
				}
			}

			if (!isSprinting() && movementInput.moveForward >= var2 && var4
					&& !isUsingItem() && !this.isPotionActive(Potion.blindness)
					&& mc.gameSettings.keyBindSprint.getIsKeyPressed()) {
				setSprinting(true);
			}

			if (isSprinting()
					&& (movementInput.moveForward < var2
							|| isCollidedHorizontally || !var4)) {
				setSprinting(false);
			}

			if (capabilities.allowFlying && !var1 && movementInput.jump) {
				if (flyToggleTimer == 0) {
					flyToggleTimer = 7;
				} else {
					capabilities.isFlying = !capabilities.isFlying;
					sendPlayerAbilities();
					flyToggleTimer = 0;
				}
			}

			if (capabilities.isFlying) {
				if (movementInput.sneak) {
					motionY -= 0.15D;
				}

				if (movementInput.jump) {
					motionY += 0.15D;
				}
			}

			if (isRidingHorse()) {
				if (horseJumpPowerCounter < 0) {
					++horseJumpPowerCounter;

					if (horseJumpPowerCounter == 0) {
						horseJumpPower = 0.0F;
					}
				}

				if (var1 && !movementInput.jump) {
					horseJumpPowerCounter = -10;
					func_110318_g();
				} else if (!var1 && movementInput.jump) {
					horseJumpPowerCounter = 0;
					horseJumpPower = 0.0F;
				} else if (var1) {
					++horseJumpPowerCounter;

					if (horseJumpPowerCounter < 10) {
						horseJumpPower = horseJumpPowerCounter * 0.1F;
					} else {
						horseJumpPower = 0.8F + 2.0F / (horseJumpPowerCounter - 9) * 0.1F;
					}
				}
			} else {
				horseJumpPower = 0.0F;
			}

			super.onLivingUpdate();

			if (onGround && capabilities.isFlying) {
				capabilities.isFlying = false;
				sendPlayerAbilities();
			}
		}
	}

	@Override
	public void playSound(String p_85030_1_, float p_85030_2_, float p_85030_3_) {
		worldObj.playSound(posX, posY - yOffset, posZ, p_85030_1_, p_85030_2_,
				p_85030_3_, false);
	}

	/**
	 * Updates health locally.
	 */
	public void setPlayerSPHealth(float p_71150_1_) {
		final float var2 = getHealth() - p_71150_1_;

		if (var2 <= 0.0F) {
			setHealth(p_71150_1_);

			if (var2 < 0.0F) {
				hurtResistantTime = maxHurtResistantTime / 2;
			}
		} else {
			lastDamage = var2;
			setHealth(getHealth());
			hurtResistantTime = maxHurtResistantTime;
			damageEntity(DamageSource.generic, var2);
			hurtTime = maxHurtTime = 10;
		}
	}

	/**
	 * Set sprinting switch for Entity.
	 */
	@Override
	public void setSprinting(boolean p_70031_1_) {
		super.setSprinting(p_70031_1_);
		sprintingTicksLeft = p_70031_1_ ? 600 : 0;
	}

	/**
	 * Sets the current XP, total XP, and level number.
	 */
	public void setXPStats(float p_71152_1_, int p_71152_2_, int p_71152_3_) {
		experience = p_71152_1_;
		experienceTotal = p_71152_2_;
		experienceLevel = p_71152_3_;
	}

	@Override
	public void updateEntityActionState() {
		super.updateEntityActionState();
		moveStrafing = movementInput.moveStrafe;
		moveForward = movementInput.moveForward;
		isJumping = movementInput.jump;
		prevRenderArmYaw = renderArmYaw;
		prevRenderArmPitch = renderArmPitch;
		renderArmPitch = (float) (renderArmPitch + (rotationPitch - renderArmPitch) * 0.5D);
		renderArmYaw = (float) (renderArmYaw + (rotationYaw - renderArmYaw) * 0.5D);
	}
}
