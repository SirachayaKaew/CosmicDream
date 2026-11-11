package net.sikeawuser.dreamworld.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.sikeawuser.dreamworld.DreamMod;
import net.sikeawuser.dreamworld.registry.DMItems;

public class DMItemModelProvider extends ItemModelProvider {

    public DMItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, DreamMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(DMItems.KEY);

    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> itemRegistryObject) {
        return withExistingParent(itemRegistryObject.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(DreamMod.MOD_ID, "item/" + itemRegistryObject.getId().getPath()));
    }
}
