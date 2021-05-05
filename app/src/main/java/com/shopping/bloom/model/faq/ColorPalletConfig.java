package com.shopping.bloom.model.faq;

import java.util.List;

public class ColorPalletConfig {

    List<ColorModel> colorPallet;

    public ColorPalletConfig(List<ColorModel> colorPallet) {
        this.colorPallet = colorPallet;
    }

    public List<ColorModel> getColorPalletList() {
        return colorPallet;
    }
}
