package com.simibubi.create.content.contraptions.components.structureMovement.pulley;

import com.simibubi.create.AllBlockPartials;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.components.structureMovement.AbstractContraptionEntity;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.math.MathHelper;

public class PulleyRenderer extends AbstractPulleyRenderer {

	public PulleyRenderer(TileEntityRendererDispatcher dispatcher) {
		super(dispatcher, AllBlockPartials.ROPE_HALF, AllBlockPartials.ROPE_HALF_MAGNET);
	}

	@Override
	protected Axis getShaftAxis(KineticTileEntity te) {
		return te.getBlockState()
			.get(PulleyBlock.HORIZONTAL_AXIS);
	}

	@Override
	protected AllBlockPartials getCoil() {
		return AllBlockPartials.ROPE_COIL;
	}

	@Override
	protected SuperByteBuffer renderRope(KineticTileEntity te) {
		return CreateClient.bufferCache.renderBlock(AllBlocks.ROPE.getDefaultState());
	}

	@Override
	protected SuperByteBuffer renderMagnet(KineticTileEntity te) {
		return CreateClient.bufferCache.renderBlock(AllBlocks.PULLEY_MAGNET.getDefaultState());
	}

	@Override
	protected float getOffset(KineticTileEntity te, float partialTicks) {
		PulleyTileEntity pulley = (PulleyTileEntity) te;
		float offset = pulley.getInterpolatedOffset(partialTicks);

		if (pulley.movedContraption != null) {
			AbstractContraptionEntity e = pulley.movedContraption;
			PulleyContraption c = (PulleyContraption) pulley.movedContraption.getContraption();
			double entityPos = MathHelper.lerp(partialTicks, e.lastTickPosY, e.getY());
			offset = (float) -(entityPos - c.anchor.getY() - c.initialOffset);
		}

		return offset;
	}

	@Override
	protected boolean isRunning(KineticTileEntity te) {
		return ((PulleyTileEntity) te).running || te.isVirtual();
	}

}
