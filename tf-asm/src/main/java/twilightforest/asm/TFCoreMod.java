package twilightforest.asm;

import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.ITransformerVotingContext;
import cpw.mods.modlauncher.api.TargetType;
import cpw.mods.modlauncher.api.TransformerVoteResult;
import net.neoforged.coremod.api.ASMAPI;
import net.neoforged.neoforgespi.coremod.ICoreMod;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;
import java.util.Set;

public class TFCoreMod implements ICoreMod {
	@Override
	public Iterable<? extends ITransformer<?>> getTransformers() {
		return List.of(new ITransformer<MethodNode>() {
			@Override
			public @NotNull MethodNode transform(MethodNode node, ITransformerVotingContext context) {
				node.instructions.insertBefore(
					ASMAPI.findFirstInstruction(node, Opcodes.ALOAD),
					new MethodInsnNode(Opcodes.INVOKESTATIC, "twilightforest/ASMHooks", "test", "()V")
				);
				return node;
			}

			@Override
			public @NotNull TransformerVoteResult castVote(ITransformerVotingContext context) {
				return TransformerVoteResult.YES;
			}

			@Override
			public @NotNull Set<Target<MethodNode>> targets() {
				return Set.of(Target.targetMethod("net.minecraft.client.Minecraft", "<init>", "(Lnet/minecraft/client/main/GameConfig;)V"));
			}

			@Override
			public @NotNull TargetType<MethodNode> getTargetType() {
				return TargetType.METHOD;
			}
		});
	}
}