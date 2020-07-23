package io.github.cottonmc.libcd.api.util.nbt;

import net.minecraft.class_2479;
import net.minecraft.class_2481;
import net.minecraft.class_2489;
import net.minecraft.class_2494;
import net.minecraft.class_2495;
import net.minecraft.class_2497;
import net.minecraft.class_2499;
import net.minecraft.class_2501;
import net.minecraft.class_2503;
import net.minecraft.class_2516;
import net.minecraft.class_2519;
import net.minecraft.nbt.*;

import java.util.Arrays;
import java.util.List;

/**
 * A wrapper for the ListTag class, since that's obfuscated.
 */
public class WrappedListTag {
	private class_2499 underlying;

	public WrappedListTag(class_2499 underlying) {
		this.underlying = underlying;
	}

	/**
	 * @return A new, empty list tag.
	 */
	public static WrappedListTag create() {
		return new WrappedListTag(new class_2499());
	}

	/**
	 * @param orig The list to add into the tag.
	 * @return a new list tag containing tagified forms of the passed list.
	 */
	public static WrappedListTag create(List orig) {
		class_2499 ret = new class_2499();
		for (Object o : orig) {
			ret.method_10533(ret.size(), NbtUtils.getTagFor(o));
		}
		return new WrappedListTag(ret);
	}

	/**
	 * @param orig The array to add into the tag.
	 * @return a new list tag containing tagified forms of the passed array.
	 */
	public static WrappedListTag create(Object[] orig) {
		class_2499 ret = new class_2499();
		for (Object o : orig) {
			ret.method_10533(ret.size(), NbtUtils.getTagFor(o));
		}
		return new WrappedListTag(ret);
	}

	/**
	 * @return The underlying list tag. Only call from java.
	 */
	public class_2499 getUnderlying() {
		return underlying;
	}

	/**
	 * List tags can only accept one type of object tag.
	 * @return The type of tag the list will accept.
	 */
	public String getListType() {
		return NbtUtils.getTypeName(underlying.method_10601());
	}

	/**
	 * @return How many elements are in the list.
	 */
	public int getSize() {
		return underlying.size();
	}

	/**
	 * Get an object from the list.
	 * @param index The index of the item to get.
	 * @return The object form of the tag at that point.
	 */
	public Object get(int index) {
		return NbtUtils.getObjectFor(underlying.method_10534(index));
	}

	/**
	 * Set an object in the list. Will override.
	 * @param index The index of the item to set.
	 * @param value The object form to add in
	 * @return Whether the object could be successfully added.
	 */
	public boolean set(int index, Object value) {
		switch(underlying.method_10601()) {
			case 0:
				return underlying.method_10535(index, NbtUtils.getTagFor(value));
			case 1:
				if (value instanceof Byte) {
					return underlying.method_10535(index, class_2481.method_23233((Byte)value));
				} else return false;
			case 2:
				if (value instanceof Short) return underlying.method_10535(index, class_2516.method_23254((Short)value));
				 else return false;
			case 3:
				if (value instanceof Integer) return underlying.method_10535(index, class_2497.method_23247((Integer)value));
				else return false;
			case 4:
				if (value instanceof Long) return underlying.method_10535(index, class_2503.method_23251((Long)value));
				else return false;
			case 5:
				if (value instanceof Float) return underlying.method_10535(index, class_2494.method_23244((Float)value));
				else return false;
			case 6:
				if (value instanceof Double) return underlying.method_10535(index, class_2489.method_23241((Double)value));
				else return false;
			case 7:
				if (value instanceof Byte[]) return underlying.method_10535(index, new class_2479(Arrays.asList((Byte[])value)));
				else return false;
			case 8:
				if (value instanceof String) return underlying.method_10535(index, class_2519.method_23256((String)value));
				else return false;
			case 9:
				if (value instanceof WrappedListTag) return underlying.method_10535(index, ((WrappedListTag)value).getUnderlying());
				else return false;
			case 10:
				if (value instanceof WrappedCompoundTag) return underlying.method_10535(index, ((WrappedCompoundTag)value).getUnderlying());
				else return false;
			case 11:
				if (value instanceof Integer[]) return underlying.method_10535(index, new class_2495(Arrays.asList((Integer[])value)));
				else return false;
			case 12:
				if (value instanceof Long[]) return underlying.method_10535(index, new class_2501(Arrays.asList((Long[])value)));
				else return false;
		}
		return false;
	}

	/**
	 * Add an element to the end of the list.
	 * @param value The object to add.
	 * @return Whether the tagified object could be added.
	 */
	public boolean add(Object value) {
		return add(getSize(), value);
	}

	/**
	 * Add an element to the list, at a certain index. Will not override.
	 * @param index The index to add at.
	 * @param value The object to add.
	 * @return Whether the tagified object could be added.
	 */
	public boolean add(int index, Object value) {
		switch(underlying.method_10601()) {
			case 0:
				return underlying.method_10533(index, NbtUtils.getTagFor(value));
			case 1:
				if (value instanceof Byte) {
					return underlying.method_10533(index, class_2481.method_23233((Byte)value));
				} else return false;
			case 2:
				if (value instanceof Short) return underlying.method_10533(index, class_2516.method_23254((Short)value));
				else return false;
			case 3:
				if (value instanceof Integer) return underlying.method_10533(index, class_2497.method_23247((Integer)value));
				else return false;
			case 4:
				if (value instanceof Long) return underlying.method_10533(index, class_2503.method_23251((Long)value));
				else return false;
			case 5:
				if (value instanceof Float) return underlying.method_10533(index, class_2494.method_23244((Float)value));
				else return false;
			case 6:
				if (value instanceof Double) return underlying.method_10533(index, class_2489.method_23241((Double)value));
				else return false;
			case 7:
				if (value instanceof Byte[]) return underlying.method_10533(index, new class_2479(Arrays.asList((Byte[])value)));
				else return false;
			case 8:
				if (value instanceof String) return underlying.method_10533(index, class_2519.method_23256((String)value));
				else return false;
			case 9:
				if (value instanceof WrappedListTag) return underlying.method_10533(index, ((WrappedListTag)value).getUnderlying());
				else return false;
			case 10:
				if (value instanceof WrappedCompoundTag) return underlying.method_10533(index, ((WrappedCompoundTag)value).getUnderlying());
				else return false;
			case 11:
				if (value instanceof Integer[]) return underlying.method_10533(index, new class_2495(Arrays.asList((Integer[])value)));
				else return false;
			case 12:
				if (value instanceof Long[]) return underlying.method_10533(index, new class_2501(Arrays.asList((Long[])value)));
				else return false;
		}
		return false;
	}

	/**
	 * Remove an element from the list. If this is the last element, the list's type will be reset.
	 * @param index The index of the element to remove.
	 * @return The object form of the tag removed.
	 */
	public Object remove(int index) {
		return NbtUtils.getObjectFor(underlying.method_10536(index));
	}

	/**
	 * Empty all entries from the list, and reset its type.
	 */
	public void clear() {
		underlying.clear();
	}

	/**
	 * @return Whether there are no elements in the list.
	 */
	public boolean isEmpty() {
		return underlying.isEmpty();
	}

	/**
	 * @return The string form of the underlying list tag.
	 */
	public String toString() {
		return underlying.method_10714();
	}

	/**
	 * @return The hash code of the underlying list tag.
	 */
	public int hashCode() {
		return underlying.hashCode();
	}
}
