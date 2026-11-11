package net.sikeawuser.dreamworld.datagen;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;
import net.sikeawuser.dreamworld.DreamMod;
import net.sikeawuser.dreamworld.registry.DMBlocks;
import net.sikeawuser.dreamworld.registry.DMItems;

public class DMLangProvider extends LanguageProvider {
    public DMLangProvider(PackOutput output) {
        super(output, DreamMod.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addItem(DMItems.KEY , "Key");
        addBlock(DMBlocks.INVISIBLE_BLOCK, "Invisible Block");
        add("creativetab.dreamworld.dream_tab" , "Dreamworld");
    }
}
