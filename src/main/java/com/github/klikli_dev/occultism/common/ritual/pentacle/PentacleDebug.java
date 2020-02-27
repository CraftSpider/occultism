/*
 * MIT License
 *
 * Copyright 2020 klikli-dev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.klikli_dev.occultism.common.ritual.pentacle;

import com.github.klikli_dev.occultism.registry.BlockRegistry;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.common.multiblock.StateMatcher;

public class PentacleDebug extends Pentacle {

    //region Fields
    private final String[][] pattern = new String[][]{{"  GRG  ", " G W G ", "G  W  G", "RWW0WWR", "G  W  G", " G W G ", "  GRG  "}};

    private final Object[] mapping = new Object[]{'0', StateMatcher.fromBlockLoose(
            BlockRegistry.GOLDEN_SACRIFICIAL_BOWL), 'W', PentacleStateMatcher.glyphType(
            GLYPH_WHITE), 'R', PentacleStateMatcher.glyphType(GLYPH_RED), 'G', PentacleStateMatcher.glyphType(
            GLYPH_GOLD), ' ', this.api.anyMatcher()};
    //endregion Fields


    //region Initialization
    public PentacleDebug() {
        super("debug");
    }
    //endregion Initialization

    //region Overrides
    @Override
    protected IMultiblock setupMultiblock() {
        return this.api.makeMultiblock(this.pattern, this.mapping).setSymmetrical(true);
    }
    //endregion Overrides
}
