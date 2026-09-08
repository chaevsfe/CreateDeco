package com.github.talrey.createdeco;

import com.github.talrey.createdeco.registrate.SharedProperties;
import com.github.talrey.createdeco.api.*;
import com.github.talrey.createdeco.blocks.*;
import com.zurrtum.create.AllItems;
import com.zurrtum.create.content.decoration.MetalLadderBlock;
import com.zurrtum.create.content.decoration.palettes.ConnectedGlassPaneBlock;
import com.zurrtum.create.content.decoration.palettes.ConnectedPillarBlock;
import com.zurrtum.create.content.decoration.palettes.WindowBlock;
import com.zurrtum.create.content.decoration.placard.PlacardBlock;
import com.github.talrey.createdeco.registrate.builders.BlockBuilder;
import com.github.talrey.createdeco.registrate.entry.BlockEntityEntry;
import com.github.talrey.createdeco.registrate.entry.BlockEntry;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.github.talrey.createdeco.api.CageLamps.*;

public class BlockRegistry {

	public static HashMap<DyeColor, String> BRICK_COLORS = new HashMap<>() {{
		put(DyeColor.LIGHT_BLUE, "blue");
		put(DyeColor.YELLOW, "dean");
		put(DyeColor.BLACK, "dusk");
		put(DyeColor.WHITE, "pearl");
		put(DyeColor.RED, "scarlet");
		put(DyeColor.GREEN, "verdant");
		put(DyeColor.BROWN, "umber");
		put(null, "red");
	}};

	public static HashMap<DyeColor, HashMap<String, BlockEntry<Block>>>      BRICKS = new HashMap<>();
	public static HashMap<DyeColor, HashMap<String, BlockEntry<StairBlock>>> STAIRS = new HashMap<>();
	public static HashMap<DyeColor, HashMap<String, BlockEntry<SlabBlock>>>   SLABS = new HashMap<>();
	public static HashMap<DyeColor, HashMap<String, BlockEntry<WallBlock>>>   WALLS= new HashMap<>();

	public static HashMap<String, BlockEntry<DecalBlock>> DECALS = new HashMap<>();
	public static HashMap<String, BlockEntry<CageLampBlock>> YELLOW_CAGE_LAMPS = new HashMap<>();
	public static HashMap<String, BlockEntry<CageLampBlock>>    RED_CAGE_LAMPS = new HashMap<>();
	public static HashMap<String, BlockEntry<CageLampBlock>>  GREEN_CAGE_LAMPS = new HashMap<>();
	public static HashMap<String, BlockEntry<CageLampBlock>>   BLUE_CAGE_LAMPS = new HashMap<>();

	public static HashMap<String, BlockEntry<WindowBlock>> WINDOWS      = new HashMap<>();
	public static HashMap<String, BlockEntry<WindowBlock>> WINDOW_PANES = new HashMap<>();
	public static HashMap<String, BlockEntry<DoorBlock>> DOORS          = new HashMap<>();
	public static HashMap<String, BlockEntry<DoorBlock>> LOCK_DOORS     = new HashMap<>();
	public static HashMap<String, BlockEntry<TrapDoorBlock>> TRAPDOORS  = new HashMap<>();
	public static HashMap<String, BlockEntry<IronBarsBlock>> BARS       = new HashMap<>();
	public static HashMap<String, BlockEntry<IronBarsBlock>> BAR_PANELS = new HashMap<>();
	public static HashMap<String, BlockEntry<MeshFenceBlock>> MESH_FENCES   = new HashMap<>();
	public static HashMap<String, BlockEntry<ConnectedPillarBlock>> SHEET_METAL_PILLARS = new HashMap<>();

	public static HashMap<String, BlockEntry<CatwalkBlock>> CATWALKS                = new HashMap<>();
	public static HashMap<String, BlockEntry<CatwalkStairBlock>> CATWALK_STAIRS     = new HashMap<>();
	public static HashMap<String, BlockEntry<CatwalkRailingBlock>> CATWALK_RAILINGS = new HashMap<>();

	public static HashMap<String, BlockEntry<MetalLadderBlock>> LADDERS = new HashMap<>();
	public static HashMap<String, BlockEntry<HullBlock>> HULLS          = new HashMap<>();
	public static HashMap<String, BlockEntry<SupportBlock>> SUPPORTS    = new HashMap<>();
	public static HashMap<String, BlockEntry<SupportWedgeBlock>> WEDGES = new HashMap<>();
	public static HashMap<String, BlockEntry<FacadeBlock>> FACADES      = new HashMap<>();

	public static HashMap<DyeColor, BlockEntry<? extends PlacardBlock>> PLACARDS = new HashMap<>();
	public static BlockEntityEntry<DyedPlacardBlock.Entity> PLACARD_ENTITIES;

	public static HashMap<String, BlockEntry<CoinStackBlock>> COIN_BLOCKS  = new HashMap<>();

	public static HashMap<DyeColor, BlockEntry<ShippingContainerBlock>> SHIPPING_CONTAINERS = new HashMap<>();
	public static HashMap<DyeColor, BlockEntityEntry<ShippingContainerBlock.Entity>> CONTAINER_ENTITIES = new HashMap<>();

	public static DyeColor fromName (String color) {
		for (DyeColor dye : BRICK_COLORS.keySet()) {
			if (BRICK_COLORS.get(dye).equals(color)) return dye;
		}
		return null;
	}

	public static void init() {
		// load the class and register everything
		CreateDecoMod.LOGGER.info("Registering blocks for " + CreateDecoMod.NAME);

		// Props registration
		CreateDecoMod.REGISTRATE.defaultCreativeTab(CreativeTabs.PROPS_KEY);

		ItemRegistry.METAL_TYPES.forEach(BlockRegistry::registerBars);
		ItemRegistry.METAL_TYPES.forEach(BlockRegistry::registerFences);
		ItemRegistry.METAL_TYPES.forEach(BlockRegistry::registerCatwalks);
		ItemRegistry.METAL_TYPES.forEach(BlockRegistry::registerLadders);
		ItemRegistry.METAL_TYPES.forEach(BlockRegistry::registerHulls);
		ItemRegistry.METAL_TYPES.forEach(BlockRegistry::registerSupports);
		ItemRegistry.METAL_TYPES.forEach(BlockRegistry::registerCageLamps);
		ItemRegistry.METAL_TYPES.forEach(BlockRegistry::registerSheetMetal);
		ItemRegistry.METAL_TYPES.forEach(BlockRegistry::registerDoors);
		registerShippingContainers();
		registerDecals();
		registerPlacards();
		ItemRegistry.COIN_METALS.forEach(BlockRegistry::registerCoins);

		// Bricks registration
		CreateDecoMod.REGISTRATE.defaultCreativeTab(CreativeTabs.BRICKS_KEY);
		registerBricks();
	}

	private static void registerBars (String metal, Function<String, Item> getter) {
		boolean postFlag = (metal.contains("Netherite")|| metal.contains("Industrial Iron"));

		BAR_PANELS.put(metal, Bars.build(CreateDecoMod.REGISTRATE, metal, "overlay", postFlag)
				.register());

		if (metal.equals("Iron")) return;

		BARS.put(metal, Bars.build(CreateDecoMod.REGISTRATE, metal, "", postFlag)
				.register());
	}

	private static void registerFences (String metal, Function<String, Item> getter) {
		MESH_FENCES.put(metal, MeshFences.build(CreateDecoMod.REGISTRATE, metal)
			.register());
	}

	private static void registerDecals () {
		ArrayList<BlockBuilder<DecalBlock, ?>> decals;
		decals = Decals.build(CreateDecoMod.REGISTRATE);
		decals.forEach(bb -> {
			//DECALS.putIfAbsent("null", new HashMap<>());
			DECALS.put(bb.getName(), bb.register());
		});
	}


	private static void registerCageLamps (String metal, Function<String, Item> getter) {
		Identifier cage = CreateDecoMod.id(
				"block/palettes/cage_lamp/"
						+ metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_lamp"
		);
		Supplier<Item> material = metal.equals("Andesite") ? () -> AllItems.ANDESITE_ALLOY : null;

		YELLOW_CAGE_LAMPS.put(metal, CageLamps.build(
						CreateDecoMod.REGISTRATE, metal, DyeColor.YELLOW, cage, YELLOW_ON, YELLOW_OFF
				)
				
				.register());
		RED_CAGE_LAMPS.put(metal, CageLamps.build(
						CreateDecoMod.REGISTRATE, metal, DyeColor.RED, cage, RED_ON, RED_OFF
				)
				
				.register());
		GREEN_CAGE_LAMPS.put(metal, CageLamps.build(
						CreateDecoMod.REGISTRATE, metal, DyeColor.GREEN, cage, GREEN_ON, GREEN_OFF
				)
				
				.register());
		BLUE_CAGE_LAMPS.put(metal, CageLamps.build(
						CreateDecoMod.REGISTRATE, metal, DyeColor.BLUE, cage, BLUE_ON, BLUE_OFF
				)
				
				.register());
	}

	private static void registerCatwalks (String metal, Function<String, Item> getter) {
		//String regName = metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_");
		CATWALKS.put(metal, Catwalks.build(
						CreateDecoMod.REGISTRATE, metal)
				
				.register());
		CATWALK_STAIRS.put(metal, Catwalks.buildStair(
						CreateDecoMod.REGISTRATE, metal)
				.register());
		CATWALK_RAILINGS.put(metal, Catwalks.buildRailing(
						CreateDecoMod.REGISTRATE, metal)
				.register());
		WEDGES.put(metal, Wedges.build(
						CreateDecoMod.REGISTRATE, metal)
				
				.register());
//		FACADES.put(metal, Facades.build(CreateDecoMod.REGISTRATE, metal)
//				.recipe( (ctx, prov)-> {
//					Bars.recipeStonecutting(()->getter.apply("ingot"), ctx, prov);
//				})
//				.register());
	}

	private static void registerSheetMetal (String metal, Function<String, Item> getter) {
		SHEET_METAL_PILLARS.put(metal, SheetMetal.buildBlock(CreateDecoMod.REGISTRATE, metal)
				
				.register());
	}

	public static final BlockEntry<WindowBlock>
			ANDESITE_WINDOW = Windows.metalWindowBlock("Andesite"),
			COPPER_WINDOW = Windows.metalWindowBlock("Copper"),
			IRON_WINDOW = Windows.metalWindowBlock("Iron"),
			INDUSTRIAL_IRON_WINDOW = Windows.metalWindowBlock("Industrial Iron"),
			BRASS_WINDOW = Windows.metalWindowBlock("Brass"),
			ZINC_WINDOW = Windows.metalWindowBlock("Zinc");

	public static final BlockEntry<ConnectedGlassPaneBlock>
			ANDESITE_WINDOW_PANE = Windows.metalWindowPane("Andesite", ANDESITE_WINDOW),
			COPPER_WINDOW_PANE = Windows.metalWindowPane("Copper", COPPER_WINDOW),
			IRON_WINDOW_PANE = Windows.metalWindowPane("Iron", IRON_WINDOW),
			INDUSTRIAL_IRON_WINDOW_PANE = Windows.metalWindowPane("Industrial Iron", INDUSTRIAL_IRON_WINDOW),
			BRASS_WINDOW_PANE = Windows.metalWindowPane("Brass", BRASS_WINDOW),
			ZINC_WINDOW_PANE = Windows.metalWindowPane("Zinc", ZINC_WINDOW);

	private static void registerDoors (String metal, Function<String, Item> getter) {
		if (metal.equals("Iron")
			|| metal.equals("Gold")
			|| metal.equals("Netherite")) {
			return;
		}
		else if (metal.equals("Copper")) {
			LOCK_DOORS.put(metal, Doors.build(CreateDecoMod.REGISTRATE, metal, true)
				
				.register());
			return;
		}

		DOORS.put(metal, Doors.build(CreateDecoMod.REGISTRATE, metal, false)
				
				.register());
		LOCK_DOORS.put(metal, Doors.build(CreateDecoMod.REGISTRATE, metal, true)
				
				.register());
		TRAPDOORS.put(metal, Doors.buildTrapdoor(CreateDecoMod.REGISTRATE, metal)
				
				.register());
	}

	private static void registerHulls (String metal, Function<String, Item> getter) {
		HULLS.put(metal, Hulls.build(CreateDecoMod.REGISTRATE, metal)
				
				.register()
		);
	}

	private static void registerLadders (String metal, Function<String, Item> getter) {
		if (metal.contains("opper") || metal.contains("ndesite") || metal.contains("rass")) return;

		LADDERS.put(metal, Ladders.build(CreateDecoMod.REGISTRATE, metal)
				
				.register()
		);
	}

	private static void registerSupports (String metal, Function<String, Item> getter) {
		SUPPORTS.put(metal, Supports.build(CreateDecoMod.REGISTRATE, metal)
				
				.register()
		);
	}

	private static void registerShippingContainers () {
		for (DyeColor color : DyeColor.values()) {
			SHIPPING_CONTAINERS.put(color, ShippingContainers.build(CreateDecoMod.REGISTRATE, color)
					
					.register()
			);
			CONTAINER_ENTITIES.put(color, CreateDecoMod.REGISTRATE.blockEntity(
							"shipping_container_" + color.getName().toLowerCase(Locale.ROOT).replaceAll(" ", "_"),
							ShippingContainerBlock.Entity::new)
					.validBlocks(SHIPPING_CONTAINERS.get(color))
					.register()
			);
		}
	}

	private static void registerPlacards () {
		for (DyeColor color : DyeColor.values()) {
			if (color == DyeColor.WHITE) { // Create's is the default
				continue;
			}
			String regName = color.name().toLowerCase(Locale.ROOT)
					.replaceAll(" ", "_") + "_placard";

			PLACARDS.put(color, CreateDecoMod.REGISTRATE.block(regName, DyedPlacardBlock::new)
					.initialProperties(SharedProperties::copperMetal)
					.tag(BlockTags.MINEABLE_WITH_PICKAXE)
					.simpleItem()
					
					
					.register());
		}



		@SuppressWarnings("unchecked")
		BlockEntry<? extends PlacardBlock>[] validPlacards = new BlockEntry[PLACARDS.size()];
		int color = 0;
		for (BlockEntry<? extends PlacardBlock> block : PLACARDS.values()) {
			validPlacards[color] = block;
		}
		PLACARD_ENTITIES = CreateDecoMod.REGISTRATE.blockEntity("dyed_placard", DyedPlacardBlock.Entity::new)
				
				.validBlocks(PLACARDS.values().toArray(validPlacards))
				.register();
	}

	private static void registerCoins (String metal, Function<String, Item> getter) {
		if (metal.equals("Andesite")) return;
		String regName = metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_");
		Identifier side   = CreateDecoMod.id("block/" + regName + "_coinstack_side");
		Identifier top    = CreateDecoMod.id("block/" + regName + "_coinstack_top");
		Identifier bottom = CreateDecoMod.id("block/" + regName + "_coinstack_bottom");

		COIN_BLOCKS.put(metal, Coins.buildCoinStackBlock(
				CreateDecoMod.REGISTRATE,
				()-> ItemRegistry.COINS.get(metal).get(),
				metal, side, bottom, top
		).register());
	}

	private static void registerBricks () {
		BRICK_COLORS.forEach( (color, name)-> {
			ArrayList<BlockBuilder<Block, ?>>     blocks;
			ArrayList<BlockBuilder<StairBlock,?>> stairs;
			ArrayList<BlockBuilder<SlabBlock,?>>  slabs;
			ArrayList<BlockBuilder<WallBlock,?>>  walls;

			blocks = Bricks.buildBlock(CreateDecoMod.REGISTRATE, name);
			blocks.forEach(bb -> {
				BRICKS.putIfAbsent(color, new HashMap<>());
				BRICKS.get(color).put(bb.getName(), bb.register());
			});
			stairs = Bricks.buildStair(CreateDecoMod.REGISTRATE, name);
			stairs.forEach(bb -> {
				STAIRS.putIfAbsent(color, new HashMap<>());
				STAIRS.get(color).put(bb.getName(), bb.register());
			});
			slabs = Bricks.buildSlab(CreateDecoMod.REGISTRATE, name);
			slabs.forEach(bb -> {
				SLABS.putIfAbsent(color, new HashMap<>());
				SLABS.get(color).put(bb.getName(), bb.register());
			});
			walls = Bricks.buildWall(CreateDecoMod.REGISTRATE, name);
			walls.forEach(bb -> {
				WALLS.putIfAbsent(color, new HashMap<>());
				WALLS.get(color).put(bb.getName(), bb.register());
			});
		});
	}

//	private static TagKey<Block> of (String namespace, String path) {
//		return BlockTags.create(
//			Identifier.fromNamespaceAndPath(namespace, path)
//		);
//	}
//
//	private static TagKey<Block> common (String path) {
//		return of("c", path);
//	}
}
