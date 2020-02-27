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

package com.github.klikli_dev.occultism.network;

import com.github.klikli_dev.occultism.Occultism;
import com.github.klikli_dev.occultism.api.common.container.IStorageControllerContainer;
import com.github.klikli_dev.occultism.api.common.tile.IStorageController;
import com.github.klikli_dev.occultism.util.InputUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.items.ItemHandlerHelper;

/**
 * Inserts the mouse held item into the opened storage controller.
 */
public class MessageInsertMouseHeldItem extends MessageBase<MessageInsertMouseHeldItem> {
    //region Fields
    private int mouseButton;
    //endregion Fields

    //region Initialization
    public MessageInsertMouseHeldItem() {

    }

    public MessageInsertMouseHeldItem(int mouseButton) {
        this.mouseButton = mouseButton;
    }
    //endregion Initialization

    //region Overrides
    @Override
    public void onClientReceived(Minecraft minecraft, MessageInsertMouseHeldItem message, EntityPlayer player,
                                 MessageContext context) {

    }

    @Override
    public void onServerReceived(MinecraftServer minecraftServer, MessageInsertMouseHeldItem message,
                                 EntityPlayerMP player, MessageContext context) {

        if (player.openContainer instanceof IStorageControllerContainer) {
            IStorageController storageController = ((IStorageControllerContainer) player.openContainer)
                                                           .getStorageController();

            ItemStack result = ItemStack.EMPTY;
            ItemStack carriedByMouse = player.inventory.getItemStack();

            if (message.mouseButton == InputUtil.MOUSE_BUTTON_LEFT) {
                //Left mouse button means try to insert entire stack
                int remainder = storageController.insertStack(carriedByMouse, false);
                //if not everything could be inserted, leave the rest in hand
                if (remainder != 0)
                    result = ItemHandlerHelper.copyStackWithSize(carriedByMouse, remainder);
            }
            else if (message.mouseButton == InputUtil.MOUSE_BUTTON_RIGHT) {
                //right mouse button means insert one

                ItemStack toInsert = carriedByMouse.copy();
                toInsert.setCount(1);
                carriedByMouse.shrink(1);

                //handle correct result depending on if the stack was inserted or not
                int remainder = storageController.insertStack(toInsert, false) + carriedByMouse.getCount();
                if (remainder != 0)
                    result = ItemHandlerHelper.copyStackWithSize(carriedByMouse, remainder);
            }

            //store result mouse held item
            player.inventory.setItemStack(result);
            //send new mouse held item to client
            Occultism.network.sendTo(new MessageUpdateMouseHeldItem(result), player);

            //update the storage controller
            Occultism.network.sendTo(storageController.getMessageUpdateStacks(), player);
            player.openContainer.detectAndSendChanges();
        }
    }

    @Override
    public void fromBytes(ByteBuf byteBuf) {
        this.mouseButton = byteBuf.readInt();
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeInt(this.mouseButton);
    }
    //endregion Overrides
}
