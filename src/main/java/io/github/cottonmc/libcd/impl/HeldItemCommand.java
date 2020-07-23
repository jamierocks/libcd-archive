package io.github.cottonmc.libcd.impl;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.class_1799;
import net.minecraft.class_2168;
import net.minecraft.class_2378;
import net.minecraft.class_2487;
import net.minecraft.class_2558;
import net.minecraft.class_2561;
import net.minecraft.class_2568;
import net.minecraft.class_2583;
import net.minecraft.class_2585;
import net.minecraft.class_2960;
import net.minecraft.class_3222;

public class HeldItemCommand implements Command<class_2168> {

	@Override
	public int run(CommandContext<class_2168> context) throws CommandSyntaxException {
		class_3222 player = context.getSource().method_9207();
		class_1799 toDescribe = player.method_6047();
		StringBuilder description = new StringBuilder();
		class_2960 id = class_2378.field_11142.method_10221(toDescribe.method_7909());
		String idString = (id==null) ? "unknown" : id.toString();
		description.append(idString);
		
		class_2561 feedback = new class_2585(idString);
		
		class_2487 tag = toDescribe.method_7969();
		if (tag!=null) {
			description.append(tag.method_10714());
			feedback.method_10852(tag.method_10715());
		}
		
		class_2583 clickableStyle = new class_2583()
			.method_10949(new class_2568(class_2568.class_2569.field_11762, new class_2585("Click to put this text in the chat bar")))
			.method_10958(new class_2558(class_2558.class_2559.field_11745, description.toString()));
		
		feedback.method_10862(clickableStyle);
		player.method_9203(feedback);
		
		return 1;
	}

}
