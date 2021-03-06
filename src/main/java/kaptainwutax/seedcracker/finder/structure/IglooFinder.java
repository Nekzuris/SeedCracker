package kaptainwutax.seedcracker.finder.structure;

import kaptainwutax.seedcracker.SeedCracker;
import kaptainwutax.seedcracker.cracker.structure.StructureData;
import kaptainwutax.seedcracker.cracker.structure.StructureFeatures;
import kaptainwutax.seedcracker.finder.Finder;
import kaptainwutax.seedcracker.render.Cube;
import kaptainwutax.seedcracker.render.Cuboid;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.Vector4f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IglooFinder extends AbstractTempleFinder {

    protected static List<BlockPos> SEARCH_POSITIONS = buildSearchPositions(CHUNK_POSITIONS, pos -> {
        return false;
    });

    public IglooFinder(World world, ChunkPos chunkPos) {
        super(world, chunkPos, new Vec3i(7, 5, 8));

        //Igloos are weird.
        this.finders.forEach(finder -> {
            finder.searchPositions = SEARCH_POSITIONS;
        });
    }

    @Override
    public List<BlockPos> findInChunk() {
        Map<PieceFinder, List<BlockPos>> result = this.findInChunkPieces();
        List<BlockPos> combinedResult = new ArrayList<>();

        result.forEach((pieceFinder, positions) -> {
            combinedResult.addAll(positions);

            positions.forEach(pos -> {
                if(SeedCracker.get().onStructureData(new StructureData(this.chunkPos, StructureFeatures.IGLOO))) {
                    this.renderers.add(new Cuboid(pos, pieceFinder.getLayout(), new Vector4f(0.0f, 1.0f, 1.0f, 1.0f)));
                    this.renderers.add(new Cube(pos, new Vector4f(0.0f, 1.0f, 1.0f, 1.0f)));
                }
            });
        });

        return combinedResult;
    }

    @Override
    public void buildStructure(PieceFinder finder) {
        BlockState air = Blocks.AIR.getDefaultState();
        BlockState snow = Blocks.SNOW_BLOCK.getDefaultState();
        BlockState ice = Blocks.ICE.getDefaultState();

        for(int y = 0; y < 3; y++) {
            finder.addBlock(snow, 2, y, 0);
            finder.addBlock(snow, 2, y, 1);
            finder.addBlock(snow, 1, y, 2);
            finder.addBlock(snow, 0, y, 3);
            finder.addBlock(snow, 0, y, 4);
            finder.addBlock(ice, 0, 1, 4);
            finder.addBlock(snow, 0, y, 5);
            finder.addBlock(snow, 1, y, 6);
            finder.addBlock(snow, 2, y, 7);

            finder.addBlock(snow, 3, y, 7);

            finder.addBlock(snow, 4, y, 0);
            finder.addBlock(snow, 4, y, 1);
            finder.addBlock(snow, 5, y, 2);
            finder.addBlock(snow, 6, y, 3);
            finder.addBlock(snow, 6, y, 4);
            finder.addBlock(ice, 6, 1, 4);
            finder.addBlock(snow, 6, y, 5);
            finder.addBlock(snow, 5, y, 6);
            finder.addBlock(snow, 4, y, 7);
        }
    }

    @Override
    protected StructureFeature<?> getStructureFeature() {
        return Feature.IGLOO;
    }

    @Override
    public boolean isValidDimension(DimensionType dimension) {
        return dimension == DimensionType.OVERWORLD;
    }

    public static List<Finder> create(World world, ChunkPos chunkPos) {
        List<Finder> finders = new ArrayList<>();
        finders.add(new IglooFinder(world, chunkPos));
        return finders;
    }

}
