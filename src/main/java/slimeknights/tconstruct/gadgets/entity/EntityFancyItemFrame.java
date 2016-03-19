package slimeknights.tconstruct.gadgets.entity;

import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import io.netty.buffer.ByteBuf;
import slimeknights.tconstruct.gadgets.TinkerGadgets;

// exists solely to be distinguishable from the vanilla itemframe
// also because network handling requires us to recreate what vanilla does.. meh
public class EntityFancyItemFrame extends EntityItemFrame implements IEntityAdditionalSpawnData {

  private FrameType type;

  public EntityFancyItemFrame(World worldIn, BlockPos p_i45852_2_, EnumFacing p_i45852_3_, int meta) {
    this(worldIn, p_i45852_2_, p_i45852_3_, FrameType.fromMeta(meta));
  }

  public EntityFancyItemFrame(World worldIn, BlockPos p_i45852_2_, EnumFacing p_i45852_3_, FrameType type) {
    super(worldIn, p_i45852_2_, p_i45852_3_);
    this.type = type;
  }

  public EntityFancyItemFrame(World worldIn) {
    super(worldIn);
  }

  @Override
  public String getName() {
    if (this.hasCustomName())
    {
      return this.getCustomNameTag();
    }

    ItemStack foo = new ItemStack(TinkerGadgets.fancyFrame, 1, type.ordinal());
    return foo.getDisplayName();
  }

  @Override
  public void writeEntityToNBT(NBTTagCompound tagCompound) {
    int nr = type != null ? type.ordinal() : 0;
    tagCompound.setInteger("frame", nr);
    super.writeEntityToNBT(tagCompound);
  }

  @Override
  public void readEntityFromNBT(NBTTagCompound tagCompund) {
    int nr = tagCompund.getInteger("frame");
    type = FrameType.values()[nr % FrameType.values().length];

    super.readEntityFromNBT(tagCompund);
  }

  @Override
  public void writeSpawnData(ByteBuf buffer) {
    buffer.writeShort(this.facingDirection.getHorizontalIndex());
    buffer.writeShort(type != null ? this.type.ordinal() : 0);
  }

  @Override
  public void readSpawnData(ByteBuf additionalData) {
    EnumFacing facing = EnumFacing.getHorizontal(additionalData.readShort());
    updateFacingWithBoundingBox(facing);
    this.type = FrameType.values()[additionalData.readShort()];
  }

  public String getType() {
    if(type == null) {
      return FrameType.JEWEL.toString();
    }
    return type.toString();
  }

  public enum FrameType {
    JEWEL,
    ALUBRASS,
    COBALT,
    ARDITE,
    MANYULLYN,
    GOLD;

    public static FrameType fromMeta(int meta) {
      return FrameType.values()[meta % FrameType.values().length];
    }
  }
}
