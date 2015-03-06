package net.minecraft.src;

import java.util.AbstractSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.minecraft.util.LongHashMap;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.NextTickListEntry;

public class NextTickHashSet extends AbstractSet {
	private final HashSet emptySet = new HashSet();
	private final LongHashMap longHashMap = new LongHashMap();
	private int size = 0;

	public NextTickHashSet(Set oldSet) {
		addAll(oldSet);
	}

	@Override
	public boolean add(Object obj) {
		if (!(obj instanceof NextTickListEntry))
			return false;
		else {
			final NextTickListEntry entry = (NextTickListEntry) obj;

			final long key = ChunkCoordIntPair.chunkXZ2Int(entry.xCoord >> 4,
					entry.zCoord >> 4);
			HashSet set = (HashSet) longHashMap.getValueByKey(key);

			if (set == null) {
				set = new HashSet();
				longHashMap.add(key, set);
			}

			final boolean added = set.add(entry);

			if (added) {
				++size;
			}

			return added;
		}
	}

	@Override
	public boolean contains(Object obj) {
		if (!(obj instanceof NextTickListEntry))
			return false;
		else {
			final NextTickListEntry entry = (NextTickListEntry) obj;

			final long key = ChunkCoordIntPair.chunkXZ2Int(entry.xCoord >> 4,
					entry.zCoord >> 4);
			final HashSet set = (HashSet) longHashMap.getValueByKey(key);
			return set == null ? false : set.contains(entry);
		}
	}

	public Iterator getNextTickEntries(int chunkX, int chunkZ) {
		final HashSet set = getNextTickEntriesSet(chunkX, chunkZ);
		return set.iterator();
	}

	public HashSet getNextTickEntriesSet(int chunkX, int chunkZ) {
		final long key = ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ);
		HashSet set = (HashSet) longHashMap.getValueByKey(key);

		if (set == null) {
			set = emptySet;
		}

		return set;
	}

	@Override
	public Iterator iterator() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public boolean remove(Object obj) {
		if (!(obj instanceof NextTickListEntry))
			return false;
		else {
			final NextTickListEntry entry = (NextTickListEntry) obj;

			final long key = ChunkCoordIntPair.chunkXZ2Int(entry.xCoord >> 4,
					entry.zCoord >> 4);
			final HashSet set = (HashSet) longHashMap.getValueByKey(key);

			if (set == null)
				return false;
			else {
				final boolean removed = set.remove(entry);

				if (removed) {
					--size;
				}

				return removed;
			}
		}
	}

	@Override
	public int size() {
		return size;
	}
}
