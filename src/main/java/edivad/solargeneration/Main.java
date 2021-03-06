package edivad.solargeneration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edivad.solargeneration.message.IntTrackerContainerMessage;
import edivad.solargeneration.proxy.ClientProxy;
import edivad.solargeneration.proxy.IProxy;
import edivad.solargeneration.proxy.ServerProxy;
import edivad.solargeneration.tabs.SolarGenerationTab;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@Mod(Main.MODID)
public class Main {

	public static final String MODID = "solargeneration";
	public static final String MODNAME = "Solar Generation";
	public static final String PROTOCOL = "1.14.4-1";

	public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

	public static final ItemGroup solarGenerationTab = new SolarGenerationTab("solargeneration_tab");

	public static final Logger logger = LogManager.getLogger();

	public static final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, "network"), () -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals);

	public Main()
	{
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		NETWORK.registerMessage(0, IntTrackerContainerMessage.class, IntTrackerContainerMessage::encode, IntTrackerContainerMessage::decode, IntTrackerContainerMessage.Handler::handle);
	}

	private void setup(final FMLCommonSetupEvent event)
	{
		proxy.init();
	}

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {

		@SubscribeEvent
		public static void onBlockRegistry(final RegistryEvent.Register<Block> event)
		{
			ModBlocks.register(event.getRegistry());
		}

		@SubscribeEvent
		public static void onItemsRegistry(final RegistryEvent.Register<Item> event)
		{
			ModItems.register(event.getRegistry());
		}

		@SubscribeEvent
		public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> event)
		{
			ModBlocks.registerTiles(event.getRegistry());
		}

		@SubscribeEvent
		public static void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> event)
		{
			ModBlocks.registerContainers(event.getRegistry());
		}
	}
}
