package com.blazeloader.TerrainEdit.main;

import com.blazeloader.api.api.block.ApiBlock;
import com.blazeloader.api.api.block.NotificationType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

/**
 * Block compatibility layer for TE to simplify updates
 * TODO: Add BlockPos cache.  Not that Mojang does it, though...
 * TODO: Integrate into BlazeLoader
 */
public class BlockAccess {

    public static IBlockState getBlockStateAt(World world, int x, int y, int z) {
        return getBlockStateAt(world, new BlockPos(x, y, z));
    }

    public static IBlockState getBlockStateAt(World world, BlockPos pos) {
        return ApiBlock.getBlockAt(world, pos);
    }

    public static Block getBlockTypeAt(World world, BlockPos pos) {
        return getBlockStateAt(world, pos).getBlock();
    }

    public static Block getBlockTypeAt(World world, int x, int y, int z) {
        return getBlockTypeAt(world, new BlockPos(x, y, z));
    }

    public static int getBlockDataAt(World world, BlockPos pos) {
        IBlockState state = getBlockStateAt(world, pos);
        return state.getBlock().getMetaFromState(state);
    }

    public static int getBlockDataAt(World world, int x, int y, int z) {
        return getBlockDataAt(world, new BlockPos(x, y, z));
    }

    public static void setBlockTypeAt(World world, BlockPos pos, Block block) {
        setBlockTypeAt(world, pos, block, NotificationType.BLOCK_UPDATE, NotificationType.NOTIFY_CLIENTS);
    }

    public static void setBlockTypeAt(World world, int x, int y, int z, Block block) {
        setBlockTypeAt(world, x, y, z, block, NotificationType.BLOCK_UPDATE, NotificationType.NOTIFY_CLIENTS);
    }

    public static void setBlockDataAt(World world, BlockPos pos, int data) {
        setBlockDataAt(world, pos, data, NotificationType.BLOCK_UPDATE, NotificationType.NOTIFY_CLIENTS);
    }

    public static void setBlockDataAt(World world, int x, int y, int z, int data) {
        setBlockDataAt(world, x, y, z, data, NotificationType.BLOCK_UPDATE, NotificationType.NOTIFY_CLIENTS);
    }

    public static void setBlockStateAt(World world, int x, int y, int z, IBlockState block) {
        setBlockStateAt(world, new BlockPos(x, y, z), block);
    }

    public static void setBlockTypeAt(World world, BlockPos pos, Block block, NotificationType... flags) {
        setBlockStateAt(world, pos, block.getDefaultState(), flags);
    }

    public static void setBlockTypeAt(World world, int x, int y, int z, Block block, NotificationType... flags) {
        setBlockTypeAt(world, new BlockPos(x, y, z), block, flags);
    }

    public static void setBlockDataAt(World world, BlockPos pos, int data, NotificationType... flags) {
        IBlockState state = getBlockStateAt(world, pos);
        setBlockStateAt(world, pos, state.getBlock().getStateFromMeta(data), flags);
    }

    public static void setBlockDataAt(World world, int x, int y, int z, int data, NotificationType... flags) {
        setBlockDataAt(world, new BlockPos(x, y, z), data, flags);
    }

    public static void setBlockStateAt(World world, BlockPos pos, IBlockState block) {
        setBlockStateAt(world, pos, block, NotificationType.BLOCK_UPDATE, NotificationType.NOTIFY_CLIENTS);
    }

    public static void setBlockStateAt(World world, int x, int y, int z, IBlockState block, NotificationType... flags) {
        setBlockStateAt(world, new BlockPos(x, y, z), block, flags);
    }

    public static void setBlockStateAt(World world, BlockPos pos, IBlockState block, NotificationType... flags) {
        int flgs = 0;
        for (NotificationType type : flags) {
            flgs += type.getType();
        }
        ApiBlock.setBlockAt(world, pos, block, flgs);
    }

    public static void setBlockAt(World world, BlockPos pos, Block block, int meta, NotificationType... flags) {
        setBlockStateAt(world, pos, block.getStateFromMeta(meta), flags);
    }

    public static void setBlockAt(World world, int x, int y, int z, Block block, int meta, NotificationType... flags) {
        setBlockAt(world, new BlockPos(x, y, z), block, meta, flags);
    }

    public static void setBlockAt(World world, BlockPos pos, Block block, int meta) {
        setBlockAt(world, pos, block, meta, NotificationType.BLOCK_UPDATE, NotificationType.NOTIFY_CLIENTS);
    }

    public static void setBlockAt(World world, int x, int y, int z, Block block, int meta) {
        setBlockAt(world, new BlockPos(x, y, z), block, meta);
    }
}
