package net.sikeawuser.dreamworld.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sikeawuser.dreamworld.DreamMod;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = DreamMod.MOD_ID , bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent gatherDataEvent) {
        DataGenerator generator = gatherDataEvent.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper helper = gatherDataEvent.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookup = gatherDataEvent.getLookupProvider();

        DMBlockTagProvider blockTagProvider = generator.addProvider(gatherDataEvent.includeServer(), new DMBlockTagProvider(output, lookup , helper));
        generator.addProvider(gatherDataEvent.includeServer(), new DMItemTagGenerator(output, lookup , blockTagProvider.contentsGetter() , helper));
        generator.addProvider(gatherDataEvent.includeServer(), new LootTableProvider(output , Set.of() , List.of(new LootTableProvider.SubProviderEntry(DMBlockLoot::new , LootContextParamSets.BLOCK))));

        generator.addProvider(gatherDataEvent.includeClient(), new DMLangProvider(output));
        generator.addProvider(gatherDataEvent.includeClient(), new DMSoundProvider(output , helper));
    }
}
