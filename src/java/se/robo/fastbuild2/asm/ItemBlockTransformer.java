package se.robo.fastbuild2.asm;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.FLOAD;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.F_SAME;

import java.util.HashMap;
import java.util.Iterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.FrameNode;

import se.robo.fastbuild2.FastBuild2Core;
import net.minecraft.launchwrapper.IClassTransformer;

public class ItemBlockTransformer implements IClassTransformer {

	 private final HashMap<String, String> obfStrings;
	 private final HashMap<String, String> mcpStrings;
	
	public ItemBlockTransformer() {
		obfStrings = new HashMap<String, String>();
		obfStrings.put("className", "abh"); // net.minecraft.item.ItemBlock 
		obfStrings.put("javaClassName", "abh"); // net/minecraft/item/ItemBlock
		obfStrings.put("itemStackJavaClassName", "add"); // net/minecraft/item/ItemStack
		obfStrings.put("entityPlayerJavaClassName", "yz"); // net/minecraft/entity/player/EntityPlayer
		obfStrings.put("worldJavaClassName", "ahb"); // net/minecraft/world/World 
		obfStrings.put("targetMethodName", "a"); // onItemUse 
		obfStrings.put("targetMethodDesc", "(L" + obfStrings.get("itemStackJavaClassName") + ";L" + obfStrings.get("entityPlayerJavaClassName") + ";L" + obfStrings.get("worldJavaClassName") + ";IIIIFFF)Z"); 
		
		mcpStrings = new HashMap<String, String>();
		mcpStrings.put("className", "net.minecraft.item.ItemBlock");
		mcpStrings.put("javaClassName", "net/minecraft/item/ItemBlock");
		mcpStrings.put("itemStackJavaClassName", "net/minecraft/item/ItemStack");
		mcpStrings.put("entityPlayerJavaClassName", "net/minecraft/entity/player/EntityPlayer");
		mcpStrings.put("worldJavaClassName", "net/minecraft/world/World");
		mcpStrings.put("targetMethodName", "onItemUse");
		mcpStrings.put("targetMethodDesc", "(L" + mcpStrings.get("itemStackJavaClassName") + ";L" + mcpStrings.get("entityPlayerJavaClassName") + ";L" + mcpStrings.get("worldJavaClassName") + ";IIIIFFF)Z"); 
	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if (name.equals(obfStrings.get("className"))) {
			return transformItemInWorldManager(basicClass, obfStrings);
		} else if (name.equals(mcpStrings.get("className"))) {
			return transformItemInWorldManager(basicClass, mcpStrings);
		}
		return basicClass;
	}

	private byte[] transformItemInWorldManager(byte[] bytes, HashMap<String, String> hm) {
		FastBuild2Core.log.info("FastBuild2 ASM Magic Time!");
		FastBuild2Core.log.info("Class Transformation running on " + hm.get("javaClassName") + "...");
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(classNode, 0);
		
		// find method to inject into
		Iterator<MethodNode> methods = classNode.methods.iterator();
		while (methods.hasNext()) {
			MethodNode m = methods.next();
			if (m.name.equals(hm.get("targetMethodName")) && m.desc.equals(hm.get("targetMethodDesc"))) {
				FastBuild2Core.log.info("Found target method " + m.name + m.desc + "!");

				// Step to the first ALOAD
				 int offset = 1;
				 while (m.instructions.get(offset).getOpcode() != ALOAD)
					 offset++;
				 FastBuild2Core.log.info("Found ALOAD Node at offset " + offset + "!");
				 FastBuild2Core.log.info("Patching method " + hm.get("javaClassName") + "/" + m.name + m.desc + "...");
				 
				// make new instruction list
				InsnList toInject = new InsnList();

				// construct instruction nodes for list
				toInject.add(new MethodInsnNode(INVOKESTATIC, "se/robo/fastbuild2/BuildHandler", "canStart", "()Z", false));
				LabelNode label1 = new LabelNode(new Label());
				toInject.add(new JumpInsnNode(IFEQ, label1)); // if
				toInject.add(new VarInsnNode(ALOAD, 0)); // this
				toInject.add(new VarInsnNode(ALOAD, 1)); // ItemStack
				toInject.add(new VarInsnNode(ALOAD, 2)); // EntityPlayer
				toInject.add(new VarInsnNode(ALOAD, 3)); // World
				toInject.add(new VarInsnNode(ILOAD, 4)); // x
				toInject.add(new VarInsnNode(ILOAD, 5)); // y
				toInject.add(new VarInsnNode(ILOAD, 6)); // z
				toInject.add(new VarInsnNode(ILOAD, 7)); // side
				toInject.add(new VarInsnNode(FLOAD, 8)); // hitX
				toInject.add(new VarInsnNode(FLOAD, 9)); // hitY
				toInject.add(new VarInsnNode(FLOAD, 10));  // hitZ
				toInject.add(new MethodInsnNode(INVOKESTATIC, "se/robo/fastbuild2/BuildHandler", "build", 
						"(L"+hm.get("javaClassName")+";L"+hm.get("itemStackJavaClassName")+";L"+hm.get("entityPlayerJavaClassName")+";L"+hm.get("worldJavaClassName")+";IIIIFFF)Z", false));
				toInject.add(new InsnNode(IRETURN));
				toInject.add(label1);
				toInject.add(new FrameNode(F_SAME, 0, null, 0, null));
				
				m.instructions.insertBefore(m.instructions.get(offset), toInject);

			}
		}
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(writer);
		return writer.toByteArray();
	}
	
}
