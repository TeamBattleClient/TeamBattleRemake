package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

public class WrDisplayListAllocator {
	private int blockIndex = -1;
	private WrDisplayListBlock currentBlock = null;
	private final List<WrDisplayListBlock> listBlocks = new ArrayList();

	public int allocateDisplayLists(int len) {
		if (len > 0 && len <= 16384) {
			if (currentBlock == null || !currentBlock.canAllocate(len)) {
				if (blockIndex + 1 < listBlocks.size()) {
					++blockIndex;
					currentBlock = listBlocks.get(blockIndex);
				} else {
					currentBlock = new WrDisplayListBlock();
					blockIndex = listBlocks.size();
					listBlocks.add(currentBlock);
				}

				if (!currentBlock.canAllocate(len))
					throw new IllegalArgumentException("Can not allocate: "
							+ len);
			}

			return currentBlock.allocate(len);
		} else
			throw new IllegalArgumentException("Invalid display list length: "
					+ len);
	}

	public void deleteDisplayLists() {
		for (int i = 0; i < listBlocks.size(); ++i) {
			final WrDisplayListBlock block = listBlocks.get(i);
			block.deleteDisplayLists();
		}

		listBlocks.clear();
		currentBlock = null;
		blockIndex = -1;
	}

	public void resetAllocatedLists() {
		currentBlock = null;
		blockIndex = -1;

		for (int i = 0; i < listBlocks.size(); ++i) {
			final WrDisplayListBlock block = listBlocks.get(i);
			block.reset();
		}
	}
}
