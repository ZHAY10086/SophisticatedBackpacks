package net.p3pp3rf1y.sophisticatedbackpacks.upgrades.filter;

import net.minecraft.item.ItemStack;
import net.p3pp3rf1y.sophisticatedbackpacks.api.IBackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.api.IIOFilterUpgrade;
import net.p3pp3rf1y.sophisticatedbackpacks.settings.memory.MemorySettingsCategory;
import net.p3pp3rf1y.sophisticatedbackpacks.upgrades.ContentsFilterLogic;
import net.p3pp3rf1y.sophisticatedbackpacks.upgrades.FilterLogic;
import net.p3pp3rf1y.sophisticatedbackpacks.upgrades.IContentsFilteredUpgrade;
import net.p3pp3rf1y.sophisticatedbackpacks.upgrades.UpgradeWrapperBase;
import net.p3pp3rf1y.sophisticatedbackpacks.util.NBTHelper;

import java.util.Optional;
import java.util.function.Consumer;

public class FilterUpgradeWrapper extends UpgradeWrapperBase<FilterUpgradeWrapper, FilterUpgradeItem> implements IContentsFilteredUpgrade, IIOFilterUpgrade {
	private final ContentsFilterLogic filterLogic;

	public FilterUpgradeWrapper(IBackpackWrapper backpackWrapper, ItemStack upgrade, Consumer<ItemStack> upgradeSaveHandler) {
		super(backpackWrapper, upgrade, upgradeSaveHandler);
		filterLogic = new ContentsFilterLogic(upgrade, upgradeSaveHandler, upgradeItem.getFilterSlotCount(), backpackWrapper::getInventoryHandler, backpackWrapper.getSettingsHandler().getTypeCategory(MemorySettingsCategory.class));
	}

	public void setDirection(Direction direction) {
		NBTHelper.setEnumConstant(upgrade, "direction", direction);
		save();
		backpackWrapper.refreshInventoryForInputOutput();
	}

	public Direction getDirection() {
		return NBTHelper.getEnumConstant(upgrade, "direction", Direction::fromName).orElse(Direction.BOTH);
	}

	@Override
	public ContentsFilterLogic getFilterLogic() {
		return filterLogic;
	}

	@Override
	public Optional<FilterLogic> getInputFilter() {
		Direction direction = getDirection();
		return direction == Direction.INPUT || direction == Direction.BOTH ? Optional.of(getFilterLogic()) : Optional.empty();
	}

	@Override
	public Optional<FilterLogic> getOutputFilter() {
		Direction direction = getDirection();
		return direction == Direction.OUTPUT || direction == Direction.BOTH ? Optional.of(getFilterLogic()) : Optional.empty();
	}
}
