package io.github.cottonmc.libcd.util.nbt;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import net.minecraft.class_2479;
import net.minecraft.class_2481;
import net.minecraft.class_2487;
import net.minecraft.class_2489;
import net.minecraft.class_2494;
import net.minecraft.class_2495;
import net.minecraft.class_2497;
import net.minecraft.class_2499;
import net.minecraft.class_2501;
import net.minecraft.class_2503;
import net.minecraft.class_2516;
import net.minecraft.class_2519;
import net.minecraft.class_2520;
import net.minecraft.nbt.*;

import java.util.Arrays;

public class NbtUtils {
	/**
	 * @param type The int type of the tag you want to get the name of.
	 * @return The string name of the tag type, in all lower case.
	 */
	public static String getTypeName(int type) {
		switch (type) {
			case 0:
				return "end";
			case 1:
				return "byte";
			case 2:
				return "short";
			case 3:
				return "int";
			case 4:
				return "long";
			case 5:
				return "float";
			case 6:
				return "double";
			case 7:
				return "byte array";
			case 8:
				return "string";
			case 9:
				return "list";
			case 10:
				return "compound";
			case 11:
				return "int array";
			case 12:
				return "long array";
			case 99:
				return "any number";
			default:
				return "unknown";
		}
	}

	/**
	 * @param type The string form of the tag type you want to get the magic number of. Not case sensitive.
	 * @return The int number of the tag type.
	 */
	public static int getTypeNumber(String type) {
		String lower = type.toLowerCase();
		switch(lower) {
			case "end":
				return 0;
			case "byte":
				return 1;
			case "short":
				return 2;
			case "int":
				return 3;
			case "long":
				return 4;
			case "float":
				return 5;
			case "double":
				return 6;
			case "byte array":
				return 7;
			case "string":
				return 8;
			case "list":
				return 9;
			case "compound":
				return 10;
			case "int array":
				return 11;
			case "long array":
				return 12;
			case "any number":
			case "number":
				return 99;
			default:
				return -1;
		}
	}

	/**
	 * @param value The object to get a tagified form of.
	 * @return The tagified form of the object.
	 */
	public static class_2520 getTagFor(Object value) {
		if (value == null) return null;
		if (value instanceof Byte) {
			return new class_2481((byte)value);
		} else if (value instanceof Boolean) {
			return new class_2481((byte)((boolean)value ? 1 : 0));
		} else if (value instanceof Short) {
			return new class_2516((short)value);
		} else if (value instanceof Integer) {
			return new class_2497((int)value);
		} else if (value instanceof Long) {
			return new class_2503((long)value);
		} else if (value instanceof Float) {
			return new class_2494((float)value);
		} else if (value instanceof Double) {
			return new class_2489((double) value);
		} else if (value instanceof Byte[]) {
			return new class_2479(Arrays.asList((Byte[])value));
		} else if (value instanceof String) {
			return new class_2519((String)value);
		} else if (value instanceof WrappedListTag) {
			return ((WrappedListTag)value).getUnderlying();
		} else if (value instanceof WrappedCompoundTag) {
			return ((WrappedCompoundTag) value).getUnderlying();
		} else if (value instanceof Integer[]) {
			return new class_2495(Arrays.asList((Integer[])value));
		} else if (value instanceof Long[]) {
			return new class_2501(Arrays.asList((Long[])value));
		} else return new class_2519(value.toString());
	}

	/**
	 * @param tag The tag to get the object form of.
	 * @return The object form of the tag.
	 */
	public static Object getObjectFor(class_2520 tag) {
		if (tag == null) return null;
		if (tag instanceof class_2481) return ((class_2481)tag).method_10698();
		else if (tag instanceof class_2516) return ((class_2516)tag).method_10696();
		else if (tag instanceof class_2497) {
			return ((class_2497)tag).method_10701();
		} else if (tag instanceof class_2503) {
			return ((class_2503)tag).method_10699();
		} else if (tag instanceof class_2494) {
			return ((class_2494)tag).method_10700();
		} else if (tag instanceof class_2489) {
			return ((class_2489)tag).method_10697();
		} else if (tag instanceof class_2479) {
			return ((class_2479)tag).method_10521();
		} else if (tag instanceof class_2519) {
			return tag.method_10714();
		} else if (tag instanceof class_2499) {
			return new WrappedListTag((class_2499)tag);
		} else if (tag instanceof class_2487) {
			return new WrappedCompoundTag((class_2487)tag);
		} else if (tag instanceof class_2495) {
			class_2497[] tags = ((class_2495)tag).toArray(new class_2497[0]);
			IntList ret = new IntArrayList();
			for (class_2497 intTag : tags) {
				ret.add(intTag.method_10701());
			}
			return ret.toArray(new int[0]);
		} else if (tag instanceof class_2501) {
			class_2503[] tags = ((class_2501)tag).toArray(new class_2503[0]);
			LongList ret = new LongArrayList();
			for (class_2503 longTag : tags) {
				ret.add(longTag.method_10699());
			}
			return ret.toArray(new long[0]);
		} else return tag.method_10714();
	}
}
