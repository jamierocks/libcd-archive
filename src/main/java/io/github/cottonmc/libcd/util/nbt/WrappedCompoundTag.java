package io.github.cottonmc.libcd.util.nbt;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.class_2487;
import net.minecraft.class_2499;
import net.minecraft.class_2520;
import java.util.List;
import java.util.UUID;

/**
 * A wrapper for the CompoundTag class, since that's obfuscated.
 */
public class WrappedCompoundTag {
	private class_2487 underlying;

	public WrappedCompoundTag(class_2487 underlying) {
		this.underlying = underlying;
	}

	/**
	 * @return A new, empty compound tag.
	 */
	public static WrappedCompoundTag create() {
		return new WrappedCompoundTag(new class_2487());
	}

	/**
	 * @return The underlying compound tag. Only call from Java.
	 */
	public class_2487 getUnderlying() {
		return underlying;
	}

	/**
	 * @return an array of all the keys in the tag.
	 */
	public String[] getKeys() {
		return underlying.method_10541().toArray(new String[0]);
	}

	/**
	 * @param key The key to check.
	 * @return The type of the key.
	 */
	public String getType(String key) {
		return NbtUtils.getTypeName(underlying.method_10540(key));
	}

	/**
	 * @param key The key to check for.
	 * @return Whether the tag has this key.
	 */
	public boolean hasTag(String key) {
		return underlying.method_10545(key);
	}

	/**
	 * @param key The key to check for.
	 * @param type The type to check for. Must match the values in {@link WrappedCompoundTag#getType(String)} exactly.
	 * @return Whether the tag has this key, and the key has this type.
	 */
	public boolean hasTag(String key, String type) {
		return underlying.method_10545(key) && getType(key).equals(type);
	}

	/**
	 * @param key The key to check for.
	 * @param type The type to check for, as its raw int. You can use {@link net.fabricmc.fabric.api.util.NbtType} for this.
	 * @return Whether the tag has this key, and the key has this type.
	 */
	public boolean hasTag(String key, int type) {
		return underlying.method_10573(key, type);
	}

	/**
	 * @param key The tag to get.
	 * @return The string form of this tag.
	 */
	public Object getTag(String key) {
		return NbtUtils.getObjectFor(underlying.method_10580(key));
	}

	/**
	 * Get the byte in the tag.
	 * @param key The key to get from..
	 * @return The value of the byte tag with this key, or 0.
	 */
	public byte getByte(String key) {
		return underlying.method_10571(key);
	}

	/**
	 * Insert a byte into the tag.
	 * @param key The key to insert at.
	 * @param value The byte to insert.
	 */
	public void putByte(String key, byte value) {
		underlying.method_10567(key, value);
	}

	/**
	 * Get a boolean in the tag. Saved as a byte.
	 * @param key The key to get from.
	 * @return The value of the boolean tag with this key, or false.
	 */
	public boolean getBoolean(String key) {
		return underlying.method_10577(key);
	}

	/**
	 * Insert a boolean into the tag. Saved as a byte.
	 * @param key The key to insert at.
	 * @param value The boolean to insert.
	 */
	public void putBoolean(String key, boolean value) {
		underlying.method_10556(key, value);
	}

	/**
	 * Get a short in the tag.
	 * @param key The key to get from.
	 * @return The value of the boolean tag with this key, or 0.
	 */
	public short getShort(String key) {
		return underlying.method_10568(key);
	}

	/**
	 * Insert a short into the tag.
	 * @param key The key to insert at.
	 * @param value The short to insert.
	 */
	public void putShort(String key, short value) {
		underlying.method_10575(key, value);
	}

	/**
	 * Get an int in the tag.
	 * @param key The key to get from.
	 * @return The value of the int tag with this key, or 0.
	 */
	public int getInt(String key) {
		return underlying.method_10550(key);
	}

	/**
	 * Insert an int into the tag.
	 * @param key The key to insert at.
	 * @param value The int to insert.
	 */
	public void putInt(String key, int value) {
		underlying.method_10569(key, value);
	}

	/**
	 * Get a long in the tag.
	 * @param key The key to get from.
	 * @return The value of the long tag with this key, or 0.
	 */
	public long getLong(String key) {
		return underlying.method_10537(key);
	}

	/**
	 * Insert a long into the tag.
	 * @param key The key to insert at.
	 * @param value The long to insert.
	 */
	public void putLong(String key, long value) {
		underlying.method_10544(key, value);
	}

	/**
	 * Check if the tag has a UUID.
	 * @param key The key to check at.
	 * @return Whether the tag has a UUID with this key.
	 */
	public boolean hasUuid(String key) {
		return underlying.method_10576(key);
	}

	/**
	 * Get a UUID in the tag. Stored as two longs titled "<key>Most" and "<key>Least".
	 * @param key The key to get from.
	 * @return The value of the UUID tags with this key, or "00000000-0000-0000-0000-000000000000".
	 */
	public String getUuid(String key) {
		return underlying.method_10584(key).toString();
	}

	/**
	 * Insert a UUID into the tag. Saved as two longs titled "<key>Most" and "<key>Least".
	 * @param key The key to insert at.
	 * @param value The UUID to insert.
	 */
	public void putUuid(String key, String value) {
		underlying.method_10560(key, UUID.fromString(value));
	}

	/**
	 * Get a float in the tag.
	 * @param key The key to get from.
	 * @return The value of the float tag with this key, or 0.
	 */
	public float getFloat(String key) {
		return underlying.method_10583(key);
	}

	/**
	 * Insert a float into the tag.
	 * @param key The key to insert at.
	 * @param value The float to insert.
	 */
	public void putFloat(String key, float value) {
		underlying.method_10548(key, value);
	}

	/**
	 * Get a float in the tag.
	 * @param key The key to get from.
	 * @return The value of the long tag with this key, or 0.
	 */
	public double getDouble(String key) {
		return underlying.method_10574(key);
	}

	/**
	 * Insert a double into the tag.
	 * @param key The key to insert at.
	 * @param value The float to insert.
	 */
	public void putDouble(String key, double value) {
		underlying.method_10549(key, value);
	}

	/**
	 * Get an array of bytes in the tag.
	 * @param key The key to get from.
	 * @return The value of the byte array tag with this key, or [0].
	 */
	public byte[] getByteArray(String key) {
		return underlying.method_10547(key);
	}

	/**
	 * Insert an array of bytes into the tag.
	 * @param key The key to insert at.
	 * @param value The array of bytes to insert.
	 */
	public void putByteArray(String key, byte[] value) {
		underlying.method_10570(key, value);
	}

	/**
	 * Get a string in the tag.
	 * @param key The key to get from.
	 * @return The value of the string tag with this key, or "".
	 */
	public String getString(String key) {
		return underlying.method_10558(key);
	}

	/**
	 * Insert a string into the tag.
	 * @param key The key to insert at.
	 * @param value The string to insert.
	 */
	public void putString(String key, String value) {
		underlying.method_10582(key, value);
	}

	/**
	 * Check if the tag has a list of the given type.
	 * @param key The key to check at.
	 * @param type The name of the NBT type to check for.
	 * @return Whether a list at that tag and of that type exists.
	 */
	public boolean hasList(String key, String type) {
		return underlying.method_10573(key, NbtType.LIST) && ((class_2499)underlying.method_10580(key)).method_10601() == NbtUtils.getTypeNumber(type);
	}

	/**
	 * @param key The key to check.
	 * @return The type of the list at the given key, or "" if it's not a list.
	 */
	public String getListType(String key) {
		class_2520 tag = underlying.method_10580(key);
		if (tag instanceof class_2499) {
			return NbtUtils.getTypeName(((class_2499)tag).method_10601());
		} else return "";
	}

	/**
	 * Get a list in the tag, wrapped for ease of use in scripts.
	 * @param key The key to get from.
	 * @param type The type of the list to get.
	 * @return The wrapped form of the list tag at the given key and of the given type, or an empty wrapped list.
	 */
	public WrappedListTag getList(String key, String type) {
		return new WrappedListTag(underlying.method_10554(key, NbtUtils.getTypeNumber(type)));
	}

	/**
	 * Insert a list into the tag. Must be a wrapped list tag.
	 * To create a new empty list tag, call {@link WrappedListTag#create()}.
	 * To create a list tag from a pre-existing list or array,
	 * call {@link WrappedListTag#create(List)} or {@link WrappedListTag#create(Object[])}, respectively.
	 * @param key The key to insert at.
	 * @param value The wrapped list to insert.
	 */
	public void putList(String key, WrappedListTag value) {
		underlying.method_10566(key, value.getUnderlying());
	}

	/**
	 * Get a compound in the tag, wrapped for ease of use in scripts.
	 * @param key The key to get from.
	 * @return The wrapped form of the compound tag at this key, or an empty wrapped compound.
	 */
	public WrappedCompoundTag getCompound(String key) {
		return new WrappedCompoundTag(underlying.method_10562(key));
	}

	/**
	 * Insert a compound into the tag. Must be a wrapped compound tag.
	 * To create a new empty compound tag, call {@link WrappedCompoundTag#create()}.
	 * @param key The key to insert at.
	 * @param value The wrapped compound to insert.
	 */
	public void putCompound(String key, WrappedCompoundTag value) {
		underlying.method_10566(key, value.getUnderlying());
	}

	/**
	 * Get an int array in the tag.
	 * @param key The key to get from.
	 * @return The value of the int array tag with this key, or [0].
	 */
	public int[] getIntArray(String key) {
		return underlying.method_10561(key);
	}

	/**
	 * Insert an array of longs into the tag.
	 * @param key The key to insert at.
	 * @param value The array of ints to insert.
	 */
	public void putIntArray(String key, int[] value) {
		underlying.method_10539(key, value);
	}

	/**
	 * Get an array of longs in the tag.
	 * @param key The key to get from.
	 * @return The value of the long array tag with this key, or [0].
	 */
	public long[] getLongArray(String key) {
		return underlying.method_10565(key);
	}

	/**
	 * Insert an array of longs into the tag.
	 * @param key The key to insert at.
	 * @param value the array of longs to insert.
	 */
	public void putLongArray(String key, long[] value) {
		underlying.method_10564(key, value);
	}

	/**
	 * Remove a tag from the compound.
	 * @param key The key of the tag to remove.
	 */
	public void remove(String key) {
		underlying.method_10551(key);
	}

	/**
	 * Remove all keys from this tag.
	 */
	public void clear() {
		for (String key : getKeys()) {
			remove(key);
		}
	}

	/**
	 * @return Whether there are no tags in the compound.
	 */
	public boolean isEmpty() {
		return underlying.isEmpty();
	}

	/**
	 * @return The string form of the underlying compound tag.
	 */
	public String toString() {
		return underlying.method_10714();
	}

	/**
	 * @return The hash code of the underlying compound tag.
	 */
	public int hashCode() {
		return underlying.hashCode();
	}
}
